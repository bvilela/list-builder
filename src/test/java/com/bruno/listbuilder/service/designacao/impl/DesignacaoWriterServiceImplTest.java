package com.bruno.listbuilder.service.designacao.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestUtils;

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
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFCongressoAssembleiaVisitaSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
		dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
		dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
		assertDoesNotThrow(() -> service.writerPDF(dto));
		assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
	}
	
	@Test
	void shouldWriterPDFException() throws ListBuilderException, IllegalAccessException {
		FieldUtils.writeField(properties, "outputDir", null, true);
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		var ex = assertThrows(ListBuilderException.class, () -> service.writerPDF(dto));
		assertTrue(ex.getMessage().contains("Erro ao Gerar PDF - Erro"));
	}

	@Test
	void shouldWriterDocxSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		assertDoesNotThrow(() -> service.writerDocx(dto));
	}
	
	@Test
	void shouldWriterDocxCongressoAssembleiaVisitaSuccess() throws ListBuilderException {
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		dto.getAudioVideo().get(0).setName("Teste 1 (congresso)");
		dto.getAudioVideo().get(1).setName("Teste 2 (assembleia)");
		dto.getAudioVideo().get(2).setName("Teste 3 (visita)");
		assertDoesNotThrow(() -> service.writerDocx(dto));
		assertFalse(dto.getAudioVideo().get(0).toString().isEmpty());
	}
	
	@Test
	void shouldWriterDocxException() throws ListBuilderException, IllegalAccessException {
		FieldUtils.writeField(properties, "outputDir", null, true);
		DesignacaoWriterDTO dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		var ex = assertThrows(ListBuilderException.class, () -> service.writerDocx(dto));
		assertTrue(ex.getMessage().contains("Erro ao Gerar Docx - Erro"));
	}
	
}
