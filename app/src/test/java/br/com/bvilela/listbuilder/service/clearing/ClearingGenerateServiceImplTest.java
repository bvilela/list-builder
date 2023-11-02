package br.com.bvilela.listbuilder.service.clearing;

import br.com.bvilela.listbuilder.builder.clearing.ClearingInputDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import br.com.bvilela.listbuilder.dto.util.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.impl.DateServiceImpl;
import br.com.bvilela.listbuilder.service.impl.GroupServiceImpl;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ClearingGenerateServiceImplTest
        extends BaseGenerateServiceTest<ClearingInputDTO, ClearingInputDtoBuilder> {

    private static final List<String> MOCK_LIST_GROUPS =
            List.of(
                    "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3",
                    "1, 2, 3", "1, 2, 3");

    @InjectMocks private ClearingGenerateServiceImpl service;

    @InjectMocks private AppProperties appProperties;

    @Mock private DateServiceImpl dateService;

    @Mock private GroupServiceImpl groupService;

    @Mock private ClearingWriterService writerService;

    @Mock private SendNotificationService notificationService;

    private PropertiesTestUtils propertiesUtils;

    public ClearingGenerateServiceImplTest() {
        super(ListTypeEnum.LIMPEZA, ClearingInputDtoBuilder.create());
    }

    @BeforeEach
    void setupBeforeEach() {
        propertiesUtils = new PropertiesTestUtils(appProperties);
        propertiesUtils.setInputDir(testUtils.getResourceDirectory());
        service =
                new ClearingGenerateServiceImpl(
                        appProperties,
                        writerService,
                        dateService,
                        groupService,
                        notificationService);
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
        assertEquals("dados-limpeza.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListInvalidFilePathException() {
        callGenerateListAndVerifyExceptionMessage("Erro ao ler arquivo - Arquivo não encontrado");
    }

    @Test
    void shouldGenerateListFileSintaxeException() {
        testUtils.writeFileInputSyntaxError();
        callGenerateListAndVerifyExceptionMessage("Erro ao ler arquivo - Arquivo não é um JSON válido");
    }

    @DisplayName("Generate List Exception - Last Date Required")
    @ParameterizedTest(name = "Last Date is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void generateListExceptionLastDateRequired(String lastDate) {
        writeFileInputFromDto(builder.withSuccess().withLastDate(lastDate).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.LAST_DATE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionLastDateInvalid() {
        var dto = builder.withLastDateInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Última Data da Lista Anterior inválida: '%s' não é uma data válida",
                        dto.getLastDate());
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionGroupsNull() {
        writeFileInputFromDto(builder.withGroupsNull().build());
        callGenerateListAndVerifyExceptionMessage("Grupos está vazio!");
    }

    @Test
    void shouldGenerateListExceptionLastGroupNull() {
        writeFileInputFromDto(builder.withLastGroupNull().build());
        callGenerateListAndVerifyExceptionMessage("Último grupo não informado!");
    }

    @DisplayName("Generate List Exception - Midweek Day Required")
    @ParameterizedTest(name = "Last Date is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void generateListExceptionMidweekDayRequired(String midweekDay) {
        writeFileInputFromDto(builder.withSuccess().withMeetingDayMidweek(midweekDay).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekInvalid() {
        var dto = builder.withMidweekInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMidweekMeetingDay());
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionWeekendNull() {
        writeFileInputFromDto(builder.withWeekendNull().build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendEmpty() {
        writeFileInputFromDto(builder.withWeekendEmpty().build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendBlank() {
        writeFileInputFromDto(builder.withWeekendBlank().build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendInvalid() {
        var dto = builder.withWeekendInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getWeekendMeetingDay());
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionGeneratedListIsEmpty() {
        writeFileInputFromDto(builder.withSuccess().build());
        Mockito.when(
                        dateService.generateListDatesLimpeza(
                                ArgumentMatchers.any(DateServiceInputDTO.class),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(List.of());
        callGenerateListAndVerifyExceptionMessage("Lista de Datas e/ou Lista de Grupos VAZIA!");
    }

    @Test
    void shouldGenerateListLayout1Success() {
        var expectedList =
                List.of(
                        new ItemDateDTO(LocalDate.of(2022, 4, 2)),
                        new ItemDateDTO(LocalDate.of(2022, 4, 4), "exception1"),
                        new ItemDateDTO(LocalDate.of(2022, 4, 8), "exception2"),
                        new ItemDateDTO(LocalDate.of(2022, 4, 12)),
                        new ItemDateDTO(LocalDate.of(2022, 4, 16)),
                        new ItemDateDTO(LocalDate.of(2022, 4, 19)),
                        new ItemDateDTO(LocalDate.of(2022, 4, 23)),
                        new ItemDateDTO(LocalDate.of(2022, 4, 26)),
                        new ItemDateDTO(LocalDate.of(2022, 4, 30)));
        writeFileInputFromDto(builder.withSuccess().build());
        Mockito.when(
                        dateService.generateListDatesLimpeza(
                                ArgumentMatchers.any(DateServiceInputDTO.class),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(expectedList);
        Mockito.when(
                        groupService.generateListGroupsLimpeza(
                                ArgumentMatchers.any(ClearingInputDTO.class),
                                Mockito.<ItemDateDTO>anyList(),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(MOCK_LIST_GROUPS);
        propertiesUtils.setLayoutLimpeza(1);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    @Test
    void shouldGenerateListLayout2SuccessCase1() {
        var expectedList =
                List.of(
                        new ItemDateDTO(1, LocalDate.of(2022, 4, 1)),
                        new ItemDateDTO(1, LocalDate.of(2022, 4, 2)),
                        new ItemDateDTO(2, LocalDate.of(2022, 4, 4), "exception1"),
                        new ItemDateDTO(2, LocalDate.of(2022, 4, 5), "exception1"),
                        new ItemDateDTO(3, LocalDate.of(2022, 4, 8)),
                        new ItemDateDTO(3, LocalDate.of(2022, 4, 9)),
                        new ItemDateDTO(4, LocalDate.of(2022, 4, 14), "exception1"),
                        new ItemDateDTO(4, LocalDate.of(2022, 4, 15), "exception1"),
                        new ItemDateDTO(5, LocalDate.of(2022, 4, 16)),
                        new ItemDateDTO(6, LocalDate.of(2022, 4, 18)),
                        new ItemDateDTO(6, LocalDate.of(2022, 4, 19)),
                        new ItemDateDTO(7, LocalDate.of(2022, 4, 22)),
                        new ItemDateDTO(7, LocalDate.of(2022, 4, 23)),
                        new ItemDateDTO(8, LocalDate.of(2022, 4, 25)),
                        new ItemDateDTO(8, LocalDate.of(2022, 4, 26)),
                        new ItemDateDTO(9, LocalDate.of(2022, 4, 29)),
                        new ItemDateDTO(9, LocalDate.of(2022, 4, 30)));
        writeFileInputFromDto(builder.withSuccess().build());
        Mockito.when(
                        dateService.generateListDatesLimpeza(
                                ArgumentMatchers.any(DateServiceInputDTO.class),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(expectedList);
        Mockito.when(
                        groupService.generateListGroupsLimpeza(
                                ArgumentMatchers.any(ClearingInputDTO.class),
                                Mockito.<ItemDateDTO>anyList(),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(MOCK_LIST_GROUPS);
        propertiesUtils.setLayoutLimpeza(2);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    @Test
    void shouldGenerateListLayout2SuccessWithAddRemoveToList() {
        var expectedList =
                List.of(
                        new ItemDateDTO(1, LocalDate.of(2022, 4, 1)),
                        new ItemDateDTO(1, LocalDate.of(2022, 4, 2)));
        var addToList = Map.of("15-04-2022", "Após a Celebração");
        var removeToList = List.of("12-04-2022");
        writeFileInputFromDto(
                builder.withSuccess()
                        .withAddToList(addToList)
                        .withRemoveFromList(removeToList)
                        .build());
        Mockito.when(
                        dateService.generateListDatesLimpeza(
                                ArgumentMatchers.any(DateServiceInputDTO.class),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(expectedList);
        Mockito.when(
                        groupService.generateListGroupsLimpeza(
                                ArgumentMatchers.any(ClearingInputDTO.class),
                                Mockito.<ItemDateDTO>anyList(),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(MOCK_LIST_GROUPS);
        propertiesUtils.setLayoutLimpeza(2);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    @Test
    void shouldGenerateListLayout2SuccessWithAddToListException() {
        var addToList = Map.of("aa-04-2022", "Após a Celebração");
        writeFileInputFromDto(builder.withSuccess().withAddToList(addToList).build());
        propertiesUtils.setLayoutLimpeza(2);
        var exception = assertThrows(ListBuilderException.class, () -> service.generateList());
        assertEquals("Valor 'aa-04-2022' não é uma data válida", exception.getMessage());
    }

    @Test
    void shouldGenerateListLayout2SuccessWithRemoveToListException() {
        var removeToList = List.of("12-04-aaaaa");
        writeFileInputFromDto(builder.withSuccess().withRemoveFromList(removeToList).build());
        propertiesUtils.setLayoutLimpeza(2);
        var exception =
                assertThrows(ListBuilderException.class, () -> service.generateList());
        assertEquals(
                "Valor '12-04-aaaaa' não é uma data válida",
                exception.getMessage());
    }

    @Test
    void shouldGetLabelNoExceptionAndNoLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1));
        var ret = service.getLabel(dto, false);
        assertEquals("Terça", ret);
    }

    @DisplayName("Get Label Exception And No Label")
    @ParameterizedTest(name = "Label Exception is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldGetLabelExceptionAndNoLabel(String messageException) {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), messageException);
        var ret = service.getLabel(dto, false);
        assertEquals("Terça", ret);
    }

    @Test
    void shouldGetLabelExceptionMessageBlankAndNoLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), "myMessage");
        var ret = service.getLabel(dto, false);
        assertEquals("Terça", ret);
    }

    @Test
    void shouldGetLabelNoExceptionAndAndLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1));
        var ret = service.getLabel(dto, true);
        assertEquals("Terça - Após a Reunião", ret);
    }

    @DisplayName("Get Label Exception And Label")
    @ParameterizedTest(name = "Label Exception is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldGetLabelExceptionNullAndLabel(String messageException) {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), messageException);
        var ret = service.getLabel(dto, true);
        assertEquals("Terça - Após a Reunião", ret);
    }

    @Test
    void shouldGetLabelExceptionMessageBlankAndLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), "myMessage");
        var ret = service.getLabel(dto, true);
        assertEquals("Terça - myMessage", ret);
    }

    private void callGenerateListAndVerifyExceptionMessage(String expectedMessageError) {
        testUtils.validateException(() -> service.generateList(), expectedMessageError);
    }
}
