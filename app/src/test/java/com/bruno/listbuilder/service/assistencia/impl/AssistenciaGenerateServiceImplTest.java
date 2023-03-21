package com.bruno.listbuilder.service.assistencia.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import com.bruno.listbuilder.builder.FileInputDataAssistenciaDtoBuilder;
import com.bruno.listbuilder.service.BaseGenerateServiceTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.assistencia.AssistenciaWriterService;

@SpringBootApplication
class AssistenciaGenerateServiceImplTest
		extends BaseGenerateServiceTest<FileInputDataAssistenciaDTO, FileInputDataAssistenciaDtoBuilder> {

	@InjectMocks
	private AssistenciaGenerateServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@Mock
	private DateService dateService;

	@Mock
	private AssistenciaWriterService writerService;

	@Mock
	private NotificationService notificationService;

	private AssistenciaGenerateServiceImplTest() throws ListBuilderException {
		super(ListTypeEnum.ASSISTENCIA, FileInputDataAssistenciaDtoBuilder.create());
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", testUtils.getResourceDirectory(), true);
		service = new AssistenciaGenerateServiceImpl(properties, dateService, writerService, notificationService);
	}

	@Test
	void shouldModoExecutionNotNull() {
		Assertions.assertNotNull(service.getListType());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(testUtils.getListType(), service.getListType());
	}
	
	@Test
	void shouldCorrectFileInputName() {
		assertEquals("dados-assistencia.json", service.getListType().getInputFileName());
	}

	@Test
	void shouldGenerateListInvalidFilePathException() throws IllegalAccessException {
		validateListBuilderException("Erro ao ler arquivo - Arquivo não encontrado");
	}

	@Test
	void shouldGenerateListFileSintaxeException() throws IllegalAccessException {
		testUtils.writeFileInputSyntaxError();
		validateListBuilderException("Erro ao ler arquivo - Arquivo não é um JSON válido");
	}

	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withLastDateNull().build());
		validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		writeFileInputFromDto(builder.withLastDateEmpty().build());
		validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		writeFileInputFromDto(builder.withLastDateBlank().build());
		validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateInvalid() throws IllegalAccessException {
		var dto = builder.withLastDateInvalid().build();
		writeFileInputFromDto(dto);
		var expectedMessageError = String.format("Última Data da Lista Anterior inválida: '%s' não é uma data válida",
				dto.getLastDate());
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListExceptionMidweekNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withMidweekNull().build());
		validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekEmpty() throws IllegalAccessException {
		writeFileInputFromDto(builder.withMidweekEmpty().build());
		validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekBlank() throws IllegalAccessException {
		writeFileInputFromDto(builder.withMidweekBlank().build());
		validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekInvalid() throws IllegalAccessException {
		var dto = builder.withMidweekInvalid().build();
		writeFileInputFromDto(dto);
		var expectedMessageError = String.format(
				"Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayMidweek());
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListExceptionWeekendNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withWeekendNull().build());
		validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendEmpty() throws IllegalAccessException {
		writeFileInputFromDto(builder.withWeekendEmpty().build());
		validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendBlank() throws IllegalAccessException {
		writeFileInputFromDto(builder.withWeekendBlank().build());
		validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendInvalid() throws IllegalAccessException {
		var dto = builder.withWeekendInvalid().build();
		writeFileInputFromDto(dto);
		var expectedMessageError = String.format(
				"Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayWeekend());
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListExceptionGeneratedListIsEmpty() throws IllegalAccessException {
		writeFileInputFromDto(builder.withSuccess().build());
		Mockito.when(dateService.generateListDatesAssistencia(ArgumentMatchers.any(DateServiceInputDTO.class))).thenReturn(List.of());
		validateListBuilderException(MessageConfig.LIST_DATE_EMPTY);
	}

	@Test
	void shouldGenerateListSuccess() {
		// @formatter:off
		var expectedList = List.of(
				cld(4, 2),  cld(4, 4),  cld(4, 8), cld(4, 12),
				cld(4, 16), cld(4, 19), cld(4, 8), cld(4, 23),
				cld(4, 26), cld(4, 19), cld(4, 30));
		// @formatter:on
		writeFileInputFromDto(builder.withSuccess().build());
		Mockito.when(dateService.generateListDatesAssistencia(ArgumentMatchers.any(DateServiceInputDTO.class))).thenReturn(expectedList);
		Assertions.assertDoesNotThrow(() -> service.generateList());
	}

	/** Create Local Date */
	private LocalDate cld(int month, int day) {
		return LocalDate.of(2022, month, day);
	}

	private void validateListBuilderException(String expectedMessageError) throws IllegalAccessException {
		testUtils.validateException(ListBuilderException.class, () -> service.generateList(), expectedMessageError);
	}

}
