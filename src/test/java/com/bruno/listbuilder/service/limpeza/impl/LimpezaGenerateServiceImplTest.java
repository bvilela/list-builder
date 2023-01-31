package com.bruno.listbuilder.service.limpeza.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
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
import com.bruno.listbuilder.service.BaseGenerateServiceTest;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.impl.DateServiceImpl;
import com.bruno.listbuilder.service.impl.GroupServiceImpl;

@SpringBootApplication
class LimpezaGenerateServiceImplTest
		extends BaseGenerateServiceTest<FileInputDataLimpezaDTO, FileInputDataLimpezaDtoBuilder> {

	private static final List<String> MOCK_LIST_GROUPS = List.of("1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3",
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

	public LimpezaGenerateServiceImplTest() throws ListBuilderException {
		super(ListTypeEnum.LIMPEZA, FileInputDataLimpezaDtoBuilder.create());
	}

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", testUtils.getResourceDirectory(), true);
		service = new LimpezaGenerateServiceImpl(properties, dateService, groupService, writerService,
				notificationService, convertImageService);
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
	void shouldGenerateListExceptionGroupsNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withGroupsNull().build());
		validateListBuilderException("Grupos está vazio!");
	}

	@Test
	void shouldGenerateListExceptionLastGroupNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withLastGroupNull().build());
		validateListBuilderException("Último grupo não informado!");
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
		var expectedMessageError = String.format("Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayWeekend());
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListExceptionGeneratedListIsEmpty() throws IllegalAccessException {
		writeFileInputFromDto(builder.withSuccess().build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class))).thenReturn(List.of());
		validateListBuilderException("Lista de Datas e/ou Lista de Grupos VAZIA!");
	}

	@Test
	void shouldGenerateListLayout1Success() throws IllegalAccessException, ListBuilderException {
		var expectedList = List.of(new ItemDateDTO(LocalDate.of(2022, 4, 2)),
				new ItemDateDTO(LocalDate.of(2022, 4, 4), "exception1"),
				new ItemDateDTO(LocalDate.of(2022, 4, 8), "exception2"), new ItemDateDTO(LocalDate.of(2022, 4, 12)),
				new ItemDateDTO(LocalDate.of(2022, 4, 16)), new ItemDateDTO(LocalDate.of(2022, 4, 19)),
				new ItemDateDTO(LocalDate.of(2022, 4, 23)), new ItemDateDTO(LocalDate.of(2022, 4, 26)),
				new ItemDateDTO(LocalDate.of(2022, 4, 30)));
		writeFileInputFromDto(builder.withSuccess().build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(expectedList);
		when(groupService.generateListGroupsLimpeza(any(FileInputDataLimpezaDTO.class), Mockito.<ItemDateDTO>anyList(),
				any(Integer.class))).thenReturn(MOCK_LIST_GROUPS);
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
		writeFileInputFromDto(builder.withSuccess().build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(expectedList);
		when(groupService.generateListGroupsLimpeza(any(FileInputDataLimpezaDTO.class), Mockito.<ItemDateDTO>anyList(),
				any(Integer.class))).thenReturn(MOCK_LIST_GROUPS);
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
		var addToList = Map.of("15-04-2022", "Após a Celebração");
		var removeToList = List.of("12-04-2022");
		writeFileInputFromDto(builder.withSuccess().withAddToList(addToList).withRemoveFromList(removeToList).build());
		when(dateService.generateListDatesLimpeza(any(DateServiceInputDTO.class), any(Integer.class)))
				.thenReturn(expectedList);
		when(groupService.generateListGroupsLimpeza(any(FileInputDataLimpezaDTO.class), Mockito.<ItemDateDTO>anyList(),
				any(Integer.class))).thenReturn(MOCK_LIST_GROUPS);
		FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
		assertDoesNotThrow(() -> service.generateList());
	}

	@Test
	void shouldGenerateListLayout2SuccessWithAddToListException() throws IllegalAccessException, ListBuilderException {
		var addToList = Map.of("aa-04-2022", "Após a Celebração");
		writeFileInputFromDto(builder.withSuccess().withAddToList(addToList).build());
		FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		assertEquals("Erro ao gerar lista 'LIMPEZA': Valor 'aa-04-2022' não é uma data válida", ex.getMessage());
	}

	@Test
	void shouldGenerateListLayout2SuccessWithRemoveToListException()
			throws IllegalAccessException, ListBuilderException {
		var removeToList = List.of("12-04-aaaaa");
		writeFileInputFromDto(builder.withSuccess().withRemoveFromList(removeToList).build());
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

	private void validateListBuilderException(String expectedMessageError) throws IllegalAccessException {
		testUtils.validateExpection(ListBuilderException.class, () -> service.generateList(), expectedMessageError);
	}
}
