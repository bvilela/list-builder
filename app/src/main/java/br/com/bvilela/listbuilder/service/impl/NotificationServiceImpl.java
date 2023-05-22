package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.lib.enuns.ColorEnum;
import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.lib.service.GoogleCalendarCreateService;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.DesignacaoEntityEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.NotificationService;
import br.com.bvilela.listbuilder.utils.AppUtils;
import br.com.bvilela.listbuilder.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	
	@Value("${notif.active}")
	private boolean notifActive;

	@Value("${notif.name:#{null}}")
	private String notifName;
	
	@Value("${notif.cleaning.premeeting:false}")
	private boolean notifCleaningPreMeeting;
	
	@Value("${notif.christianlife.midweek.meeting.day:#{null}}")
	private String notifChristianlifeMidweekMeetingDay;
	
	private final GoogleCalendarCreateService calendarService;

	private record NotifVidaCrista(List<VidaCristaExtractWeekItemDTO> listItems, LocalDate date) {
	}

	@Override
	@SneakyThrows
	public void limpeza(FinalListLimpezaDTO limpezaDto, int idLayout) {
		
		if (!notifActive) {
			return;
		}
		
		checkNotifName();
		
		List<CalendarEvent> listNotification;
		LocalDate lastDate;
		
		if (idLayout == 2) {
			listNotification = createEventsLimpezaLayout2(limpezaDto);
			lastDate = limpezaDto.getItemsLayout2().get(limpezaDto.getItemsLayout2().size() - 1).getDate2();
		} else {
			listNotification = createEventsLimpezaLayout1(limpezaDto);
			lastDate = limpezaDto.getItems().get(limpezaDto.getItems().size() - 1).getDate();
		}
		
		CalendarEvent nextList = doNextList(ListTypeEnum.LIMPEZA, lastDate);
		listNotification.add(nextList);
		
		calendarService.createEvents(listNotification);
	}

	@Override
	@SneakyThrows
	public void assistencia(List<LocalDate> list) {
		if (!notifActive) {
			return;
		}
		var lastDate = list.get(list.size() - 1);
		CalendarEvent nextList = doNextList(ListTypeEnum.ASSISTENCIA, lastDate);
		calendarService.createEvent(nextList);
	}

	@Override
	@SneakyThrows
	public void vidaCrista(List<VidaCristaExtractWeekDTO> listWeeks) {
		if (!notifActive) {
			return;
		}

		checkNotifName();
		
		var meetingDay = checkMidweekMeetingDay();

		List<NotifVidaCrista> eventsToNotif = new ArrayList<>();
		for (VidaCristaExtractWeekDTO week : listWeeks) {
			var list = week.getItems().stream().filter(item -> groupContainsName(item.getParticipants())).toList();
			if (!list.isEmpty()) {
				var recordItem = new NotifVidaCrista(list, week.getDate1());
				eventsToNotif.add(recordItem);
			}
		}

		if (!eventsToNotif.isEmpty()) {
			createAndSendEventsVidaCrista(eventsToNotif, meetingDay.getDayOfWeek());
		}

		var lastWeek = listWeeks.get(listWeeks.size() - 1);
		CalendarEvent nextList = doNextList(ListTypeEnum.VIDA_CRISTA, lastWeek.getDate1());
		calendarService.createEvent(nextList);
	}
	
	@Override
	@SneakyThrows
	public void designacao(DesignacaoWriterDTO dto) {

		if (!notifActive) {
			return;
		}
		
		var president = dto.getPresident().stream().filter(i -> groupContainsName(i.getName())).toList();
		var readers = Stream.concat(dto.getReaderWatchtower().stream(), dto.getReaderBibleStudy().stream()).filter(i -> groupContainsName(i.getName())).toList();
		var auvioVideo = dto.getAudioVideo().stream().filter(i -> groupContainsName(i.getName().split(" e ")[0])).toList();
		
		List<CalendarEvent> calendarEventList = new ArrayList<>();
		createEventsDesignacao(president, DesignacaoEntityEnum.PRESIDENT.getLabel(), calendarEventList);
		createEventsDesignacao(readers, "Leitura", calendarEventList);
		createEventsDesignacao(auvioVideo, "Som", calendarEventList);
		
		var lastDate = dto.getPresident().get(dto.getPresident().size()-1).getDate();
		CalendarEvent nextList = doNextList(ListTypeEnum.DESIGNACAO, lastDate);
		
		calendarEventList.add(nextList);
		calendarService.createEvents(calendarEventList);
	}

	private void createEventsDesignacao(List<DesignacaoWriterItemDTO> list,
			String sumary, List<CalendarEvent> calendarEventList) {
		if (!list.isEmpty()) {
			for (DesignacaoWriterItemDTO item : list) {
				var event = CalendarEvent.builder().setSummary(sumary)
						.setDateTimeStart(LocalDateTime.of(item.getDate(), LocalTime.of(19, 30, 0)))
						.setDateTimeEnd(LocalDateTime.of(item.getDate(), LocalTime.of(21, 30, 0))).setColor(ColorEnum.TOMATE)
						.build();
				calendarEventList.add(event);
			}
		}
	}

	@SneakyThrows
	private void createAndSendEventsVidaCrista(List<NotifVidaCrista> eventsToNotif, DayOfWeek dayOfWeek) {
		List<CalendarEvent> calendarEventList = new ArrayList<>();

		for (NotifVidaCrista notifRecord : eventsToNotif) {
			for (VidaCristaExtractWeekItemDTO item : notifRecord.listItems) {
				var date = DateUtils.nextDayOfWeek(notifRecord.date, dayOfWeek);
				
				var event = CalendarEvent.builder().setSummary(item.getTitle())
						.setDescription(AppUtils.printList(item.getParticipants()))
						.setDateTimeStart(LocalDateTime.of(date, LocalTime.of(19, 30, 0)))
						.setDateTimeEnd(LocalDateTime.of(date, LocalTime.of(21, 30, 0))).setColor(ColorEnum.TOMATE)
						.build();
				calendarEventList.add(event);
			}
		}

		calendarService.createEvents(calendarEventList);
	}

	private void checkNotifName() throws ListBuilderException {
		if (Objects.isNull(notifName)) {
			throw new ListBuilderException("Defina a propriedade 'notif.name'!");
		}
	}
	
	private DayOfWeekEnum checkMidweekMeetingDay() throws ListBuilderException {
		if (Objects.isNull(notifChristianlifeMidweekMeetingDay)) {
			throw new ListBuilderException("Defina a propriedade 'notif.christianlife.midweek.meeting.day'!");
		}
		
		var meetingDay = AppUtils.removeAccents(notifChristianlifeMidweekMeetingDay);
		DayOfWeekEnum meetingDayEnum = null;
		try {
			meetingDayEnum = DayOfWeekEnum.valueOf(meetingDay.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ListBuilderException("Propriedade 'notif.christianlife.midweek.meeting.day' não é um Dia da Semana Válido!");
		}
		
		return meetingDayEnum;
	}
	
	private boolean groupContainsName(List<String> list) {
		if (Objects.isNull(list)) {
			return false;
		}
		return list.stream().filter(e -> e.toLowerCase().contains(notifName.toLowerCase())).count() > 0;
	}
	
	private boolean groupContainsName(String list) {
		if (Objects.isNull(list)) {
			return false;
		}
		return list.toLowerCase().contains(notifName.toLowerCase());
	}

	private CalendarEvent doNextList(ListTypeEnum executionModeEnum, LocalDate lastDateList) {
		var dateEvent = lastDateList.minusDays(7);
		var listName = StringUtils.capitalize(executionModeEnum.toString());
		return CalendarEvent.builder()
				.setSummary(listName.concat(" Fazer Lista"))
				.setDescription(String.format("Fazer a próxima lista de %s. Atual termina em: %s", listName, DateUtils.format(lastDateList)))
				.setDateTimeStart(LocalDateTime.of(dateEvent, LocalTime.of(18, 0, 0)))
				.setDateTimeEnd(LocalDateTime.of(dateEvent, LocalTime.of(18, 30, 0))).setColor(ColorEnum.GRAFITE)
				.build();
	}
	
	private List<CalendarEvent> createEventsLimpezaLayout1(FinalListLimpezaDTO limpezaDto) {
		List<CalendarEvent> listNotification = new ArrayList<>();
		
		var list = limpezaDto.getItems().stream().filter(e -> groupContainsName(e.getGroup())).toList();
		for (FinalListLimpezaItemDTO item : list) {

			CalendarEvent dto1 = CalendarEvent.builder()
					.setSummary("Limpeza Salão")
					.setDateTimeStart(LocalDateTime.of(item.getDate(), LocalTime.of(17, 00, 0)))
					.setDateTimeEnd(LocalDateTime.of(item.getDate(), LocalTime.of(18, 0, 0)))
					.setColor(ColorEnum.SALVIA)
					.build();
			listNotification.add(dto1);
		}
		return listNotification;
	}
	
	private List<CalendarEvent> createEventsLimpezaLayout2(FinalListLimpezaDTO limpezaDto) {
		List<CalendarEvent> listNotification = new ArrayList<>();
		
		var list = limpezaDto.getItemsLayout2().stream().filter(e -> groupContainsName(e.getGroup())).toList();
		for (FinalListLimpezaItemLayout2DTO item : list) {

			if (notifCleaningPreMeeting) {
				CalendarEvent dto1 = CalendarEvent.builder()
						.setSummary("Limpeza Pré-Reunião")
						.setDateTimeStart(LocalDateTime.of(item.getDate1(), LocalTime.of(17, 00, 0)))
						.setDateTimeEnd(LocalDateTime.of(item.getDate1(), LocalTime.of(18, 0, 0)))
						.setColor(ColorEnum.SALVIA)
						.build();
				listNotification.add(dto1);
			}

			CalendarEvent dto2 = CalendarEvent.builder()
					.setSummary(notifCleaningPreMeeting ? "Limpeza Pós-Reunião" : "Limpeza Salão")
					.setDateTimeStart(LocalDateTime.of(item.getDate2(), LocalTime.of(21, 30, 0)))
					.setDateTimeEnd(LocalDateTime.of(item.getDate2(), LocalTime.of(22, 0, 0)))
					.setColor(ColorEnum.SALVIA)
					.build();
			listNotification.add(dto2);
		}
		return listNotification;
	}

}
