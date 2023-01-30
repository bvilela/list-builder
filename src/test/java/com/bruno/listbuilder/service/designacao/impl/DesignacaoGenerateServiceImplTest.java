package com.bruno.listbuilder.service.designacao.impl;

import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputDesignacaoFromDto;
import static com.bruno.listbuilder.utils.TestFileUtilsWriteFile.writeFileInputDesignacaoSyntaxError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bruno.listbuilder.builder.designacao.FileInputDataDesignacaoDtoBuilder;
import com.bruno.listbuilder.builder.designacao.FileInputDataDesignacaoListDtoBuilder;
import com.bruno.listbuilder.builder.designacao.FileInputDataDesignacaoReaderDtoBuilder;
import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.service.GroupService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.designacao.DesignacaoCounterService;
import com.bruno.listbuilder.service.designacao.DesignacaoWriterService;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.utils.TestFileUtilsWriteFile;

class DesignacaoGenerateServiceImplTest {

	@InjectMocks
	private DesignacaoGenerateServiceImpl service;

	@InjectMocks
	private AppProperties properties;

	@Mock
	private DateService dateService;

	@Mock
	private GroupService groupService;

	@Mock
	private DesignacaoWriterService writerService;

	@Mock
	private NotificationService notificationService;
	
	@Mock
	private DesignacaoCounterService counterService;

