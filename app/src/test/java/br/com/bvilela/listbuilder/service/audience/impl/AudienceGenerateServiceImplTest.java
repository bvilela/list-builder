package br.com.bvilela.listbuilder.service.audience.impl;

import br.com.bvilela.listbuilder.builder.FileInputDataAudienceDtoBuilder;
import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.BaseGenerateServiceTest;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.audience.AudienceWriterService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootApplication
class AudienceGenerateServiceImplTest
        extends BaseGenerateServiceTest<
        FileInputDataAudienceDTO, FileInputDataAudienceDtoBuilder> {

    @InjectMocks private AudienceGenerateServiceImpl service;

    @InjectMocks private AppProperties properties;

    @Mock private DateService dateService;

    @Mock private AudienceWriterServiceLayoutFullImpl writerServiceLayoutFull;

    @Mock private AudienceWriterServiceLayoutCompactImpl writerServiceLayoutCompact;

    @Mock private SendNotificationService notificationService;

    public AudienceGenerateServiceImplTest() {
        super(ListTypeEnum.ASSISTENCIA, FileInputDataAudienceDtoBuilder.create());
    }

    @BeforeEach
    @SneakyThrows
    public void setup() {
        MockitoAnnotations.openMocks(this);
        FieldUtils.writeField(properties, "inputDir", testUtils.getResourceDirectory(), true);
        FieldUtils.writeField(properties, "layoutAudience", AudienceWriterLayoutEnum.FULL.name(), true);
        Map<String, AudienceWriterService> writerServiceMap = new HashMap<>();
        writerServiceMap.put(AudienceWriterLayoutEnum.FULL.name(), writerServiceLayoutFull);
        writerServiceMap.put(AudienceWriterLayoutEnum.COMPACT.name(), writerServiceLayoutCompact);
        service =
                new AudienceGenerateServiceImpl(
                        properties, dateService, notificationService, writerServiceMap);
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
        assertEquals("dados-assistencia.json", service.getListType().getInputFileName());
    }

    @Test
    void shouldGenerateListInvalidFilePathException() {
        validateListBuilderException("Erro ao ler arquivo - Arquivo não encontrado");
    }

    @Test
    void shouldGenerateListFileSintaxeException() {
        testUtils.writeFileInputSyntaxError();
        validateListBuilderException("Erro ao ler arquivo - Arquivo não é um JSON válido");
    }

    @DisplayName("Generate List Exception - Last Date Required")
    @ParameterizedTest(name = "Last Date is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldGenerateListExceptionLastDate(String lastDate) {
        writeFileInputFromDto(builder.withSuccess().withLastDate(lastDate).build());
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

    @DisplayName("Generate List Exception - Midweek Required")
    @ParameterizedTest(name = "Midweek is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @SneakyThrows
    void shouldGenerateListExceptionMidweek(String meetingDayMidweek) {
        writeFileInputFromDto(
                builder.withSuccess().withMeetingDayMidweek(meetingDayMidweek).build());
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

    @DisplayName("Generate List Exception - Weekend Required")
    @ParameterizedTest(name = "Weekend is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @SneakyThrows
    void shouldGenerateListExceptionWeekend(String meetingDayWeekend) {
        writeFileInputFromDto(
                builder.withSuccess().withMeetingDayWeekend(meetingDayWeekend).build());
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
                        dateService.generateAudienceListDates(
                                ArgumentMatchers.any(DateServiceInputDTO.class), ArgumentMatchers.any(Integer.class)))
                .thenReturn(List.of());
        validateListBuilderException(MessageConfig.LIST_DATE_EMPTY);
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
        Mockito.when(
                        dateService.generateAudienceListDates(
                                ArgumentMatchers.any(DateServiceInputDTO.class), ArgumentMatchers.any(Integer.class)))
                .thenReturn(expectedList);
        Assertions.assertDoesNotThrow(() -> service.generateList());
    }

    /** Create Local Date */
    private LocalDate cld(int month, int day) {
        return LocalDate.of(2022, month, day);
    }

    private void validateListBuilderException(String expectedMessageError) {
        testUtils.validateException(() -> service.generateList(), expectedMessageError);
    }
}
