package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class NotifyServiceImplTest {

    @InjectMocks private NotifyServiceImpl service;

    @InjectMocks private NotifyProperties notifProperties;

    @Mock private NotifyDesignationServiceImpl notifyDesignationService;

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
        service = new NotifyServiceImpl(notifProperties, notifyDesignationService, calendarService);
    }

    // *********************** ASSISTENCIA *********************** \\
    @Test
    void shouldAssitenciaNotifActiveFalse() {
        Assertions.assertDoesNotThrow(() -> service.assistencia(dtoAssistencia));
    }

    @Test
    void shouldAssitenciaNotifActiveTrue() {
        setNotifActive();
        Assertions.assertDoesNotThrow(() -> service.assistencia(dtoAssistencia));
    }

    // *********************** LIMPEZA *********************** \\
    @Test
    void shouldLimpezaNotifActiveFalse() {
        Assertions.assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    void shouldLimpezaNotifActiveTrueNotifNameNull() {
        setNotifActive();
        Assertions.assertThrows(
                ListBuilderException.class, () -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    void shouldAssistenciaNotifActiveTrueWithoutNotifPersonSuccess() {
        setNotifActive();
        setNotifName("xpto");
        Assertions.assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    void shouldAssistenciaNotifActiveTrueNotifPersonSuccess() {
        setNotifActive();
        setNotifName("P1");
        Assertions.assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    void shouldLimpezaNotifActiveTrueNotifPersonLayout1Success() {
        var item = new FinalListLimpezaItemDTO(LocalDate.of(2022, 6, 10), "Label", "P1, P2, P3");
        final FinalListLimpezaDTO dtoLimpezaLayout1 =
                FinalListLimpezaDTO.builder().items(List.of(item)).build();
        setNotifActive();
        setNotifName("P1");
        Assertions.assertDoesNotThrow(() -> service.limpeza(dtoLimpezaLayout1, LIMPEZA_LAYOUT1));
    }

    @Test
    void shouldVidaCristaNotifActiveGroupNull() {
        setNotifActive();
        setnotifChristianlifeMidweekMeetingDay("terca");
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
        setNotifName("XPTO");
        Assertions.assertDoesNotThrow(() -> service.limpeza(newDtoLimpeza, LIMPEZA_LAYOUT2));
    }

    @Test
    @SneakyThrows
    void shouldAssistenciaNotifActiveTrueNotifPersonPreMeetingSuccess() {
        setNotifActive();
        setNotifName("P1");
        FieldUtils.writeField(notifProperties, "notifCleaningPreMeeting", true, true);
        Assertions.assertDoesNotThrow(() -> service.limpeza(dtoLimpeza, LIMPEZA_LAYOUT2));
    }

    // *********************** VIDA CRISTA *********************** \\
    @Test
    void shouldVidaCristaNotifActiveFalse() {
        var list =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        Assertions.assertDoesNotThrow(() -> service.vidaCrista(list));
    }

    @Test
    void shouldVidaCristaNotifActiveTrueNotifNameNull() {
        setNotifActive();
        Assertions.assertThrows(
                ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
    }

    @Test
    void shouldVidaCristaNotifActiveTrueWithoutNotifSuccess() {
        setNotifActive();
        setnotifChristianlifeMidweekMeetingDay("terca");
        setNotifName("P1");
        Assertions.assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
    }

    @Test
    void shouldVidaCristaNotifActiveTrueNotifSuccess() {
        setNotifActive();
        setnotifChristianlifeMidweekMeetingDay("terca");
        var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
        setNotifName(name);
        Assertions.assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
    }

    @Test
    void shouldVidaCristaNotifActiveTrueButNameItemNull() {
        setNotifActive();
        setnotifChristianlifeMidweekMeetingDay("terca");
        dtoVidaCrista.get(0).getItems().get(0).setParticipants(null);
        setNotifName("XPTO");
        Assertions.assertDoesNotThrow(() -> service.vidaCrista(dtoVidaCrista));
    }

    @Test
    void shouldVidaCristaMidweekMeetingDayNullException() {
        setNotifActive();
        var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
        setNotifName(name);
        setnotifChristianlifeMidweekMeetingDay("teste");
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
        Assertions.assertEquals(
                "Propriedade 'notif.christianlife.midweek.meeting.day' não é um Dia da Semana Válido!",
                exception.getMessage());
    }

    @Test
    void shouldVidaCristaMidweekMeetingDayInvalidException() {
        setNotifActive();
        var name = dtoVidaCrista.get(0).getItems().get(0).getParticipants().get(0);
        setNotifName(name);
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class, () -> service.vidaCrista(dtoVidaCrista));
        Assertions.assertEquals(
                "Defina a propriedade 'notif.christianlife.midweek.meeting.day'!",
                exception.getMessage());
    }


    // *********************** DESIGNACAO *********************** \\
    @Test
    void designationNotifyInactiveSuccess() {
        Assertions.assertDoesNotThrow(
                () -> service.designacao(DesignacaoWriterDtoBuilder.create().withRandomData().build()));
    }

    @Test
    void designationNotifyActiveSuccess() {
        setNotifActive();
        Mockito.when(notifyDesignationService.getNotifyPresident(any(DesignacaoWriterDTO.class))).thenReturn(Collections.emptyList());
        Mockito.when(notifyDesignationService.getNotifyReader(any(DesignacaoWriterDTO.class))).thenReturn(Collections.emptyList());
        Mockito.when(notifyDesignationService.getNotifyAudioVideo(any(DesignacaoWriterDTO.class))).thenReturn(Collections.emptyList());
        Assertions.assertDoesNotThrow(
                () -> service.designacao(DesignacaoWriterDtoBuilder.create().withRandomData().build()));
    }

    // *********************** UTILS *********************** \\
    @SneakyThrows
    private void setNotifActive() {
        FieldUtils.writeField(notifProperties, "notifActive", true, true);
    }

    @SneakyThrows
    private void setnotifChristianlifeMidweekMeetingDay(String value) {
        FieldUtils.writeField(notifProperties, "notifChristianlifeMidweekMeetingDay", value, true);
    }

    @SneakyThrows
    private void setNotifName(String name) {
        FieldUtils.writeField(notifProperties, "notifName", name, true);
    }
}
