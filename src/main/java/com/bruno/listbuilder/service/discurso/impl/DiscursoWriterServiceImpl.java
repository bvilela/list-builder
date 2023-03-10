package com.bruno.listbuilder.service.discurso.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoItemDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.discurso.DiscursoWriterService;
import com.bruno.listbuilder.utils.AppUtils;
import com.bruno.listbuilder.utils.DateUtils;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscursoWriterServiceImpl implements DiscursoWriterService {

	private AppProperties properties;

	private static final String SEND = "send";
	private static final String RECEIVE = "receive";
	private static final ListTypeEnum LIST_TYPE = ListTypeEnum.DISCURSO;
	
	private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();

	@Autowired
	public DiscursoWriterServiceImpl(AppProperties properties) {
		this.properties = properties;
	}

	@Override
	public Path writerPDF(FileInputDataDiscursoDTO dto) throws ListBuilderException {
		try {
			
			log.info("Iniciando Geração da lista em PDF");
			
			FileUtils.createDirectories(properties.getOutputDir());
			var baseDate = getBaseDate(dto);
			String fileName = FileUtils.generateOutputFileNamePDF(LIST_TYPE, baseDate);
			Path path = Paths.get(properties.getOutputDir(), fileName);

			writerDocument(dto, path);
			
			return path;

		} catch (Exception e) {
			throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
		}
	}

	public LocalDate getBaseDate(FileInputDataDiscursoDTO dto) {
		if (!AppUtils.listIsNullOrEmpty(dto.getReceive())) {
			return dto.getReceive().get(0).getDateConverted();
		}
		return dto.getSend().get(0).getDateConverted();
	}

	private void writerDocument(FileInputDataDiscursoDTO dto, Path path)
			throws DocumentException, ListBuilderException, IOException {
		
		try (var outputStream = new FileOutputStream(path.toString())) {
			Document document = pdfUtils.getDocument(LIST_TYPE);
			PdfWriter.getInstance(document, outputStream);

			document.open();

			pdfUtils.addImageHeader(document, LIST_TYPE);
			
			int numberOfColumns = getNumberOfColumns(dto);
			PdfPTable table = pdfUtils.getTable(document, numberOfColumns, LIST_TYPE);

			addReceiveSendHeaders(dto, table);

			var maxItens = getBiggestList(dto);

			for (int i = 0; i < maxItens; i++) {
				addItem(dto.getReceive(), table, i);
				addItem(dto.getSend(), table, i);
				
				if (!AppUtils.listIsNullOrEmpty(dto.getReceive()) &&
						!AppUtils.listIsNullOrEmpty(dto.getSend())) {
					addBlankRow(table, 20);
				}
			}

			document.add(table);
			document.close();
		}
	}

	private void addReceiveSendHeaders(FileInputDataDiscursoDTO dto, PdfPTable table) throws ListBuilderException {
		var receiveNonEmpty = !AppUtils.listIsNullOrEmpty(dto.getReceive());
		var sendNonEmpty = !AppUtils.listIsNullOrEmpty(dto.getSend());
		
		if (receiveNonEmpty) {
			table.addCell(createCellSubHeader(RECEIVE));	
		}
		
		if (sendNonEmpty) {
			table.addCell(createCellSubHeader(SEND));	
		}
		
		if (receiveNonEmpty && sendNonEmpty) {
			addBlankRow(table, 10);	
		}
	}

	private int getNumberOfColumns(FileInputDataDiscursoDTO dto) {
		int numberOfColumns = 0;
		if (!AppUtils.listIsNullOrEmpty(dto.getReceive())) {
			numberOfColumns++;
		}
		if (!AppUtils.listIsNullOrEmpty(dto.getSend())) {
			numberOfColumns++;
		}
		return numberOfColumns;
	}

	private void addItem(List<FileInputDataDiscursoItemDTO> list, PdfPTable table, int i) {
		if (AppUtils.listIsNullOrEmpty(list)) {
			addBlankRow(table, 20);
			return;
		}			
		
		if (i < list.size()) {
			addItem(table, list.get(i));
		} else {
			addBlankCell(table);
		}
	}

	private int getBiggestList(FileInputDataDiscursoDTO dto) {
		if (AppUtils.listIsNullOrEmpty(dto.getReceive())) {
			return dto.getSend().size();
		}
		if (AppUtils.listIsNullOrEmpty(dto.getSend())) {
			return dto.getReceive().size();
		}		
		return dto.getReceive().size() >= dto.getSend().size() ? dto.getReceive().size() : dto.getSend().size();
	}

	private PdfPCell createCellSubHeader(String subHeader) throws ListBuilderException {
		if (Objects.isNull(subHeader) || subHeader.isBlank() || !List.of(SEND, RECEIVE).contains(subHeader)) {
			throw new ListBuilderException("Cabeçalho deve ser: '%s' ou '%s'", RECEIVE, SEND);
		}

		var cell = pdfUtils.addImageSubHeader(LIST_TYPE, subHeader + ".jpg");
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}

	private void addBlankRow(PdfPTable table, float height) {
		PdfPCell blankRow = new PdfPCell(new Phrase("\n"));
		blankRow.setFixedHeight(height);
		blankRow.setColspan(2);
		blankRow.setBorder(Rectangle.NO_BORDER);
		table.addCell(blankRow);
	}

	private void addBlankCell(PdfPTable table) {
		var cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
	}

	private void addItem(PdfPTable table, FileInputDataDiscursoItemDTO dto) {
		PdfPCell cell = new PdfPCell();
		cell.addElement(
				pdfUtils.createParagraphBold12Normal12("Data: ", DateUtils.formatDDMMMM(dto.getDateConverted())));
		cell.addElement(pdfUtils.createParagraphBold12Normal12("Tema: ", getThemeLabel(dto)));
		cell.addElement(pdfUtils.createParagraphBold12Normal12("Orador: ", dto.getSpeaker()));
		cell.addElement(pdfUtils.createParagraphBold12Normal12("Congregação: ", dto.getCongregation()));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(15);
		cell.setPaddingRight(25);
		table.addCell(cell);
	}
	
	private String getThemeLabel(FileInputDataDiscursoItemDTO dto) {
		return dto.getThemeTitle().equals("?") ? "" : dto.getThemeTitle();
	}

}
