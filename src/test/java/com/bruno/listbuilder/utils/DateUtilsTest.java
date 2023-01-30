package com.bruno.listbuilder.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.bruno.listbuilder.enuns.DayOfWeekEnum;
import com.bruno.listbuilder.exception.ListBuilderException;

class DateUtilsTest {
	
	private static final String JAN = "janeiro";
	private static final String FEV = "fevereiro";
	private static final String MAR = "março";
	private static final String ABR = "abril";
	private static final String MAI = "maio";
	private static final String JUN = "junho";
	private static final String JUL = "julho";
	private static final String AGO = "agosto";
	private static final String SET = "setembro";
	private static final String OUT = "outubro";
	private static final String NOV = "novembro";
	private static final String DEZ = "dezembro";
	
	private static List<LocalDate> LIST_DATE_MOCK;
	
	public DateUtilsTest() {
		LIST_DATE_MOCK = generateListDateMock();
	}
	
	private List<LocalDate> generateListDateMock() {
		var list = new ArrayList<LocalDate>();
		
		for (int i = 1; i < 31; i++) {
			list.add(LocalDate.of(2022, 1, i));
		}
		return list;
	}

	private String full(int month) {
		return DateUtils.getNameMonthFull(LocalDate.of(2022, month, 1));
	}
	
	private String fullC(int month) {
		return DateUtils.getNameMonthFullCapitalize(LocalDate.of(2022, month, 1));
	}
	
	private String shorter(int month) {
		return DateUtils.getNameMonthShort(LocalDate.of(2022, month, 1));
	}
	
	private String shorterC(int month) {
		return DateUtils.getNameMonthShortCapitalize(LocalDate.of(2022, month, 1));
	}

	@Test
	void shouldGetNameMonthFull() {
		assertEquals(JAN, full(1));
		assertEquals(FEV, full(2));
		assertEquals(MAR, full(3));
		assertEquals(ABR, full(4));
		assertEquals(MAI, full(5));
		assertEquals(JUN, full(6));
		assertEquals(JUL, full(7));
		assertEquals(AGO, full(8));
		assertEquals(SET, full(9));
		assertEquals(OUT, full(10));
		assertEquals(NOV, full(11));
		assertEquals(DEZ, full(12));
	}
	
	@Test
	void shouldGetNameMonthFullCapitalize() {
		assertEquals("Janeiro", fullC(1));
		assertEquals("Fevereiro", fullC(2));
		assertEquals("Março", fullC(3));
		assertEquals("Abril", fullC(4));
		assertEquals("Maio", fullC(5));
		assertEquals("Junho", fullC(6));
		assertEquals("Julho", fullC(7));
		assertEquals("Agosto", fullC(8));
		assertEquals("Setembro", fullC(9));
		assertEquals("Outubro", fullC(10));
		assertEquals("Novembro", fullC(11));
		assertEquals("Dezembro", fullC(12));
	}
	
	@Test
	void shouldGetNameMonthShort() {
		assertEquals("jan.", shorter(1));
		assertEquals("fev.", shorter(2));
		assertEquals("mar.", shorter(3));
		assertEquals("abr.", shorter(4));
		assertEquals("mai.", shorter(5));
		assertEquals("jun.", shorter(6));
		assertEquals("jul.", shorter(7));
		assertEquals("ago.", shorter(8));
		assertEquals("set.", shorter(9));
		assertEquals("out.", shorter(10));
		assertEquals("nov.", shorter(11));
		assertEquals("dez.", shorter(12));
	}
	
	@Test
	void shouldGetNameMonthShortCapitalize() {
		assertEquals("Jan.", shorterC(1));
		assertEquals("Fev.", shorterC(2));
		assertEquals("Mar.", shorterC(3));
		assertEquals("Abr.", shorterC(4));
		assertEquals("Mai.", shorterC(5));
		assertEquals("Jun.", shorterC(6));
		assertEquals("Jul.", shorterC(7));
		assertEquals("Ago.", shorterC(8));
		assertEquals("Set.", shorterC(9));
		assertEquals("Out.", shorterC(10));
		assertEquals("Nov.", shorterC(11));
		assertEquals("Dez.", shorterC(12));
	}
	
	@Test 
	void shouldNextDayOfWeek() {
		assertEquals(LocalDate.of(2022, 06, 13), DateUtils.nextDayOfWeek(LocalDate.of(2022, 06, 10), DayOfWeek.MONDAY));
		assertEquals(LocalDate.of(2022, 06, 17), DateUtils.nextDayOfWeek(LocalDate.of(2022, 06, 10), DayOfWeek.FRIDAY));
		assertEquals(LocalDate.of(2022, 07, 05), DateUtils.nextDayOfWeek(LocalDate.of(2022, 06, 30), DayOfWeek.TUESDAY));
	}
	
