package br.com.bvilela.listbuilder.service.notification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.builder.VidaCristaExtractWeekDtoBuilder;
import br.com.bvilela.listbuilder.builder.clearing.FinalListLimpezaDtoBuilder;
import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendNotificationServiceTest {

    @InjectMocks private SendNotificationService service;

    @Mock private NotifyDesignationService notifyDesignationService;

    @Mock private NotifyClearingService notifyClearingService;

    @Mock private NotifyAudienceService notifyAudienceService;

    @Mock private NotifyChristianLifeService notifyChristianLifeService;

    @Mock private GoogleCalendarCreateService calendarService;

    private static final CalendarEvent CALENDAR_EVENT =
            CalendarEvent.builder()
                    .setSummary("test")
                    .setDateTimeStart(LocalDateTime.now())
                    .setDateTimeEnd(LocalDateTime.now().plusHours(1))
                    .build();

    private final ClearingWriterDTO clearingDTO = FinalListLimpezaDtoBuilder.createMockLayout2();

    @BeforeEach
    void setupBeforeEach() {
        service =
                new SendNotificationService(
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
        int clearingLayout1 = 1;
        when(notifyClearingService.createEvents(any(ClearingWriterDTO.class), any(Integer.class)))
                .thenReturn(events);
        service.clearing(clearingDTO, clearingLayout1);
        verify(calendarService, never()).createEvents(events);
    }

    @Test
    void clearingEventsNotNullEmpty() {
        int clearingLayout2 = 2;
        List<CalendarEvent> events = List.of(CALENDAR_EVENT);
        when(notifyClearingService.createEvents(any(ClearingWriterDTO.class), any(Integer.class)))
                .thenReturn(events);
        service.clearing(clearingDTO, clearingLayout2);
        verify(calendarService, times(1)).createEvents(events);
    }

    // *********************** AUDIENCE *********************** \\
    @Test
    void audienceEventsNull() {
        var dtoAssistencia = List.of(LocalDate.now());
        when(notifyAudienceService.createEvent(ArgumentMatchers.anyList())).thenReturn(null);
        service.audience(dtoAssistencia);
        verify(calendarService, never()).createEvent(null);
    }

    @Test
    void audienceEventsNotNull() {
        var dtoAssistencia = List.of(LocalDate.now());
        when(notifyAudienceService.createEvent(ArgumentMatchers.anyList()))
                .thenReturn(CALENDAR_EVENT);
        service.audience(dtoAssistencia);
        verify(calendarService, times(1)).createEvent(CALENDAR_EVENT);
    }

    // *********************** CHRISTIAN LIFE *********************** \\
    @DisplayName("Send Clearing Events: Events is Null/Empty")
    @ParameterizedTest(name = "Events is \"{0}\"")
    @NullAndEmptySource
    void christianLifeEventsNullEmpty(List<CalendarEvent> events) {
        var dtoVidaCrista =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        when(notifyChristianLifeService.createEvents(ArgumentMatchers.anyList()))
                .thenReturn(events);
        service.christianLife(dtoVidaCrista);
        verify(calendarService, never()).createEvents(events);
    }

    @Test
    void christianLifeEventsNotNullEmpty() {
        var dtoVidaCrista =
                List.of(VidaCristaExtractWeekDtoBuilder.create().withRandomDataOneMonth().build());
        List<CalendarEvent> events = List.of(CALENDAR_EVENT);
        when(notifyChristianLifeService.createEvents(ArgumentMatchers.anyList()))
                .thenReturn(events);
        service.christianLife(dtoVidaCrista);
        verify(calendarService, times(1)).createEvents(events);
    }

    // *********************** DESIGNATION *********************** \\
    @Test
    void designationEventsNull() {
        var designationDTO = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        when(notifyDesignationService.createEvents(any(DesignationWriterDTO.class)))
                .thenReturn(null);
        service.designation(designationDTO);
        verify(calendarService, never()).createEvent(null);
    }

    @Test
    void designationEventsNotNull() {
        var designationDTO = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        List<CalendarEvent> events = List.of(CALENDAR_EVENT);
        when(notifyDesignationService.createEvents(any(DesignationWriterDTO.class)))
                .thenReturn(events);
        service.designation(designationDTO);
        verify(calendarService, times(1)).createEvents(events);
    }
}
