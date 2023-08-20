package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.util.AppUtils;
import br.com.bvilela.listbuilder.util.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyChristianLifeService {

    private final NotifyProperties properties;

    private record NotifyVidaCrista(
            List<ChristianLifeExtractWeekItemDTO> listItems, LocalDate date) {}

    public List<CalendarEvent> createEvents(List<ChristianLifeExtractWeekDTO> weeks) {
        if (properties.notifyInactive()) {
            return Collections.emptyList();
        }

        properties.checkNotifyNameFilled();

        var meetingDayEnum = properties.getChristianLifeMeetingDayEnum();

        List<NotifyVidaCrista> eventsToNotif = new ArrayList<>();
        for (ChristianLifeExtractWeekDTO week : weeks) {
            var list =
                    week.getItems().stream()
                            .filter(
                                    item ->
                                            NotifyUtils.containsName(
                                                    item.getParticipants(),
                                                    properties.getName()))
                            .toList();
            if (!list.isEmpty()) {
                var recordItem = new NotifyVidaCrista(list, week.getInitialDate());
                eventsToNotif.add(recordItem);
            }
        }

        if (eventsToNotif.isEmpty()) {
            return List.of(createDoNextListEvent(weeks));
        }

        var events = createEventsVidaCrista(eventsToNotif, meetingDayEnum.getDayOfWeek());
        var nextListEvent = createDoNextListEvent(weeks);
        events.add(nextListEvent);

        return events;
    }

    private CalendarEvent createDoNextListEvent(List<ChristianLifeExtractWeekDTO> weeks) {
        var lastWeek = weeks.get(weeks.size() - 1);
        return NotifyUtils.createDoNextListEvent(
                ListTypeEnum.VIDA_CRISTA, lastWeek.getInitialDate());
    }

    private List<CalendarEvent> createEventsVidaCrista(
            List<NotifyVidaCrista> eventsToNotif, DayOfWeek dayOfWeek) {
        List<CalendarEvent> calendarEventList = new ArrayList<>();

        for (NotifyVidaCrista notifRecord : eventsToNotif) {
            for (ChristianLifeExtractWeekItemDTO item : notifRecord.listItems) {
                var date = DateUtils.nextDayOfWeek(notifRecord.date, dayOfWeek);

                var event =
                        CalendarEvent.builder()
                                .setSummary(item.getTitle())
                                .setDescription(AppUtils.printList(item.getParticipants()))
                                .setDateTimeStart(LocalDateTime.of(date, LocalTime.of(19, 30, 0)))
                                .setDateTimeEnd(LocalDateTime.of(date, LocalTime.of(21, 30, 0)))
                                .setColor(ColorEnum.TOMATE)
                                .build();
                calendarEventList.add(event);
            }
        }

        return calendarEventList;
    }
}
