package com.bruno.listbuilder.service.assistencia.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.utils.TestUtils;

@SpringBootApplication
class AssistenciaWriterServiceImplTest {
	
	@InjectMocks
	private AssistenciaWriterServiceImpl service;

	@InjectMocks
	private AppProperties properties;
	
	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		String pathOutput = Paths.get("src", "test", "resources").toFile().getAbsolutePath();
		FieldUtils.writeField(properties, "outputDir", pathOutput, true);
		service = new AssistenciaWriterServiceImpl(properties);
	}
	
	@AfterAll
	static void setupAfterAll() {
		TestUtils.cleanResourceDir();
	}
	
	@Test
	void shouldWriterPDFSuccess() throws ListBuilderException {
		// @formatter:off
		var list = List.of(
				cld(4, 2),  cld(4, 4),  cld(4, 8), cld(4, 12),
				cld(4, 16), cld(4, 19), cld(4, 8), cld(4, 23),
				cld(4, 26), cld(4, 19), cld(4, 30));
		// @formatter:on
		assertDoesNotThrow(() -> service.writerPDF(list));
	}
	
	/** Create Local Date */
	private LocalDate cld(int month, int day) {
		return LocalDate.of(2022, month, day);
	}

}
