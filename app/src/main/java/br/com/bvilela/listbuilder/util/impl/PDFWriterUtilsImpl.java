package br.com.bvilela.listbuilder.util.impl;

import br.com.bvilela.listbuilder.config.SizeBase;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.util.AppUtils;
import br.com.bvilela.listbuilder.util.FileUtils;
import br.com.bvilela.listbuilder.util.WriterUtils;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import lombok.SneakyThrows;

public final class PDFWriterUtilsImpl implements WriterUtils<Document> {

    private static final FontFamily FONT_FAMILY = FontFamily.TIMES_ROMAN;
    private static final Font NORMAL12 = new Font(FONT_FAMILY, 12);
    private static final Font NORMAL13 = new Font(FONT_FAMILY, 13);
    private static final Font NORMAL14 = new Font(FONT_FAMILY, 14);
    private static final Font BOLD12 = new Font(FONT_FAMILY, 12, Font.BOLD);
    private static final Font BOLD13 = new Font(FONT_FAMILY, 13, Font.BOLD);
    private static final Font BOLD14 = new Font(FONT_FAMILY, 14, Font.BOLD);
    private static final Font BOLD16 = new Font(FONT_FAMILY, 16, Font.BOLD);

    private final ListTypeEnum listType;

    public PDFWriterUtilsImpl(ListTypeEnum listType) {
        this.listType = listType;
    }

    @Override
    public Document getDocument() {
        var pageMg = listType.getPageMg();
        return new Document(
                PageSize.A4,
                pageMg.getLeft(),
                pageMg.getRight(),
                pageMg.getTop(),
                pageMg.getBottom());
    }

    public Paragraph createEmptyParagraph() {
        return new Paragraph(" ");
    }

    public Paragraph createParagraphNormal(String text) {
        return new Paragraph(text, NORMAL12);
    }

    public Paragraph createParagraphBold12(String text) {
        return new Paragraph(text, BOLD12);
    }

    public Paragraph createParagraphBold14(String text) {
        return new Paragraph(text, BOLD14);
    }

    public Paragraph createParagraphBold16(String text) {
        return new Paragraph(text, BOLD16);
    }

    public Paragraph createParagraphBold12Normal12(String boldText, String normalText) {
        return createParagraphBoldNormal(boldText, BOLD12, normalText, NORMAL12);
    }

    public Paragraph createParagraphBold12Normal14(String boldText, String normalText) {
        return createParagraphBoldNormal(boldText, BOLD12, normalText, NORMAL14);
    }

    public Paragraph createParagraphBold13Normal13(String boldText, String normalText) {
        return createParagraphBoldNormal(boldText, BOLD13, normalText, NORMAL13);
    }

    public PdfPCell newCellNoBorder(Paragraph paragraph) {
        var cell = new PdfPCell(paragraph);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private Paragraph createParagraphBoldNormal(
            String boldText, Font boldFont, String normalText, Font normalFont) {
        Chunk chunkBold = new Chunk(boldText, boldFont);
        Chunk chunkNormal = new Chunk(normalText, normalFont);

        Phrase phrase = new Phrase();
        phrase.add(chunkBold);
        phrase.add(chunkNormal);

        var paragraph = new Paragraph();
        paragraph.add(phrase);

        return paragraph;
    }

    @Override
    @SneakyThrows
    public void addImageHeader(Document document) {
        try {
            var imageUrl = FileUtils.getClassPathImageHeader(listType).getURL();
            Image image = getImage(listType.getHeader(), imageUrl);
            document.add(image);

        } catch (DocumentException | IOException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Imagem do Cabeçalho. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    public PdfPCell addImageSubHeader(ListTypeEnum listType, String imgName) {
        try {
            var imageUrl = FileUtils.getClassPathImage(listType, imgName).getURL();
            Image image = getImage(listType.getSubHeader(), imageUrl);

            var size = listType.getSubHeader();
            image.scaleAbsolute(size.getWidth(), size.getHeight());
            image.setAlignment(Image.MIDDLE);

            var cell = new PdfPCell(image);
            cell.setBorder(Rectangle.NO_BORDER);

            return cell;

        } catch (IOException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Imagem do SubCabeçalho. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    public void addImageSubHeader(Document document, ListTypeEnum listType, String imgName) {
        try {
            var imageUrl = FileUtils.getClassPathImage(listType, imgName).getURL();
            Image image = getImage(listType.getSubHeader(), imageUrl);
            document.add(image);
            document.add(new Paragraph(" "));
        } catch (DocumentException | IOException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Imagem do Cabeçalho. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private Image getImage(SizeBase sizeBase, URL imageUrl) {
        Image image = Image.getInstance(imageUrl);
        image.scaleAbsolute(sizeBase.getWidth(), sizeBase.getHeight());
        image.setAlignment(Image.MIDDLE);
        return image;
    }

    @SneakyThrows
    public PdfPTable getTable(Document document, int numberColumns, ListTypeEnum listType) {
        float horizontalMarginsDiscount = AppUtils.getHorizontalMargins(listType);
        var columnWidth =
                (document.getPageSize().getWidth() - horizontalMarginsDiscount) / numberColumns;
        float[] columns = new float[numberColumns];

        Arrays.fill(columns, columnWidth);

        PdfPTable table = new PdfPTable(columns.length);
        table.setTotalWidth(columns);
        table.setLockedWidth(true);
        return table;
    }
}
