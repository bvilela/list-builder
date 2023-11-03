package br.com.bvilela.listbuilder.service.designation;

import br.com.bvilela.listbuilder.builder.designation.DesignationInputDtoBuilder;
import br.com.bvilela.listbuilder.builder.designation.InputListDtoBuilder;
import br.com.bvilela.listbuilder.builder.designation.DesignationInputReaderDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.GroupService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DesignationGenerateServiceImplTest
        extends BaseGenerateServiceTest<DesignationInputDTO, DesignationInputDtoBuilder> {

    @InjectMocks private DesignationGenerateServiceImpl service;

    @InjectMocks private AppProperties appProperties;

    @Mock private DateService dateService;

    @Mock private GroupService groupService;

    @Mock private DesignationWriterService writerService;

    @Mock private SendNotificationService notificationService;

    @Mock private DesignationCounterService counterService;

    public DesignationGenerateServiceImplTest() {
        super(ListTypeEnum.DESIGNACAO, DesignationInputDtoBuilder.create().withRandomData());
    }

    @BeforeEach
    public void setup() {
        new PropertiesTestUtils(appProperties).setInputDir(testUtils.getResourceDirectory());
        service =
                new DesignationGenerateServiceImpl(
                        appProperties,
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
        assertEquals(testUtils.getListType(), service.getListType());
    }

    @Test
    void shouldCorrectFileInputName() {
        assertEquals("dados-designacao.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListFileNotFoundException() {
        callGenerateListAndVerifyExceptionMessage(MessageConfig.FILE_NOT_FOUND);
    }

    @Test
    void shouldGenerateListFileSyntaxException() {
        this.testUtils.writeFileInputSyntaxError();
        callGenerateListAndVerifyExceptionMessage(MessageConfig.FILE_SYNTAX_ERROR);
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
        callGenerateListAndVerifyExceptionMessage(MessageConfig.LAST_DATE_REQUIRED);
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
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
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
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekInvalid() {
        var dto = builder.withMeetingDayMidweek("ABC").build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMidweekMeetingDay());
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
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
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendInvalid() {
        var dto = builder.withMeetingDayWeekend("ABC").build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getWeekendMeetingDay());
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
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
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
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
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
        Assertions.assertFalse(dto.toString().isBlank());
    }
    // *************************** MICROPHONE - FIM *************************** \\

    // *************************** PRESIDENT - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionPresidentNull() {
        writeFileInputFromDto(builder.withPresident(null).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.PRESIDENT_REQUIRED);
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
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withLast(last)
                        .build();
        writeFileInputFromDto(builder.withPresident(presidentDto).build());
        callGenerateListAndVerifyExceptionMessage("Presidente: ".concat(MessageConfig.LAST_REQUIRED));
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
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withPresident(presidentDto).build());
        callGenerateListAndVerifyExceptionMessage("Presidente: ".concat(MessageConfig.LIST_REQUIRED));
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
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withPresident(presidentDto).build());
        callGenerateListAndVerifyExceptionMessage("Presidente: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
    }
    // *************************** PRESIDENT - FIM *************************** \\

    // *************************** AUDIOVIDEO - INICIO ***************************
    // \\
    @Test
    void shouldGenerateListExceptionAudioVideoNull() {
        writeFileInputFromDto(builder.withAudioVideo(null).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.AUDIOVIDEO_REQUIRED);
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
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withLast(last)
                        .build();
        writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
        callGenerateListAndVerifyExceptionMessage("Aúdio e Vídeo: ".concat(MessageConfig.LAST_REQUIRED));
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
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
        callGenerateListAndVerifyExceptionMessage("Aúdio e Vídeo: ".concat(MessageConfig.LIST_REQUIRED));
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
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withList(list)
                        .build();
        writeFileInputFromDto(builder.withAudioVideo(audioVideo).build());
        callGenerateListAndVerifyExceptionMessage("Aúdio e Vídeo: ".concat(MessageConfig.LIST_ELEMENT_REQUIRED));
    }
    // *************************** AUDIOVIDEO - FIM *************************** \\

    // *************************** READER - INICIO *************************** \\
    @Test
    void shouldGenerateListExceptionReaderNull() {
        writeFileInputFromDto(builder.withReader(null).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.READER_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionReaderWatchtowerNull() {
        var readerDto =
                DesignationInputReaderDtoBuilder.create()
                        .withRandomData()
                        .withWatchtower(null)
                        .build();
        writeFileInputFromDto(builder.withReader(readerDto).build());
        var expectedMessageError =
                "Leitor A Sentinela: ".concat(MessageConfig.READER_WATCHTOWER_REQUIRED);
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionReaderBibleStudyNull() {
        var readerDto =
                DesignationInputReaderDtoBuilder.create()
                        .withRandomData()
                        .withBibleStudy(null)
                        .build();
        writeFileInputFromDto(builder.withReader(readerDto).build());
        var expectedMessageError =
                "Leitor Estudo Bíblico: ".concat(MessageConfig.READER_BIBLESTUDY_REQUIRED);
        callGenerateListAndVerifyExceptionMessage(expectedMessageError);
    }

    @Test
    void shouldGenerateListExceptionReaderWatchtowerLastNull() {
        validateGenerateListReaderWatchtowerLastException(null);
    }

    @Test
    void shouldGenerateListExceptionReaderWatchtowerLastEmpty() {
        validateGenerateListReaderWatchtowerLastException("");
    }

    @Test
    void shouldGenerateListExceptionReaderWatchtowerLastBlank() {
        validateGenerateListReaderWatchtowerLastException(" ");
    }

    private void validateGenerateListReaderWatchtowerLastException(String last) {
        var readerDto = DesignationInputReaderDtoBuilder.create().withRandomData().build();
        readerDto.setWatchtower(
                InputListDtoBuilder.create()
                        .withRandomData()
                        .withLast(last)
                        .build());
        writeFileInputFromDto(builder.withReader(readerDto).build());
        callGenerateListAndVerifyExceptionMessage("Leitor A Sentinela: ".concat(MessageConfig.LAST_REQUIRED));
    }
    // *************************** READER - FIM *************************** \\

    private void callGenerateListAndVerifyExceptionMessage(String expectedMessageError) {
        this.testUtils.validateException(() -> service.generateList(), expectedMessageError);
    }
}
