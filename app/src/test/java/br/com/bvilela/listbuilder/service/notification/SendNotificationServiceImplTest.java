package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyAudienceServiceImpl;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyChristianLifeServiceImpl;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyClearingServiceImpl;
import br.com.bvilela.listbuilder.service.notification.impl.NotifyDesignationServiceImpl;
import br.com.bvilela.listbuilder.service.notification.impl.SendNotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendNotificationServiceImplTest {

    @InjectMocks private SendNotificationServiceImpl service;

    @Mock private NotifyDesignationServiceImpl notifyDesignationService;

    @Mock private NotifyClearingServiceImpl notifyClearingService;

    @Mock private NotifyAudienceServiceImpl notifyAudienceService;

    @Mock private NotifyChristianLifeServiceImpl notifyChristianLifeService;

    @Mock private GoogleCalendarCreateService calendarService;

    private static final CalendarEvent calendarEvent = CalendarEvent
            .builder()
            .setSummary("test")
            .setDateTimeStart(LocalDateTime.now())
            .setDateTimeEnd(LocalDateTime.now().plusHours(1))
            .build();

    private final FinalListLimpezaDTO clearingDTO =
            FinalListLimpezaDTO .builder()
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


    // *********************** CLEARING *********************** \\
    @DisplayName("Send Clearing Events: Events is Null/Empty")
    @ParameterizedTest(name = "Events is \"{0}\"")
    @NullAndEmptySource
    void clearingEventsNullEmpty(List<CalendarEvent> events) {
        int CLEARING_LAYOUT1 = 1;
        when(notifyClearingService.createEvents(any(FinalListLimpezaDTO.class), any(Integer.class))).thenReturn(events);
        service.limpeza(clearingDTO, CLEARING_LAYOUT1);
        verify(calendarService, never()).createEvents(events);
    }

    @Test
    void clearingEventsNotNullEmpty() {
        int CLEARING_LAYOUT2 = 2;
        List<CalendarEvent> events = List.of(calendarEvent);
        when(notifyClearingService.createEvents(any(FinalListLimpezaDTO.class), any(Integer.class))).thenReturn(events);
        service.limpeza(clearingDTO, CLEARING_LAYOUT2);
        verify(calendarService, times(1)).createEvents(events);
    }


    // *********************** AUDIENCE *********************** \\
    @Test
    void audienceEventsNull() {
        var dtoAssistencia = List.of(LocalDate.now());
        when(notifyAudienceService.createEvent(anyList())).thenReturn(null);
        service.assistencia(dtoAssistencia);
        verify(calendarService, never()).createEvent(null);
    }

    @Test
    void audienceEventsNotNull() {
        var dtoAssistencia = List.of(LocalDate.now());
        when(notifyAudienceService.createEvent(anyList())).thenReturn(calendarEvent);
        service.assistencia(dtoAssistencia);
        verify(calendarService, times(1)).createEvent(calendarEvent);
    }


    // *********************** CHRISTIAN LIFE *********************** \\
    @DisplayName("Send Clearing Events: Events is Null/Empty")
    @ParameterizedTest(name = "Events is \"{0}\"")
    @NullAndEmptySource
    void christianLifeEventsNullEmpty(List<CalendarEvent> events) {
        var dtoVidaCrista =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        when(notifyChristianLifeService.createEvents(anyList())).thenReturn(events);
        service.vidaCrista(dtoVidaCrista);
        verify(calendarService, never()).createEvents(events);
    }

    @Test
    void christianLifeEventsNotNullEmpty() {
        var dtoVidaCrista =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        List<CalendarEvent> events = List.of(calendarEvent);
        when(notifyChristianLifeService.createEvents(anyList())).thenReturn(events);
        service.vidaCrista(dtoVidaCrista);
        verify(calendarService, times(1)).createEvents(events);
    }


    // *********************** DESIGNATION *********************** \\
    @Test
    void designationEventsNull() {
        var designationDTO = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        when(notifyDesignationService.createEvents(any(DesignacaoWriterDTO.class))).thenReturn(null);
        service.designacao(designationDTO);
        verify(calendarService, never()).createEvent(null);
    }

    @Test
    void designationEventsNotNull() {
        var designationDTO = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        List<CalendarEvent> events = List.of(calendarEvent);
        when(notifyDesignationService.createEvents(any(DesignacaoWriterDTO.class))).thenReturn(events);
        service.designacao(designationDTO);
        verify(calendarService, times(1)).createEvents(events);
    }

}
