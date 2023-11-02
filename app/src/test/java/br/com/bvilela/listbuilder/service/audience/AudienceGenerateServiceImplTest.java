package br.com.bvilela.listbuilder.service.audience;

import br.com.bvilela.listbuilder.annotation.NullAndEmptyAndBlankSource;
import br.com.bvilela.listbuilder.builder.AudienceInputDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.audience.AudienceInputDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static br.com.bvilela.listbuilder.utils.RandomUtils.getMockedAudienceInputDTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudienceGenerateServiceImplTest
        extends BaseGenerateServiceTest<AudienceInputDTO, AudienceInputDtoBuilder> {

    @InjectMocks
    private AudienceGenerateServiceImpl service;
    @Mock
    private AppProperties appProperties;
    @Mock
    private DateService dateService;
    @Mock
    private AudienceWriterService writerService;
    @Mock
    private SendNotificationService notificationService;

    public AudienceGenerateServiceImplTest() {
        super(ListTypeEnum.ASSISTENCIA, AudienceInputDtoBuilder.create());
    }

    @Test
    void whenGetListType_thenReturnListTypeNotNull() {
        assertNotNull(service.getListType());
    }

    @Test
    void givenTestUtils_whenGetListType_thenReturnSameType() {
        assertEquals(testUtils.getListType(), service.getListType());
    }

    @Test
    void givenGetListType_whenGetInputFileName_thenReturnExpectedName() {
        assertEquals("dados-assistencia.json", service.getListType().getInputFileName());
    }

    @Test
    void givenRun_whenFileNotFound_thenException() {
        callGenerateListAndVerifyExceptionMessage("Erro ao ler arquivo - Arquivo não encontrado");
    }

    @Test
    void givenRun_whenInputFileSyntaxError_thenException() {
        testUtils.writeFileInputSyntaxError();
        callGenerateListAndVerifyExceptionMessage("Erro ao ler arquivo - Arquivo não é um JSON válido");
    }

    @ParameterizedTest
    @NullAndEmptyAndBlankSource
    void givenInputFile_whenLasDateNotFilled_thenException(String lastDate) {
        writeFileInputFromDto(builder.withSuccess().withLastDate(lastDate).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.LAST_DATE_REQUIRED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-13-2022", "40-01-2022", "29-02-2023"})
    void givenInputFile_whenLastDateInvalid_thenException(String lastDate) {
        var dto = getMockedAudienceInputDTO();
        dto.setLastDate(lastDate);
        writeFileInputFromDto(dto);
        callGenerateListAndVerifyExceptionMessage(
                "Última Data da Lista Anterior inválida: '%s' não é uma data válida",
                dto.getLastDate()
        );
    }

    @ParameterizedTest
    @NullAndEmptyAndBlankSource
    void givenInputFile_whenMeetingDayMidweekNotFilled_thenException(String midweekMeetingDay) {
        writeFileInputFromDto(
                builder.withSuccess().withMeetingDayMidweek(midweekMeetingDay).build());
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"tercaaa", "XPTO", "333"})
    void givenInputFile_whenMeetingDayMidweekInvalid_thenException(String midweekMeetingDay) {
        var dto = getMockedAudienceInputDTO();
        dto.setMidweekMeetingDay(midweekMeetingDay);
        writeFileInputFromDto(dto);
        callGenerateListAndVerifyExceptionMessage(
                "Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
                dto.getMidweekMeetingDay()
        );
    }

    @ParameterizedTest
    @NullAndEmptyAndBlankSource
    void givenInputFile_whenMeetingDayWeekendNotFilled_thenException(String meetingDayWeekend) {
        var dto = getMockedAudienceInputDTO();
        dto.setWeekendMeetingDay(meetingDayWeekend);
        writeFileInputFromDto(dto);
        callGenerateListAndVerifyExceptionMessage(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"doming", "XPTO", "333"})
    void givenInputFile_whenMeetingDayWeekendInvalid_thenException(String meetingDayWeekend) {
        var dto = getMockedAudienceInputDTO();
        dto.setWeekendMeetingDay(meetingDayWeekend);
        writeFileInputFromDto(dto);
        callGenerateListAndVerifyExceptionMessage(
                "Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
                dto.getWeekendMeetingDay()
        );
    }

    @Test
    void givenInputFile_whenGenerateList_thenSuccess() {
        writeFileInputFromDto(builder.withSuccess().build());
        when(appProperties.getInputDir()).thenReturn(testUtils.getResourceDirectory());
        when(appProperties.getLayoutAudience()).thenReturn(AudienceWriterLayoutEnum.FULL.name());
        when(dateService.generateAudienceListDates(
                any(AudienceInputDTO.class), any(AudienceWriterLayoutEnum.class)))
                .thenReturn(new EasyRandom().objects(LocalDate.class, 5).toList());

        assertDoesNotThrow(() -> service.generateList());

        verify(appProperties).getInputDir();
        verify(dateService).generateAudienceListDates(any(AudienceInputDTO.class), any(AudienceWriterLayoutEnum.class));
        verify(writerService).writerPDF(anyList(), any(AudienceWriterLayoutEnum.class));
        verify(notificationService).audience(anyList());
    }

    private void callGenerateListAndVerifyExceptionMessage(String expectedMessageError, Object...args) {
        callGenerateListAndVerifyExceptionMessage(String.format(expectedMessageError, args));
    }

    private void callGenerateListAndVerifyExceptionMessage(String expectedMessageError) {
        when(appProperties.getInputDir()).thenReturn(testUtils.getResourceDirectory());

        var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
        assertEquals(expectedMessageError, ex.getMessage());

        verify(appProperties).getInputDicr();
        verify(dateService, never()).generateAudienceListDates(any(AudienceInputDTO.class), any(AudienceWriterLayoutEnum.class));
        verify(writerService, never()).writerPDF(anyList(), any(AudienceWriterLayoutEnum.class));
        verify(notificationService, never()).audience(anyList());
    }
}
