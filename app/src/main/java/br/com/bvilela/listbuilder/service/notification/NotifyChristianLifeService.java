package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import java.util.List;

public interface NotifyChristianLifeService {

    List<CalendarEvent> createEvents(List<VidaCristaExtractWeekDTO> weeks);
}
