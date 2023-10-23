package br.com.bvilela.listbuilder.service.audience;

import br.com.bvilela.listbuilder.annotation.NullAndBlankSource;
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
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    /*
    @BeforeEach
    public void setup() {
        var propertiesUtils = new PropertiesTestUtils(appProperties);
        propertiesUtils.setInputDir(testUtils.getResourceDirectory());
        propertiesUtils.setLayoutAudience(AudienceWriterLayoutEnum.FULL);
        service = new AudienceGenerateServiceImpl(
                appProperties, dateService, notificationService, writerService);
    }
    */

    @Test
    void givenGetListType_whenListTypeNotNull_ThenReturn() {
        assertNotNull(service.getListType());
    }

    @Test
    void givenBaseTestService_whenListType_ThenReturnSame() {
        assertEquals(testUtils.getListType(), service.getListType());
    }

    @Test
    void shouldCorrectFileInputName() {
        assertEquals("dados-assistencia.json", service.getListType().getInputFileName());
    }

    @Test
    void givenRun_whenFileNotFound_ThenException() {
        callGenerateListAndVerifyException("Erro ao ler arquivo - Arquivo não encontrado");
    }

    @Test
    void givenRun_whenInputFileSyntaxError_ThenException() {
        testUtils.writeFileInputSyntaxError();
        callGenerateListAndVerifyException("Erro ao ler arquivo - Arquivo não é um JSON válido");
    }

    @ParameterizedTest
    @NullAndBlankSource
    void givenInputFile_whenLasDateNotFilled_ThenException(String lastDate) {
        writeFileInputFromDto(builder.withSuccess().withLastDate(lastDate).build());
        callGenerateListAndVerifyException(MessageConfig.LAST_DATE_REQUIRED);
    }

    @Test
    void shouldGenerateListExceptionLastDateInvalid() {
        var dto = builder.withLastDateInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Última Data da Lista Anterior inválida: '%s' não é uma data válida",
                        dto.getLastDate());
        callGenerateListAndVerifyException(expectedMessageError);
    }

    @ParameterizedTest
    @NullAndBlankSource
    void shouldGenerateListExceptionMidweek(String meetingDayMidweek) {
        writeFileInputFromDto(
                builder.withSuccess().withMeetingDayMidweek(meetingDayMidweek).build());
        callGenerateListAndVerifyException(MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionMidweekInvalid() {
        var dto = builder.withMidweekInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Meio de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMeetingDayMidweek());
        callGenerateListAndVerifyException(expectedMessageError);
    }

    @ParameterizedTest
    @NullAndBlankSource
    void shouldGenerateListExceptionWeekend(String meetingDayWeekend) {
        writeFileInputFromDto(
                builder.withSuccess().withMeetingDayWeekend(meetingDayWeekend).build());
        callGenerateListAndVerifyException(MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND);
    }

    @Test
    void shouldGenerateListExceptionWeekendInvalid() {
        var dto = builder.withWeekendInvalid().build();
        writeFileInputFromDto(dto);
        var expectedMessageError =
                String.format(
                        "Dia da Reunião de Fim de Semana - Valor '%s' não é um Dia da Semana válido!",
                        dto.getMeetingDayWeekend());
        callGenerateListAndVerifyException(expectedMessageError);
    }

    @Test
    void shouldGenerateListSuccess() {
        var expectedList =
                List.of(
                        cld(4, 2),
                        cld(4, 4),
                        cld(4, 8),
                        cld(4, 12),
                        cld(4, 16),
                        cld(4, 19),
                        cld(4, 8),
                        cld(4, 23),
                        cld(4, 26),
                        cld(4, 19),
                        cld(4, 30));
        writeFileInputFromDto(builder.withSuccess().build());
        when(appProperties.getInputDir()).thenReturn(testUtils.getResourceDirectory());
        when(dateService.generateAudienceListDates(
                any(AudienceInputDTO.class), any(AudienceWriterLayoutEnum.class)))
                .thenReturn(expectedList);
        assertDoesNotThrow(() -> service.generateList());
    }

    /** Create Local Date */
    private LocalDate cld(int month, int day) {
        return LocalDate.of(2022, month, day);
    }

    private void callGenerateListAndVerifyException(String expectedMessageError) {
        when(appProperties.getInputDir()).thenReturn(testUtils.getResourceDirectory());

        var ex = assertThrows(ListBuilderException.class, () -> service.generateList());
        assertEquals(expectedMessageError, ex.getMessage());

        verify(appProperties).getInputDir();
    }
}
