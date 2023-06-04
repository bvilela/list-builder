package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;

import java.time.LocalDate;
import java.util.List;

public interface NotifyClearingService {

    List<CalendarEvent> getNotifyClearing(FinalListLimpezaDTO limpezaDto, int layout);

    LocalDate getDateDoNextListEvent(FinalListLimpezaDTO limpezaDto);
}
