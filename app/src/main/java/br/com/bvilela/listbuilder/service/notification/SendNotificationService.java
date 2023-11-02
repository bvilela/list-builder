package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class SendNotificationService {

    private final NotifyDesignationService notifyDesignationService;

    private final NotifyClearingService notifyClearingService;

    private final NotifyAudienceService notifyAudienceService;

    private final NotifyChristianLifeService notifyChristianLifeService;

    private final GoogleCalendarCreateService calendarService;

    public void clearing(ClearingWriterDTO dto, int layout) {
        var events = notifyClearingService.createEvents(dto, layout);
        sendEventsNullSafe(events);
    }

    public void audience(List<LocalDate> dates) {
        try {
            var nextListEvent = notifyAudienceService.createEvent(dates);
            sendEventNullSafe(nextListEvent);
        } catch (Exception ex) {
            throw ex; //TODO: finalizar: log para a questao de permissao TOKEN
        }

    }

    public void christianLife(List<ChristianLifeExtractWeekDTO> weeks) {
        var events = notifyChristianLifeService.createEvents(weeks);
        sendEventsNullSafe(events);
    }

    public void designation(DesignationWriterDTO dto) {
        var events = notifyDesignationService.createEvents(dto);
        sendEventsNullSafe(events);
    }

    private void sendEventsNullSafe(List<CalendarEvent> events) {
        if (CollectionUtils.isEmpty(events)) {
            return;
        }
        calendarService.createEvents(events);
    }

    private void sendEventNullSafe(CalendarEvent event) {
        if (event == null) {
            return;
        }
        calendarService.createEvent(event);
    }
}
