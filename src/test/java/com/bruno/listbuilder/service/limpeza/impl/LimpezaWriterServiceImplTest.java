package com.bruno.listbuilder.service.limpeza.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

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
		TestFileUtilsWriteFile.cleanResourceDir();
	}

	@Test
	void shouldWriterPDFLayout1Success() throws ListBuilderException {
		assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 1));
		assertFalse(dto.toString().isBlank());
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
		assertDoesNotThrow(() -> service.writerPDF(dto, "footerMessage", "headerMessage", 2));
		assertFalse(dto.toString().isBlank());
	}

}
