package com.bruno.listbuilder.service.discurso.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestUtils;

@SpringBootApplication
class DiscursoWriterServiceImplTest {
	
	@InjectMocks
	private DiscursoWriterServiceImpl service;

	@InjectMocks
	private AppProperties properties;
	
	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
		FieldUtils.writeField(properties, "outputDir", pathOutput, true);
		service = new DiscursoWriterServiceImpl(properties);
	}
	
	@AfterAll
	static void setupAfterAll() {
		TestUtils.cleanResourceDir();
	}
	
	@Test
	void shouldWriterPDFException() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getReceive().get(0).setDateConverted(null);
		var ex = assertThrows(ListBuilderException.class, () -> service.writerPDF(dto));
		assertTrue(ex.getMessage().contains("Erro ao Gerar PDF - Erro: "));
	}
	
	@Test
	void shouldWriterPDFSuccessSendReceive() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFSuccessSendOnly() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setReceive(null);
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFSuccessReceiveOnly() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setSend(null);
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFReceiveWithWildcardSuccess() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getReceive().get(0).setThemeNumber(null);
		dto.getReceive().get(0).setThemeTitle("?");
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFSendWithWildcardSuccess() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.getSend().get(0).setThemeNumber(null);
		dto.getSend().get(0).setThemeTitle("?");
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFSuccessReceiveListSmaller() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		var newList = new ArrayList<>(dto.getReceive());
		newList.remove(0);
		dto.setReceive(newList);
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldWriterPDFSuccessSendListSmaller() throws ListBuilderException {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		var newList = new ArrayList<>(dto.getSend());
		newList.remove(0);
		dto.setSend(newList);
		assertDoesNotThrow(() -> service.writerPDF(dto));
	}
	
	@Test
	void shouldGetBaseDateSendNull() {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setSend(null);
		dto.getReceive().get(0).setDateConverted(LocalDate.of(2022, 1, 1));
		assertEquals(LocalDate.of(2022, 1, 1), service.getBaseDate(dto));
	}
	
	@Test
	void shouldGetBaseDateReceiveNull() {
		var dto = FileInputDataDiscursoDtoBuilder.create().withRandomData().build();
		dto.setReceive(null);
		dto.getSend().get(0).setDateConverted(LocalDate.of(2022, 2, 1));
		assertEquals(LocalDate.of(2022, 2, 1), service.getBaseDate(dto));
	}
}
