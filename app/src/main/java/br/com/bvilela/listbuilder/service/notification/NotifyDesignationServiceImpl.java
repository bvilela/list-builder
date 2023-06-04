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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyDesignationServiceImpl implements NotifyDesignationService {

    private final NotifyProperties properties;

    @Override
    public List<CalendarEvent> getNotifyPresident(DesignacaoWriterDTO dto) {
        var entity = NotifDesignacaoEntityEnum.PRESIDENT;
        if (notifDesignationEntityInactive(entity)) {
            logNotifyInative(entity);
            return Collections.emptyList();
        }

        var president =
                dto.getPresident().stream()
                        .filter(
                                i ->
                                        NotifyUtils.containsName(
                                                i.getName(), properties.getNotifName()))
                        .toList();
        return createDesignationEvents(president, DesignacaoEntityEnum.PRESIDENT.getLabel());
    }

    @Override
    public List<CalendarEvent> getNotifyReader(DesignacaoWriterDTO dto) {
        var entity = NotifDesignacaoEntityEnum.READER;
        if (notifDesignationEntityInactive(entity)) {
            logNotifyInative(entity);
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
        return createDesignationEvents(readers, "Leitura");
    }

    @Override
    public List<CalendarEvent> getNotifyAudioVideo(DesignacaoWriterDTO dto) {
        var entity = NotifDesignacaoEntityEnum.AUDIO_VIDEO;
        if (notifDesignationEntityInactive(entity)) {
            logNotifyInative(entity);
            return Collections.emptyList();
        }

        var audioVideo = dto.getAudioVideo().stream().filter(this::filterByFirstName).toList();
        return createDesignationEvents(audioVideo, "Som");
    }

    private void logNotifyInative(NotifDesignacaoEntityEnum entity) {
        log.info("Notificação para {} inativa!", entity.getLabel());
    }

    private boolean filterByFirstName(DesignacaoWriterItemDTO item) {
        String firstName = item.getName().split(" e ")[0];
        return NotifyUtils.containsName(firstName, properties.getNotifName());
    }

    private boolean notifDesignationEntityInactive(NotifDesignacaoEntityEnum type) {
        return !properties.getNotifDesignationTypeActive().contains(type.getLabel());
    }

    private List<CalendarEvent> createDesignationEvents(
            List<DesignacaoWriterItemDTO> list, String summary) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<CalendarEvent> eventList = new ArrayList<>();
        for (DesignacaoWriterItemDTO item : list) {
            var event =
                    CalendarEvent.builder()
                            .setSummary(summary)
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
