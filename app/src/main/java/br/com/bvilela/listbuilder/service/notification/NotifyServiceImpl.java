package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final NotifyProperties properties;

    private final NotifyDesignationServiceImpl notifyDesignationService;

    private final NotifyClearingServiceImpl notifyClearingService;

    private final GoogleCalendarCreateService calendarService;

    private record NotifVidaCrista(List<VidaCristaExtractWeekItemDTO> listItems, LocalDate date) {}

    private boolean notifyInactive() {
        return !properties.isNotifyActive();
    }

    @Override
    public void limpeza(FinalListLimpezaDTO limpezaDto, int layout) {
        if (notifyInactive()) {
            return;
        }

        properties.checkNotifyNameFilled();

        var events = notifyClearingService.getNotifyClearing(limpezaDto, layout);

        var dateDoNextList = notifyClearingService.getDateDoNextListEvent(limpezaDto);
        CalendarEvent nextListEvent = createDoNextListEvent(ListTypeEnum.LIMPEZA, dateDoNextList);
        events.add(nextListEvent);

        calendarService.createEvents(events);
    }

    @Override
    public void assistencia(List<LocalDate> list) {
        if (notifyInactive()) {
            return;
        }

        var lastDate = list.get(list.size() - 1);
        CalendarEvent nextList = createDoNextListEvent(ListTypeEnum.ASSISTENCIA, lastDate);
        calendarService.createEvent(nextList);
    }

    @Override
    public void vidaCrista(List<VidaCristaExtractWeekDTO> listWeeks) {
        if (notifyInactive()) {
            return;
        }

        properties.checkNotifyNameFilled();

        var meetingDay = checkMidweekMeetingDay();

        List<NotifVidaCrista> eventsToNotif = new ArrayList<>();
        for (VidaCristaExtractWeekDTO week : listWeeks) {
            var list =
                    week.getItems().stream()
                            .filter(
                                    item ->
                                            NotifyUtils.containsName(
                                                    item.getParticipants(),
                                                    properties.getNotifyName()))
                            .toList();
            if (!list.isEmpty()) {
                var recordItem = new NotifVidaCrista(list, week.getInitialDate());
                eventsToNotif.add(recordItem);
            }
        }

        if (!eventsToNotif.isEmpty()) {
            createAndSendEventsVidaCrista(eventsToNotif, meetingDay.getDayOfWeek());
        }

        var lastWeek = listWeeks.get(listWeeks.size() - 1);
        CalendarEvent nextList =
                createDoNextListEvent(ListTypeEnum.VIDA_CRISTA, lastWeek.getInitialDate());
        calendarService.createEvent(nextList);
    }

    @Override
    public void designacao(DesignacaoWriterDTO dto) {
        if (notifyInactive()) {
            return;
        }

        properties.checkNotifyNameFilled();

        var presidentEvents = notifyDesignationService.getNotifyPresident(dto);
        var readerEvents = notifyDesignationService.getNotifyReader(dto);
        var audioVideoEvents = notifyDesignationService.getNotifyAudioVideo(dto);

        var lastDate = dto.getPresident().get(dto.getPresident().size() - 1).getDate();
        CalendarEvent nextListEvent = createDoNextListEvent(ListTypeEnum.DESIGNACAO, lastDate);

        List<CalendarEvent> eventList = new ArrayList<>();
        eventList.addAll(presidentEvents);
        eventList.addAll(readerEvents);
        eventList.addAll(audioVideoEvents);
        eventList.add(nextListEvent);

        calendarService.createEvents(eventList);
    }

    private void createAndSendEventsVidaCrista(
            List<NotifVidaCrista> eventsToNotif, DayOfWeek dayOfWeek) {
        List<CalendarEvent> calendarEventList = new ArrayList<>();

        for (NotifVidaCrista notifRecord : eventsToNotif) {
            for (VidaCristaExtractWeekItemDTO item : notifRecord.listItems) {
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

        calendarService.createEvents(calendarEventList);
    }

    @SneakyThrows
    private DayOfWeekEnum checkMidweekMeetingDay() {
        if (properties.getNotifyChristianlifeMidweekMeetingDay() == null) {
            throw new ListBuilderException(
                    "Defina a propriedade 'notify.christianlife.midweek.meeting.day'!");
        }

        var meetingDay =
                AppUtils.removeAccents(properties.getNotifyChristianlifeMidweekMeetingDay());
        DayOfWeekEnum meetingDayEnum;
        try {
            meetingDayEnum = DayOfWeekEnum.valueOf(meetingDay.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ListBuilderException(
                    "Propriedade 'notify.christianlife.midweek.meeting.day' não é um Dia da Semana Válido!");
        }

        return meetingDayEnum;
    }

    private CalendarEvent createDoNextListEvent(
            ListTypeEnum executionModeEnum, LocalDate lastDateList) {
        var dateEvent = lastDateList.minusDays(7);
        var listName = StringUtils.capitalize(executionModeEnum.toString());
        return CalendarEvent.builder()
                .setSummary(listName.concat(" Fazer Lista"))
                .setDescription(
                        String.format(
                                "Fazer a próxima lista de %s. Atual termina em: %s",
                                listName, DateUtils.format(lastDateList)))
                .setDateTimeStart(LocalDateTime.of(dateEvent, LocalTime.of(18, 0, 0)))
                .setDateTimeEnd(LocalDateTime.of(dateEvent, LocalTime.of(18, 30, 0)))
                .setColor(ColorEnum.GRAFITE)
                .build();
    }

}
