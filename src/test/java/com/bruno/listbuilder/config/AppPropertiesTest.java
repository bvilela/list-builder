package com.bruno.listbuilder.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class AppPropertiesTest {

	private static final String FILE_NAME_LIMPEZA_VALUE = "inputFileNameLimpezaValue";
	private static final String OUTPUT_DIR_VALUE = "outputDirValue";
	private static final String INPUT_DIR_VALUE = "inputDirValue";
	private static final int LAYOUT_LIMPEZA = 1;
	private static final String FILE_NAME_ASSISTENCIA_VALUE = "inputFileNameAssistenciaValue";
	private static final String FILE_NAME_DISCURSOS_VALUE = "inputFileNameDiscursosValue";
	private static final String FILE_NAME_DISCURSOS_TEMAS_VALUE = "inputFileNameDiscursosTemasValue";
	private static final String FILE_NAME_VIDA_CRISTA_VALUE = "inputFileNameVidaCristaValue";

	@InjectMocks
	private AppProperties properties;

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", INPUT_DIR_VALUE, true);
		FieldUtils.writeField(properties, "outputDir", OUTPUT_DIR_VALUE, true);
		FieldUtils.writeField(properties, "inputFileNameLimpeza", FILE_NAME_LIMPEZA_VALUE, true);
		FieldUtils.writeField(properties, "layoutLimpeza", LAYOUT_LIMPEZA, true);
		FieldUtils.writeField(properties, "inputFileNameAssistencia", FILE_NAME_ASSISTENCIA_VALUE, true);
		FieldUtils.writeField(properties, "inputFileNameDiscursos", FILE_NAME_DISCURSOS_VALUE, true);
		FieldUtils.writeField(properties, "inputFileNameDiscursosTemas", FILE_NAME_DISCURSOS_TEMAS_VALUE, true);
		FieldUtils.writeField(properties, "inputFileNameVidaCrista", FILE_NAME_VIDA_CRISTA_VALUE, true);
	}

	@Test
	void shouldGetValues() {
		assertEquals(INPUT_DIR_VALUE, properties.getInputDir());
		assertEquals(OUTPUT_DIR_VALUE, properties.getOutputDir());
		assertEquals(FILE_NAME_LIMPEZA_VALUE, properties.getInputFileNameLimpeza());
		assertEquals(1, properties.getLayoutLimpeza());
		assertEquals(FILE_NAME_ASSISTENCIA_VALUE, properties.getInputFileNameAssistencia());
		assertEquals(FILE_NAME_DISCURSOS_VALUE, properties.getInputFileNameDiscursos());
		assertEquals(FILE_NAME_DISCURSOS_TEMAS_VALUE, properties.getInputFileNameDiscursosTemas());
		assertEquals(FILE_NAME_VIDA_CRISTA_VALUE, properties.getInputFileNameVidaCrista());
	}
}
