package br.com.bvilela.listbuilder.service.vidacrista.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.service.vidacrista.VidaCristaWriterService;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemType;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.utils.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VidaCristaWriterServiceImpl implements VidaCristaWriterService {

	private final AppProperties properties;
	private static final ListTypeEnum LIST_TYPE = ListTypeEnum.VIDA_CRISTA;
	private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();

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

	@SneakyThrows
	private void writerDocument(List<VidaCristaExtractWeekDTO> listWeeks, Path path) {
		
		try (var outputStream = new FileOutputStream(path.toString())) {
			Document document = pdfUtils.getDocument(LIST_TYPE);
			PdfWriter.getInstance(document, outputStream);

			document.open();

			PdfPTable table;
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

	@SneakyThrows
	private PdfPTable addNewPage(Document document) {
		var pageMg = LIST_TYPE.getPageMg();
		document.setMargins(pageMg.getLeft(), pageMg.getRight(), pageMg.getTop() - AppUtils.getPointsFromMM(3), pageMg.getBottom());
		document.newPage();
		return addHeaderAndTable(document);
	}

	@SneakyThrows
	private void printContentWeek(VidaCristaExtractWeekDTO week, PdfPTable columnTable) {
		if (week.isSkip()) {
			var paragraph1 = pdfUtils.createParagraphBold16(" ");
			var paragraph2 = pdfUtils.createParagraphBold16(week.getSkipMessage());
			var cell1 = pdfUtils.newCellNoBorder(paragraph1);
			var cell2 = pdfUtils.newCellNoBorder(paragraph2);
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
					default -> log.info("Nenhuma ação para o tipo {}", item.getType());
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
		var paragraph = pdfUtils.createParagraphNormal(participants);
		var cell = pdfUtils.newCellNoBorder(paragraph);
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
		var paragraph = pdfUtils.createParagraphBold12Normal14(item.getTitle(), participants);
		var cell = pdfUtils.newCellNoBorder(paragraph);
		cell.setPaddingTop(10);
		columnTable.addCell(cell);
	}

	private void addItemTitle(PdfPTable columnTable, VidaCristaExtractWeekItemDTO item) {
		var paragraph = pdfUtils.createParagraphBold12(item.getTitle());
		var cell = pdfUtils.newCellNoBorder(paragraph);
		cell.setPaddingTop(10);
		columnTable.addCell(cell);
	}

	@SneakyThrows
	private PdfPTable addHeaderAndTable(Document document) {
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
