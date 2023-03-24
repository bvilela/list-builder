package br.com.bvilela.listbuilder.service.designacao.impl;

import java.nio.file.Paths;

import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.utils.TestUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

@SpringBootApplication
class DesignacaoWriterServiceImplTest {
	
	@InjectMocks
	private DesignacaoWriterServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
		FieldUtils.writeField(properties, "outputDir", pathOutput, true);
		service = new DesignacaoWriterServiceImpl(properties);
	}
	
	@AfterAll
	static void setupAfterAll() {
		TestUtils.cleanResourceDir();
	}
	
	@Test
	void shouldWriterPDFSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFCongressoAssembleiaVisitaSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
		dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
		dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
		Assertions.assertDoesNotThrow(() -> service.writerPDF(dto));
		Assertions.assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
	}
	
	@Test
	void shouldWriterPDFException() throws ListBuilderException, IllegalAccessException {
		FieldUtils.writeField(properties, "outputDir", null, true);
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		var ex = Assertions.assertThrows(ListBuilderException.class, () -> service.writerPDF(dto));
		Assertions.assertTrue(ex.getMessage().contains("Erro ao Gerar PDF - Erro"));
	}

	@Test
	void shouldWriterDocxSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		Assertions.assertDoesNotThrow(() -> service.writerDocx(dto));
	}
	
	@Test
	void shouldWriterDocxCongressoAssembleiaVisitaSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
		dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
		dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
		Assertions.assertDoesNotThrow(() -> service.writerDocx(dto));
		Assertions.assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
	}
	
	@Test
	void shouldWriterDocxException() throws ListBuilderException, IllegalAccessException {
		FieldUtils.writeField(properties, "outputDir", null, true);
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		var ex = Assertions.assertThrows(ListBuilderException.class, () -> service.writerDocx(dto));
		Assertions.assertTrue(ex.getMessage().contains("Erro ao Gerar Docx - Erro"));
	}
	
}
