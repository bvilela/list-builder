package br.com.bvilela.listbuilder.service.assistencia.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.assistencia.AssistenciaWriterService;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssistenciaWriterServiceImpl implements AssistenciaWriterService {

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

            float[] columnsWidth = new float[] {155, 155, 200};

            PdfPTable table = new PdfPTable(columnsWidth.length);
            table.setTotalWidth(columnsWidth);
            table.setLockedWidth(true);

            int count = 0;
            for (LocalDate date : listDates) {
                if (count == 0) {
                    setHeader(columnsWidth, table);
                    addBlankRow(table, 2);
                }

                count++;
                addDayOfWeekLabel(table, date);
                addDateLabel(table, date);
                table.addCell(new PdfPCell());

                if (count == 2) {
                    count = 0;
                    addBlankRow(table, 10);
                }
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
        }
    }

    private void addDateLabel(PdfPTable table, LocalDate date) {
        String formattedDate = DateUtils.formatDDMMM(date);
        var phrase = new Phrase();
        phrase.setFont(fontDefault);
        phrase.add(formattedDate);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(2);
        cell.setPaddingBottom(4);
        cell.setFixedHeight(20);
        table.addCell(cell);
    }

    private void addDayOfWeekLabel(PdfPTable table, LocalDate date) {
        var phrase = new Phrase();
        phrase.setFont(fontDefault);
        phrase.add(DayOfWeekEnum.getByDayOfWeek(date.getDayOfWeek()).getName());
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
        blankRow.setColspan(3);
        blankRow.setBorder(Rectangle.NO_BORDER);
        table.addCell(blankRow);
    }

    private void setHeader(float[] columnsWidth, PdfPTable table) {
        for (int i = 0; i < columnsWidth.length; i++) {
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
