package com.bruno.listbuilder.service.limpeza.impl;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.limpeza.LimpezaWriterService;
import com.bruno.listbuilder.utils.DateUtils;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.impl.PDFWriterUtilsImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class LimpezaWriterServiceImpl implements LimpezaWriterService {
	
	private AppProperties properties;
	
	private static final ListTypeEnum LIST_TYPE = ListTypeEnum.LIMPEZA;
	
	private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();
	
	@Autowired
	public LimpezaWriterServiceImpl(AppProperties properties) {
		this.properties = properties;
	}
	
	@Override
	public Path writerPDF(FinalListLimpezaDTO dto, String footerMessage, String headerMessage, int layout) throws ListBuilderException {
		
		FileUtils.createDirectories(properties.getOutputDir());
		
		LocalDate dateBase = dto.getItems() != null ? dto.getItems().get(0).getDate() : dto.getItemsLayout2().get(0).getDate2();
		String fileName = FileUtils.generateOutputFileNamePDF(LIST_TYPE, dateBase);
		Path path = Paths.get(properties.getOutputDir(), fileName);
		
        try (var outputStream = new FileOutputStream(path.toString())) {
        	
        	Document document = pdfUtils.getDocument(LIST_TYPE);
            PdfWriter.getInstance(document, outputStream);
            
            document.open();
            
            pdfUtils.addImageHeader(document, LIST_TYPE);
            addImageMonth(document, LIST_TYPE, dateBase);
            this.addHeaderMessage(document, headerMessage);
            
            if (layout == 2) {
            	bodyLayout2(dto, document);	
            } else {
            	bodyLayout1(dto, document);
            }
            
            this.addFooterMessage(document, footerMessage);

            document.close();
            
            return path;

        } catch(Exception e) {
            throw new ListBuilderException("Erro ao Gerar PDF - Erro: %s", e.getMessage());
        }
	}
	
	private void addImageMonth(Document document, ListTypeEnum listType, LocalDate date) throws ListBuilderException {
		try {
			var month = String.format("%02d", date.getMonthValue());
			var imageMonthName = String.format("month_%s.jpg", month);
			pdfUtils.addImageSubHeader(document, listType, imageMonthName);

		} catch (Exception e) {
			throw new ListBuilderException("Erro ao adicionar Imagem do nome do Mes. Erro: %s", e.getMessage());
		}
	}

	private void bodyLayout1(FinalListLimpezaDTO dto, Document document) throws DocumentException {
		for (FinalListLimpezaItemDTO item : dto.getItems()) {
			
			var paragraphGroup = pdfUtils.createParagraphBold13Normal13("Grupo: ", item.getGroup());
			document.add(paragraphGroup);
			
			String dateFormatted = this.getDateDayOfWeekLabel(item.getDate(), item.getLabel());			
			var paragraphDate = pdfUtils.createParagraphBold13Normal13("Data: ", dateFormatted);			
			document.add(paragraphDate);
			document.add(new Paragraph(" "));
			
		}
	}
	
	private void bodyLayout2(FinalListLimpezaDTO dto, Document document) throws DocumentException {
		for (FinalListLimpezaItemLayout2DTO item : dto.getItemsLayout2()) {
			
			var paragraphGroup = pdfUtils.createParagraphBold13Normal13("Grupo: ", item.getGroup());
			document.add(paragraphGroup);
			
			String dateFormatted1 = this.getDateDayOfWeekLabel(item.getDate1(), item.getLabel1());
			String dateFormatted2 = null;
			Paragraph paragraph2 = null;
			
			if (Objects.isNull(item.getDate2())) {
				paragraph2 = pdfUtils.createParagraphBold13Normal13("Datas: ", dateFormatted1);
			} else {
				dateFormatted2 = this.getDateDayOfWeekLabel(item.getDate2(), item.getLabel2());
				paragraph2 = pdfUtils.createParagraphBold13Normal13("Datas: ", String.format("%s e %s", dateFormatted1, dateFormatted2));
			}
			
			document.add(paragraph2);
			document.add(new Paragraph(" "));
		}
	}
	
	private String getDateDayOfWeekLabel(LocalDate date, String label) {
		return String.format("%s (%s)", DateUtils.format(date), label);
	}
	
	private void addHeaderMessage(Document document, String headerMessage) throws ListBuilderException {
		try {
			if (Objects.nonNull(headerMessage) && !headerMessage.isBlank()) {
				var paragraph = pdfUtils.createParagraphBold14(headerMessage);
				paragraph.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph);
				document.add(new Paragraph(" "));
			}
		} catch (DocumentException e) {
			throw new ListBuilderException("Erro ao adicionar Mensagem no Cabecalho. Erro: %s", e.getMessage());
		}
	}

	
	private void addFooterMessage(Document document, String footerMessage) throws ListBuilderException {
		try {
			if (Objects.nonNull(footerMessage) && !footerMessage.isBlank()) {
				document.add(pdfUtils.createParagraphBold12(footerMessage));
			}
		} catch (DocumentException e) {
			throw new ListBuilderException("Erro ao adicionar Mensagem no Rodape. Erro: %s", e.getMessage());
		}
	}

}
