package br.com.bvilela.listbuilder.service.christianlife;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.AppUtils;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.util.FileUtils;
import br.com.bvilela.listbuilder.util.impl.PDFWriterUtilsImpl;
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
public class ChristianLifeWriterService {

	private final AppProperties properties;
	private static final ListTypeEnum LIST_TYPE = ListTypeEnum.VIDA_CRISTA;
	private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl(LIST_TYPE);

	@SneakyThrows
	public Path writerPDF(List<ChristianLifeExtractWeekDTO> listWeeks) {
		try {
			FileUtils.createDirectories(properties.getOutputDir());
			String fileName = FileUtils.generateOutputFileNamePDF(LIST_TYPE, listWeeks.get(0).getInitialDate());
			Path path = Paths.get(properties.getOutputDir(), fileName);

			writerDocument(listWeeks, path);
			
			return path;

		} catch (Exception e) {
			throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
		}
	}

	@SneakyThrows
	private void writerDocument(List<ChristianLifeExtractWeekDTO> listWeeks, Path path) {
		
		try (var outputStream = new FileOutputStream(path.toString())) {
			Document document = pdfUtils.getDocument();
			PdfWriter.getInstance(document, outputStream);

			document.open();

			pdfUtils.addImageHeader(document);

			PdfPTable table = createPdfPTable(document);

			int countWeekInPage = 0;
			for (ChristianLifeExtractWeekDTO week : listWeeks) {
				if (countWeekInPage == 2) {
					countWeekInPage = 0;
					document.add(table);
					table = addNewPage(document);
				}

				countWeekInPage++;
				PdfPTable columnTable = new PdfPTable(1);

				addCellWeekHeader(columnTable, week);
				
				addContentWeek(week, columnTable);

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

	private PdfPTable addNewPage(Document document) {
		var pageMg = LIST_TYPE.getPageMg();
		document.setMargins(pageMg.getLeft(), pageMg.getRight(), pageMg.getTop() - AppUtils.getPointsFromMM(3), pageMg.getBottom());
		document.newPage();
		pdfUtils.addImageHeader(document);
		return createPdfPTable(document);
	}

	private void addContentWeek(ChristianLifeExtractWeekDTO week, PdfPTable columnTable) {
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
			addItemByItemType(columnTable, week.getItems());
		}
	}

	private void addItemByItemType(PdfPTable columnTable, List<ChristianLifeExtractWeekItemDTO> items) {
		items.forEach(item -> {
			switch (item.getType()) {
				case PRESIDENT -> addItemPresident(columnTable, item);
				case NO_PARTICIPANTS -> addItemTitle(columnTable, item);
				case WITH_PARTICIPANTS -> addItemWithParticipants(columnTable, item);
				case LABEL -> addItemLabel(columnTable, item);
				case BIBLE_STUDY -> addItemBibleStudy(columnTable, item, items);
				default -> log.info("Nenhuma ação para o tipo {}", item.getType());
			}
		});
	}

	private void addItemBibleStudy(PdfPTable columnTable, ChristianLifeExtractWeekItemDTO studyBibleLeader,
								   List<ChristianLifeExtractWeekItemDTO> items) {
		if (properties.isChristianlifeIncludeBibleStudyReader()) {
			addBibleStudyLeaderAndReader(columnTable, studyBibleLeader, items);
		} else {
			addItemWithParticipants(columnTable, studyBibleLeader);
		}
	}

	private void addEmptyColumn(PdfPTable table) {
		var cell = pdfUtils.newCellNoBorder(new Paragraph(""));
		table.addCell(cell);
	}

	private void addItemWithParticipants(PdfPTable columnTable, ChristianLifeExtractWeekItemDTO item) {
		addItemTitle(columnTable, item);
		var participants = AppUtils.printList(item.getParticipants());
		var paragraph = pdfUtils.createParagraphNormal(participants);
		var cell = pdfUtils.newCellNoBorder(paragraph);
		cell.setPaddingTop(-1);
		columnTable.addCell(cell);
	}

	private void addItemLabel(PdfPTable columnTable, ChristianLifeExtractWeekItemDTO item) {
		var imgName = getImageNameByLabel(item.getTitle());
		var cell = pdfUtils.addImageSubHeader(LIST_TYPE, imgName);
		cell.setPaddingTop(15);
		cell.setPaddingBottom(-8);
		cell.setPaddingLeft(0);
		cell.setPaddingRight(15);
		columnTable.addCell(cell);
	}

	@SneakyThrows
	private void addBibleStudyLeaderAndReader(PdfPTable columnTable,
											  ChristianLifeExtractWeekItemDTO studyBibleLeader,
											  List<ChristianLifeExtractWeekItemDTO> items) {
		addItemTitle(columnTable, studyBibleLeader);

		var indexStudyBibleLeader = items.indexOf(studyBibleLeader);
		var studyBibleReader = items.get(indexStudyBibleLeader+1);
		if (!ChristianLifeExtractItemTypeEnum.BIBLE_STUDY_READER.equals(studyBibleReader.getType())) {
			throw new ListBuilderException("Item após o Dirigente do Estudo Não é do tipo Leitor!");
		}

		var readerName = AppUtils.printList(studyBibleReader.getParticipants());
		var leaderName = AppUtils.printList(studyBibleLeader.getParticipants());
		var paragraph = pdfUtils.createParagraphNormal12Bold12Normal12(
				leaderName + "   -   ",
				studyBibleReader.getTitle() + ": ",
				readerName);
		var cell = pdfUtils.newCellNoBorder(paragraph);
		cell.setPaddingTop(-1);
		columnTable.addCell(cell);
	}

	private void addItemPresident(PdfPTable columnTable, ChristianLifeExtractWeekItemDTO item) {
		var participants = AppUtils.printList(item.getParticipants());
		participants = StringUtils.leftPad(participants, 30, " ");
		var paragraph = pdfUtils.createParagraphBold12Normal14(item.getTitle(), participants);
		var cell = pdfUtils.newCellNoBorder(paragraph);
		cell.setPaddingTop(10);
		columnTable.addCell(cell);
	}

	private void addItemTitle(PdfPTable columnTable, ChristianLifeExtractWeekItemDTO item) {
		var paragraph = pdfUtils.createParagraphBold12(item.getTitle());
		var cell = pdfUtils.newCellNoBorder(paragraph);
		cell.setPaddingTop(10);
		columnTable.addCell(cell);
	}

	@SneakyThrows
	private PdfPTable createPdfPTable(Document document) {
		var columnWidth = document.getPageSize().getWidth() / 2;
		float[] columnsWidth = new float[] { columnWidth, columnWidth };
		PdfPTable table = new PdfPTable(columnsWidth.length);
		table.setTotalWidth(columnsWidth);
		table.setLockedWidth(true);
		return table;
	}

	private void addCellWeekHeader(PdfPTable table, ChristianLifeExtractWeekDTO week) {
		var text = getWeekHeaderLabel(week);
		PdfPCell cell = new PdfPCell(pdfUtils.createParagraphBold12(text));
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderColorBottom(BaseColor.BLACK);
		cell.setBorderWidthBottom(1f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(5);
		table.addCell(cell);
	}

	private String getWeekHeaderLabel(ChristianLifeExtractWeekDTO week) {
		var initialDate = week.getInitialDate();
		var endDate = week.getEndDate();
		var readOfWeek = getReadOfWeek(week);

		if (initialDate.getMonthValue() == endDate.getMonthValue()) {
			var label = initialDate.getDayOfMonth() + " - " + endDate.getDayOfMonth() + " DE "
					+ DateUtils.getNameMonthFull(initialDate) + " – " + readOfWeek;
			return label.toUpperCase();
		}
		var label = dayOfMonthLabel(initialDate) + " - " + dayOfMonthLabel(endDate) + " – " + readOfWeek;
		return label.toUpperCase();
	}

	@SneakyThrows
	private String getReadOfWeek(ChristianLifeExtractWeekDTO week) {
		var readOfWeek = week.getItems().stream().filter(e -> e.getType() == ChristianLifeExtractItemTypeEnum.READ_OF_WEEK)
				.toList();

		if (readOfWeek.size() != 1) {
			throw new ListBuilderException("Erro ao Obter Leitura da Semana");
		}

		return readOfWeek.get(0).getTitle().replace("-", " - ");
	}

	private String dayOfMonthLabel(LocalDate date) {
		return date.getDayOfMonth() + " DE " + DateUtils.getNameMonthShort(date);
	}

	@SneakyThrows
	private void addImageColumnDivider(Document document) {
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

	@SneakyThrows
	public String getImageNameByLabel(String label) {
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
