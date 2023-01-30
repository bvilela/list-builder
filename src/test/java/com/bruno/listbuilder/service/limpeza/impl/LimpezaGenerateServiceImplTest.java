package com.bruno.listbuilder.service.limpeza.impl;

import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputLimpezaFromDto;
import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputLimpezaSyntaxError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.FileInputDataLimpezaDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.dto.ItemDateDTO;
import com.bruno.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.impl.DateServiceImpl;
import com.bruno.listbuilder.service.impl.GroupServiceImpl;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

@SpringBootApplication
class LimpezaGenerateServiceImplTest {

	private static final List<String> MOCK_LIST_GROUPS = List.of(
			"1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", 
			"1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3");

	@InjectMocks
	private LimpezaGenerateServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@Mock
	private DateServiceImpl dateService;

	@Mock
	private GroupServiceImpl groupService;

	@Mock
	private LimpezaWriterServiceImpl writerService;
	
	@Mock
	private NotificationService notificationService;
	
	@Mock
	private ConvertImageService convertImageService;

	private static String resourceDirectory;

	@BeforeAll
	static void setupBeforeAll() throws ListBuilderException {
		resourceDirectory = Paths.get("src", "test", "resources", ListTypeEnum.LIMPEZA.toString().toLowerCase())
				.toFile().getAbsolutePath();
		FileUtils.createDirectories(resourceDirectory);
		TestFileUtilsWriteFile.cleanDirLimpeza();
	}

