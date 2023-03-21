package com.bruno.listbuilder.service.limpeza.impl;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
class LimpezaWriterServiceImplTest {

	@InjectMocks
	private LimpezaWriterServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	private FinalListLimpezaDTO dto;

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
		FieldUtils.writeField(properties, "outputDir", pathOutput, true);
		service = new LimpezaWriterServiceImpl(properties);
		dto = FinalListLimpezaDTO.builder()
				.items(List.of(new FinalListLimpezaItemDTO(LocalDate.now(), "label", "P1, P2"))).build();
	}
	
	@AfterAll
	static void setupAfterAll() {
		TestUtils.cleanResourceDir();
	}

	@Test
	void shouldWriterPDFLayout1Success() throws ListBuilderException {
		Assertions.assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 1));
		Assertions.assertFalse(dto.toString().isBlank());
	}

	@Test
	void shouldWriterPDFLayout2Success() throws ListBuilderException {
		dto = FinalListLimpezaDTO.builder().itemsLayout2(List.of(
				FinalListLimpezaItemLayout2DTO.builder().withGroup("P1, P2")
				.withDate1(LocalDate.now()).withLabel1("label1")
				.withDate2(LocalDate.now()).withLabel2("label2").build(),
				FinalListLimpezaItemLayout2DTO.builder().withGroup("P1, P2")
				.withDate1(LocalDate.now()).withLabel1("label1").build()
		)).build();
		Assertions.assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 2));
		Assertions.assertFalse(dto.toString().isBlank());
	}

}
