package com.bruno.listbuilder.service.impl;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.utils.impl.PDFWriterUtilsImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConvertImageServiceImpl implements ConvertImageService {
	
	@Value("${convert.pdf.to.image:false}")
	private boolean convertPdfToImage;
	
	private final PDFWriterUtilsImpl pdfUtils = new PDFWriterUtilsImpl();

	@Override
	public void convertToImage(Path path) throws ListBuilderException {
		try {
			if (this.convertPdfToImage) {
				log.info("Convertendo PDF para PNG...");
				pdfUtils.convertDocumentToImage(path);
				log.info("Conversao concluida com sucesso!");
			}
		} catch(Exception e) {
            throw new ListBuilderException("Erro ao Convert PDF para Imagem - Erro: %s", e.getMessage());
        }
	}

}
