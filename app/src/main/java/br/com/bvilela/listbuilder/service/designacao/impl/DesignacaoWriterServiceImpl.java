package br.com.bvilela.listbuilder.service.designacao.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.SizeConfig;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.designacao.DesignacaoWriterService;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.utils.impl.DocxWriterUtilsImpl;
import br.com.bvilela.listbuilder.utils.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DesignacaoWriterServiceImpl implements DesignacaoWriterService {

    private final AppProperties properties;

    private static final ListTypeEnum LIST_TYPE = ListTypeEnum.DESIGNACAO;
    private static final int BREAK_LINE_DEFAULT = 2;
    private static final int BREAK_LINE_PRESIDENT = 1;
    private static final float HORIZONTAL_MARGINS_DISCOUNT =
            AppUtils.getHorizontalMargins(LIST_TYPE);
    private final DocxWriterUtilsImpl docxUtils = new DocxWriterUtilsImpl();
    private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();

    @Override
    @SneakyThrows
    public Path writerPDF(DesignacaoWriterDTO dto) {
        try {
            FileUtils.createDirectories(properties.getOutputDir());
            String fileName =
                    FileUtils.generateOutputFileNamePDF(
                            LIST_TYPE, dto.getPresident().get(0).getDate());
            Path path = Paths.get(properties.getOutputDir(), fileName);

            writerPDFDocument(dto, path);

            return path;

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private void writerPDFDocument(DesignacaoWriterDTO dto, Path path) {

        try (var outputStream = new FileOutputStream(path.toString())) {
            Document document = pdfUtils.getDocument(LIST_TYPE);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfPTable tableMaster = addHeaderAndTableMaster(document);

            writeSectionsLine1(tableMaster, dto);
            writeSectionsLine2(tableMaster, dto);
            writeSectionsLine3(tableMaster, dto);

            document.add(tableMaster);
            document.close();
        }
    }

    private void writeSectionsLine1(PdfPTable table, DesignacaoWriterDTO dto) {
        List<DesignacaoWriterItemDTO> listReader = getAllReaders(dto);
        final int marginTop = 0;
        writeSection(table, listReader, "reader", BREAK_LINE_DEFAULT, marginTop);
        writeSection(table, dto.getAudioVideo(), "audioVideo", BREAK_LINE_DEFAULT, marginTop);
    }

    private void writeSectionsLine2(PdfPTable table, DesignacaoWriterDTO dto) {
        final int marginTop = 15;
        writeSection(table, dto.getMicrophone(), "microphone", BREAK_LINE_DEFAULT, marginTop);
        writeSection(table, dto.getIndicator(), "indicator", BREAK_LINE_DEFAULT, marginTop);
    }

    private void writeSectionsLine3(PdfPTable table, DesignacaoWriterDTO dto) {
        final int marginTop = 15;
        writeSection(table, dto.getPresident(), "president", BREAK_LINE_PRESIDENT, marginTop);
        table.addCell(pdfUtils.newCellNoBorder(new Paragraph("")));
    }

    @SneakyThrows
    private void writeSection(
            PdfPTable table,
            List<DesignacaoWriterItemDTO> list,
            String section,
            int breakLine,
            int paddingTop) {
        PdfPTable columnTable = new PdfPTable(1);
        addSubHeader(columnTable, section, paddingTop);

        var columnCell = new PdfPCell(columnTable);
        columnCell.setBorder(Rectangle.NO_BORDER);

        int count = 0;
        for (DesignacaoWriterItemDTO item : list) {
            count++;
            var dayWeekEnum = DayOfWeekEnum.getByDayOfWeek(item.getDate().getDayOfWeek());
            var dateLabel = getDateLabel(item, dayWeekEnum);

            float firstColumn = 90f;
            float secondColumn = 164f;
            float[] columnsWidth = new float[] {firstColumn, secondColumn};
            PdfPTable cellTable = new PdfPTable(columnsWidth.length);
            cellTable.setTotalWidth(columnsWidth);
            cellTable.setLockedWidth(true);

            PdfPCell internalCellDate = new PdfPCell(pdfUtils.createParagraphBold12(dateLabel));
            PdfPCell internalCellName;
            if (isTextBold(item)) {
                internalCellName = new PdfPCell(pdfUtils.createParagraphBold12(item.getName()));
            } else {
                internalCellName = new PdfPCell(pdfUtils.createParagraphNormal(item.getName()));
            }

            internalCellDate.setBorder(Rectangle.NO_BORDER);
            internalCellName.setBorder(Rectangle.NO_BORDER);
            internalCellDate.setPadding(0);
            internalCellDate.setPaddingLeft(2);
            internalCellName.setPadding(0);
            cellTable.addCell(internalCellDate);
            cellTable.addCell(internalCellName);

            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            if (count == breakLine) {
                count = 0;
                cell.setPaddingBottom(12);
            }

            cell.addElement(cellTable);
            columnTable.addCell(cell);
        }

        table.addCell(columnCell);
    }

    private PdfPTable addHeaderAndTableMaster(Document document) {
        pdfUtils.addImageHeader(document, LIST_TYPE);
        return pdfUtils.getTable(document, 2, LIST_TYPE);
    }

    private void addSubHeader(PdfPTable table, String section, int paddingTop) {
        var imageName = String.format("%s.jpg", section);

        var cell = pdfUtils.addImageSubHeader(LIST_TYPE, imageName);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(5);
        cell.setPaddingTop(paddingTop);
        table.addCell(cell);
    }

    private boolean isTextBold(DesignacaoWriterItemDTO item) {
        var name = item.getName().toLowerCase();
        return name.contains("congresso") || name.contains("assembleia") || name.contains("visita");
    }

    private List<DesignacaoWriterItemDTO> getAllReaders(DesignacaoWriterDTO dto) {
        List<DesignacaoWriterItemDTO> listReader = new ArrayList<>();
        listReader.addAll(dto.getReaderBibleStudy());
        listReader.addAll(dto.getReaderWatchtower());
        listReader.sort(Comparator.comparing(DesignacaoWriterItemDTO::getDate));
        return listReader;
    }

    private String getDateLabel(DesignacaoWriterItemDTO item, DayOfWeekEnum dayWeekEnum) {
        return DateUtils.formatDDMM(item.getDate())
                .concat(" (")
                .concat(dayWeekEnum.getName())
                .concat(") ");
    }

    @Override
    @SneakyThrows
    public Path writerDocx(DesignacaoWriterDTO dto) {
        try {
            FileUtils.createDirectories(properties.getOutputDir());
            String fileName =
                    FileUtils.generateOutputFileNameDocx(
                            LIST_TYPE, dto.getPresident().get(0).getDate());
            Path path = Paths.get(properties.getOutputDir(), fileName);

            writeDocxDocument(dto, path);

            return path;

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao Gerar Docx - Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private void writeDocxDocument(DesignacaoWriterDTO dto, Path path) {
        try (XWPFDocument doc = docxUtils.getDocument(LIST_TYPE)) {

            docxUtils.addImageHeader(doc, LIST_TYPE);

            // Create table e Config Widths
            XWPFTable table = doc.createTable();
            XWPFTableRow rowSubHeader = table.getRow(0);

            // Column length is measured in twentieths of a point
            var horizontalMargins = AppUtils.getSizePointTimesTwenty(HORIZONTAL_MARGINS_DISCOUNT);
            var columnWidth = (SizeConfig.DOCX_A4_WIDTH_POINT - horizontalMargins) / 2;

            rowSubHeader.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(columnWidth);
            rowSubHeader.addNewTableCell().getCTTc().addNewTcPr().addNewTcW().setW(columnWidth);

            int indexRowGeneral = 1;

            // Readers and AudioVideo
            this.docxAddImageSubHeader(rowSubHeader.getCell(0), "reader.jpg");
            this.docxAddImageSubHeader(rowSubHeader.getCell(1), "audioVideo.jpg");
            indexRowGeneral = writeSection(table, this.getAllReaders(dto), 0, indexRowGeneral);
            indexRowGeneral = writeSection(table, dto.getAudioVideo(), 1, indexRowGeneral);

            // Indicator and Microphone
            indexRowGeneral++;
            var rowSubHeader2 = table.createRow();
            this.docxAddImageSubHeader(rowSubHeader2.getCell(0), "microphone.jpg");
            this.docxAddImageSubHeader(rowSubHeader2.getCell(1), "indicator.jpg");
            indexRowGeneral = writeSection(table, dto.getMicrophone(), 0, indexRowGeneral);
            indexRowGeneral = writeSection(table, dto.getIndicator(), 1, indexRowGeneral);

            // President
            indexRowGeneral++;
            this.docxAddImageSubHeader(table.createRow().getCell(0), "president.jpg");
            writeSection(table, dto.getPresident(), 0, indexRowGeneral);

            docxUtils.setNoBordersTable(table);

            // save it to .docx file
            try (FileOutputStream out = new FileOutputStream(path.toFile())) {
                doc.write(out);
            }
        }
    }

    private int writeSection(
            XWPFTable table,
            List<DesignacaoWriterItemDTO> list,
            int columnIndex,
            int indexRowGeneral) {
        int countLines = 0;
        for (DesignacaoWriterItemDTO item : list) {
            XWPFTableRow row =
                    columnIndex == 0 ? table.createRow() : table.getRow(indexRowGeneral++);
            countLines++;

            int spacingAfter = 0;
            if (countLines == 2) {
                countLines = 0;
                spacingAfter = AppUtils.getSizePointTimesTwenty(8);
            }

            var dayWeekEnum = DayOfWeekEnum.getByDayOfWeek(item.getDate().getDayOfWeek());
            var dateLabel = getDateLabel(item, dayWeekEnum);
            var paragraph = row.getCell(columnIndex).getParagraphs().get(0);
            paragraph.setIndentationLeft(AppUtils.getSizePointTimesTwenty(10));
            paragraph.setSpacingAfter(spacingAfter);

            if (isTextBold(item)) {
                docxUtils.setParagraphBoldBold(paragraph, dateLabel, item.getName());
            } else {
                docxUtils.setParagraphBoldNormal(paragraph, dateLabel, item.getName());
            }
        }
        return indexRowGeneral;
    }

    private void docxAddImageSubHeader(XWPFTableCell cell, String imgName) {
        var paragraph = cell.getParagraphs().get(0);
        paragraph.setSpacingBefore(AppUtils.getSizePointTimesTwenty(12));
        paragraph.setSpacingAfter(AppUtils.getSizePointTimesTwenty(6));
        docxUtils.addImageSubHeader(paragraph, LIST_TYPE, imgName);
    }
}
