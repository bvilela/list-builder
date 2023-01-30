package com.bruno.listbuilder.service.vidacrista.impl;

import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputVidaCristaFromDto;
import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputVidaCristaSyntaxError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bruno.listbuilder.builder.FileInputDataVidaCristaDtoBuilder;
import com.bruno.listbuilder.builder.FileInputDataVidaCristaRenameItemDtoBuilder;
import com.bruno.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import com.bruno.listbuilder.builder.VidaCristaExtractWeekItemDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.enuns.VidaCristaExtractItemType;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.vidacrista.VidaCristaExtractService;
import com.bruno.listbuilder.service.vidacrista.VidaCristaWriterService;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

class VidaCristaGenerateServiceImplTest {

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
	
	@Mock
	private ConvertImageService convertImageService;

	private final static String MODE_EXECUTION = ListTypeEnum.VIDA_CRISTA.toString();
	private static String resourceDirectory;

	@BeforeAll
	static void setupBeforeAll() throws ListBuilderException {
		resourceDirectory = Paths.get("src", "test", "resources", MODE_EXECUTION.toLowerCase()).toFile()
				.getAbsolutePath();
		FileUtils.createDirectories(resourceDirectory);
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", resourceDirectory, true);
		service = new VidaCristaGenerateServiceImpl(properties, extractService, writerService, notificationService, convertImageService);
	}

