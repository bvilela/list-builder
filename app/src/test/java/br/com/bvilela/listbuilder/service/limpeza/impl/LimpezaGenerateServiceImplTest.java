package br.com.bvilela.listbuilder.service.limpeza.impl;

import br.com.bvilela.listbuilder.builder.FileInputDataLimpezaDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.NotificationService;
import br.com.bvilela.listbuilder.service.impl.DateServiceImpl;
import br.com.bvilela.listbuilder.service.impl.GroupServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootApplication
class LimpezaGenerateServiceImplTest
        extends BaseGenerateServiceTest<FileInputDataLimpezaDTO, FileInputDataLimpezaDtoBuilder> {

    private static final List<String> MOCK_LIST_GROUPS =
            List.of(
                    "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3", "1, 2, 3",
                    "1, 2, 3", "1, 2, 3");

    @InjectMocks private LimpezaGenerateServiceImpl service;

    @InjectMocks private AppProperties properties;

    @Mock private DateServiceImpl dateService;

    @Mock private GroupServiceImpl groupService;

    @Mock private LimpezaWriterServiceImpl writerService;

    @Mock private NotificationService notificationService;

    public LimpezaGenerateServiceImplTest() throws ListBuilderException {
        super(ListTypeEnum.LIMPEZA, FileInputDataLimpezaDtoBuilder.create());
    }

    @BeforeEach
    void setupBeforeEach() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        FieldUtils.writeField(
                properties,
                "inputDir",
                this.testUtils.getResourceDirectory(),
                true);
        service =
                new LimpezaGenerateServiceImpl(
                        properties, writerService, dateService, groupService, notificationService);
    }

    @Test
    void shouldModoExecutionNotNull() {
        Assertions.assertNotNull(service.getListType());
    }

    @Test
    void shouldGetExecutionMode() {
        assertEquals(this.testUtils.getListType(), service.getListType());
    }

    @Test
    void shouldCorrectFileInputName() {
        assertEquals("dados-limpeza.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListInvalidFilePathException() {
        validateListBuilderException("Erro ao ler arquivo - Arquivo não encontrado");
    }

    @Test
    void shouldGenerateListFileSintaxeException() {
        this.testUtils.writeFileInputSyntaxError();
        validateListBuilderException("Erro ao ler arquivo - Arquivo não é um JSON válido");
    }

    @Test
    void shouldGenerateListExceptionLastDateNull() {
        writeFileInputFromDto(builder.withLastDateNull().build());
        validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionLastDateEmpty() {
        writeFileInputFromDto(builder.withLastDateEmpty().build());
        validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionLastDateBlank() {
        writeFileInputFromDto(builder.withLastDateBlank().build());
        validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionLastDateInvalid() {
        var dto = builder.withLastDateInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Última Data da Lista Anterior inválida: '%s' não é uma data válida",
                        dto.getLastDate());
        validateListBuilderException(expectedMessageError);
    }

    @Test
    // TODO: refactory
    void shouldGenerateListExceptionGroupsNull() {
        writeFileInputFromDto(builder.withGroupsNull().build());
        validateListBuilderException("Grupos está vazio!");
    }

    @Test
    void shouldGenerateListExceptionLastGroupNull() {
        writeFileInputFromDto(builder.withLastGroupNull().build());
        validateListBuilderException("Último grupo não informado!");
    }

    @Test
    //TODO: refactory
    void shouldGenerateListExceptionMidweekNull() {
        writeFileInputFromDto(builder.withMidweekNull().build());
        validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekEmpty() {
        writeFileInputFromDto(builder.withMidweekEmpty().build());
        validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekBlank() {
        writeFileInputFromDto(builder.withMidweekBlank().build());
        validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekInvalid() {
        var dto = builder.withMidweekInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMeetingDayMidweek());
        validateListBuilderException(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionWeekendNull() {
        writeFileInputFromDto(builder.withWeekendNull().build());
        validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendEmpty() {
        writeFileInputFromDto(builder.withWeekendEmpty().build());
        validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendBlank() {
        writeFileInputFromDto(builder.withWeekendBlank().build());
        validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendInvalid() {
        var dto = builder.withWeekendInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMeetingDayWeekend());
        validateListBuilderException(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionGeneratedListIsEmpty() {
        writeFileInputFromDto(builder.withSuccess().build());
        Mockito.when(
                        dateService.generateListDatesLimpeza(
                                ArgumentMatchers.any(DateServiceInputDTO.class),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(List.of());
        validateListBuilderException("Lista de Datas e/ou Lista de Grupos VAZIA!");
    }

    @Test
    @SneakyThrows
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
                                ArgumentMatchers.any(FileInputDataLimpezaDTO.class),
                                Mockito.<ItemDateDTO>anyList(),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(MOCK_LIST_GROUPS);
        FieldUtils.writeField(properties, "layoutLimpeza", 1, true);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    @Test
    @SneakyThrows
    void shouldGenerateListLayout2SuccessCase1() {
        // @formatter:off
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
        // @formatter:on
        writeFileInputFromDto(builder.withSuccess().build());
        Mockito.when(
                        dateService.generateListDatesLimpeza(
                                ArgumentMatchers.any(DateServiceInputDTO.class),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(expectedList);
        Mockito.when(
                        groupService.generateListGroupsLimpeza(
                                ArgumentMatchers.any(FileInputDataLimpezaDTO.class),
                                Mockito.<ItemDateDTO>anyList(),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(MOCK_LIST_GROUPS);
        FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    @Test
    @SneakyThrows
    void shouldGenerateListLayout2SuccessWithAddRemoveToList() {
        // @formatter:off
        var expectedList =
                List.of(
                        new ItemDateDTO(1, LocalDate.of(2022, 4, 1)),
                        new ItemDateDTO(1, LocalDate.of(2022, 4, 2)));
        // @formatter:on
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
                                ArgumentMatchers.any(FileInputDataLimpezaDTO.class),
                                Mockito.<ItemDateDTO>anyList(),
                                ArgumentMatchers.any(Integer.class)))
                .thenReturn(MOCK_LIST_GROUPS);
        FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    @Test
    void shouldGenerateListLayout2SuccessWithAddToListException()
            throws IllegalAccessException, ListBuilderException {
        var addToList = Map.of("aa-04-2022", "Após a Celebração");
        writeFileInputFromDto(builder.withSuccess().withAddToList(addToList).build());
        FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.generateList());
        assertEquals(
                "Erro ao gerar lista 'LIMPEZA': Valor 'aa-04-2022' não é uma data válida",
                exception.getMessage());
    }

    @Test
    @SneakyThrows
    void shouldGenerateListLayout2SuccessWithRemoveToListException() {
        var removeToList = List.of("12-04-aaaaa");
        writeFileInputFromDto(builder.withSuccess().withRemoveFromList(removeToList).build());
        FieldUtils.writeField(properties, "layoutLimpeza", 2, true);
        var exception =
                Assertions.assertThrows(ListBuilderException.class, () -> service.generateList());
        assertEquals(
                "Erro ao gerar lista 'LIMPEZA': Valor '12-04-aaaaa' não é uma data válida",
                exception.getMessage());
    }

    @Test
    @SneakyThrows
    void shouldGetLabelNoExceptionAndNoLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1));
        var ret = service.getLabel(dto, false);
        assertEquals("Terça", ret);
    }

    @DisplayName("Get Label Exception And No Label")
    @ParameterizedTest(name = "Label Exception is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @SneakyThrows
    void shouldGetLabelExceptionAndNoLabel(String messageException) {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), messageException);
        var ret = service.getLabel(dto, false);
        assertEquals("Terça", ret);
    }

    @Test
    @SneakyThrows
    void shouldGetLabelExceptionMessageBlankAndNoLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), "myMessage");
        var ret = service.getLabel(dto, false);
        assertEquals("Terça", ret);
    }

    @Test
    @SneakyThrows
    void shouldGetLabelNoExceptionAndAndLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1));
        var ret = service.getLabel(dto, true);
        assertEquals("Terça - Após a Reunião", ret);
    }

    @DisplayName("Get Label Exception And Label")
    @ParameterizedTest(name = "Label Exception is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @SneakyThrows
    void shouldGetLabelExceptionNullAndLabel(String messageException) {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), messageException);
        var ret = service.getLabel(dto, true);
        assertEquals("Terça - Após a Reunião", ret);
    }

    @Test
    @SneakyThrows
    void shouldGetLabelExceptionMessageBlankAndLabel() {
        var dto = new ItemDateDTO(LocalDate.of(2022, 3, 1), "myMessage");
        var ret = service.getLabel(dto, true);
        assertEquals("Terça - myMessage", ret);
    }

    private void validateListBuilderException(String expectedMessageError) {
        this.testUtils.validateException(
                () -> service.generateList(), expectedMessageError);
    }
}
