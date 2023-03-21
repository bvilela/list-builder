package com.bruno.listbuilder.service.vidacrista.impl;

import com.bruno.listbuilder.builder.FileInputDataVidaCristaDtoBuilder;
import com.bruno.listbuilder.builder.FileInputDataVidaCristaRenameItemDtoBuilder;
import com.bruno.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import com.bruno.listbuilder.builder.VidaCristaExtractWeekItemDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.vidacrista.FileInputDataVidaCristaDTO;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.enuns.VidaCristaExtractItemType;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.BaseGenerateServiceTest;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.vidacrista.VidaCristaExtractService;
import com.bruno.listbuilder.service.vidacrista.VidaCristaWriterService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VidaCristaGenerateServiceImplTest
		extends BaseGenerateServiceTest<FileInputDataVidaCristaDTO, FileInputDataVidaCristaDtoBuilder> {

	@InjectMocks
	private VidaCristaGenerateServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@Mock
	private VidaCristaExtractService extractService;

	@Mock
	private VidaCristaWriterService writerService;

	@Mock
	private NotificationService notificationService;

	public VidaCristaGenerateServiceImplTest() throws ListBuilderException {
		super(ListTypeEnum.VIDA_CRISTA, FileInputDataVidaCristaDtoBuilder.create().withRandomData());
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", BaseGenerateServiceTest.testUtils.getResourceDirectory(), true);
		service = new VidaCristaGenerateServiceImpl(properties, extractService, writerService, notificationService);
	}

	@Test
	void shouldModoExecutionNotNull() {
		Assertions.assertNotNull(service.getListType());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(BaseGenerateServiceTest.testUtils.getListType(), service.getListType());
	}
	
	@Test
	void shouldCorrectFileInputName() {
		assertEquals("dados-vida-crista.json", service.getListType().getInputFileName());
	}

	@Test
	void shouldGenerateListFileNotFoundException() throws IllegalAccessException {
		validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileSyntaxException() throws IllegalAccessException {
		BaseGenerateServiceTest.testUtils.writeFileInputSyntaxError();
		validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
	}

	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		validateGenerateListLastDateException(null);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		validateGenerateListLastDateException("");
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		validateGenerateListLastDateException(" ");
	}

	private void validateGenerateListLastDateException(String lastDate) throws IllegalAccessException {
		writeFileInputFromDto(builder.withLastDate(lastDate).build());
		validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionParticipantsNull() throws IllegalAccessException {
		validateGenerateListParticipantsException(null);
	}

	@Test
	void shouldGenerateListExceptionParticipantsEmpty() throws IllegalAccessException {
		validateGenerateListParticipantsException(List.of());
	}

	private void validateGenerateListParticipantsException(List<List<String>> participants)
			throws IllegalAccessException {
		writeFileInputFromDto(builder.withParticipants(participants).build());
		validateListBuilderException(MessageConfig.PARTICIPANTS_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionNumberWeeksExtractDiffNumberWeekInputDto() throws IllegalAccessException {
		writeFileInputFromDto(builder.build());
		var expectedMessageError = "Quantidade de semanas extraída do site é diferente da quantidade de semanas com participantes";
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListRenameItemsExceptionWeekIndexNull() throws IllegalAccessException {
		var renameItem = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(null, "original Title", null)
				.build();
		var dto = builder.withRandomData().withRenameItems(List.of(renameItem)).build();
		writeFileInputFromDto(dto);
		var expectedMessageError = "Numero da Semana é obrigatório";
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListRenameItemsExceptionWeekOriginalNameNull() throws IllegalAccessException {
		baseGenerateListRenameItemsExceptionWeekOriginalName(null);
	}

	@Test
	void shouldGenerateListRenameItemsExceptionWeekOriginalNameEmpty() throws IllegalAccessException {
		baseGenerateListRenameItemsExceptionWeekOriginalName("");
	}

	@Test
	void shouldGenerateListRenameItemsExceptionWeekOriginalNameBlank() throws IllegalAccessException {
		baseGenerateListRenameItemsExceptionWeekOriginalName(" ");
	}

	private void baseGenerateListRenameItemsExceptionWeekOriginalName(String originalName)
			throws IllegalAccessException {
		var renameItem = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(1, originalName, "new Title")
				.build();
		var dto = builder.withRandomData().withRenameItems(List.of(renameItem)).build();
		writeFileInputFromDto(dto);
		var expectedMessageError = "Nome Original do Item é obrigatório";
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldProcessRenameOrRemoveItemRename() {
		var weekItemDto = VidaCristaExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
		var listItemWeekToRemove = new ArrayList<VidaCristaExtractWeekItemDTO>();
		var itemRename = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(1, "Title 1", "Title 1 renamed")
				.build();
		service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
		Assertions.assertTrue(listItemWeekToRemove.isEmpty());
		assertEquals("Title 1 renamed", weekItemDto.getTitle());
	}

	@Test
	void shouldProcessRenameOrRemoveItemRemoveCaseNull() {
		var weekItemDto = VidaCristaExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
		var listItemWeekToRemove = new ArrayList<VidaCristaExtractWeekItemDTO>();
		var itemRename = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(1, "Title 1", null).build();
		service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
		assertEquals(1, listItemWeekToRemove.size());
		assertEquals("Title 1", weekItemDto.getTitle());
	}

	@Test
	void shouldProcessRenameOrRemoveItemRemoveCaseEmpty() {
		var weekItemDto = VidaCristaExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
		var listItemWeekToRemove = new ArrayList<VidaCristaExtractWeekItemDTO>();
		var itemRename = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(1, "Title 1", "").build();
		service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
		assertEquals(1, listItemWeekToRemove.size());
		assertEquals("Title 1", weekItemDto.getTitle());
	}

	@Test
	void shouldCheckRenameItemFromWeekNoRenameItemsForThisWeek() {
		int weekIndex = 0;
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 3, week.getItems().get(6).getTitle(), "Title Renamed").build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
	}

	@Test
	void shouldCheckRenameItemFromWeekMatchNameToRename() {
		int weekIndex = 0;
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, week.getItems().get(week.getItems().size() - 1).getTitle(), "Title Renamed")
				.build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		assertEquals("Title Renamed", week.getItems().get(week.getItems().size() - 1).getTitle());
		Assertions.assertFalse(listRenameItems.get(0).toString().isEmpty());
	}

	@Test
	void shouldCheckRenameItemFromWeekNoMatchNameToRename() {
		int weekIndex = 0;
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, "ABC", "Title Renamed").build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		Assertions.assertTrue(week.getItems().stream().filter(e -> e.getTitle().equalsIgnoreCase("Title Renamed")).toList()
				.isEmpty());
	}

	@Test
	void shouldCheckRenameItemFromWeekMatchNameToRemove() {
		int weekIndex = 0;
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var originalWeekItems = new ArrayList<>(week.getItems());
		var initialWeekItemsSize = week.getItems().size();
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, week.getItems().get(initialWeekItemsSize - 1).getTitle(), null).build());
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
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var originalWeekItems = cloneListWithoutReference(week.getItems());
		var initialWeekItemsSize = week.getItems().size();
		var auxList = new ArrayList<>(week.getItems());
		auxList.remove(initialWeekItemsSize - 1);
		week.setItems(auxList);
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, week.getItems().get(1).getTitle(), "Title Rename").build());
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
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var auxList = new ArrayList<>(week.getItems());
		auxList.add(VidaCristaExtractWeekItemDtoBuilder.create()
				.withRandomData(VidaCristaExtractItemType.WITH_PARTICIPANTS).build());
		week.setItems(auxList);
		var initialWeekItemsSize = week.getItems().size();
		var listRenameItems = List.of(
				FileInputDataVidaCristaRenameItemDtoBuilder.create()
						.withData(weekIndex + 1, week.getItems().get(initialWeekItemsSize - 2).getTitle(),
								"Title Renamed")
						.build(),
				FileInputDataVidaCristaRenameItemDtoBuilder.create()
						.withData(weekIndex + 1, week.getItems().get(initialWeekItemsSize - 1).getTitle(), null)
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
		Assertions.assertNotEquals(originalWeekItems.get(6), week.getItems().get(6));
		assertEquals("Title Renamed", week.getItems().get(6).getTitle());
		Assertions.assertNotNull(originalWeekItems.get(7));
	}

	private void validateListBuilderException(String expectedMessageError) throws IllegalAccessException {
		BaseGenerateServiceTest.testUtils.validateException(ListBuilderException.class, () -> service.generateList(), expectedMessageError);
	}

	private List<VidaCristaExtractWeekItemDTO> cloneListWithoutReference(List<VidaCristaExtractWeekItemDTO> listSrc) {
		var newList = new ArrayList<VidaCristaExtractWeekItemDTO>();
		for (VidaCristaExtractWeekItemDTO item : listSrc) {
			var newItem = new VidaCristaExtractWeekItemDTO();
			newItem.setType(item.getType());
			newItem.setTitle(item.getTitle());
			newItem.setParticipants(item.getParticipants());
			newList.add(newItem);
		}
		return newList;
	}

}