	@AfterAll
	static void setupAfterAll() {
		TestFileUtilsWriteFile.cleanDirVidaCrista();
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getExecutionMode());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(ListTypeEnum.VIDA_CRISTA, service.getExecutionMode());
	}

	@Test
	void shouldGenerateListFileNotFoundException() throws IllegalAccessException {
		baseGenerateListException("xpto.json", MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileSyntaxException() throws IllegalAccessException {
		String fileNameSyntaxError = "dados-vida-crista-syntax-error.json";
		writeFileInputVidaCristaSyntaxError(fileNameSyntaxError);
		baseGenerateListException(fileNameSyntaxError, MessageConfig.FILE_SYNTAX_ERROR);
	}

	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		baseGenerateListExceptionLastDate("data-vida-crista-last-date-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		baseGenerateListExceptionLastDate("data-vida-crista-last-date-empty.json", "");
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		baseGenerateListExceptionLastDate("data-vida-crista-last-date-blank.json", " ");
	}

	@Test
	void shouldGenerateListExceptionParticipantsNull() throws IllegalAccessException {
		baseGenerateListExceptionParticipants("dados-vida-crista-participants-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionParticipantsEmpty() throws IllegalAccessException {
		baseGenerateListExceptionParticipants("dados-vida-crista-participants-empty.json", List.of());
	}

	@Test
	void shouldGenerateListExceptionNumberWeeksExtractDiffNumberWeekInputDto() throws IllegalAccessException {
		var dto = FileInputDataVidaCristaDtoBuilder.create().withRandomData().build();
		var fileName = "dados-vida-crista-success.json";
		writeFileInputVidaCristaFromDto(fileName, dto);
		var messageError = "Quantidade de semanas extraída do site é diferente da quantidade de semanas com participantes";
		baseGenerateListException(fileName, messageError);
	}
	
	@Test
	void shouldGenerateListRenameItemsExceptionWeekIndexNull() throws IllegalAccessException {
		var renameItem = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(null, "original Title", null).build();
		var dto = FileInputDataVidaCristaDtoBuilder.create().withRandomData().withRenameItems(List.of(renameItem)).build();
		var fileName = "dados-vida-crista-rename-weekindex.json";
		writeFileInputVidaCristaFromDto(fileName, dto);
		var messageError = "Numero da Semana é obrigatório";
		baseGenerateListException(fileName, messageError);
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
	
	private void baseGenerateListRenameItemsExceptionWeekOriginalName(String originalName) throws IllegalAccessException {
		var renameItem = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(1, originalName, "new Title").build();
		var dto = FileInputDataVidaCristaDtoBuilder.create().withRandomData().withRenameItems(List.of(renameItem)).build();
		var fileName = "dados-vida-crista-rename-weekindex.json";
		writeFileInputVidaCristaFromDto(fileName, dto);
		var messageError = "Nome Original do Item é obrigatório";
		baseGenerateListException(fileName, messageError);
	}
	
	@Test
	void shouldProcessRenameOrRemoveItemRename() {
		var weekItemDto = VidaCristaExtractWeekItemDtoBuilder.create().withTitle("Title 1").build();
		var listItemWeekToRemove = new ArrayList<VidaCristaExtractWeekItemDTO>();
		var itemRename = FileInputDataVidaCristaRenameItemDtoBuilder.create().withData(1, "Title 1", "Title 1 renamed").build();
		service.processRenameOrRemoveItem(weekItemDto, itemRename, listItemWeekToRemove);
		assertTrue(listItemWeekToRemove.isEmpty());
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
				.withData(weekIndex + 1, week.getItems().get(week.getItems().size()-1).getTitle(), "Title Renamed").build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		assertEquals("Title Renamed", week.getItems().get(week.getItems().size()-1).getTitle());
		assertFalse(listRenameItems.get(0).toString().isEmpty());
	}
	
	@Test
	void shouldCheckRenameItemFromWeekNoMatchNameToRename() {
		int weekIndex = 0;
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, "ABC", "Title Renamed").build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		assertTrue(week.getItems().stream().filter(e -> e.getTitle().equalsIgnoreCase("Title Renamed")).toList().isEmpty());
	}
	
	@Test
	void shouldCheckRenameItemFromWeekMatchNameToRemove() {
		int weekIndex = 0;
		var week = VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build();
		var originalWeekItems = new ArrayList<>(week.getItems());
		var initialWeekItemsSize = week.getItems().size();
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, week.getItems().get(initialWeekItemsSize-1).getTitle(), null).build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		assertEquals(initialWeekItemsSize-1, week.getItems().size());
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
		auxList.remove(initialWeekItemsSize-1);
		week.setItems(auxList);
		var listRenameItems = List.of(FileInputDataVidaCristaRenameItemDtoBuilder.create()
				.withData(weekIndex + 1, week.getItems().get(1).getTitle(), "Title Rename").build());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		assertEquals(initialWeekItemsSize-1, week.getItems().size());
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
		auxList.add(VidaCristaExtractWeekItemDtoBuilder.create().withRandomData(VidaCristaExtractItemType.WITH_PARTICIPANTS).build());
		week.setItems(auxList);
		var initialWeekItemsSize = week.getItems().size();
		var listRenameItems = List.of(
				FileInputDataVidaCristaRenameItemDtoBuilder.create()
					.withData(weekIndex + 1, week.getItems().get(initialWeekItemsSize-2).getTitle(), "Title Renamed").build(),
				FileInputDataVidaCristaRenameItemDtoBuilder.create()
					.withData(weekIndex + 1, week.getItems().get(initialWeekItemsSize-1).getTitle(), null).build()
		);
		var originalWeekItems = cloneListWithoutReference(week.getItems());
		service.checkRenameItemFromWeek(week, listRenameItems, weekIndex);
		assertEquals(initialWeekItemsSize, originalWeekItems.size());
		assertEquals(initialWeekItemsSize-1, week.getItems().size());
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
	
	private void baseGenerateListExceptionParticipants(String fileName, List<List<String>> participants)
			throws IllegalAccessException {
		var dto = FileInputDataVidaCristaDtoBuilder.create().withRandomData().build();
		dto.setParticipants(participants);
		writeFileInputVidaCristaFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.PARTICIPANTS_REQUIRED);
	}

	private void baseGenerateListExceptionLastDate(String fileName, String lastDate) throws IllegalAccessException {
		var dto = FileInputDataVidaCristaDtoBuilder.create().withRandomData().build();
		dto.setLastDate(lastDate);
		writeFileInputVidaCristaFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
	}

	private void baseGenerateListException(String file, String message) throws IllegalAccessException {
		var ex = baseGenerateListException(file);
		String baseMessage = String.format("Erro ao gerar lista '%s': %s", MODE_EXECUTION, message);
		assertEquals(ex.getMessage(), baseMessage);
	}

	private ListBuilderException baseGenerateListException(String file) throws IllegalAccessException {
		setInputFileName(file);
		return assertThrows(ListBuilderException.class, () -> service.generateList());
	}

	private void setInputFileName(String file) throws IllegalAccessException {
		FieldUtils.writeField(properties, "inputFileNameVidaCrista", file, true);
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
