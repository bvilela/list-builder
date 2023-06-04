package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.enuns.DesignacaoEntityEnum;
import br.com.bvilela.listbuilder.enuns.NotifDesignacaoEntityEnum;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyDesignationService {

    private final NotifyProperties properties;

    public List<CalendarEvent> getNotifyPresident(DesignacaoWriterDTO dto) {
        if (notifDesignationEntityInactive(NotifDesignacaoEntityEnum.PRESIDENT)) {
            return Collections.emptyList();
        }

        var president =
                dto.getPresident().stream()
                        .filter(
                                i ->
                                        NotifyUtils.containsName(
                                                i.getName(), properties.getNotifName()))
                        .toList();
        return createEventsDesignacao(president, DesignacaoEntityEnum.PRESIDENT.getLabel());
    }

    public List<CalendarEvent> getNotifyReader(DesignacaoWriterDTO dto) {
        if (notifDesignationEntityInactive(NotifDesignacaoEntityEnum.READER)) {
            return Collections.emptyList();
        }

        var readers =
                Stream.concat(
                                dto.getReaderWatchtower().stream(),
                                dto.getReaderBibleStudy().stream())
                        .filter(
                                i ->
                                        NotifyUtils.containsName(
                                                i.getName(), properties.getNotifName()))
                        .toList();
        return createEventsDesignacao(readers, "Leitura");
    }

    public List<CalendarEvent> getNotifyAudioVideo(DesignacaoWriterDTO dto) {
        if (notifDesignationEntityInactive(NotifDesignacaoEntityEnum.AUDIO_VIDEO)) {
            return Collections.emptyList();
        }

        var audioVideo = dto.getAudioVideo().stream().filter(this::filterByFirstName).toList();
        return createEventsDesignacao(audioVideo, "Som");
    }

    private boolean filterByFirstName(DesignacaoWriterItemDTO item) {
        String firstName = item.getName().split(" e ")[0];
        return NotifyUtils.containsName(firstName, properties.getNotifName());
    }

    private boolean notifDesignationEntityInactive(NotifDesignacaoEntityEnum type) {
        return !properties.getNotifDesignationTypeActive().contains(type.getName());
    }

    private List<CalendarEvent> createEventsDesignacao(
            List<DesignacaoWriterItemDTO> list, String sumary) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<CalendarEvent> eventList = new ArrayList<>();
        for (DesignacaoWriterItemDTO item : list) {
            var event =
                    CalendarEvent.builder()
                            .setSummary(sumary)
                            .setDateTimeStart(
                                    LocalDateTime.of(item.getDate(), LocalTime.of(19, 30, 0)))
                            .setDateTimeEnd(
                                    LocalDateTime.of(item.getDate(), LocalTime.of(21, 30, 0)))
                            .setColor(ColorEnum.TOMATE)
                            .build();
            eventList.add(event);
        }
        return eventList;
    }
}