	@Test
	void shouldGetMonthByNamePT() {
		checkMonth(1, JAN);
		checkMonth(2, FEV);
		checkMonth(3, MAR);
		checkMonth(4, ABR);
		checkMonth(5, MAI);
		checkMonth(6, JUN);
		checkMonth(7, JUL);
		checkMonth(8, AGO);
		checkMonth(9, SET);
		checkMonth(10, OUT);
		checkMonth(11, NOV);
		checkMonth(12, DEZ);
	}
	
	@Test
	void shouldGetMonthOrdinalByNamePT() {
		assertEquals(1, DateUtils.getMonthOrdinalByNamePT(JAN));
		assertEquals(2, DateUtils.getMonthOrdinalByNamePT(FEV));
		assertEquals(3, DateUtils.getMonthOrdinalByNamePT(MAR));
		assertEquals(4, DateUtils.getMonthOrdinalByNamePT(ABR));
		assertEquals(5, DateUtils.getMonthOrdinalByNamePT(MAI));
		assertEquals(6, DateUtils.getMonthOrdinalByNamePT(JUN));
		assertEquals(7, DateUtils.getMonthOrdinalByNamePT(JUL));
		assertEquals(8, DateUtils.getMonthOrdinalByNamePT(AGO));
		assertEquals(9, DateUtils.getMonthOrdinalByNamePT(SET));
		assertEquals(10, DateUtils.getMonthOrdinalByNamePT(OUT));
		assertEquals(11, DateUtils.getMonthOrdinalByNamePT(NOV));
		assertEquals(12, DateUtils.getMonthOrdinalByNamePT(DEZ));
	}
	
	private void checkMonth(int value, String monthName) {
		assertEquals(value, DateUtils.getMonthByNamePT(StringUtils.capitalize(monthName)).getValue());
		assertEquals(value, DateUtils.getMonthByNamePT(monthName.toLowerCase()).getValue());
		assertEquals(value, DateUtils.getMonthByNamePT(monthName.toUpperCase()).getValue());
	}

	@Test
	void shouldGetNameMonthPTByOrdinal() {
		assertEquals(JAN, DateUtils.getNameMonthPtByOrdinal(1));
		assertEquals(FEV, DateUtils.getNameMonthPtByOrdinal(2));
		assertEquals(MAR, DateUtils.getNameMonthPtByOrdinal(3));
		assertEquals(ABR, DateUtils.getNameMonthPtByOrdinal(4));
		assertEquals(MAI, DateUtils.getNameMonthPtByOrdinal(5));
		assertEquals(JUN, DateUtils.getNameMonthPtByOrdinal(6));
		assertEquals(JUL, DateUtils.getNameMonthPtByOrdinal(7));
		assertEquals(AGO, DateUtils.getNameMonthPtByOrdinal(8));
		assertEquals(SET, DateUtils.getNameMonthPtByOrdinal(9));
		assertEquals(OUT, DateUtils.getNameMonthPtByOrdinal(10));
		assertEquals(NOV, DateUtils.getNameMonthPtByOrdinal(11));
		assertEquals(DEZ, DateUtils.getNameMonthPtByOrdinal(12));
	}
	
	@Test
	void shouldFormatDDMMMM() {
		assertEquals("01 de janeiro", DateUtils.formatDDMMMM(LocalDate.of(2022, 1, 1)));
		assertEquals("02 de fevereiro", DateUtils.formatDDMMMM(LocalDate.of(2022, 2, 2)));
		assertEquals("03 de março", DateUtils.formatDDMMMM(LocalDate.of(2022, 3, 3)));
		assertEquals("04 de abril", DateUtils.formatDDMMMM(LocalDate.of(2022, 4, 4)));
		assertEquals("05 de maio", DateUtils.formatDDMMMM(LocalDate.of(2022, 5, 5)));
		assertEquals("06 de junho", DateUtils.formatDDMMMM(LocalDate.of(2022, 6, 6)));
		assertEquals("07 de julho", DateUtils.formatDDMMMM(LocalDate.of(2022, 7, 7)));
		assertEquals("08 de agosto", DateUtils.formatDDMMMM(LocalDate.of(2022, 8, 8)));
		assertEquals("09 de setembro", DateUtils.formatDDMMMM(LocalDate.of(2022, 9, 9)));
		assertEquals("10 de outubro", DateUtils.formatDDMMMM(LocalDate.of(2022, 10, 10)));
		assertEquals("11 de novembro", DateUtils.formatDDMMMM(LocalDate.of(2022, 11, 11)));
		assertEquals("12 de dezembro", DateUtils.formatDDMMMM(LocalDate.of(2022, 12, 12)));
	}
	
