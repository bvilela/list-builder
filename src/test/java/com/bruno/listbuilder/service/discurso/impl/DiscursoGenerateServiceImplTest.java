package com.bruno.listbuilder.service.discurso.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.DiscursoAllThemesDtoBuilder;
import com.bruno.listbuilder.builder.FileInputDataDiscursoDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.BaseGenerateServiceTest;
import com.bruno.listbuilder.service.ConvertImageService;

@SpringBootApplication
class DiscursoGenerateServiceImplTest
		extends BaseGenerateServiceTest<FileInputDataDiscursoDTO, FileInputDataDiscursoDtoBuilder> {

	@InjectMocks
	private DiscursoGenerateServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@Mock
	private DiscursoWriterServiceImpl writerService;

	@Mock
	private ConvertImageService convertImageService;

	private static final String MSG_MISSING_THEME_NUMBER_TITLE = "Data: 05-06-2022 - Informe o Número do Tema ou Título!";

	public DiscursoGenerateServiceImplTest() throws ListBuilderException {
		super(ListTypeEnum.DISCURSO, FileInputDataDiscursoDtoBuilder.create().withRandomData());
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", testUtils.getResourceDirectory(), true);
		service = new DiscursoGenerateServiceImpl(properties, writerService, convertImageService);
		createFileThemes();
	}
	
	private void createFileThemes() {
		testUtils.writeFileInputDiscursoAllThemes(DiscursoAllThemesDtoBuilder.create().withRandomData().build());
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getListType());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(testUtils.getListType(), service.getListType());
	}
	
	@Test
	void shouldCorrectFileInputName() {
		assertEquals("dados-discurso.json", service.getListType().getInputFileName());
	}

	@Test
	void shouldGenerateListFileAllThemesInvalidPathFileException() throws IllegalAccessException {
		createFileInputDataOK();
		testUtils.cleanDirectory();
		validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileAllThemesSintaxeException() throws IllegalAccessException {
		createFileInputDataOK();
		testUtils.writeFileInputDiscursoAllThemesSyntaxError();
		validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
	}

	@Test
	void shouldGenerateListFileAllThemesNullException() throws IllegalAccessException {
		createFileInputDataOK();
		testUtils.writeFileInputDiscursoAllThemes(DiscursoAllThemesDtoBuilder.create().withNullData().build());
		validateListBuilderException(MessageConfig.THEMES_REQUIRED);
	}

	@Test
	void shouldGenerateListFileAllThemesEmptyException() throws IllegalAccessException {
		createFileInputDataOK();
		testUtils.writeFileInputDiscursoAllThemes(DiscursoAllThemesDtoBuilder.create().withEmptyData().build());
		validateListBuilderException(MessageConfig.THEMES_REQUIRED);
	}

	private void createFileInputDataOK() {
		writeFileInputFromDto(builder.build());
	}

	@Test
	void shouldGenerateListFileInputInvalidPathException() throws IllegalAccessException {
		validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileInputSintaxeException() throws IllegalAccessException {
		testUtils.writeFileInputSyntaxError();
		validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
	}

	@Test
	void shouldGenerateListFileInputThemeNumberTitleNullException() throws IllegalAccessException {
		final String themeNumber = null;
		final String themeTitle = null;
		final String expectedMessageError = MSG_MISSING_THEME_NUMBER_TITLE;
		validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
	}

	@Test
	void shouldGenerateListFileInputThemeNumberTitleBlankException() throws IllegalAccessException {
		final String themeNumber = " ";
		final String themeTitle = " ";
		final String expectedMessageError = MSG_MISSING_THEME_NUMBER_TITLE;
		validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
	}

	@Test
	void shouldGenerateListFileInputThemeNumberInvalidTitleNullException() throws IllegalAccessException {
		final String themeNumber = "ABC";
		final String themeTitle = " ";
		final String expectedMessageError = "05-06-2022 - Numero do Tema invalido. Não é um número válido!";
		validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
	}

	@Test
	void shouldGenerateListFileInputThemeNumberNotFoundException() throws IllegalAccessException {
		final String themeNumber = "1234";
		final String themeTitle = null;
		final String expectedMessageError = "Nenhum tema encontrada para o Número: 1234";
		validateFileInputThemeNumberTitleException(themeNumber, themeTitle, expectedMessageError);
	}

	private void validateFileInputThemeNumberTitleException(String themeNumber, String themeTitle,
			String expectedMessageError) throws IllegalAccessException {
		var dto = builder.build();
		dto.getReceive().get(0).setThemeNumber(themeNumber);
		dto.getReceive().get(0).setThemeTitle(themeTitle);
		writeFileInputFromDto(dto);
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListFileInputSendAndReceiveNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withRandomData().withReceive(null).withSend(null).build());
		validateListBuilderException(MessageConfig.LIST_SEND_REVEICE_NULL);
	}

	@Test
	void shouldGenerateListSendReceiveSuccessByThemeNumberWithThemeTitleNull() throws IllegalAccessException {
		var dto = builder.build();
		dto.getReceive().get(0).setThemeNumber("1");
		dto.getReceive().get(0).setThemeTitle(null);
		validateGenerateListSuccess(dto);
	}

	@Test
	void shouldGenerateListReceiveSuccessByThemeNumberWithThemeTitleNull() throws IllegalAccessException {
		var dto = builder.build();
		dto.setSend(null);
		dto.getReceive().get(0).setThemeNumber("1");
		dto.getReceive().get(0).setThemeTitle(null);
		validateGenerateListSuccess(dto);
	}

	@Test
	void shouldGenerateListSendSuccessByThemeNumberWithThemeTitleNull() throws IllegalAccessException {
		var dto = builder.build();
		dto.setReceive(null);
		dto.getSend().get(0).setThemeNumber("1");
		dto.getSend().get(0).setThemeTitle(null);
		validateGenerateListSuccess(dto);
	}

	@Test
	void shouldGenerateListFileInputSuccessByThemeNumberWithThemeTitleEmpty() throws IllegalAccessException {
		var dto = builder.build();
		dto.getReceive().get(0).setThemeNumber("1");
		dto.getReceive().get(0).setThemeTitle("");
		validateGenerateListSuccess(dto);
	}

	@Test
	void shouldGenerateListFileInputSuccessByThemeTitle() throws IllegalAccessException {
		var dto = builder.build();
		dto.getReceive().get(0).setThemeNumber(null);
		assertNotNull(dto.getReceive().get(0).getThemeTitle());
		validateGenerateListSuccess(dto);
		assertFalse(dto.getReceive().get(0).toString().isBlank());
	}

	void validateGenerateListSuccess(FileInputDataDiscursoDTO dto) throws IllegalAccessException {
		writeFileInputFromDto(dto);
		assertDoesNotThrow(() -> service.generateList());
	}

	private void validateListBuilderException(String expectedMessageError) throws IllegalAccessException {
		testUtils.validateException(ListBuilderException.class, () -> service.generateList(), expectedMessageError);
	}

}
