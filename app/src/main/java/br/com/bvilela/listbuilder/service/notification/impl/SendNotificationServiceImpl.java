package br.com.bvilela.listbuilder.service.notification.impl;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import java.time.LocalDate;
import java.util.List;

import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class SendNotificationServiceImpl implements SendNotificationService {

    private final NotifyDesignationServiceImpl notifyDesignationService;

    private final NotifyClearingServiceImpl notifyClearingService;

    private final NotifyAudienceServiceImpl notifyAudienceService;

    private final NotifyChristianLifeServiceImpl notifyChristianLifeService;

    private final GoogleCalendarCreateService calendarService;

    @Override
    public void limpeza(FinalListLimpezaDTO dto, int layout) {
        var events = notifyClearingService.createEvents(dto, layout);
        sendEventsNullSafe(events);
    }

    @Override
    public void assistencia(List<LocalDate> dates) {
        var nextListEvent = notifyAudienceService.createEvent(dates);
        sendEventsNullSafe(nextListEvent);
    }

    @Override
    public void vidaCrista(List<VidaCristaExtractWeekDTO> weeks) {
        var events = notifyChristianLifeService.createEvents(weeks);
        sendEventsNullSafe(events);
    }

    @Override
    public void designacao(DesignacaoWriterDTO dto) {
        var events = notifyDesignationService.createEvents(dto);
        sendEventsNullSafe(events);
    }

    private void sendEventsNullSafe(List<CalendarEvent> events) {
        if (CollectionUtils.isEmpty(events)) {
            return;
        }
        calendarService.createEvents(events);
    }

    private void sendEventsNullSafe(CalendarEvent event) {
        if (event == null) {
            return;
        }
        calendarService.createEvent(event);
    }
}
