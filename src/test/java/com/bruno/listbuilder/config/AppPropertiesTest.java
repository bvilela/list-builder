package com.bruno.listbuilder.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class AppPropertiesTest {

	private static final String OUTPUT_DIR_VALUE = "outputDirValue";
	private static final String INPUT_DIR_VALUE = "inputDirValue";
	private static final int LAYOUT_LIMPEZA = 1;

	@InjectMocks
	private AppProperties properties;

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", INPUT_DIR_VALUE, true);
		FieldUtils.writeField(properties, "outputDir", OUTPUT_DIR_VALUE, true);
		FieldUtils.writeField(properties, "layoutLimpeza", LAYOUT_LIMPEZA, true);
	}

	@Test
	void shouldGetValues() {
		assertEquals(INPUT_DIR_VALUE, properties.getInputDir());
		assertEquals(OUTPUT_DIR_VALUE, properties.getOutputDir());
		assertEquals(LAYOUT_LIMPEZA, properties.getLayoutLimpeza());
	}
}
