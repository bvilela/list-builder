package br.com.bvilela.listbuilder.service.clearing;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemLayout2DTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.util.FileUtils;
import br.com.bvilela.listbuilder.util.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimpezaWriterService {

    private final AppProperties properties;

    private static final ListTypeEnum LIST_TYPE = ListTypeEnum.LIMPEZA;

    private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl(LIST_TYPE);

    @SneakyThrows
    public Path writerPDF(
            ClearingWriterDTO dto, String footerMessage, String headerMessage, int layout) {

        FileUtils.createDirectories(properties.getOutputDir());

        LocalDate dateBase =
                dto.getItems() != null
                        ? dto.getItems().get(0).getDate()
                        : dto.getItemsLayout2().get(0).getDate2();
        String fileName = FileUtils.generateOutputFileNamePDF(LIST_TYPE, dateBase);
        Path path = Paths.get(properties.getOutputDir(), fileName);

        try (var outputStream = new FileOutputStream(path.toString())) {

            Document document = pdfUtils.getDocument();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            pdfUtils.addImageHeader(document);
            addImageMonth(document, dateBase);
            this.addHeaderMessage(document, headerMessage);

            if (layout == 2) {
                bodyLayout2(dto, document);
            } else {
                bodyLayout1(dto, document);
            }

            this.addFooterMessage(document, footerMessage);

            document.close();

            return path;

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private void addImageMonth(Document document, LocalDate date) {
        try {
            var month = String.format("%02d", date.getMonthValue());
            var imageMonthName = String.format("month_%s.jpg", month);
            pdfUtils.addImageSubHeader(document, LIST_TYPE, imageMonthName);

        } catch (Exception e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Imagem do nome do Mes. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private void bodyLayout1(ClearingWriterDTO dto, Document document) {
        for (ClearingWriterItemDTO item : dto.getItems()) {

            var paragraphGroup = pdfUtils.createParagraphBold13Normal13("Grupo: ", item.getGroup());
            document.add(paragraphGroup);

            String dateFormatted = this.getDateDayOfWeekLabel(item.getDate(), item.getLabel());
            var paragraphDate = pdfUtils.createParagraphBold13Normal13("Data: ", dateFormatted);
            document.add(paragraphDate);
            document.add(new Paragraph(" "));
        }
    }

    @SneakyThrows
    private void bodyLayout2(ClearingWriterDTO dto, Document document) {
        for (ClearingWriterItemLayout2DTO item : dto.getItemsLayout2()) {

            var paragraphGroup = pdfUtils.createParagraphBold13Normal13("Grupo: ", item.getGroup());
            document.add(paragraphGroup);

            String dateFormatted1 = this.getDateDayOfWeekLabel(item.getDate1(), item.getLabel1());
            String dateFormatted2;
            Paragraph paragraph2;

            if (Objects.isNull(item.getDate2())) {
                paragraph2 = pdfUtils.createParagraphBold13Normal13("Datas: ", dateFormatted1);
            } else {
                dateFormatted2 = this.getDateDayOfWeekLabel(item.getDate2(), item.getLabel2());
                paragraph2 =
                        pdfUtils.createParagraphBold13Normal13(
                                "Datas: ",
                                String.format("%s e %s", dateFormatted1, dateFormatted2));
            }

            document.add(paragraph2);
            document.add(new Paragraph(" "));
        }
    }

    private String getDateDayOfWeekLabel(LocalDate date, String label) {
        return String.format("%s (%s)", DateUtils.format(date), label);
    }

    @SneakyThrows
    private void addHeaderMessage(Document document, String headerMessage) {
        try {
            if (Objects.nonNull(headerMessage) && !headerMessage.isBlank()) {
                var paragraph = pdfUtils.createParagraphBold14(headerMessage);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
                document.add(new Paragraph(" "));
            }
        } catch (DocumentException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Mensagem no Cabecalho. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private void addFooterMessage(Document document, String footerMessage) {
        try {
            if (Objects.nonNull(footerMessage) && !footerMessage.isBlank()) {
                document.add(pdfUtils.createParagraphBold12(footerMessage));
            }
        } catch (DocumentException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Mensagem no Rodape. Erro: %s", e.getMessage());
        }
    }
}