	private final static String MODE_EXECUTION = ListTypeEnum.DESIGNACAO.toString();
	private static String resourceDirectory;
	private static FileInputDataDesignacaoDtoBuilder defaultBuilder;

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
		service = new DesignacaoGenerateServiceImpl(properties, dateService, groupService, writerService,
				counterService, notificationService);
		defaultBuilder = FileInputDataDesignacaoDtoBuilder.create().withRandomData();
	}

	@AfterAll
	static void setupAfterAll() {
		TestFileUtilsWriteFile.cleanDirDesignacao();
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getExecutionMode());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(ListTypeEnum.DESIGNACAO, service.getExecutionMode());
	}

	@Test
	void shouldGenerateListFileNotFoundException() throws IllegalAccessException {
		baseGenerateListException("xpto.json", MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileSyntaxException() throws IllegalAccessException {
		String fileNameSyntaxError = "dados-designacao-syntax-error.json";
		writeFileInputDesignacaoSyntaxError(fileNameSyntaxError);
		baseGenerateListException(fileNameSyntaxError, MessageConfig.FILE_SYNTAX_ERROR);
	}
	
	
	
	
	// *************************** LASTDATE - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		baseGenerateListExceptionLastDate("data-designacao-last-date-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		baseGenerateListExceptionLastDate("data-designacao-last-date-empty.json", "");
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		baseGenerateListExceptionLastDate("data-designacao-last-date-blank.json", " ");
	}
	
	private void baseGenerateListExceptionLastDate(String fileName, String lastDate) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withLastDate(lastDate).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.LAST_DATE_REQUIRED);
		assertFalse(dto.toString().isBlank());
	}
	
	@Test
	void shouldGenerateListExceptionLastDateInvalid() throws IllegalAccessException {
		String fileName = "data-designacao-last-date-invalid.json";
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withLastDate("01-20-2021").build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		var msgErro = String.format("Última Data da Lista Anterior inválida: '%s' não é uma data válida",
				dto.getLastDate());
		baseGenerateListException(fileName, msgErro);
	}
	// *************************** LASTDATE - FIM *************************** \\
	
	
	
	
	// *************************** MIDWEEK - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionMidweekNull() throws IllegalAccessException {
		baseGenerateListExceptionMidweek("data-designacao-midweek-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionMidweekEmpty() throws IllegalAccessException {
		baseGenerateListExceptionMidweek("data-designacao-midweek-empty.json", "");
	}

	@Test
	void shouldGenerateListExceptionMidweekBlank() throws IllegalAccessException {
		baseGenerateListExceptionMidweek("data-designacao-midweek-blank.json", " ");
	}
	
	private void baseGenerateListExceptionMidweek(String fileName, String dayMidweek) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withMeetingDayMidweek(dayMidweek).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekInvalid() throws IllegalAccessException {
		String fileName = "data-designacao-midweek-invalid.json";
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withMeetingDayMidweek("ABC").build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		var msgError = String.format("Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayMidweek());
		baseGenerateListException(fileName, msgError);
	}
	// *************************** MIDWEEK - FIM *************************** \\
	
	
	
	
	// *************************** WEEKEND - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionWeekendNull() throws IllegalAccessException {
		baseGenerateListExceptionWeekend("data-designacao-weekend-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionWeekendEmpty() throws IllegalAccessException {
		baseGenerateListExceptionWeekend("data-designacao-weekend-empty.json", "");
	}

	@Test
	void shouldGenerateListExceptionWeekendBlank() throws IllegalAccessException {
		baseGenerateListExceptionWeekend("data-limpeza-weekend-blank.json", " ");
	}
	
	private void baseGenerateListExceptionWeekend(String fileName, String dayWeekend) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withMeetingDayWeekend(dayWeekend).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendInvalid() throws IllegalAccessException {
		String fileName = "data-limpeza-weekend-invalid.json";
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withMeetingDayWeekend("ABC").build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		var msgError = String.format("Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayWeekend());
		baseGenerateListException(fileName, msgError);
	}
	// *************************** WEEKEND - FIM *************************** \\	
	
	
	
	
	// *************************** INDICATOR - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionIndicatorNull() throws IllegalAccessException {
		baseGenerateListExceptionIndicator("data-designacao-indicator-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionIndicatorEmpty() throws IllegalAccessException {
		baseGenerateListExceptionIndicator("data-designacao-indicator-empty.json", List.of());
	}

	@Test
	void shouldGenerateListExceptionIndicatorElementEmpty() throws IllegalAccessException {
		baseGenerateListExceptionIndicatorElement("data-designacao-indicator-element-empty.json", List.of(""));
	}
	
	@Test
	void shouldGenerateListExceptionIndicatorElementBlank() throws IllegalAccessException {
		baseGenerateListExceptionIndicatorElement("data-designacao-indicator-element-blank.json", List.of(" "));
	}
	
	private void baseGenerateListExceptionIndicator(String fileName, List<String> indicator) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withIndicator(indicator).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.INDICATOR_REQUIRED);
		assertFalse(dto.toString().isBlank());
	}
	
	private void baseGenerateListExceptionIndicatorElement(String fileName, List<String> indicator) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withIndicator(indicator).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.INDICATOR_ELEMENT_REQUIRED);
	}
	// *************************** INDICATOR - FIM *************************** \\
	
	
	
	
	// *************************** MICROPHONE - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionMicrophoneNull() throws IllegalAccessException {
		baseGenerateListExceptionMicrophone("data-designacao-microphone-null.json", null);
	}

	@Test
	void shouldGenerateListExceptionMicrophoneEmpty() throws IllegalAccessException {
		baseGenerateListExceptionMicrophone("data-designacao-microphone-empty.json", List.of());
	}

	@Test
	void shouldGenerateListExceptionMicrophoneElementEmpty() throws IllegalAccessException {
		baseGenerateListExceptionMicrophoneElement("data-designacao-microphone-element-empty.json", List.of(""));
	}
	
	@Test
	void shouldGenerateListExceptionMicrophoneElementBlank() throws IllegalAccessException {
		baseGenerateListExceptionMicrophoneElement("data-designacao-microphone-element-blank.json", List.of(" "));
	}
	
	private void baseGenerateListExceptionMicrophone(String fileName, List<String> microphone) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withMicrophone(microphone).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.MICROPHONE_REQUIRED);
		assertFalse(dto.toString().isBlank());
	}
	
	private void baseGenerateListExceptionMicrophoneElement(String fileName, List<String> microphone) throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withMicrophone(microphone).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, MessageConfig.MICROPHONE_ELEMENT_REQUIRED);
	}
	// *************************** MICROPHONE - FIM *************************** \\
	
	
	
	
	// *************************** PRESIDENT - INICIO *************************** \\
	// President Entity Null
	@Test
	void shouldGenerateListExceptionPresidentNull() throws IllegalAccessException {
		baseEntityRequiredNull("president", defaultBuilder.withPresident(null), MessageConfig.PRESIDENT_REQUIRED);
	}
	
	// President Last
	@Test
	void shouldGenerateListExceptionPresidentLastNull() throws IllegalAccessException {
		baseGenerateListExceptionPresidentLastException("data-designacao-president-last-null.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionPresidentLastEmpty() throws IllegalAccessException {
		baseGenerateListExceptionPresidentLastException("data-designacao-president-last-empty.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionPresidentLastBlank() throws IllegalAccessException {
		baseGenerateListExceptionPresidentLastException("data-designacao-president-last-blank.json", null);
	}
	
	private void baseGenerateListExceptionPresidentLastException(String fileName, String last) throws IllegalAccessException {
		var presidentDto = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withLast(last).build();
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withPresident(presidentDto).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Presidente: ".concat(MessageConfig.LAST_REQUIRED));
	}
	
	// President List
	@Test
	void shouldGenerateListExceptionPresidentListNull() throws IllegalAccessException {
		baseGenerateListExceptionPresidentListException("data-designacao-president-list-null.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionPresidentListEmpty() throws IllegalAccessException {
		baseGenerateListExceptionPresidentListException("data-designacao-president-list-empty.json", List.of());
	}
	
	private void baseGenerateListExceptionPresidentListException(String fileName, List<String> list) throws IllegalAccessException {
		var presidentDto = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withPresident(presidentDto).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Presidente: ".concat(MessageConfig.LIST_REQUIRED));
	}
	
	// President List Element
	@Test
	void shouldGenerateListExceptionPresidentListElementEmpty() throws IllegalAccessException {
		baseGenerateListExceptionPresidentListElementException("data-designacao-president-list-element-empty.json", List.of(""));
	}
	
	@Test
	void shouldGenerateListExceptionPresidentListElementBlank() throws IllegalAccessException {
		baseGenerateListExceptionPresidentListElementException("data-designacao-president-list-element-blank.json", List.of(" "));
	}

	private void baseGenerateListExceptionPresidentListElementException(String fileName, List<String> list) throws IllegalAccessException {
		var presidentDto = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withPresident(presidentDto).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Presidente: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
	}
	// *************************** PRESIDENT - FIM *************************** \\



	// *************************** AUDIOVIDEO - INICIO *************************** \\	
	// AudioVideo Entity Null
	@Test
	void shouldGenerateListExceptionAudioVideoNull() throws IllegalAccessException {
		baseEntityRequiredNull("audio-video", defaultBuilder.withAudioVideo(null), MessageConfig.AUDIOVIDEO_REQUIRED);
	}
	
	// AudioVideo Last
	@Test
	void shouldGenerateListExceptionAudioVideoLastNull() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoLastException("data-designacao-audio-video-last-null.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionAudioVideoLastEmpty() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoLastException("data-designacao-audio-video-last-empty.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionAudioVideoLastBlank() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoLastException("data-designacao-audio-video-last-blank.json", null);
	}
	
	private void baseGenerateListExceptionAudioVideoLastException(String fileName, String last) throws IllegalAccessException {
		var audioVideo = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withLast(last).build();
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withAudioVideo(audioVideo).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Aúdio e Vídeo: ".concat(MessageConfig.LAST_REQUIRED));
	}
	
	// AudioVideo List
	@Test
	void shouldGenerateListExceptionAudioVideoListNull() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoListException("data-designacao-audio-video-list-null.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionAudioVideoListEmpty() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoListException("data-designacao-audio-video-list-empty.json", List.of());
	}
	
	private void baseGenerateListExceptionAudioVideoListException(String fileName, List<String> list) throws IllegalAccessException {
		var audioVideo = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withAudioVideo(audioVideo).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Aúdio e Vídeo: ".concat(MessageConfig.LIST_REQUIRED));
	}
	
	// AudioVideo List Element
	@Test
	void shouldGenerateListExceptionAudioVideoListElementEmpty() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoListElementException("data-designacao-audio-video-list-element-empty.json", List.of(""));
	}
	
	@Test
	void shouldGenerateListExceptionAudioVideoListElementBlank() throws IllegalAccessException {
		baseGenerateListExceptionAudioVideoListElementException("data-designacao-audio-video-list-element-blank.json", List.of(" "));
	}

	private void baseGenerateListExceptionAudioVideoListElementException(String fileName, List<String> list) throws IllegalAccessException {
		var audioVideo = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withAudioVideo(audioVideo).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Aúdio e Vídeo: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
	}
	// *************************** AUDIOVIDEO - FIM *************************** \\



	// *************************** READER - INICIO *************************** \\	
	// Reader Master Entity Null
	@Test
	void shouldGenerateListExceptionReaderNull() throws IllegalAccessException {
		baseEntityRequiredNull("reader", defaultBuilder.withReader(null), MessageConfig.READER_REQUIRED);
	}
	
	// Reader Watchtower Entity Null
	@Test
	void shouldGenerateListExceptionReaderWatchtowerNull() throws IllegalAccessException {
		var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().withWatchtower(null).build();
		var msgError = "Leitor A Sentinela: ".concat(MessageConfig.READER_WATCHTOWER_REQUIRED);
		baseEntityRequiredNull("reader-watchtower", defaultBuilder.withReader(readerDto), msgError);
	}
	
	// Reader Watchtower Last
	@Test
	void shouldGenerateListExceptionReaderWatchtowerLastNull() throws IllegalAccessException {
		baseGenerateListExceptionReaderWatchtowerLastException("data-designacao-reader-watchtower-last-null.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionReaderWatchtowerLastEmpty() throws IllegalAccessException {
		baseGenerateListExceptionReaderWatchtowerLastException("data-designacao-reader-watchtower-last-empty.json", null);
	}
	
	@Test
	void shouldGenerateListExceptionReaderWatchtowerLastBlank() throws IllegalAccessException {
		baseGenerateListExceptionReaderWatchtowerLastException("data-designacao-reader-watchtower-last-blank.json", null);
	}
	
	private void baseGenerateListExceptionReaderWatchtowerLastException(String fileName, String last) throws IllegalAccessException {
		var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().build();
		readerDto.setWatchtower(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withLast(last).build());
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().withReader(readerDto).build();
		writeFileInputDesignacaoFromDto(fileName, dto);
		baseGenerateListException(fileName, "Leitor A Sentinela: ".concat(MessageConfig.LAST_REQUIRED));
	}
	
	// Reader BibleStudy Entity Null
	@Test
	void shouldGenerateListExceptionReaderBibleStudyNull() throws IllegalAccessException {
		var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().withBibleStudy(null).build();
		var msgError = "Leitor Estudo Bíblico: ".concat(MessageConfig.READER_BIBLESTUDY_REQUIRED);
		baseEntityRequiredNull("reader-bible-study", defaultBuilder.withReader(readerDto), msgError);
	}
	// *************************** READER - FIM *************************** \\
	
	
	
	// *************************** UTILS *************************** \\
	private void baseGenerateListException(String file, String message) throws IllegalAccessException {
		var ex = baseGenerateListException(file);
		String baseMessage = getMessageComplete(message);
		assertEquals(ex.getMessage(), baseMessage);
	}

	private String getMessageComplete(String message) {
		return String.format("Erro ao gerar lista '%s': %s", MODE_EXECUTION, message);
	}

	private ListBuilderException baseGenerateListException(String file) throws IllegalAccessException {
		setInputFileName(file);
		return assertThrows(ListBuilderException.class, () -> service.generateList());
	}

	private void setInputFileName(String file) throws IllegalAccessException {
		FieldUtils.writeField(properties, "inputFileNameDesignacao", file, true);
	}
	
	private void baseEntityRequiredNull(String entity, FileInputDataDesignacaoDtoBuilder builder, String msgError) throws IllegalAccessException {
		var fileName = String.format("data-designacao-%s-null.json", entity);
		writeFileInputDesignacaoFromDto(fileName, builder.build());
		baseGenerateListException(fileName, msgError);
	}

	@Test
	void shouldGenerateListExceptionListDatesEmpty() throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		when(dateService.generateListDatesDesignacao(any(DateServiceInputDTO.class))).thenReturn(List.of());
		var fileName = "data-designacao-ok.json";
		setInputFileName(fileName);
		writeFileInputDesignacaoFromDto(fileName, dto);
		var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
		assertEquals(getMessageComplete(MessageConfig.LIST_DATE_EMPTY), ex.getMessage());
	}
}
