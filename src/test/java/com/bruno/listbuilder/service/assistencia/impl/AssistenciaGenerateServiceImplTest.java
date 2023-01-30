package com.bruno.listbuilder.service.assistencia.impl;

import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputAssistenciaFromDto;
import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputAssistenciaSyntaxError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.FileInputDataAssistenciaDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.assistencia.AssistenciaWriterService;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

@SpringBootApplication
class AssistenciaGenerateServiceImplTest {

	private static final String PROPERTIE_INPUT_FILE_NAME_ASSISTENCIA = "inputFileNameAssistencia";

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

	private final static String MODE_EXECUTION = ListTypeEnum.ASSISTENCIA.toString();
	private static String resourceDirectory;

	@BeforeAll
	static void setupBeforeAll() throws ListBuilderException {
		resourceDirectory = Paths.get("src", "test", "resources", MODE_EXECUTION.toLowerCase()).toFile()
				.getAbsolutePath();
		FileUtils.createDirectories(resourceDirectory);
	}

	@AfterAll
	static void setupAfterAll() {
		TestFileUtilsWriteFile.cleanDirAssistencia();
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", resourceDirectory, true);
		service = new AssistenciaGenerateServiceImpl(properties, dateService, writerService, notificationService);
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getExecutionMode());
	}

	@Test
	void shouldGetExecutionMode() {
		var mode = service.getExecutionMode();
		assertEquals(ListTypeEnum.ASSISTENCIA, mode);
	}

	@Test
	void shouldGenerateListInvalidFilePathException() throws IllegalAccessException {
		String fileInvalidName = "xpto.json";
		baseGenerateListException(fileInvalidName, "Erro ao ler arquivo - Arquivo não encontrado");
	}

	@Test
	void shouldGenerateListFileSintaxeException() throws IllegalAccessException {
		String fileSyntaxError = "data-assistencia-syntax-error.json";
		writeFileInputAssistenciaSyntaxError(fileSyntaxError);
		baseGenerateListException(fileSyntaxError, "Erro ao ler arquivo - Arquivo não é um JSON válido");
	}

	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		String fileName = "data-assistencia-last-date-null.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withLastDateNull().build());
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		String fileName = "data-assistencia-last-date-empty.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withLastDateEmpty().build());
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		String fileName = "data-assistencia-last-date-blank.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withLastDateBlank().build());
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateInvalid() throws IllegalAccessException {
		String fileName = "data-assistencia-last-date-invalid.json";
		var dto = FileInputDataAssistenciaDtoBuilder.create().withLastDateInvalid().build();
		writeFileInputAssistenciaFromDto(fileName, dto);
		var msgErro = String.format("Última Data da Lista Anterior inválida: '%s' não é uma data válida",
				dto.getLastDate());
		baseGenerateListException(fileName, msgErro);
	}

	@Test
	void shouldGenerateListExceptionMidweekNull() throws IllegalAccessException {
		String fileName = "data-assistencia-midweek-null.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withMidweekNull().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekEmpty() throws IllegalAccessException {
		String fileName = "data-assistencia-midweek-empty.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withMidweekEmpty().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekBlank() throws IllegalAccessException {
		String fileName = "data-assistencia-midweek-blank.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withMidweekBlank().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekInvalid() throws IllegalAccessException {
		String fileName = "data-assistencia-midweek-invalid.json";
		var dto = FileInputDataAssistenciaDtoBuilder.create().withMidweekInvalid().build();
		writeFileInputAssistenciaFromDto(fileName, dto);
		var msgError = String.format("Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayMidweek());
		baseGenerateListException(fileName, msgError);
	}

	@Test
	void shouldGenerateListExceptionWeekendNull() throws IllegalAccessException {
		String fileName = "data-assistencia-weekend-null.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withWeekendNull().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendEmpty() throws IllegalAccessException {
		String fileName = "data-assistencia-weekend-empty.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withWeekendEmpty().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendBlank() throws IllegalAccessException {
		String fileName = "data-assistencia-weekend-blank.json";
		writeFileInputAssistenciaFromDto(fileName,
				FileInputDataAssistenciaDtoBuilder.create().withWeekendBlank().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendInvalid() throws IllegalAccessException {
		String fileName = "data-assistencia-weekend-invalid.json";
		var dto = FileInputDataAssistenciaDtoBuilder.create().withWeekendInvalid().build();
		writeFileInputAssistenciaFromDto(fileName, dto);
		var msgError = String.format("Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayWeekend());
		baseGenerateListException(fileName, msgError);
	}

	@Test
	void shouldGenerateListExceptionGeneratedListIsEmpty() throws IllegalAccessException {
		String fileName = "data-assistencia-ok.json";
		writeFileInputAssistenciaFromDto(fileName, FileInputDataAssistenciaDtoBuilder.create().withSuccess().build());
		when(dateService.generateListDatesAssistencia(any(DateServiceInputDTO.class))).thenReturn(List.of());
		baseGenerateListException(fileName, MessageConfig.LIST_DATE_EMPTY);
	}

	@Test
	void shouldGenerateListSuccess() throws IllegalAccessException, ListBuilderException {
		// @formatter:off
		var expectedList = List.of(
				cld(4, 2),  cld(4, 4),  cld(4, 8), cld(4, 12),
				cld(4, 16), cld(4, 19), cld(4, 8), cld(4, 23),
				cld(4, 26), cld(4, 19), cld(4, 30));
		// @formatter:on
		String fileName = "data-assistencia-ok.json";
		writeFileInputAssistenciaFromDto(fileName, FileInputDataAssistenciaDtoBuilder.create().withSuccess().build());
		when(dateService.generateListDatesAssistencia(any(DateServiceInputDTO.class))).thenReturn(expectedList);
		FieldUtils.writeField(properties, PROPERTIE_INPUT_FILE_NAME_ASSISTENCIA, fileName, true);
		assertDoesNotThrow(() -> service.generateList());
	}

	/** Create Local Date */
	private LocalDate cld(int month, int day) {
		return LocalDate.of(2022, month, day);
	}

	void baseGenerateListException(String file, String message) throws IllegalAccessException {
		FieldUtils.writeField(properties, PROPERTIE_INPUT_FILE_NAME_ASSISTENCIA, file, true);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		String baseMessage = String.format("Erro ao gerar lista '%s': %s", MODE_EXECUTION, message);
		assertEquals(ex.getMessage(), baseMessage);
	}

}
