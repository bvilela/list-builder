package br.com.bvilela.listbuilder.service.audience.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.audience.AudienceWriterService;
import br.com.bvilela.listbuilder.utils.DateUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.utils.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("COMPACT")
@RequiredArgsConstructor
public class AudienceWriterServiceLayoutCompactImpl implements AudienceWriterService {

    private final AppProperties properties;

    private static final ListTypeEnum LIST_TYPE = ListTypeEnum.ASSISTENCIA;
    private final String[] header = new String[] {"Dia", "Data", "Assistência"};
    private final Font fontDefault = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();

    @Override
    @SneakyThrows
    public void writerPDF(List<LocalDate> listDates) {
        FileUtils.createDirectories(properties.getOutputDir());

        String fileName =
                FileUtils.generateOutputFileNamePDF(
                        LIST_TYPE, listDates.get(0), listDates.get(listDates.size() - 1));
        Path path = Paths.get(properties.getOutputDir(), fileName);

        try (var outputStream = new FileOutputStream(path.toString())) {

            Document document = pdfUtils.getDocument(LIST_TYPE);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            pdfUtils.addImageHeader(document, LIST_TYPE);

            PdfPTable table = createPdfPTable();

            Month currentMonth = null;
            for (LocalDate date : listDates) {

                if (changeMonth(currentMonth, date)) {
                    addBlankRow(table, 12);
                    setMonthLabel(table, date);
                    addBlankRow(table, 4);
                    setTableHeader(table);
                    addBlankRow(table, 2);
                    currentMonth = date.getMonth();
                }

                addDayOfWeekLabel(table, date);
                addDateLabel(table, date);
                addBlankCell(table);
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
        }
    }

    private boolean changeMonth(Month currentMonth, LocalDate date) {
        return !date.getMonth().equals(currentMonth);
    }

    @SneakyThrows
    private static PdfPTable createPdfPTable() {
        float[] columnsWidth = new float[] {155, 155, 200}; // 160, 350
        PdfPTable table = new PdfPTable(columnsWidth.length);
        table.setTotalWidth(columnsWidth);
        table.setLockedWidth(true);
        return table;
    }

    private void setMonthLabel(PdfPTable table, LocalDate date) {
        var formattedDate = StringUtils.capitalize(DateUtils.formatMMMMyyyy(date));
        var paragraph = pdfUtils.createParagraphBold16(formattedDate);
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setColspan(table.getNumberOfColumns());
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addDateLabel(PdfPTable table, LocalDate date) {
        addLabel(table, DateUtils.formatDDMMM(date));
    }

    private void addDayOfWeekLabel(PdfPTable table, LocalDate date) {
        addLabel(table, DayOfWeekEnum.getByDayOfWeek(date.getDayOfWeek()).getName());
    }

    private void addBlankCell(PdfPTable table) {
        table.addCell(new PdfPCell());
    }

    private void addLabel(PdfPTable table, String text) {
        var phrase = new Phrase();
        phrase.setFont(fontDefault);
        phrase.add(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(2);
        cell.setPaddingBottom(4);
        cell.setFixedHeight(20);
        table.addCell(cell);
    }

    private void addBlankRow(PdfPTable table, float height) {
        PdfPCell blankRow = new PdfPCell(new Phrase("\n"));
        blankRow.setFixedHeight(height);
        blankRow.setColspan(table.getNumberOfColumns());
        blankRow.setBorder(Rectangle.NO_BORDER);
        table.addCell(blankRow);
    }

    private void setTableHeader(PdfPTable table) {
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            var phrase = new Phrase();
            var font = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            phrase.setFont(font);
            phrase.add(header[i]);
            PdfPCell cell = new PdfPCell(phrase);
            cell.setBackgroundColor(new BaseColor(128, 128, 128));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPaddingTop(1);
            cell.setPaddingBottom(5);
            table.addCell(cell);
        }
    }
}
