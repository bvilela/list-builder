package br.com.bvilela.listbuilder.service.christianlife;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.bvilela.listbuilder.annotation.NullAndEmptyAndBlankSource;
import br.com.bvilela.listbuilder.builder.christianlife.ChristianLifeInputDtoBuilder;
import br.com.bvilela.listbuilder.builder.christianlife.ChristianLifeInputRenameItemDtoBuilder;
import br.com.bvilela.listbuilder.builder.christianlife.ChristianLifeExtractWeekDtoBuilder;
import br.com.bvilela.listbuilder.builder.christianlife.ChristianLifeExtractWeekItemDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChristianLifeGenerateServiceImplTest
        extends BaseGenerateServiceTest<ChristianLifeInputDTO, ChristianLifeInputDtoBuilder> {

    @InjectMocks private ChristianLifeGenerateServiceImpl service;

    @InjectMocks private AppProperties appProperties;

    @Mock private ChristianLifeExtractService extractService;

    @Mock private ChristianLifeWriterService writerService;

    @Mock private SendNotificationService notificationService;

    public ChristianLifeGenerateServiceImplTest() {
        super(
                ListTypeEnum.VIDA_CRISTA,
                ChristianLifeInputDtoBuilder.create().withRandomData());
    }

    @BeforeEach
    public void setup() {
        new PropertiesTestUtils(appProperties).setInputDir(testUtils.getResourceDirectory());
        service =
                new ChristianLifeGenerateServiceImpl(
                        appProperties, extractService, writerService, notificationService);
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
        assertEquals("dados-vida-crista.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListFileNotFoundException() {
        callGenerateListAndVerifyExceptionMessage(MessageConfig.FILE_NOT_FOUND);
    }

    @Test
    void shouldGenerateListFileSyntaxException() {
        testUtils.writeFileInputSyntaxError();
        callGenerateListAndVerifyExceptionMessage(MessageConfig.FILE_SYNTAX_ERROR);
    }

    @ParameterizedTest
    @NullAndEmptyAndBlankSource
    void givenInputFile_whenLastDateNotFilled_thenException(String lastDate) {
        writeFileInputFromDto(builder.withLastDate(lastDate).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.LAST_DATE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionParticipantsNull() {
        validateGenerateListParticipantsException(null);
    }

    @Test
    void shouldGenerateListExceptionParticipantsEmpty() {
        validateGenerateListParticipantsException(List.of());
    }

    private void validateGenerateListParticipantsException(List<List<String>> participants) {
        writeFileInputFromDto(builder.withParticipants(participants).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.PARTICIPANTS_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionNumberWeeksExtractDiffNumberWeekInputDto() {
        writeFileInputFromDto(builder.build());
        var expectedMessageError =
                "Quantidade de semanas extraída do site é diferente da quantidade de semanas com participantes";
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldGenerateListRenameItemsExceptionWeekIndexNull() {
        var renameItem =
                ChristianLifeInputRenameItemDtoBuilder.create()
                        .withData(null, "original Title", null)
                        .build();
        var dto = builder.withRandomData().withRenameItems(List.of(renameItem)).build();
        writeFileInputFromDto(dto);
        var expectedMessageError = "Numero da Semana é obrigatório";
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @DisplayName("Generate List - Rename Items Exception: Week OriginalName Not Filled")
    @ParameterizedTest(name = "Week OriginalName is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void shouldGenerateListRenameItemsExceptionWeekOriginalNameNotFilled(String originalName) {
        var renameItem =
                ChristianLifeInputRenameItemDtoBuilder.create()
                        .withData(1, originalName, "new Title")
                        .build();
        var dto = builder.withRandomData().withRenameItems(List.of(renameItem)).build();
        writeFileInputFromDto(dto);
        var expectedMessageError = "Nome Original do Item é obrigatório";
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldProcessRenameOrRemoveItemRename() {
        var weekItemDto = ChristianLifeExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
        var listItemWeekToRemove = new ArrayList<ChristianLifeExtractWeekItemDTO>();
        var itemRename =
                ChristianLifeInputRenameItemDtoBuilder.create()
                        .withData(1, "Title 1", "Title 1 renamed")
                        .build();
        service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
        assertTrue(listItemWeekToRemove.isEmpty());
        assertEquals("Title 1 renamed", weekItemDto.getTitle());
    }

    @Test
    void shouldProcessRenameOrRemoveItemRemoveCaseNull() {
        var weekItemDto = ChristianLifeExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
        var listItemWeekToRemove = new ArrayList<ChristianLifeExtractWeekItemDTO>();
        var itemRename =
                ChristianLifeInputRenameItemDtoBuilder.create()
                        .withData(1, "Title 1", null)
                        .build();
        service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
        assertEquals(1, listItemWeekToRemove.size());
        assertEquals("Title 1", weekItemDto.getTitle());
    }

    @Test
    void shouldProcessRenameOrRemoveItemRemoveCaseEmpty() {
        var weekItemDto = ChristianLifeExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
        var listItemWeekToRemove = new ArrayList<ChristianLifeExtractWeekItemDTO>();
        var itemRename =
                ChristianLifeInputRenameItemDtoBuilder.create()
                        .withData(1, "Title 1", "")
                        .build();
        service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
        assertEquals(1, listItemWeekToRemove.size());
        assertEquals("Title 1", weekItemDto.getTitle());
    }

    @Test
    void shouldCheckRenameItemFromWeekNoRenameItemsForThisWeek() {
        int weekIndex = 0;
        var week = ChristianLifeExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
        var listRenameItems =
                List.of(
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(
                                        weekIndex + 3,
                                        week.getItems().get(6).getTitle(),
                                        "Title Renamed")
                                .build());
        service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
    }

    @Test
    void shouldCheckRenameItemFromWeekMatchNameToRename() {
        int weekIndex = 0;
        var week = ChristianLifeExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
        var listRenameItems =
                List.of(
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(
                                        weekIndex + 1,
                                        week.getItems().get(week.getItems().size() - 1).getTitle(),
                                        "Title Renamed")
                                .build());
        service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
        assertEquals("Title Renamed", week.getItems().get(week.getItems().size() - 1).getTitle());
        Assertions.assertFalse(listRenameItems.get(0).toString().isEmpty());
    }

    @Test
    void shouldCheckRenameItemFromWeekNoMatchNameToRename() {
        int weekIndex = 0;
        var week = ChristianLifeExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
        var listRenameItems =
                List.of(
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(weekIndex + 1, "ABC", "Title Renamed")
                                .build());
        service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
        assertTrue(
                week.getItems().stream()
                        .filter(e -> e.getTitle().equalsIgnoreCase("Title Renamed"))
                        .toList()
                        .isEmpty());
    }

    @Test
    void shouldCheckRenameItemFromWeekMatchNameToRemove() {
        int weekIndex = 0;
        var week = ChristianLifeExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
        var originalWeekItems = new ArrayList<>(week.getItems());
        var initialWeekItemsSize = week.getItems().size();
        var listRenameItems =
                List.of(
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(
                                        weekIndex + 1,
                                        week.getItems().get(initialWeekItemsSize - 1).getTitle(),
                                        null)
                                .build());
        service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
        assertEquals(initialWeekItemsSize - 1, week.getItems().size());
        assertEquals(originalWeekItems.get(0), week.getItems().get(0));
        assertEquals(originalWeekItems.get(1), week.getItems().get(1));
        assertEquals(originalWeekItems.get(2), week.getItems().get(2));
        assertEquals(originalWeekItems.get(3), week.getItems().get(3));
        assertEquals(originalWeekItems.get(4), week.getItems().get(4));
        assertEquals(originalWeekItems.get(5), week.getItems().get(5));
    }

    @Test
    void shouldCheckRenameItemFromWeekMatchNameToRenameNoItemsCanRenamed() {
        int weekIndex = 0;
        var week = ChristianLifeExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
        var originalWeekItems = cloneListWithoutReference(week.getItems());
        var initialWeekItemsSize = week.getItems().size();
        var auxList = new ArrayList<>(week.getItems());
        auxList.remove(initialWeekItemsSize - 1);
        week.setItems(auxList);
        var listRenameItems =
                List.of(
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(
                                        weekIndex + 1,
                                        week.getItems().get(1).getTitle(),
                                        "Title Rename")
                                .build());
        service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
        assertEquals(initialWeekItemsSize - 1, week.getItems().size());
        assertEquals(originalWeekItems.get(0), week.getItems().get(0));
        assertEquals(originalWeekItems.get(1), week.getItems().get(1));
        assertEquals(originalWeekItems.get(2), week.getItems().get(2));
        assertEquals(originalWeekItems.get(3), week.getItems().get(3));
        assertEquals(originalWeekItems.get(4), week.getItems().get(4));
        assertEquals(originalWeekItems.get(5), week.getItems().get(5));
    }

    @Test
    void shouldCheckRenameItemFromWeekMatchNameToRemoveAndRename() {
        int weekIndex = 0;
        var week = ChristianLifeExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
        var auxList = new ArrayList<>(week.getItems());
        auxList.add(
                ChristianLifeExtractWeekItemDtoBuilder.create()
                        .withRandomData(ChristianLifeExtractItemTypeEnum.WITH_PARTICIPANTS)
                        .build());
        week.setItems(auxList);
        var initialWeekItemsSize = week.getItems().size();
        var listRenameItems =
                List.of(
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(
                                        weekIndex + 1,
                                        week.getItems().get(initialWeekItemsSize - 2).getTitle(),
                                        "Title Renamed")
                                .build(),
                        ChristianLifeInputRenameItemDtoBuilder.create()
                                .withData(
                                        weekIndex + 1,
                                        week.getItems().get(initialWeekItemsSize - 1).getTitle(),
                                        null)
                                .build());
        var originalWeekItems = cloneListWithoutReference(week.getItems());
        service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
        assertEquals(initialWeekItemsSize, originalWeekItems.size());
        assertEquals(initialWeekItemsSize - 1, week.getItems().size());
        assertEquals(originalWeekItems.get(0), week.getItems().get(0));
        assertEquals(originalWeekItems.get(1), week.getItems().get(1));
        assertEquals(originalWeekItems.get(2), week.getItems().get(2));
        assertEquals(originalWeekItems.get(3), week.getItems().get(3));
        assertEquals(originalWeekItems.get(4), week.getItems().get(4));
        assertEquals(originalWeekItems.get(5), week.getItems().get(5));
        assertNotEquals(originalWeekItems.get(6), week.getItems().get(6));
        assertEquals("Title Renamed", week.getItems().get(6).getTitle());
        assertNotNull(originalWeekItems.get(7));
    }

    @DisplayName("Remove Weeks If Necessary By Input DTO")
    @ParameterizedTest(name = "MapRemove is \"{0}\"")
    @MethodSource("removeWeeksIfNecessaryByInputDtoParameter")
    void removeWeeksIfNecessaryByInputDTO(
            Map<LocalDate, String> mapRemove, boolean isSkip, String skipMessage) {
        List<ChristianLifeExtractWeekDTO> weeks =
                List.of(
                        ChristianLifeExtractWeekDtoBuilder.create()
                                .withRandomData()
                                .withInitialDate(LocalDate.of(2023, 6, 5))
                                .withEndDate(LocalDate.of(2023, 6, 11))
                                .build(),
                        ChristianLifeExtractWeekDtoBuilder.create()
                                .withRandomData()
                                .withInitialDate(LocalDate.of(2023, 6, 12))
                                .withEndDate(LocalDate.of(2023, 6, 18))
                                .build(),
                        ChristianLifeExtractWeekDtoBuilder.create()
                                .withRandomData()
                                .withInitialDate(LocalDate.of(2023, 6, 19))
                                .withEndDate(LocalDate.of(2023, 6, 25))
                                .build(),
                        ChristianLifeExtractWeekDtoBuilder.create()
                                .withRandomData()
                                .withInitialDate(LocalDate.of(2023, 6, 26))
                                .withEndDate(LocalDate.of(2023, 7, 2))
                                .build());
        service.removeWeeksIfNecessaryByInputDTO(weeks, mapRemove);
        assertFalse(weeks.get(0).isSkip());
        assertNull(weeks.get(0).getSkipMessage());
        assertFalse(weeks.get(1).isSkip());
        assertNull(weeks.get(1).getSkipMessage());
        assertFalse(weeks.get(2).isSkip());
        assertNull(weeks.get(2).getSkipMessage());
        assertEquals(isSkip, weeks.get(3).isSkip());
        assertEquals(skipMessage, weeks.get(3).getSkipMessage());
    }

    private static Stream<Arguments> removeWeeksIfNecessaryByInputDtoParameter() {
        var argumentsMatchDateRemoveMidWeek =
                Arguments.of(Map.of(LocalDate.of(2023, 6, 28), "Congresso"), true, "Congresso");
        var argumentsMatchDateRemoveInitWeek =
                Arguments.of(Map.of(LocalDate.of(2023, 6, 26), "Congresso"), true, "Congresso");
        var argumentsMatchDateRemoveEndWeek =
                Arguments.of(Map.of(LocalDate.of(2023, 7, 2), "Congresso"), true, "Congresso");
        var argumentsNoRemove = Arguments.of(null, false, null);
        var argumentsAfterWeeks =
                Arguments.of(Map.of(LocalDate.of(2023, 8, 28), "Congresso"), false, null);
        var argumentsBeforeWeeks =
                Arguments.of(Map.of(LocalDate.of(2023, 1, 1), "Congresso"), false, null);
        return Stream.of(
                argumentsMatchDateRemoveMidWeek,
                argumentsMatchDateRemoveInitWeek,
                argumentsMatchDateRemoveEndWeek,
                argumentsNoRemove,
                argumentsAfterWeeks,
                argumentsBeforeWeeks);
    }

    private void callGenerateListAndVerifyExceptionMessage(String expectedMessageError) {
        this.testUtils.validateException(() -> service.generateList(), expectedMessageError);
    }

    private List<ChristianLifeExtractWeekItemDTO> cloneListWithoutReference(
            List<ChristianLifeExtractWeekItemDTO> listSrc) {
        var newList = new ArrayList<ChristianLifeExtractWeekItemDTO>();
        for (ChristianLifeExtractWeekItemDTO item : listSrc) {
            var newItem = new ChristianLifeExtractWeekItemDTO();
            newItem.setType(item.getType());
            newItem.setTitle(item.getTitle());
            newItem.setParticipants(item.getParticipants());
            newList.add(newItem);
        }
        return newList;
    }
}
