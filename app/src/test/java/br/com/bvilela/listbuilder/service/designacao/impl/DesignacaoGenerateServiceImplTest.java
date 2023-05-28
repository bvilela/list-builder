package br.com.bvilela.listbuilder.service.designacao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.bvilela.listbuilder.builder.designacao.FileInputDataDesignacaoDtoBuilder;
import br.com.bvilela.listbuilder.builder.designacao.FileInputDataDesignacaoListDtoBuilder;
import br.com.bvilela.listbuilder.builder.designacao.FileInputDataDesignacaoReaderDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.GroupService;
import br.com.bvilela.listbuilder.service.NotificationService;
import br.com.bvilela.listbuilder.service.designacao.DesignacaoCounterService;
import br.com.bvilela.listbuilder.service.designacao.DesignacaoWriterService;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class DesignacaoGenerateServiceImplTest
        extends BaseGenerateServiceTest<
                FileInputDataDesignacaoDTO, FileInputDataDesignacaoDtoBuilder> {

    @InjectMocks private DesignacaoGenerateServiceImpl service;

    @InjectMocks private AppProperties properties;

    @Mock private DateService dateService;

    @Mock private GroupService groupService;

    @Mock private DesignacaoWriterService writerService;

    @Mock private NotificationService notificationService;

    @Mock private DesignacaoCounterService counterService;

    public DesignacaoGenerateServiceImplTest() {
        super(ListTypeEnum.DESIGNACAO, FileInputDataDesignacaoDtoBuilder.create().withRandomData());
    }

    @BeforeEach
    @SneakyThrows
    public void setup() {
        MockitoAnnotations.openMocks(this);
        FieldUtils.writeField(properties, "inputDir", this.testUtils.getResourceDirectory(), true);
        service =
                new DesignacaoGenerateServiceImpl(
                        properties,
                        dateService,
                        groupService,
                        writerService,
                        counterService,
                        notificationService);
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
        assertEquals("dados-designacao.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListFileNotFoundException() {
        validateListBuilderException(MessageConfig.FILE_NOT_FOUND);
    }

    @Test
    void shouldGenerateListFileSyntaxException() {
        this.testUtils.writeFileInputSyntaxError();
        validateListBuilderException(MessageConfig.FILE_SYNTAX_ERROR);
    }

    // *************************** LASTDATE - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionLastDateNull() {
        validateGenerateListExceptionLastDate(null);
    }

    @Test
    void shouldGenerateListExceptionLastDateEmpty() {
        validateGenerateListExceptionLastDate("");
    }

    @Test
    void shouldGenerateListExceptionLastDateBlank() {
        validateGenerateListExceptionLastDate(" ");
    }

    private void validateGenerateListExceptionLastDate(String lastDate) {
        var dto = builder.withLastDate(lastDate).build();
        writeFileInputFromDto(dto);
        validateListBuilderException(MessageConfig.LAST_DATE_REQUIRED);
        Assertions.assertFalse(dto.toString().isBlank());
    }

    @Test
    void shouldGenerateListExceptionLastDateInvalid() {
        var dto = builder.withLastDate("01-20-2021").build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Última Data da Lista Anterior inválida: '%s' não é uma data válida",
                        dto.getLastDate());
        validateListBuilderException(expectedMessageError);
    }
    // *************************** LASTDATE - FIM *************************** \\

    // *************************** MIDWEEK - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionMidweekNull() {
        validateGenerateListExceptionMidweek(null);
    }

    @Test
    void shouldGenerateListExceptionMidweekEmpty() {
        validateGenerateListExceptionMidweek("");
    }

    @Test
    void shouldGenerateListExceptionMidweekBlank() {
        validateGenerateListExceptionMidweek(" ");
    }

    private void validateGenerateListExceptionMidweek(String dayMidweek) {
        writeFileInputFromDto(builder.withMeetingDayMidweek(dayMidweek).build());
        validateListBuilderException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekInvalid() {
        var dto = builder.withMeetingDayMidweek("ABC").build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMeetingDayMidweek());
        validateListBuilderException(expectedMessageError);
    }
    // *************************** MIDWEEK - FIM *************************** \\

    // *************************** WEEKEND - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionWeekendNull() {
        validateGenerateListExceptionWeekend(null);
    }

    @Test
    void shouldGenerateListExceptionWeekendEmpty() {
        validateGenerateListExceptionWeekend("");
    }

    @Test
    void shouldGenerateListExceptionWeekendBlank() {
        validateGenerateListExceptionWeekend(" ");
    }

    private void validateGenerateListExceptionWeekend(String dayWeekend) {
        writeFileInputFromDto(builder.withMeetingDayWeekend(dayWeekend).build());
        validateListBuilderException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendInvalid() {
        var dto = builder.withMeetingDayWeekend("ABC").build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMeetingDayWeekend());
        validateListBuilderException(expectedMessageError);
    }
    // *************************** WEEKEND - FIM *************************** \\

    // *************************** INDICATOR - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionIndicatorNull() {
        validateGenerateListExceptionIndicator(null, MessageConfig.INDICATOR_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionIndicatorEmpty() {
        validateGenerateListExceptionIndicator(List.of(), MessageConfig.INDICATOR_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionIndicatorElementEmpty() {
        validateGenerateListExceptionIndicator(
                List.of(""), MessageConfig.INDICATOR_ELEMENT_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionIndicatorElementBlank() {
        validateGenerateListExceptionIndicator(
                List.of(" "), MessageConfig.INDICATOR_ELEMENT_REQUIRED);
    }

    private void validateGenerateListExceptionIndicator(
            List<String> indicator, String expectedMessageError) {
        var dto = builder.withIndicator(indicator).build();
        writeFileInputFromDto(dto);
        validateListBuilderException(expectedMessageError);
        Assertions.assertFalse(dto.toString().isBlank());
    }
    // *************************** INDICATOR - FIM *************************** \\

    // *************************** MICROPHONE - INICIO ***************************
    // \\
    @Test
    void shouldGenerateListExceptionMicrophoneNull() {
        validateGenerateListExceptionMicrophone(null, MessageConfig.MICROPHONE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionMicrophoneEmpty() {
        validateGenerateListExceptionMicrophone(List.of(), MessageConfig.MICROPHONE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionMicrophoneElementEmpty() {
        validateGenerateListExceptionMicrophone(
                List.of(""), MessageConfig.MICROPHONE_ELEMENT_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionMicrophoneElementBlank() {
        validateGenerateListExceptionMicrophone(
                List.of(" "), MessageConfig.MICROPHONE_ELEMENT_REQUIRED);
    }

    private void validateGenerateListExceptionMicrophone(
            List<String> microphone, String expectedMessageError) {
        var dto = builder.withMicrophone(microphone).build();
        writeFileInputFromDto(dto);
        validateListBuilderException(expectedMessageError);
        Assertions.assertFalse(dto.toString().isBlank());
    }
    // *************************** MICROPHONE - FIM *************************** \\

    // *************************** PRESIDENT - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionPresidentNull() {
        writeFileInputFromDto(builder.withPresident(null).build());
        validateListBuilderException(MessageConfig.PRESIDENT_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionPresidentLastNull() {
        validateGenerateListPresidentLastException(null);
    }

    @Test
    void shouldGenerateListExceptionPresidentLastEmpty() {
        validateGenerateListPresidentLastException("");
    }

    @Test
    void shouldGenerateListExceptionPresidentLastBlank() {
        validateGenerateListPresidentLastException(" ");
    }

    private void validateGenerateListPresidentLastException(String last) {
        var presidentDto =
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withLast(last)
                        .build();
        writeFileInputFromDto(builder.withPresident(presidentDto).build());
        validateListBuilderException("Presidente: ".concat(MessageConfig.LAST_REQUIRED));
    }

    @Test
    void shouldGenerateListExceptionPresidentListNull() {
        validateGenerateListPresidentListException(null);
    }

    @Test
    void shouldGenerateListExceptionPresidentListEmpty() {
        validateGenerateListPresidentListException(List.of());
    }

    private void validateGenerateListPresidentListException(List<String> list) {
        var presidentDto =
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withPresident(presidentDto).build());
        validateListBuilderException("Presidente: ".concat(MessageConfig.LIST_REQUIRED));
    }

    @Test
    void shouldGenerateListExceptionPresidentListElementEmpty() {
        baseGenerateListExceptionPresidentListElementException(List.of(""));
    }

    @Test
    void shouldGenerateListExceptionPresidentListElementBlank() {
        baseGenerateListExceptionPresidentListElementException(List.of(" "));
    }

    private void baseGenerateListExceptionPresidentListElementException(List<String> list) {
        var presidentDto =
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withPresident(presidentDto).build());
        validateListBuilderException("Presidente: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
    }
    // *************************** PRESIDENT - FIM *************************** \\

    // *************************** AUDIOVIDEO - INICIO ***************************
    // \\
    @Test
    void shouldGenerateListExceptionAudioVideoNull() {
        writeFileInputFromDto(builder.withAudioVideo(null).build());
        validateListBuilderException(MessageConfig.AUDIOVIDEO_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionAudioVideoLastNull() {
        validateGenerateListAudioVideoLastException(null);
    }

    @Test
    void shouldGenerateListExceptionAudioVideoLastEmpty() {
        validateGenerateListAudioVideoLastException("");
    }

    @Test
    void shouldGenerateListExceptionAudioVideoLastBlank() {
        validateGenerateListAudioVideoLastException(" ");
    }

    private void validateGenerateListAudioVideoLastException(String last) {
        var audioVideo =
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withLast(last)
                        .build();
        writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
        validateListBuilderException("Aúdio e Vídeo: ".concat(MessageConfig.LAST_REQUIRED));
    }

    @Test
    void shouldGenerateListExceptionAudioVideoListNull() {
        validateGenerateListAudioVideoListException(null);
    }

    @Test
    void shouldGenerateListExceptionAudioVideoListEmpty() {
        validateGenerateListAudioVideoListException(List.of());
    }

    private void validateGenerateListAudioVideoListException(List<String> list) {
        var audioVideo =
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
        validateListBuilderException("Aúdio e Vídeo: ".concat(MessageConfig.LIST_REQUIRED));
    }

    @Test
    void shouldGenerateListExceptionAudioVideoListElementEmpty() {
        validateGenerateListAudioVideoListElementException(List.of(""));
    }

    @Test
    void shouldGenerateListExceptionAudioVideoListElementBlank() {
        validateGenerateListAudioVideoListElementException(List.of(" "));
    }

    private void validateGenerateListAudioVideoListElementException(List<String> list) {
        var audioVideo =
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
        validateListBuilderException("Aúdio e Vídeo: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
    }
    // *************************** AUDIOVIDEO - FIM *************************** \\

    // *************************** READER - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionReaderNull() {
        writeFileInputFromDto(builder.withReader(null).build());
        validateListBuilderException(MessageConfig.READER_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionReaderWatchtowerNull() {
        var readerDto =
                FileInputDataDesignacaoReaderDtoBuilder.create()
                        .withRandomData()
                        .withWatchtower(null)
                        .build();
        writeFileInputFromDto(builder.withReader(readerDto).build());
        var expectedMessageError =
                "Leitor A Sentinela: ".concat(MessageConfig.READER_WATCHTOWER_REQUIRED);
        validateListBuilderException(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionReaderBibleStudyNull() {
        var readerDto =
                FileInputDataDesignacaoReaderDtoBuilder.create()
                        .withRandomData()
                        .withBibleStudy(null)
                        .build();
        writeFileInputFromDto(builder.withReader(readerDto).build());
        var expectedMessageError =
                "Leitor Estudo Bíblico: ".concat(MessageConfig.READER_BIBLESTUDY_REQUIRED);
        validateListBuilderException(expectedMessageError);
    }

    @Test
    @SneakyThrows
    void shouldGenerateListExceptionReaderWatchtowerLastNull() {
        validateGenerateListReaderWatchtowerLastException(null);
    }

    @Test
    @SneakyThrows
    void shouldGenerateListExceptionReaderWatchtowerLastEmpty() {
        validateGenerateListReaderWatchtowerLastException("");
    }

    @Test
    @SneakyThrows
    void shouldGenerateListExceptionReaderWatchtowerLastBlank() {
        validateGenerateListReaderWatchtowerLastException(" ");
    }

    private void validateGenerateListReaderWatchtowerLastException(String last) {
        var readerDto = FileInputDataDesignacaoReaderDtoBuilder.create().withRandomData().build();
        readerDto.setWatchtower(
                FileInputDataDesignacaoListDtoBuilder.create()
                        .withRandomData()
                        .withLast(last)
                        .build());
        writeFileInputFromDto(builder.withReader(readerDto).build());
        validateListBuilderException("Leitor A Sentinela: ".concat(MessageConfig.LAST_REQUIRED));
    }
    // *************************** READER - FIM *************************** \\

    @Test
    void shouldGenerateListDatesEmptyException() {
        var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
        Mockito.when(
                        dateService.generateListDatesDesignacao(
                                ArgumentMatchers.any(DateServiceInputDTO.class)))
                .thenReturn(List.of());
        writeFileInputFromDto(dto);
        validateListBuilderException(MessageConfig.LIST_DATE_EMPTY);
    }

    private void validateListBuilderException(String expectedMessageError) {
        this.testUtils.validateException(() -> service.generateList(), expectedMessageError);
    }
}
