package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import java.util.List;

public interface NotifyClearingService {

    List<CalendarEvent> createEvents(FinalListLimpezaDTO dto, int layout);
}
