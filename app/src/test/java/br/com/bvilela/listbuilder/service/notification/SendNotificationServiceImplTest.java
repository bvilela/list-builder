package br.com.bvilela.listbuilder.service.notification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SendNotificationServiceImplTest {

    @InjectMocks private SendNotificationServiceImpl service;

    @InjectMocks private NotifyProperties notifProperties; // TODO: remove

    @Mock private NotifyDesignationServiceImpl notifyDesignationService;

    @Mock private NotifyClearingServiceImpl notifyClearingService;

    @Mock private NotifyAudienceServiceImpl notifyAudienceService;

    @Mock private NotifyChristianLifeServiceImpl notifyChristianLifeService;

    @Mock private GoogleCalendarCreateService calendarService;

    private static final int LIMPEZA_LAYOUT1 = 1;
    private static final int LIMPEZA_LAYOUT2 = 2;

    private final FinalListLimpezaDTO dtoLimpeza =
            FinalListLimpezaDTO.builder()
                    .itemsLayout2(
                            List.of(
                                    FinalListLimpezaItemLayout2DTO.builder()
                                            .withGroup("P1 P2")
                                            .withDate1(LocalDate.now())
                                            .withLabel1("label1")
                                            .withDate2(LocalDate.now())
                                            .withLabel2("label2")
                                            .build(),
                                    FinalListLimpezaItemLayout2DTO.builder()
                                            .withGroup("P1, P2")
                                            .withDate1(LocalDate.now())
                                            .withLabel1("label1")
                                            .withDate2(LocalDate.now())
                                            .withLabel2("label2")
                                            .build()))
                    .build();

    private final List<LocalDate> dtoAssistencia = List.of(LocalDate.now());

    private final List<VidaCristaExtractWeekDTO> dtoVidaCrista =
            List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());

    @BeforeEach
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        service =
                new SendNotificationServiceImpl(
                        notifyDesignationService,
                        notifyClearingService,
                        notifyAudienceService,
                        notifyChristianLifeService,
                        calendarService);
    }

    // *********************** ASSISTENCIA *********************** \\
    @Test
    void shouldAssitenciaNotifActiveFalse() {
        assertDoesNotThrow(() -> service.assistencia(dtoAssistencia));
    }

    @Test
    void shouldAssitenciaNotifActiveTrue() {
        setNotifyActive();
        assertDoesNotThrow(() -> service.assistencia(dtoAssistencia));
    }

    // *********************** LIMPEZA *********************** \\
    @Test
    void shouldLimpezaNotifActiveFalse() {
        assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    /* TODO: mover classe correta
    @Test
    void shouldLimpezaNotifActiveTrueNotifNameNull() {
        setNotifyActive();
        assertThrows(
                ListBuilderException.class, () -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }
    */

    @Test
    void shouldAssistenciaNotifActiveTrueWithoutNotifPersonSuccess() {
        setNotifyActive();
        setNotifyName("xpto");
        assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    void shouldAssistenciaNotifActiveTrueNotifPersonSuccess() {
        setNotifyActive();
        setNotifyName("P1");
        assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    void shouldLimpezaNotifActiveTrueNotifPersonLayout1Success() {
        var item = new FinalListLimpezaItemDTO(LocalDate.of(2022, 6, 10), "Label", "P1, P2, P3");
        final FinalListLimpezaDTO dtoLimpezaLayout1 =
                FinalListLimpezaDTO.builder().items(List.of(item)).build();
        setNotifyActive();
        setNotifyName("P1");
        assertDoesNotThrow(() -> service.limpeza(dtoLimpezaLayout1, LIMPEZA_LAYOUT1));
    }

    @Test
    void shouldLimpezaNotifActiveGroupNull() {
        setNotifyActive();
        setNotifyChristianLifeMeetingDay("terca");
        var item = dtoLimpeza.getItemsLayout2().get(0);
        var newItem =
                FinalListLimpezaItemLayout2DTO.builder()
                        .withDate1(item.getDate1())
                        .withDate2(item.getDate2())
                        .withGroup(null)
                        .withLabel1(item.getLabel1())
                        .withLabel2(item.getLabel2())
                        .build();
        List<FinalListLimpezaItemLayout2DTO> newList =
                new ArrayList<>(dtoLimpeza.getItemsLayout2());
        newList.add(newItem);
        var newDtoLimpeza = FinalListLimpezaDTO.builder().itemsLayout2(newList).build();
        setNotifyName("XPTO");
        assertDoesNotThrow(() -> service.limpeza(newDtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    @SneakyThrows
    void shouldLimpezaNotifActiveTrueNotifPersonPreMeetingSuccess() {
        setNotifyActive();
        setNotifyName("P1");
        FieldUtils.writeField(notifProperties, "notifyCleaningPreMeeting", true, true);
        assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    // *********************** VIDA CRISTA *********************** \\
    @Test
    void shouldVidaCristaNotifActiveFalse() {
        var list =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        assertDoesNotThrow(() -> service.vidaCrista(list));
    }

    /* TODO: mover classe correta
    @Test
    void shouldVidaCristaNotifActiveTrueNotifNameNull() {
        setNotifyActive();
        assertThrows(ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
    }
    */

    @Test
    void shouldVidaCristaNotifActiveTrueWithoutNotifSuccess() {
        setNotifyActive();
        setNotifyChristianLifeMeetingDay("terca");
        setNotifyName("P1");
        assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
    }

    @Test
    void shouldVidaCristaNotifActiveTrueNotifSuccess() {
        setNotifyActive();
        setNotifyChristianLifeMeetingDay("terca");
        var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
        setNotifyName(name);
        assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
    }

    @Test
    void shouldVidaCristaNotifActiveTrueButNameItemNull() {
        setNotifyActive();
        setNotifyChristianLifeMeetingDay("terca");
        dtoVidaCrista.get(0).getItems().get(0).setParticipants(null);
        setNotifyName("XPTO");
        assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
    }

    /* TODO: mover classe correta
    @Test
    void shouldVidaCristaMidweekMeetingDayNullException() {
        setNotifyActive();
        var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
        setNotifyName(name);
        setNotifyChristianLifeMeetingDay("teste");
        var exception =
                assertThrows(ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
        assertEquals(
                "Propriedade 'notify.christianlife.midweek.meeting.day' não é um Dia da Semana Válido!",
                exception.getMessage());
    }

    @Test
    void shouldVidaCristaMidweekMeetingDayInvalidException() {
        setNotifyActive();
        var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
        setNotifyName(name);
        var exception =
                assertThrows(ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
        assertEquals(
                "Defina a propriedade 'notify.christianlife.midweek.meeting.day'!",
                exception.getMessage());
    }
    */

    // *********************** DESIGNACAO *********************** \\


    // *********************** UTILS *********************** \\
    @SneakyThrows
    private void setNotifyActive() {
        FieldUtils.writeField(notifProperties, "notifyActive", true, true);
    }

    @SneakyThrows
    private void setNotifyChristianLifeMeetingDay(String value) {
        FieldUtils.writeField(notifProperties, "notifyChristianLifeMeetingDay", value, true);
    }

    @SneakyThrows
    private void setNotifyName(String name) {
        FieldUtils.writeField(notifProperties, "notifyName", name, true);
    }
}
