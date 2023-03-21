package com.bruno.listbuilder.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.dto.ItemDateDTO;
import com.bruno.listbuilder.enuns.DayOfWeekEnum;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.utils.DateUtils;

@Service
public class DateServiceImpl implements DateService {
	
	@Override
	public List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout) {
		var list = generateListDates(dto);
		
		if (layout == 2) {
			List<ItemDateDTO> newList = new ArrayList<>();
			int ordinal = 1;
			for (ItemDateDTO e: list) {
				var before = new ItemDateDTO(ordinal, e.getDate().minusDays(1), e.getMessage());
				if (newList.stream().filter(e2 -> e2.getDate().isEqual(before.getDate())).count() == 0) {
					newList.add(before);
				}
				newList.add(new ItemDateDTO(ordinal, e));
				ordinal++;	
			}
			return newList;
		}
		return list;
	}
	
	@Override
	public List<LocalDate> generateListDatesAssistencia(DateServiceInputDTO dto) {
		var list = generateListDates(dto);
		var list2 = generateListDates(new DateServiceInputDTO(list.get(list.size()-1).getDate(), dto));
		list.addAll(list2);
		return list.stream().map(ItemDateDTO::getDate).toList();
	}
	
	@Override
	public List<LocalDate> generateListDatesDesignacao(DateServiceInputDTO dto) {
		var listDates = generateListDates(dto);
		var lastDate = listDates.get(listDates.size()-1).getDate();
		if (lastDate.getDayOfWeek() == dto.getMidweekDayWeekEnum().getDayOfWeek()) {
			var addDate = DateUtils.nextDayOfWeek(lastDate, dto.getWeekendDayWeekEnum().getDayOfWeek());
			listDates.add(new ItemDateDTO(addDate));
		}
		return listDates.stream().map(ItemDateDTO::getDate).toList();
	}

	private List<ItemDateDTO> generateListDates(DateServiceInputDTO dto) {
		
		List<LocalDate> listDatesAddOrdered = getDatesAddOrdered(dto);
		
		List<ItemDateDTO> listDates = new ArrayList<>();
		LocalDate nextDate = dto.getLastDate();
		int nextMonth = dto.getLastDate().plusMonths(1).getMonthValue();
		boolean toContinue = true;
		
		while (toContinue) {
			nextDate = getNextDate(dto.getMidweekDayWeekEnum(), dto.getWeekendDayWeekEnum(), nextDate);
			
			if (nextDate.getMonth() == dto.getLastDate().getMonth()) {
				nextMonth = dto.getLastDate().getMonthValue();
			}
			
			addDatesExceptions(dto, listDatesAddOrdered, listDates, nextDate);
			
			if (nextDate.getMonthValue() == nextMonth) {
				removeDatesExceptions(dto, listDates, nextDate);
			} else {
				toContinue = false;
			}
		}
		
		return listDates;
	}

	private List<LocalDate> getDatesAddOrdered(DateServiceInputDTO dto) {
		if (Objects.isNull(dto.getAddToList())) {
			return List.of();	
		} else {
			var list = new ArrayList<>(dto.getAddToList().keySet());
			list.sort(Comparator.naturalOrder());
			return list;
		}
	}
	
	private LocalDate getNextDate(DayOfWeekEnum midweekEnum, DayOfWeekEnum weekendEnum, LocalDate date) {
		if (date.getDayOfWeek().compareTo(midweekEnum.getDayOfWeek()) > 0) {
			return DateUtils.nextDayOfWeek(date, midweekEnum.getDayOfWeek());
		}
		return DateUtils.nextDayOfWeek(date, weekendEnum.getDayOfWeek());
	}

	private void addDatesExceptions(DateServiceInputDTO dto, List<LocalDate> listDatesAddOrdered,
			List<ItemDateDTO> listDates, LocalDate nextDate) {
		var firstDateAdd = listDatesAddOrdered.stream().findFirst();
		while (firstDateAdd.isPresent() && nextDate.isAfter(firstDateAdd.get())) {
			var message = dto.getAddToList().get(firstDateAdd.get());
			listDates.add(new ItemDateDTO(firstDateAdd.get(), message));
			listDatesAddOrdered.remove(0);
			firstDateAdd = listDatesAddOrdered.stream().findFirst();
		}
	}
	
	private void removeDatesExceptions(DateServiceInputDTO dto, List<ItemDateDTO> listDates, LocalDate nextDate) {
		if (Objects.isNull(dto.getRemoveFromList())) {
			listDates.add(new ItemDateDTO(nextDate));
		}
		else {
			if (!dto.getRemoveFromList().contains(nextDate)) {
				listDates.add(new ItemDateDTO(nextDate));
			}
		}
	}

}
