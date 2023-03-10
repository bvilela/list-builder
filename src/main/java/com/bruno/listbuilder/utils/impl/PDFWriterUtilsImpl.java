package com.bruno.listbuilder.utils.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.bruno.listbuilder.config.SizeBase;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.AppUtils;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.WriterUtils;
import com.itextpdf.text.BadElementException;
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

public final class PDFWriterUtilsImpl implements WriterUtils<Document> {

	private static final FontFamily FONT_FAMILY = FontFamily.TIMES_ROMAN;
	private static final Font NORMAL12 = new Font(FONT_FAMILY, 12);
	private static final Font NORMAL13 = new Font(FONT_FAMILY, 13);
	private static final Font NORMAL14 = new Font(FONT_FAMILY, 14);
	private static final Font BOLD12 = new Font(FONT_FAMILY, 12, Font.BOLD);
	private static final Font BOLD13 = new Font(FONT_FAMILY, 13, Font.BOLD);
	private static final Font BOLD14 = new Font(FONT_FAMILY, 14, Font.BOLD);
	private static final Font BOLD16 = new Font(FONT_FAMILY, 16, Font.BOLD);
	
	@Override
	public Document getDocument(ListTypeEnum list) {
		var mg = list.getPageMg();
		return new Document(PageSize.A4, mg.getLeft(), mg.getRight(), mg.getTop(), mg.getBottom());
	}

	public Paragraph createParagraphNormal(String message) {
		return new Paragraph(message, NORMAL12);
	}

	public Paragraph createParagraphBold12(String message) {
		return new Paragraph(message, BOLD12);
	}

	public Paragraph createParagraphBold14(String message) {
		return new Paragraph(message, BOLD14);
	}
	
	public Paragraph createParagraphBold16(String message) {
		return new Paragraph(message, BOLD16);
	}

	public Paragraph createParagraphBold12Normal14(String boldText, String normalText) {
		return createParagraphBoldNormal(boldText, BOLD12, normalText, NORMAL14);
	}

	public Paragraph createParagraphBold12Normal12(String boldText, String normalText) {
		return createParagraphBoldNormal(boldText, BOLD12, normalText, NORMAL12);
	}
	
	public Paragraph createParagraphBold13Normal13(String boldText, String normalText) {
		return createParagraphBoldNormal(boldText, BOLD13, normalText, NORMAL13);
	}

	public PdfPCell newCellNoBorder(Paragraph p) {
		var cell = new PdfPCell(p);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}

	private Paragraph createParagraphBoldNormal(String boldText, Font boldFont, String normalText, Font normalFont) {
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
	public void addImageHeader(Document document, ListTypeEnum listType) throws ListBuilderException {
		try {
			var imageUrl = FileUtils.getClassPathImageHeader(listType).getURL();
			Image image = getImage(listType.getHeader(), imageUrl);
			document.add(image);
			document.add(new Paragraph(" "));

		} catch (DocumentException | IOException e) {
			throw new ListBuilderException("Erro ao adicionar Imagem do Cabe??alho. Erro: %s", e.getMessage());
		}
	}

	public PdfPCell addImageSubHeader(ListTypeEnum listType, String imgName) throws ListBuilderException {
		try {
			var imageUrl = FileUtils.getClassPathImage(listType, imgName).getURL();
			Image image = getImage(listType.getSubHeader(), imageUrl);
			
			var size = listType.getSubHeader();
			image.scaleAbsolute(size.getWidth(), size.getHeight());
			image.setAlignment(Image.MIDDLE);
			
			var cell = new PdfPCell(image);
			cell.setBorder(Rectangle.NO_BORDER);
			
			return cell;
			
		} catch (DocumentException | IOException e) {
			throw new ListBuilderException("Erro ao adicionar Imagem do SubCabe??alho. Erro: %s", e.getMessage());
		}
	}
	
	public void addImageSubHeader(Document document, ListTypeEnum listType, String imgName)
			throws ListBuilderException {
		try {
			var imageUrl = FileUtils.getClassPathImage(listType, imgName).getURL();
			Image image = getImage(listType.getSubHeader(), imageUrl);
			document.add(image);
			document.add(new Paragraph(" "));
		} catch (DocumentException | IOException e) {
			throw new ListBuilderException("Erro ao adicionar Imagem do Cabe??alho. Erro: %s", e.getMessage());
		}
	}
	
	private Image getImage(SizeBase sizeBase, URL imageUrl) throws BadElementException, IOException {
		Image image = Image.getInstance(imageUrl);
		image.scaleAbsolute(sizeBase.getWidth(), sizeBase.getHeight());
		image.setAlignment(Image.MIDDLE);
		return image;
	}

	public void convertDocumentToImage(Path path) throws IOException {
		File newFile = new File(path.toUri());
		PDDocument pdfDocument = PDDocument.load(newFile);

		PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
		var numberOfPages = pdfDocument.getNumberOfPages();
		
		if (numberOfPages == 1) {
			BufferedImage img = pdfRenderer.renderImage(0);
			ImageIO.write(img, "JPEG", new File(path.toString().replace(".pdf", ".png")));
		}
		else {
			for (int i = 0; i < numberOfPages; i++) {
				BufferedImage img = pdfRenderer.renderImage(i);
				var pageNumber = StringUtils.leftPad(String.valueOf(i+1), 2, "0");
				var finalName = String.format("-pag%s.png", pageNumber);
				ImageIO.write(img, "JPEG", new File(path.toString().replace(".pdf", finalName)));
			}			
		}

		pdfDocument.close();
	}
	
	public PdfPTable getTable(Document document, int numberColumns, ListTypeEnum listType) throws DocumentException {
		float horizontalMarginsDiscount = AppUtils.getHorizontalMargins(listType);
		var columnWidth = (document.getPageSize().getWidth() - horizontalMarginsDiscount) / numberColumns;
		float[] columnsWidth = new float[numberColumns];
		
		for (int i = 0; i < numberColumns; i++) {
			columnsWidth[i] = columnWidth;
		}
		
		PdfPTable table = new PdfPTable(columnsWidth.length);
		table.setTotalWidth(columnsWidth);
		table.setLockedWidth(true);
		return table;
	}

}