	@AfterAll
	static void setupAfterAll() {
		TestFileUtilsWriteFile.cleanDirLimpeza();
	}

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", resourceDirectory, true);
		service = new LimpezaGenerateServiceImpl(properties, dateService, groupService, writerService, notificationService, convertImageService);
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getExecutionMode());
	}

	@Test
	void shouldGetExecutionMode() {
		var mode = service.getExecutionMode();
		assertEquals(ListTypeEnum.LIMPEZA, mode);
	}

	void baseGenerateListException(String file, String message) throws IllegalAccessException {
		FieldUtils.writeField(properties, "inputFileNameLimpeza", file, true);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		String baseMessage = String.format("Erro ao gerar lista '%s': %s", ListTypeEnum.LIMPEZA.toString(),
				message);
		assertEquals(ex.getMessage(), baseMessage);
	}

	@Test
	void shouldGenerateListInvalidFilePathException() throws IllegalAccessException {
		String fileInvalidName = "xpto.json";
		baseGenerateListException(fileInvalidName, "Erro ao ler arquivo - Arquivo não encontrado");
	}

	@Test
	void shouldGenerateListFileSintaxeException() throws IllegalAccessException {
		String fileSyntaxError = "data-limpeza-syntax-error.json";
		writeFileInputLimpezaSyntaxError(fileSyntaxError);
		baseGenerateListException(fileSyntaxError, "Erro ao ler arquivo - Arquivo não é um JSON válido");
	}

	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		String fileName = "data-limpeza-last-date-null.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withLastDateNull().build());
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		String fileName = "data-limpeza-last-date-empty.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withLastDateEmpty().build());
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		String fileName = "data-limpeza-last-date-blank.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withLastDateBlank().build());
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionLastDateInvalid() throws IllegalAccessException {
		String fileName = "data-limpeza-last-date-invalid.json";
		var dto = FileInputDataLimpezaDtoBuilder.create().withLastDateInvalid().build();
		writeFileInputLimpezaFromDto(fileName, dto);
		var msgErro = String.format("Última Data da Lista Anterior inválida: '%s' não é uma data válida",
				dto.getLastDate());
		baseGenerateListException(fileName, msgErro);
	}

	@Test
	void shouldGenerateListExceptionGroupsNull() throws IllegalAccessException {
		String fileName = "data-limpeza-groups-null.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withGroupsNull().build());
		baseGenerateListException(fileName, "Grupos está vazio!");
	}

	@Test
	void shouldGenerateListExceptionLastGroupNull() throws IllegalAccessException {
		String fileName = "data-limpeza-last-group-null.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withLastGroupNull().build());
		baseGenerateListException(fileName, "Último grupo não informado!");
	}

	@Test
	void shouldGenerateListExceptionMidweekNull() throws IllegalAccessException {
		String fileName = "data-limpeza-midweek-null.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withMidweekNull().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekEmpty() throws IllegalAccessException {
		String fileName = "data-limpeza-midweek-empty.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withMidweekEmpty().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekBlank() throws IllegalAccessException {
		String fileName = "data-limpeza-midweek-blank.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withMidweekBlank().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekInvalid() throws IllegalAccessException {
		String fileName = "data-limpeza-midweek-invalid.json";
		var dto = FileInputDataLimpezaDtoBuilder.create().withMidweekInvalid().build();
		writeFileInputLimpezaFromDto(fileName, dto);
		var msgError = String.format("Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayMidweek());
		baseGenerateListException(fileName, msgError);
	}

	@Test
	void shouldGenerateListExceptionWeekendNull() throws IllegalAccessException {
		String fileName = "data-limpeza-weekend-null.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withWeekendNull().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendEmpty() throws IllegalAccessException {
		String fileName = "data-limpeza-weekend-empty.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withWeekendEmpty().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendBlank() throws IllegalAccessException {
		String fileName = "data-limpeza-weekend-blank.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withWeekendBlank().build());
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendInvalid() throws IllegalAccessException {
		String fileName = "data-limpeza-weekend-invalid.json";
		var dto = FileInputDataLimpezaDtoBuilder.create().withWeekendInvalid().build();
		writeFileInputLimpezaFromDto(fileName, dto);
		var msgError = String.format("Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayWeekend());
		baseGenerateListException(fileName, msgError);
	}

	@Test
	void shouldGenerateListExceptionGeneratedListIsEmpty() throws IllegalAccessException {
		String fileName = "data-limpeza-ok.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withSuccess().build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(List.of());
		baseGenerateListException(fileName, "Lista de Datas e/ou Lista de Grupos VAZIA!");
	}

	@Test
	void shouldGenerateListLayout1Success() throws IllegalAccessException, ListBuilderException {
		var expectedList = List.of(new ItemDateDTO(LocalDate.of(2022, 4, 2)),
				new ItemDateDTO(LocalDate.of(2022, 4, 4), "exception1"),
				new ItemDateDTO(LocalDate.of(2022, 4, 8), "exception2"), new ItemDateDTO(LocalDate.of(2022, 4, 12)),
				new ItemDateDTO(LocalDate.of(2022, 4, 16)), new ItemDateDTO(LocalDate.of(2022, 4, 19)),
				new ItemDateDTO(LocalDate.of(2022, 4, 23)), new ItemDateDTO(LocalDate.of(2022, 4, 26)),
				new ItemDateDTO(LocalDate.of(2022, 4, 30)));
		String fileName = "data-limpeza-ok-layout1.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withSuccess().build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(expectedList);
		when(groupService.generateListGroupsLimpeza(any(FileInputDataLimpezaDTO.class), Mockito.<ItemDateDTO>anyList(),
				any(Integer.class))).thenReturn(MOCK_LIST_GROUPS);
		FieldUtils.writeField(properties, "inputFileNameLimpeza", fileName, true);
		FieldUtils.writeField(properties, "layoutLimpeza", 1, true);
		assertDoesNotThrow(() -> service.generateList());
	}

	@Test
	void shouldGenerateListLayout2SuccessCase1() throws IllegalAccessException, ListBuilderException {
		// @formatter:off
		var expectedList = List.of(
			new ItemDateDTO(1, LocalDate.of(2022, 4, 1)), new ItemDateDTO(1, LocalDate.of(2022, 4, 2)),
			new ItemDateDTO(2, LocalDate.of(2022, 4, 4), "exception1"), new ItemDateDTO(2, LocalDate.of(2022, 4, 5), "exception1"),
			new ItemDateDTO(3, LocalDate.of(2022, 4, 8)), new ItemDateDTO(3, LocalDate.of(2022, 4, 9)),
			new ItemDateDTO(4, LocalDate.of(2022, 4, 14), "exception1"), new ItemDateDTO(4, LocalDate.of(2022, 4, 15), "exception1"),
			new ItemDateDTO(5, LocalDate.of(2022, 4, 16)), 
			new ItemDateDTO(6, LocalDate.of(2022, 4, 18)), new ItemDateDTO(6, LocalDate.of(2022, 4, 19)),
			new ItemDateDTO(7, LocalDate.of(2022, 4, 22)), new ItemDateDTO(7, LocalDate.of(2022, 4, 23)),
			new ItemDateDTO(8, LocalDate.of(2022, 4, 25)), new ItemDateDTO(8, LocalDate.of(2022, 4, 26)),
			new ItemDateDTO(9, LocalDate.of(2022, 4, 29)), new ItemDateDTO(9, LocalDate.of(2022, 4, 30))
		);
		// @formatter:on
		String fileName = "data-limpeza-ok-layout2-case1.json";
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withSuccess().build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(expectedList);
		when(groupService.generateListGroupsLimpeza(any(FileInputDataLimpezaDTO.class), Mockito.<ItemDateDTO>anyList(),
				any(Integer.class))).thenReturn(MOCK_LIST_GROUPS);
		FieldUtils.writeField(properties, "inputFileNameLimpeza", fileName, true);
		FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
		assertDoesNotThrow(() -> service.generateList());
	}

	@Test
	void shouldGenerateListLayout2SuccessWithAddRemoveToList() throws IllegalAccessException, ListBuilderException {
		// @formatter:off
		var expectedList = List.of(
			new ItemDateDTO(1, LocalDate.of(2022, 4, 1)), new ItemDateDTO(1, LocalDate.of(2022, 4, 2))
		);
		// @formatter:on
		String fileName = "data-limpeza-ok-layout2-case2.json";
		var addToList = Map.of("15-04-2022", "Após a Celebração");
		var removeToList = List.of("12-04-2022");
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withSuccess()
				.withAddToList(addToList).withRemoveFromList(removeToList).build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(expectedList);
		when(groupService.generateListGroupsLimpeza(any(FileInputDataLimpezaDTO.class), Mockito.<ItemDateDTO>anyList(),
				any(Integer.class))).thenReturn(MOCK_LIST_GROUPS);
		FieldUtils.writeField(properties, "inputFileNameLimpeza", fileName, true);
		FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
		assertDoesNotThrow(() -> service.generateList());
	}
	
	@Test
	void shouldGenerateListLayout2SuccessWithAddToListException() throws IllegalAccessException, ListBuilderException {
		String fileName = "data-limpeza-ok-layout2-case2-invalid.json";
		var addToList = Map.of("aa-04-2022", "Após a Celebração");
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withSuccess()
				.withAddToList(addToList).build());
		FieldUtils.writeField(properties, "inputFileNameLimpeza", fileName, true);
		FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		assertEquals("Erro ao gerar lista 'LIMPEZA': Valor 'aa-04-2022' não é uma data válida", ex.getMessage());
	}
	
	@Test
	void shouldGenerateListLayout2SuccessWithRemoveToListException() throws IllegalAccessException, ListBuilderException {
		String fileName = "data-limpeza-ok-layout2-case2-invalid.json";
		var removeToList = List.of("12-04-aaaaa");
		writeFileInputLimpezaFromDto(fileName, FileInputDataLimpezaDtoBuilder.create().withSuccess()
				.withRemoveFromList(removeToList).build());
		FieldUtils.writeField(properties, "inputFileNameLimpeza", fileName, true);
		FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		assertEquals("Erro ao gerar lista 'LIMPEZA': Valor '12-04-aaaaa' não é uma data válida", ex.getMessage());
	}

	@Test
	void shouldGetLabelNoExceptionAndNoLabel() throws ListBuilderException {
		var dto = new ItemDateDTO(LocalDate.of(2022, 03, 01));
		var ret = service.getLabel(dto, false);
		assertEquals("Terça", ret);
	}

	@Test
	void shouldGetLabelExceptionNullAndNoLabel() throws ListBuilderException {
		baseGetLabelExceptionAndNoLabel(null);
	}

	@Test
	void shouldGetLabelExceptionEmptyAndNoLabel() throws ListBuilderException {
		baseGetLabelExceptionAndNoLabel("");
	}

	@Test
	void shouldGetLabelExceptionBlankAndNoLabel() throws ListBuilderException {
		baseGetLabelExceptionAndNoLabel(" ");
	}

	void baseGetLabelExceptionAndNoLabel(String messageException) throws ListBuilderException {
		var dto = new ItemDateDTO(LocalDate.of(2022, 03, 01), messageException);
		var ret = service.getLabel(dto, false);
		assertEquals("Terça", ret);
	}

	@Test
	void shouldGetLabelExceptionMessageBlankAndNoLabel() throws ListBuilderException {
		var dto = new ItemDateDTO(LocalDate.of(2022, 03, 01), "myMessage");
		var ret = service.getLabel(dto, false);
		assertEquals("Terça", ret);
	}

	@Test
	void shouldGetLabelNoExceptionAndAndLabel() throws ListBuilderException {
		var dto = new ItemDateDTO(LocalDate.of(2022, 03, 01));
		var ret = service.getLabel(dto, true);
		assertEquals("Terça - Após a Reunião", ret);
	}

	@Test
	void shouldGetLabelExceptionNullAndLabel() throws ListBuilderException {
		baseGetLabelExceptionAndLabel(null);
	}

	@Test
	void shouldGetLabelExceptionEmptyAndLabel() throws ListBuilderException {
		baseGetLabelExceptionAndLabel("");
	}

	@Test
	void shouldGetLabelExceptionBlankAndLabel() throws ListBuilderException {
		baseGetLabelExceptionAndLabel(" ");
	}

	@Test
	void shouldGetLabelExceptionMessageBlankAndLabel() throws ListBuilderException {
		var dto = new ItemDateDTO(LocalDate.of(2022, 03, 01), "myMessage");
		var ret = service.getLabel(dto, true);
		assertEquals("Terça - myMessage", ret);
	}

	void baseGetLabelExceptionAndLabel(String messageException) throws ListBuilderException {
		var dto = new ItemDateDTO(LocalDate.of(2022, 03, 01), messageException);
		var ret = service.getLabel(dto, true);
		assertEquals("Terça - Após a Reunião", ret);
	}
}
