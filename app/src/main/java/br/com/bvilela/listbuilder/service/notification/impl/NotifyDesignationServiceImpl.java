package br.com.bvilela.listbuilder.service.notification.impl;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.enuns.DesignacaoEntityEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.enuns.NotifDesignacaoEntityEnum;
import br.com.bvilela.listbuilder.service.notification.NotifyDesignationService;
import br.com.bvilela.listbuilder.service.notification.NotifyUtils;
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
    public List<CalendarEvent> createEvents(DesignacaoWriterDTO dto) {
        if (properties.notifyInactive()) {
            return Collections.emptyList();
        }

        properties.checkNotifyNameFilled();

        var presidentEvents = createPresidentEvents(dto);
        var readerEvents = createReaderEvents(dto);
        var audioVideoEvents = createAudioVideoEvents(dto);
        var nextListEvent = createDoNextListEvent(dto);

        List<CalendarEvent> events = new ArrayList<>();
        events.addAll(presidentEvents);
        events.addAll(readerEvents);
        events.addAll(audioVideoEvents);
        events.add(nextListEvent);

        return events;
    }

    public List<CalendarEvent> createPresidentEvents(DesignacaoWriterDTO dto) {
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
                                                i.getName(), properties.getNotifyName()))
                        .toList();
        return createDesignationEvents(president, DesignacaoEntityEnum.PRESIDENT.getLabel());
    }

    public List<CalendarEvent> createReaderEvents(DesignacaoWriterDTO dto) {
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
                                                i.getName(), properties.getNotifyName()))
                        .toList();
        return createDesignationEvents(readers, "Leitura");
    }

    public List<CalendarEvent> createAudioVideoEvents(DesignacaoWriterDTO dto) {
        var entity = NotifDesignacaoEntityEnum.AUDIO_VIDEO;

        if (notifDesignationEntityInactive(entity)) {
            logNotifyInative(entity);
            return Collections.emptyList();
        }

        var audioVideo = dto.getAudioVideo().stream().filter(this::filterByFirstName).toList();
        return createDesignationEvents(audioVideo, "Som");
    }

    public CalendarEvent createDoNextListEvent(DesignacaoWriterDTO dto) {
        var dateNotify = dto.getPresident().get(dto.getPresident().size() - 1).getDate();
        return NotifyUtils.createDoNextListEvent(ListTypeEnum.DESIGNACAO, dateNotify);
    }

    private void logNotifyInative(NotifDesignacaoEntityEnum entity) {
        log.info("Notificação para {} inativa!", entity.getLabel());
    }

    private boolean filterByFirstName(DesignacaoWriterItemDTO item) {
        String firstName = item.getName().split(" e ")[0];
        return NotifyUtils.containsName(firstName, properties.getNotifyName());
    }

    private boolean notifDesignationEntityInactive(NotifDesignacaoEntityEnum type) {
        return !properties.getNotifyDesignationTypeActive().contains(type.getLabel());
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
