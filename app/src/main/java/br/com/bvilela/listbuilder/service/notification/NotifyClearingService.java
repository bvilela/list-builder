package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemLayout2DTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyClearingService {

    private final NotifyProperties properties;

    public List<CalendarEvent> createEvents(ClearingWriterDTO dto, int layout) {
        if (properties.notifyInactive()) {
            return Collections.emptyList();
        }

        properties.checkNotifyNameFilled();

        var events =
                layout == 1 ? createEventsLimpezaLayout1(dto) : createEventsLimpezaLayout2(dto);
        events.add(createDoNextListEvent(dto));

        return events;
    }

    private List<CalendarEvent> createEventsLimpezaLayout1(ClearingWriterDTO limpezaDto) {
        List<CalendarEvent> listNotification = new ArrayList<>();

        var list =
                limpezaDto.getItems().stream()
                        .filter(
                                e ->
                                        NotifyUtils.containsName(
                                                e.getGroup(), properties.getNotifyName()))
                        .toList();
        for (ClearingWriterItemDTO item : list) {

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

    private List<CalendarEvent> createEventsLimpezaLayout2(ClearingWriterDTO limpezaDto) {
        List<CalendarEvent> listNotification = new ArrayList<>();

        var list =
                limpezaDto.getItemsLayout2().stream()
                        .filter(
                                e ->
                                        NotifyUtils.containsName(
                                                e.getGroup(), properties.getNotifyName()))
                        .toList();
        for (ClearingWriterItemLayout2DTO item : list) {

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
                            .setSummary(getSummary())
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

    private String getSummary() {
        return properties.isNotifyCleaningPreMeeting() ? "Limpeza Pós-Reunião" : "Limpeza Salão";
    }

    private CalendarEvent createDoNextListEvent(ClearingWriterDTO dto) {
        var notifyDate =
                isLayout1(dto)
                        ? dto.getItems().get(dto.getItems().size() - 1).getDate()
                        : dto.getItemsLayout2().get(dto.getItemsLayout2().size() - 1).getDate2();
        return NotifyUtils.createDoNextListEvent(ListTypeEnum.LIMPEZA, notifyDate);
    }

    private boolean isLayout1(ClearingWriterDTO dto) {
        return dto.getItemsLayout2() == null || dto.getItemsLayout2().isEmpty();
    }
}
