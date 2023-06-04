package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;

import java.util.List;

public interface NotifyDesignationService {
    List<CalendarEvent> getNotifyPresident(DesignacaoWriterDTO dto);

    List<CalendarEvent> getNotifyReader(DesignacaoWriterDTO dto);

    List<CalendarEvent> getNotifyAudioVideo(DesignacaoWriterDTO dto);
}