	@Test
	void shouldValidDayOfWeek() throws ListBuilderException {
		assertTrue(DateUtils.validDayOfWeek("segunda"));
		assertTrue(DateUtils.validDayOfWeek("terça"));
		assertTrue(DateUtils.validDayOfWeek("quarta"));
		assertTrue(DateUtils.validDayOfWeek("quinta"));
		assertTrue(DateUtils.validDayOfWeek("sexta"));
		assertTrue(DateUtils.validDayOfWeek("sábado"));
		assertTrue(DateUtils.validDayOfWeek("domingo"));
		assertFalse(DateUtils.validDayOfWeek("abc"));
		assertFalse(DateUtils.validDayOfWeek("domingooooooo"));
		assertFalse(DateUtils.validDayOfWeek("sabado-feira"));
	}
	
	@Test
	void shouldParseToLocalDate() {
		assertEquals(LocalDate.of(2022, 1, 1), DateUtils.parse("01-01-2022"));
		assertEquals(LocalDate.of(2022, 12, 31), DateUtils.parse("31-12-2022"));
	}
	
	@Test
	void shouldFormatLocalDate() {
		assertEquals("01/01/2022", DateUtils.format(LocalDate.of(2022, 1, 1)));
		assertEquals("31/12/2022", DateUtils.format(LocalDate.of(2022, 12, 31)));
	}
	
	@Test
	void shouldFormatDDMMM() {
		assertEquals("01/jan", DateUtils.formatDDMMM(LocalDate.of(2022, 1, 1)));
		assertEquals("02/fev", DateUtils.formatDDMMM(LocalDate.of(2022, 2, 2)));
		assertEquals("03/mar", DateUtils.formatDDMMM(LocalDate.of(2022, 3, 3)));
		assertEquals("04/abr", DateUtils.formatDDMMM(LocalDate.of(2022, 4, 4)));
		assertEquals("05/mai", DateUtils.formatDDMMM(LocalDate.of(2022, 5, 5)));
		assertEquals("06/jun", DateUtils.formatDDMMM(LocalDate.of(2022, 6, 6)));
		assertEquals("07/jul", DateUtils.formatDDMMM(LocalDate.of(2022, 7, 7)));
		assertEquals("08/ago", DateUtils.formatDDMMM(LocalDate.of(2022, 8, 8)));
		assertEquals("09/set", DateUtils.formatDDMMM(LocalDate.of(2022, 9, 9)));
		assertEquals("10/out", DateUtils.formatDDMMM(LocalDate.of(2022, 10, 10)));
		assertEquals("11/nov", DateUtils.formatDDMMM(LocalDate.of(2022, 11, 11)));
		assertEquals("12/dez", DateUtils.formatDDMMM(LocalDate.of(2022, 12, 12)));
	}
	
	@Test
	void extractDateByDayOfWeekShouldTuesday() {
		var listAllDates = LIST_DATE_MOCK;
		var listDates = DateUtils.extractDateByDayOfWeek(listAllDates, DayOfWeek.TUESDAY);
		var listDatesExpeted = List.of(LocalDate.of(2022, 1, 4), LocalDate.of(2022, 1, 11),
				LocalDate.of(2022, 1, 18), LocalDate.of(2022, 1, 25));
		assertEquals(listDatesExpeted, listDates);
	}
	
	@Test
	void extractDateByDayOfWeekShouldWednesday() {
		var listAllDates = LIST_DATE_MOCK;
		var listDates = DateUtils.extractDateByDayOfWeek(listAllDates, DayOfWeek.WEDNESDAY);
		var listDatesExpeted = List.of(LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 12),
				LocalDate.of(2022, 1, 19), LocalDate.of(2022, 1, 26));
		assertEquals(listDatesExpeted, listDates);
	}
	
	@Test
	void dextractDateByDayOfWeekShouldSaturday() {
		var listAllDates = LIST_DATE_MOCK;
		var listDates = DateUtils.extractDateByDayOfWeek(listAllDates, DayOfWeek.SATURDAY);
		var listDatesExpeted = List.of(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 8),
				LocalDate.of(2022, 1, 15), LocalDate.of(2022, 1, 22),  LocalDate.of(2022, 1, 29));
		assertEquals(listDatesExpeted, listDates);
	}
	
	@Test
	void extractDateByDayOfWeekShouldFriday() {
		var listAllDates = LIST_DATE_MOCK;
		var listDates = DateUtils.extractDateByDayOfWeek(listAllDates, DayOfWeekEnum.SEXTA);
		var listDatesExpeted = List.of(LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 14),
				LocalDate.of(2022, 1, 21), LocalDate.of(2022, 1, 28));
		assertEquals(listDatesExpeted, listDates);
	}
	
	@Test
	void extractDateByDayOfWeekShouldSunday() {
		var listAllDates = LIST_DATE_MOCK;
		var listDates = DateUtils.extractDateByDayOfWeek(listAllDates, DayOfWeekEnum.DOMINGO);
		var listDatesExpeted = List.of(LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 9),
				LocalDate.of(2022, 1, 16), LocalDate.of(2022, 1, 23), LocalDate.of(2022, 1, 30));
		assertEquals(listDatesExpeted, listDates);
	}

}
