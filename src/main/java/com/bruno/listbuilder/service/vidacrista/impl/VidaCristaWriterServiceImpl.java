package com.bruno.listbuilder.service.vidacrista.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.enuns.VidaCristaExtractItemType;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.vidacrista.VidaCristaWriterService;
import com.bruno.listbuilder.utils.AppUtils;
import com.bruno.listbuilder.utils.DateUtils;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VidaCristaWriterServiceImpl implements VidaCristaWriterService {

	private AppProperties properties;

	private static final ListTypeEnum LIST_TYPE = ListTypeEnum.VIDA_CRISTA;
	
	private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();

	@Autowired
	public VidaCristaWriterServiceImpl(AppProperties properties) {
		this.properties = properties;
	}

	@Override
	public Path writerPDF(List<VidaCristaExtractWeekDTO> listWeeks) throws ListBuilderException {
		try {
			FileUtils.createDirectories(properties.getOutputDir());
			String fileName = FileUtils.generateOutputFileNamePDF(LIST_TYPE, listWeeks.get(0).getDate1());
			Path path = Paths.get(properties.getOutputDir(), fileName);

			writerDocument(listWeeks, path);
			
			return path;

		} catch (Exception e) {
			throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
		}
	}

	private void writerDocument(List<VidaCristaExtractWeekDTO> listWeeks, Path path)
			throws DocumentException, ListBuilderException, IOException {
		
		try (var outputStream = new FileOutputStream(path.toString())) {
			Document document = pdfUtils.getDocument(LIST_TYPE);
			PdfWriter.getInstance(document, outputStream);

			document.open();

			PdfPTable table = null;
			int countWeekInPage = 0;

			table = addHeaderAndTable(document);
			for (VidaCristaExtractWeekDTO week : listWeeks) {
				if (countWeekInPage == 2) {
					countWeekInPage = 0;
					document.add(table);
					table = addNewPage(document);
				}

				countWeekInPage++;
				PdfPTable columnTable = new PdfPTable(1);

				addCellWeekHeader(columnTable, week);
				
				printContentWeek(week, columnTable);

				var columnCell = new PdfPCell(columnTable);
				columnCell.setBorder(Rectangle.NO_BORDER);
				columnCell.setPaddingLeft(15);
				columnCell.setPaddingRight(15);
				table.addCell(columnCell);

				if (countWeekInPage == 1) {
					addImageColumnDivider(document);
				}
			}

			if (countWeekInPage == 1) {
				addEmptyColumn(table);
			}

			document.add(table);
			document.close();
		}
	}

	private PdfPTable addNewPage(Document document) throws DocumentException, ListBuilderException {
		var mg = LIST_TYPE.getPageMg();
		document.setMargins(mg.getLeft(), mg.getRight(), (mg.getTop() - AppUtils.getPointsFromMM(3)), mg.getBottom());
		document.newPage();
		return addHeaderAndTable(document);
	}

	private void printContentWeek(VidaCristaExtractWeekDTO week, PdfPTable columnTable) throws ListBuilderException {
		if (week.isSkip()) {
			var p1 = pdfUtils.createParagraphBold16(" ");
			var p2 = pdfUtils.createParagraphBold16(week.getSkipMessage());
			var cell1 = pdfUtils.newCellNoBorder(p1);
			var cell2 = pdfUtils.newCellNoBorder(p2);
			cell2.setPaddingTop(10);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			columnTable.addCell(cell1);
			columnTable.addCell(cell2);
		} else {
			for (VidaCristaExtractWeekItemDTO item : week.getItems()) {
				switch (item.getType()) {
					case PRESIDENT -> addItemPresident(columnTable, item);
					case NO_PARTICIPANTS -> addItemTitle(columnTable, item);
					case WITH_PARTICIPANTS -> addItemWithParticipants(columnTable, item);
					case LABEL -> addItemLabel(columnTable, item);
					default -> log.info("Nenhuma ação para o tipo {}", item.getType().toString());
				}
			}	
		}
	}

	private void addEmptyColumn(PdfPTable table) {
		var cell = pdfUtils.newCellNoBorder(new Paragraph(""));
		table.addCell(cell);
	}

	private void addItemWithParticipants(PdfPTable columnTable, VidaCristaExtractWeekItemDTO item) {
		addItemTitle(columnTable, item);
		var participants = AppUtils.printList(item.getParticipants());
		var p = pdfUtils.createParagraphNormal(participants);
		var cell = pdfUtils.newCellNoBorder(p);
		cell.setPaddingTop(-1);
		columnTable.addCell(cell);
	}

	private void addItemLabel(PdfPTable columnTable, VidaCristaExtractWeekItemDTO item) throws ListBuilderException {
		var imgName = getImageNameByLabel(item.getTitle());
		var cell = pdfUtils.addImageSubHeader(LIST_TYPE, imgName);
		cell.setPaddingTop(15);
		cell.setPaddingBottom(-8);
		cell.setPaddingLeft(0);
		cell.setPaddingRight(15);
		columnTable.addCell(cell);
	}

	private void addItemPresident(PdfPTable columnTable, VidaCristaExtractWeekItemDTO item) {
		var participants = AppUtils.printList(item.getParticipants());
		participants = StringUtils.leftPad(participants, 30, " ");
		var p = pdfUtils.createParagraphBold12Normal14(item.getTitle(), participants);
		var cell = pdfUtils.newCellNoBorder(p);
		cell.setPaddingTop(10);
		columnTable.addCell(cell);
	}

	private void addItemTitle(PdfPTable columnTable, VidaCristaExtractWeekItemDTO item) {
		var p = pdfUtils.createParagraphBold12(item.getTitle());
		var cell = pdfUtils.newCellNoBorder(p);
		cell.setPaddingTop(10);
		columnTable.addCell(cell);
	}

	private PdfPTable addHeaderAndTable(Document document) throws ListBuilderException, DocumentException {
		pdfUtils.addImageHeader(document, LIST_TYPE);

		var columnWidth = document.getPageSize().getWidth() / 2;
		float[] columnsWidth = new float[] { columnWidth, columnWidth };
		PdfPTable table = new PdfPTable(columnsWidth.length);
		table.setTotalWidth(columnsWidth);
		table.setLockedWidth(true);
		return table;
	}

	private void addCellWeekHeader(PdfPTable table, VidaCristaExtractWeekDTO week) throws ListBuilderException {
		var text = getWeekHeaderLabel(week);
		PdfPCell cell = new PdfPCell(pdfUtils.createParagraphBold12(text));
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderColorBottom(BaseColor.BLACK);
		cell.setBorderWidthBottom(1f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(5);
		table.addCell(cell);
	}

	private String getWeekHeaderLabel(VidaCristaExtractWeekDTO week) throws ListBuilderException {
		var date1 = week.getDate1();
		var date2 = week.getDate2();
		var readOfWeek = getReadOfWeek(week);

		if (date1.getMonthValue() == date2.getMonthValue()) {
			var label = date1.getDayOfMonth() + " - " + date2.getDayOfMonth() + " DE "
					+ DateUtils.getNameMonthFull(date1) + " – " + readOfWeek;
			return label.toUpperCase();
		}
		var label = dayOfMonthLabel(date1) + " - " + dayOfMonthLabel(date2) + " – " + readOfWeek;
		return label.toUpperCase();
	}

	private String getReadOfWeek(VidaCristaExtractWeekDTO week) throws ListBuilderException {
		var readOfWeek = week.getItems().stream().filter(e -> e.getType() == VidaCristaExtractItemType.READ_OF_WEEK)
				.toList();

		if (readOfWeek.size() != 1) {
			throw new ListBuilderException("Erro ao Obter Leitura da Semana");
		}

		return readOfWeek.get(0).getTitle().replace("-", " - ");
	}

	private String dayOfMonthLabel(LocalDate date) {
		return date.getDayOfMonth() + " DE " + DateUtils.getNameMonthShort(date);
	}

	private void addImageColumnDivider(Document document) throws ListBuilderException {
		try {
			Path path = Paths.get("images", LIST_TYPE.toString().toLowerCase(), "divider.jpg");

			var url = new ClassPathResource(path.toString()).getURL();
			Image image = Image.getInstance(url);
			image.scaleAbsolute(3, document.getPageSize().getHeight());
			image.setAlignment(Image.MIDDLE);
			image.setAbsolutePosition(document.getPageSize().getWidth() / 2, -85);
			document.add(image);

		} catch (DocumentException | IOException e) {
			throw new ListBuilderException("Erro ao adicionar linha de divisão entre as colunas. Erro: %s",
					e.getMessage());
		}
	}
	
	public String getImageNameByLabel(String label) throws ListBuilderException {
		label = label.toUpperCase();
		if (label.contains("TESOUROS DA PALAVRA")) {
			return "treasures-word-god.jpg";
		} 
		if (label.contains("FAÇA SEU MELHOR")) {
			return "make-your-better.jpg";
		}
		if (label.contains("NOSSA VIDA CRISTÃ")) {
			return "christian-life.jpg";
		}
		throw new ListBuilderException("Erro ao obter Imagem da Label - Nenhuma Imagem encontrada para a Label");
	}

}
