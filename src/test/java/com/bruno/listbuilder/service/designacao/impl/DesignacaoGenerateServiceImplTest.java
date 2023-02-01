package com.bruno.listbuilder.service.designacao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
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
import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.BaseGenerateServiceTest;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.service.GroupService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.designacao.DesignacaoCounterService;
import com.bruno.listbuilder.service.designacao.DesignacaoWriterService;

class DesignacaoGenerateServiceImplTest
		extends BaseGenerateServiceTest<FileInputDataDesignacaoDTO, FileInputDataDesignacaoDtoBuilder> {

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

	private DesignacaoGenerateServiceImplTest() throws ListBuilderException {
		super(ListTypeEnum.DESIGNACAO, FileInputDataDesignacaoDtoBuilder.create().withRandomData());
	}

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		FieldUtils.writeField(properties, "inputDir", testUtils.getResourceDirectory(), true);
		service = new DesignacaoGenerateServiceImpl(properties, dateService, groupService, writerService,
				counterService, notificationService);
	}

	@Test
	void shouldModoExecutionNotNull() {
		assertNotNull(service.getListType());
	}

	@Test
	void shouldGetExecutionMode() {
		assertEquals(ListTypeEnum.DESIGNACAO, service.getListType());
	}

	@Test
	void shouldGenerateListFileNotFoundException() throws IllegalAccessException {
		validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
	}

	@Test
	void shouldGenerateListFileSyntaxException() throws IllegalAccessException {
		testUtils.writeFileInputSyntaxError();
		validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
	}

	// *************************** LASTDATE - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionLastDateNull() throws IllegalAccessException {
		validateGenerateListExceptionLastDate(null);
	}

	@Test
	void shouldGenerateListExceptionLastDateEmpty() throws IllegalAccessException {
		validateGenerateListExceptionLastDate("");
	}

	@Test
	void shouldGenerateListExceptionLastDateBlank() throws IllegalAccessException {
		validateGenerateListExceptionLastDate(" ");
	}

	private void validateGenerateListExceptionLastDate(String lastDate) throws IllegalAccessException {
		var dto = builder.withLastDate(lastDate).build();
		writeFileInputFromDto(dto);
		validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
		assertFalse(dto.toString().isBlank());
	}

	@Test
	void shouldGenerateListExceptionLastDateInvalid() throws IllegalAccessException {
		var dto = builder.withLastDate("01-20-2021").build();
		writeFileInputFromDto(dto);
		var expectedMessageError = String.format("Última Data da Lista Anterior inválida: '%s' não é uma data válida",
				dto.getLastDate());
		validateListBuilderException(expectedMessageError);
	}
	// *************************** LASTDATE - FIM *************************** \\

	// *************************** MIDWEEK - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionMidweekNull() throws IllegalAccessException {
		validateGenerateListExceptionMidweek(null);
	}

	@Test
	void shouldGenerateListExceptionMidweekEmpty() throws IllegalAccessException {
		validateGenerateListExceptionMidweek("");
	}

	@Test
	void shouldGenerateListExceptionMidweekBlank() throws IllegalAccessException {
		validateGenerateListExceptionMidweek(" ");
	}

	private void validateGenerateListExceptionMidweek(String dayMidweek) throws IllegalAccessException {
		writeFileInputFromDto(builder.withMeetingDayMidweek(dayMidweek).build());
		validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionMidweekInvalid() throws IllegalAccessException {
		var dto = builder.withMeetingDayMidweek("ABC").build();
		writeFileInputFromDto(dto);
		var expectedMessageError = String.format(
				"Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayMidweek());
		validateListBuilderException(expectedMessageError);
	}
	// *************************** MIDWEEK - FIM *************************** \\

	// *************************** WEEKEND - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionWeekendNull() throws IllegalAccessException {
		validateGenerateListExceptionWeekend(null);
	}

	@Test
	void shouldGenerateListExceptionWeekendEmpty() throws IllegalAccessException {
		validateGenerateListExceptionWeekend("");
	}

	@Test
	void shouldGenerateListExceptionWeekendBlank() throws IllegalAccessException {
		validateGenerateListExceptionWeekend(" ");
	}

	private void validateGenerateListExceptionWeekend(String dayWeekend) throws IllegalAccessException {
		writeFileInputFromDto(builder.withMeetingDayWeekend(dayWeekend).build());
		validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
	}

	@Test
	void shouldGenerateListExceptionWeekendInvalid() throws IllegalAccessException {
		var dto = builder.withMeetingDayWeekend("ABC").build();
		writeFileInputFromDto(dto);
		var expectedMessageError = String.format(
				"Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
				dto.getMeetingDayWeekend());
		validateListBuilderException(expectedMessageError);
	}
	// *************************** WEEKEND - FIM *************************** \\

	// *************************** INDICATOR - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionIndicatorNull() throws IllegalAccessException {
		validateGenerateListExceptionIndicator(null, MessageConfig.INDICATOR_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionIndicatorEmpty() throws IllegalAccessException {
		validateGenerateListExceptionIndicator(List.of(), MessageConfig.INDICATOR_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionIndicatorElementEmpty() throws IllegalAccessException {
		validateGenerateListExceptionIndicator(List.of(""), MessageConfig.INDICATOR_ELEMENT_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionIndicatorElementBlank() throws IllegalAccessException {
		validateGenerateListExceptionIndicator(List.of(" "), MessageConfig.INDICATOR_ELEMENT_REQUIRED);
	}

	private void validateGenerateListExceptionIndicator(List<String> indicator, String expectedMessageError)
			throws IllegalAccessException {
		var dto = builder.withIndicator(indicator).build();
		writeFileInputFromDto(dto);
		validateListBuilderException(expectedMessageError);
		assertFalse(dto.toString().isBlank());
	}
	// *************************** INDICATOR - FIM *************************** \\

	// *************************** MICROPHONE - INICIO ***************************
	// \\
	@Test
	void shouldGenerateListExceptionMicrophoneNull() throws IllegalAccessException {
		validateGenerateListExceptionMicrophone(null, MessageConfig.MICROPHONE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionMicrophoneEmpty() throws IllegalAccessException {
		validateGenerateListExceptionMicrophone(List.of(), MessageConfig.MICROPHONE_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionMicrophoneElementEmpty() throws IllegalAccessException {
		validateGenerateListExceptionMicrophone(List.of(""), MessageConfig.MICROPHONE_ELEMENT_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionMicrophoneElementBlank() throws IllegalAccessException {
		validateGenerateListExceptionMicrophone(List.of(" "), MessageConfig.MICROPHONE_ELEMENT_REQUIRED);
	}

	private void validateGenerateListExceptionMicrophone(List<String> microphone, String expectedMessageError)
			throws IllegalAccessException {
		var dto = builder.withMicrophone(microphone).build();
		writeFileInputFromDto(dto);
		validateListBuilderException(expectedMessageError);
		assertFalse(dto.toString().isBlank());
	}
	// *************************** MICROPHONE - FIM *************************** \\

	// *************************** PRESIDENT - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionPresidentNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withPresident(null).build());
		validateListBuilderException(MessageConfig.PRESIDENT_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionPresidentLastNull() throws IllegalAccessException {
		validateGenerateListPresidentLastException(null);
	}

	@Test
	void shouldGenerateListExceptionPresidentLastEmpty() throws IllegalAccessException {
		validateGenerateListPresidentLastException("");
	}

	@Test
	void shouldGenerateListExceptionPresidentLastBlank() throws IllegalAccessException {
		validateGenerateListPresidentLastException(" ");
	}

	private void validateGenerateListPresidentLastException(String last) throws IllegalAccessException {
		var presidentDto = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withLast(last).build();
		writeFileInputFromDto(builder.withPresident(presidentDto).build());
		validateListBuilderException("Presidente: ".concat(MessageConfig.LAST_REQUIRED));
	}

	@Test
	void shouldGenerateListExceptionPresidentListNull() throws IllegalAccessException {
		validateGenerateListPresidentListException(null);
	}

	@Test
	void shouldGenerateListExceptionPresidentListEmpty() throws IllegalAccessException {
		validateGenerateListPresidentListException(List.of());
	}

	private void validateGenerateListPresidentListException(List<String> list) throws IllegalAccessException {
		var presidentDto = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		writeFileInputFromDto(builder.withPresident(presidentDto).build());
		validateListBuilderException("Presidente: ".concat(MessageConfig.LIST_REQUIRED));
	}

	@Test
	void shouldGenerateListExceptionPresidentListElementEmpty() throws IllegalAccessException {
		baseGenerateListExceptionPresidentListElementException("data-designacao-president-list-element-empty.json",
				List.of(""));
	}

	@Test
	void shouldGenerateListExceptionPresidentListElementBlank() throws IllegalAccessException {
		baseGenerateListExceptionPresidentListElementException("data-designacao-president-list-element-blank.json",
				List.of(" "));
	}

	private void baseGenerateListExceptionPresidentListElementException(String fileName, List<String> list)
			throws IllegalAccessException {
		var presidentDto = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		writeFileInputFromDto(builder.withPresident(presidentDto).build());
		validateListBuilderException("Presidente: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
	}
	// *************************** PRESIDENT - FIM *************************** \\

	// *************************** AUDIOVIDEO - INICIO ***************************
	// \\
	@Test
	void shouldGenerateListExceptionAudioVideoNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withAudioVideo(null).build());
		validateListBuilderException(MessageConfig.AUDIOVIDEO_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionAudioVideoLastNull() throws IllegalAccessException {
		validateGenerateListAudioVideoLastException(null);
	}

	@Test
	void shouldGenerateListExceptionAudioVideoLastEmpty() throws IllegalAccessException {
		validateGenerateListAudioVideoLastException("");
	}

	@Test
	void shouldGenerateListExceptionAudioVideoLastBlank() throws IllegalAccessException {
		validateGenerateListAudioVideoLastException(" ");
	}

	private void validateGenerateListAudioVideoLastException(String last) throws IllegalAccessException {
		var audioVideo = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withLast(last).build();
		writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
		validateListBuilderException("Aúdio e Vídeo: ".concat(MessageConfig.LAST_REQUIRED));
	}

	@Test
	void shouldGenerateListExceptionAudioVideoListNull() throws IllegalAccessException {
		validateGenerateListAudioVideoListException(null);
	}

	@Test
	void shouldGenerateListExceptionAudioVideoListEmpty() throws IllegalAccessException {
		validateGenerateListAudioVideoListException(List.of());
	}

	private void validateGenerateListAudioVideoListException(List<String> list) throws IllegalAccessException {
		var audioVideo = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
		validateListBuilderException("Aúdio e Vídeo: ".concat(MessageConfig.LIST_REQUIRED));
	}

	@Test
	void shouldGenerateListExceptionAudioVideoListElementEmpty() throws IllegalAccessException {
		validateGenerateListAudioVideoListElementException(List.of(""));
	}

	@Test
	void shouldGenerateListExceptionAudioVideoListElementBlank() throws IllegalAccessException {
		validateGenerateListAudioVideoListElementException(List.of(" "));
	}

	private void validateGenerateListAudioVideoListElementException(List<String> list) throws IllegalAccessException {
		var audioVideo = FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withList(list).build();
		writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
		validateListBuilderException("Aúdio e Vídeo: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
	}
	// *************************** AUDIOVIDEO - FIM *************************** \\

	// *************************** READER - INICIO *************************** \\
	@Test
	void shouldGenerateListExceptionReaderNull() throws IllegalAccessException {
		writeFileInputFromDto(builder.withReader(null).build());
		validateListBuilderException(MessageConfig.READER_REQUIRED);
	}

	@Test
	void shouldGenerateListExceptionReaderWatchtowerNull() throws IllegalAccessException {
		var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().withWatchtower(null).build();
		writeFileInputFromDto(builder.withReader(readerDto).build());
		var expectedMessageError = "Leitor A Sentinela: ".concat(MessageConfig.READER_WATCHTOWER_REQUIRED);
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListExceptionReaderBibleStudyNull() throws IllegalAccessException {
		var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().withBibleStudy(null).build();
		writeFileInputFromDto(builder.withReader(readerDto).build());
		var expectedMessageError = "Leitor Estudo Bíblico: ".concat(MessageConfig.READER_BIBLESTUDY_REQUIRED);
		validateListBuilderException(expectedMessageError);
	}

	@Test
	void shouldGenerateListExceptionReaderWatchtowerLastNull() throws IllegalAccessException {
		validateGenerateListReaderWatchtowerLastException(null);
	}

	@Test
	void shouldGenerateListExceptionReaderWatchtowerLastEmpty() throws IllegalAccessException {
		validateGenerateListReaderWatchtowerLastException("");
	}

	@Test
	void shouldGenerateListExceptionReaderWatchtowerLastBlank() throws IllegalAccessException {
		validateGenerateListReaderWatchtowerLastException(" ");
	}

	private void validateGenerateListReaderWatchtowerLastException(String last) throws IllegalAccessException {
		var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().build();
		readerDto.setWatchtower(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().withLast(last).build());
		writeFileInputFromDto(builder.withReader(readerDto).build());
		validateListBuilderException("Leitor A Sentinela: ".concat(MessageConfig.LAST_REQUIRED));
	}
	// *************************** READER - FIM *************************** \\

	@Test
	void shouldGenerateListDatesEmptyException() throws IllegalAccessException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		when(dateService.generateListDatesDesignacao(any(DateServiceInputDTO.class))).thenReturn(List.of());
		writeFileInputFromDto(dto);
		validateListBuilderException(MessageConfig.LIST_DATE_EMPTY);
	}

	private void validateListBuilderException(String expectedMessageError) throws IllegalAccessException {
		testUtils.validateException(ListBuilderException.class, () -> service.generateList(), expectedMessageError);
	}

}
