package br.com.bvilela.listbuilder.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import org.apache.commons.lang3.StringUtils;

public final class DateUtils {

	private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

	private DateUtils() {
	}

	public static LocalDate nextDayOfWeek(LocalDate date, DayOfWeek dayOfWeek) {
		return date.with(TemporalAdjusters.next(dayOfWeek));
	}

	public static String getNameMonthFull(LocalDate date) {
		return format(date, "MMMM");
	}

	public static String getNameMonthFullCapitalize(LocalDate date) {
		return StringUtils.capitalize(getNameMonthFull(date));
	}

	public static String getNameMonthShort(LocalDate date) {
		return format(date, "MMM");
	}

	public static String getNameMonthShortCapitalize(LocalDate date) {
		return StringUtils.capitalize(getNameMonthShort(date));
	}

	public static int getMonthOrdinalByNamePT(String namePT) {
		return getMonthByNamePT(namePT).getValue();
	}

	public static Month getMonthByNamePT(String namePT) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMMM-yyyy")
				.toFormatter(LOCALE_PT_BR);
		var fakeDate = "01-" + namePT + "-2022";
		return LocalDate.parse(fakeDate, formatter).getMonth();
	}

	public static LocalDate parse(String date) {
		return parse(date, "dd-MM-yyyy");
	}

	private static DateTimeFormatter getFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).withLocale(LOCALE_PT_BR);
	}

	public static LocalDate parse(String date, String pattern) {
		DateTimeFormatter formatter = getFormatter(pattern);
		return LocalDate.parse(date, formatter);
	}

	public static String format(LocalDate date) {
		return format(date, "dd/MM/yyyy");
	}

	public static String format(LocalDate date, String pattern) {
		DateTimeFormatter formatter = getFormatter(pattern);
		return date.format(formatter);
	}

	public static String formatDDMM(LocalDate date) {
		return format(date, "dd/MM");
	}

	public static String formatDDMMM(LocalDate date) {
		return format(date, "dd/MMM").replace(".", "");
	}

	public static String formatDDMMMM(LocalDate date) {
		return format(date, "dd 'de' MMMM");
	}

	public static String getNameMonthPtByOrdinal(int ordinal) {
		var month = StringUtils.leftPad(String.valueOf(ordinal), 2, "0");
		var date = DateUtils.parse("01-" + month + "-2022");
		return getNameMonthFull(date);
	}

	public static boolean validDayOfWeek(String dayOfweek) {
		try {
			DayOfWeekEnum.valueOf(AppUtils.removeAccents(dayOfweek).toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public static List<LocalDate> extractDateByDayOfWeek(List<LocalDate> listDates, DayOfWeekEnum dayOfWeekEnum) {
		return extractDateByDayOfWeek(listDates, dayOfWeekEnum.getDayOfWeek());
	}

	public static List<LocalDate> extractDateByDayOfWeek(List<LocalDate> listDates, DayOfWeek dayOfWeek) {
		return listDates.stream().filter(e -> e.getDayOfWeek() == dayOfWeek).toList();
	}
}
