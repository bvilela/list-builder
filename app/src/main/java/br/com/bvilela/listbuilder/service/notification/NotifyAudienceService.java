package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import java.time.LocalDate;
import java.util.List;

public interface NotifyAudienceService {

    CalendarEvent createEvent(List<LocalDate> list);
}
