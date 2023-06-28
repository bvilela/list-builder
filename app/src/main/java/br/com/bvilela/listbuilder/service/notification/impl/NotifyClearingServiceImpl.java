package br.com.bvilela.listbuilder.service.notification.impl;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.notification.NotifyClearingService;
import br.com.bvilela.listbuilder.service.notification.NotifyUtils;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyClearingServiceImpl implements NotifyClearingService {

    private final NotifyProperties properties;

    @Override
    public List<CalendarEvent> createEvents(FinalListLimpezaDTO dto, int layout) {
        if (properties.notifyInactive()) {
            return Collections.emptyList();
        }

        properties.checkNotifyNameFilled();

        var events =
                layout == 1 ? createEventsLimpezaLayout1(dto) : createEventsLimpezaLayout2(dto);
        events.add(createDoNextListEvent(dto));

        return events;
    }

    private List<CalendarEvent> createEventsLimpezaLayout1(FinalListLimpezaDTO limpezaDto) {
        List<CalendarEvent> listNotification = new ArrayList<>();

        var list =
                limpezaDto.getItems().stream()
                        .filter(
                                e ->
                                        NotifyUtils.containsName(
                                                e.getGroup(), properties.getNotifyName()))
                        .toList();
        for (FinalListLimpezaItemDTO item : list) {

            CalendarEvent dto1 =
                    CalendarEvent.builder()
                            .setSummary("Limpeza Salão")
                            .setDateTimeStart(
                                    LocalDateTime.of(item.getDate(), LocalTime.of(17, 0, 0)))
                            .setDateTimeEnd(
                                    LocalDateTime.of(item.getDate(), LocalTime.of(18, 0, 0)))
                            .setColor(ColorEnum.SALVIA)
                            .build();
            listNotification.add(dto1);
        }
        return listNotification;
    }

    private List<CalendarEvent> createEventsLimpezaLayout2(FinalListLimpezaDTO limpezaDto) {
        List<CalendarEvent> listNotification = new ArrayList<>();

        var list =
                limpezaDto.getItemsLayout2().stream()
                        .filter(
                                e ->
                                        NotifyUtils.containsName(
                                                e.getGroup(), properties.getNotifyName()))
                        .toList();
        for (FinalListLimpezaItemLayout2DTO item : list) {

            if (properties.isNotifyCleaningPreMeeting()) {
                CalendarEvent dto1 =
                        CalendarEvent.builder()
                                .setSummary("Limpeza Pré-Reunião")
                                .setDateTimeStart(
                                        LocalDateTime.of(item.getDate1(), LocalTime.of(17, 0, 0)))
                                .setDateTimeEnd(
                                        LocalDateTime.of(item.getDate1(), LocalTime.of(18, 0, 0)))
                                .setColor(ColorEnum.SALVIA)
                                .build();
                listNotification.add(dto1);
            }

            CalendarEvent dto2 =
                    CalendarEvent.builder()
                            .setSummary(
                                    properties.isNotifyCleaningPreMeeting()
                                            ? "Limpeza Pós-Reunião"
                                            : "Limpeza Salão")
                            .setDateTimeStart(
                                    LocalDateTime.of(item.getDate2(), LocalTime.of(21, 30, 0)))
                            .setDateTimeEnd(
                                    LocalDateTime.of(item.getDate2(), LocalTime.of(22, 0, 0)))
                            .setColor(ColorEnum.SALVIA)
                            .build();
            listNotification.add(dto2);
        }
        return listNotification;
    }

    private CalendarEvent createDoNextListEvent(FinalListLimpezaDTO dto) {
        var notifyDate =
                isLayout1(dto)
                        ? dto.getItems().get(dto.getItems().size() - 1).getDate()
                        : dto.getItemsLayout2().get(dto.getItemsLayout2().size() - 1).getDate2();
        return NotifyUtils.createDoNextListEvent(ListTypeEnum.LIMPEZA, notifyDate);
    }

    private boolean isLayout1(FinalListLimpezaDTO dto) {
        return dto.getItemsLayout2() == null || dto.getItemsLayout2().isEmpty();
    }
}
