package br.com.bvilela.listbuilder.utils.impl;

import br.com.bvilela.listbuilder.config.SizeBase;
import br.com.bvilela.listbuilder.config.SizeConfig;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.utils.WriterUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

public final class DocxWriterUtilsImpl implements WriterUtils<XWPFDocument> {

    private final ListTypeEnum listType;

    public DocxWriterUtilsImpl(ListTypeEnum listType) {
        this.listType = listType;
    }

    @Override
    public XWPFDocument getDocument() {
        XWPFDocument doc = new XWPFDocument();
        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();

        // Setting PageSize A4 (595x842 Points) and Orient
        CTPageSz pageSize = sectPr.addNewPgSz();
        pageSize.setOrient(STPageOrientation.PORTRAIT);
        pageSize.setW(SizeConfig.DOCX_A4_WIDTH_POINT);
        pageSize.setH(SizeConfig.DOCX_A4_HEIGHT_POINT);

        // Setting Margin
        var pageMg = listType.getPageMg();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(convertBigInt(pageMg.getLeft()));
        pageMar.setTop(convertBigInt(pageMg.getTop()));
        pageMar.setRight(convertBigInt(pageMg.getRight()));
        pageMar.setBottom(convertBigInt(pageMg.getLeft()));

        return doc;
    }

    private BigInteger convertBigInt(float value) {
        return BigInteger.valueOf(Math.round(AppUtils.getSizePointTimesTwenty(value)));
    }

    @Override
    @SneakyThrows
    public void addImageHeader(XWPFDocument document) {
        try {
            var imgInputStream = FileUtils.getClassPathImageHeader(listType).getInputStream();
            File fileTmp = new File("file.tmp");
            try (OutputStream outputStream = new FileOutputStream(fileTmp)) {
                IOUtils.copy(imgInputStream, outputStream);
            }
            addImage(document.createParagraph(), fileTmp.toString(), listType.getHeader());

        } catch (IOException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Imagem do Cabeçalho. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    public void addImageSubHeader(XWPFParagraph paragraph, ListTypeEnum listType, String imgName) {
        try {
            var imgInputStream = FileUtils.getClassPathImage(listType, imgName).getInputStream();
            File fileTmp = new File("file.tmp");
            try (OutputStream outputStream = new FileOutputStream(fileTmp)) {
                IOUtils.copy(imgInputStream, outputStream);
            }
            addImage(paragraph, fileTmp.toString(), listType.getSubHeader());

        } catch (IOException e) {
            throw new ListBuilderException(
                    "Erro ao adicionar Imagem do Sub-Cabeçalho. Erro: %s", e.getMessage());
        }
    }

    @SneakyThrows
    private void addImage(XWPFParagraph paragraph, String imgFile, SizeBase size) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        // add image
        try (FileInputStream fis = new FileInputStream(imgFile)) {
            run.addPicture(
                    fis,
                    Document.PICTURE_TYPE_PNG,
                    imgFile,
                    Units.toEMU(size.getWidth()),
                    Units.toEMU(size.getHeight()));
        }
    }

    public void setParagraphBoldNormal(
            XWPFParagraph paragraph, String boldtext, String normalText) {
        this.setRunBold12(paragraph.createRun(), boldtext);
        paragraph.getRuns().get(0).addTab();
        this.setRunNormal12(paragraph.createRun(), normalText);
    }

    public void setParagraphBoldBold(XWPFParagraph paragraph, String text1, String text2) {
        this.setRunBold12(paragraph.createRun(), text1);
        paragraph.getRuns().get(0).addTab();
        this.setRunBold12(paragraph.createRun(), text2);
    }

    private void setRunBold12(XWPFRun run, String text) {
        this.setRun(run, 12, true, text);
    }

    private void setRunNormal12(XWPFRun run, String text) {
        this.setRun(run, 12, false, text);
    }

    private void setRun(XWPFRun run, int fontSize, boolean bold, String text) {
        run.setFontFamily("Times New Roman");
        run.setFontSize(fontSize);
        run.setBold(bold);
        run.setText(text);
    }

    public void setNoBordersTable(XWPFTable table) {
        table.getRows().forEach(r -> r.getTableCells().forEach(this::setNoBordersCell));
    }

    private void setNoBordersCell(XWPFTableCell cell) {
        CTTcBorders tblBorders = cell.getCTTc().addNewTcPr().addNewTcBorders();
        tblBorders.addNewLeft().setVal(STBorder.NIL);
        tblBorders.addNewRight().setVal(STBorder.NIL);
        tblBorders.addNewTop().setVal(STBorder.NIL);
        tblBorders.addNewBottom().setVal(STBorder.NIL);
    }
}
