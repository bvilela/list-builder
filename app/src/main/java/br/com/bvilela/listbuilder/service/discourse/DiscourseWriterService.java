package br.com.bvilela.listbuilder.service.discourse;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.discourse.writer.DiscourseWriterDTO;
import br.com.bvilela.listbuilder.dto.discourse.writer.DiscourseWriterItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.AppUtils;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.util.FileUtils;
import br.com.bvilela.listbuilder.util.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscourseWriterService {

    private final AppProperties properties;

    private static final String SEND = "send";
    private static final String RECEIVE = "receive";
    private static final ListTypeEnum LIST_TYPE = ListTypeEnum.DISCURSO;

    private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl(LIST_TYPE);

    @SneakyThrows
    public Path writerPDF(DiscourseWriterDTO dto) {
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

    public LocalDate getBaseDate(DiscourseWriterDTO dto) {
        if (!AppUtils.listIsNullOrEmpty(dto.getReceive())) {
            return dto.getReceive().get(0).getDate();
        }
        return dto.getSend().get(0).getDate();
    }

    @SneakyThrows
    private void writerDocument(DiscourseWriterDTO dto, Path path) {

        try (var outputStream = new FileOutputStream(path.toString())) {
            Document document = pdfUtils.getDocument();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            pdfUtils.addImageHeader(document);
            document.add(pdfUtils.createEmptyParagraph());

            int numberOfColumns = getNumberOfColumns(dto);
            PdfPTable table = pdfUtils.getTable(document, numberOfColumns, LIST_TYPE);

            addReceiveSendHeaders(dto, table);

            var maxItens = getBiggestList(dto);

            for (int i = 0; i < maxItens; i++) {
                addItem(dto.getReceive(), table, i);
                addItem(dto.getSend(), table, i);

                if (!AppUtils.listIsNullOrEmpty(dto.getReceive())
                        && !AppUtils.listIsNullOrEmpty(dto.getSend())) {
                    addBlankRow(table, 20);
                }
            }

            document.add(table);
            document.close();
        }
    }

    @SneakyThrows
    private void addReceiveSendHeaders(DiscourseWriterDTO dto, PdfPTable table) {
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

    private int getNumberOfColumns(DiscourseWriterDTO dto) {
        int numberOfColumns = 0;
        if (!AppUtils.listIsNullOrEmpty(dto.getReceive())) {
            numberOfColumns++;
        }
        if (!AppUtils.listIsNullOrEmpty(dto.getSend())) {
            numberOfColumns++;
        }
        return numberOfColumns;
    }

    private void addItem(List<DiscourseWriterItemDTO> list, PdfPTable table, int index) {
        if (AppUtils.listIsNullOrEmpty(list)) {
            addBlankRow(table, 20);
            return;
        }

        if (index < list.size()) {
            addItem(table, list.get(index));
        } else {
            addBlankCell(table);
        }
    }

    private int getBiggestList(DiscourseWriterDTO dto) {
        if (AppUtils.listIsNullOrEmpty(dto.getReceive())) {
            return dto.getSend().size();
        }
        if (AppUtils.listIsNullOrEmpty(dto.getSend())) {
            return dto.getReceive().size();
        }
        return Math.max(dto.getReceive().size(), dto.getSend().size());
    }

    @SneakyThrows
    private PdfPCell createCellSubHeader(String subHeader) {
        if (Objects.isNull(subHeader)
                || subHeader.isBlank()
                || !List.of(SEND, RECEIVE).contains(subHeader)) {
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

    private void addItem(PdfPTable table, DiscourseWriterItemDTO dto) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(
                pdfUtils.createParagraphBold12Normal12(
                        "Data: ", DateUtils.formatDDMMMM(dto.getDate())));

        if (dto.getPresident() != null) {
            cell.addElement(
                    pdfUtils.createParagraphBold12Normal12("Presidente: ", dto.getPresident()));
        }

        cell.addElement(pdfUtils.createParagraphBold12Normal12("Tema: ", getThemeLabel(dto)));
        cell.addElement(pdfUtils.createParagraphBold12Normal12("Orador: ", dto.getSpeaker()));
        cell.addElement(
                pdfUtils.createParagraphBold12Normal12("Congregação: ", dto.getCongregation()));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(15);
        cell.setPaddingRight(25);
        table.addCell(cell);
    }

    private String getThemeLabel(DiscourseWriterItemDTO dto) {
        return dto.getThemeTitle().equals("?") ? "" : dto.getThemeTitle();
    }
}
