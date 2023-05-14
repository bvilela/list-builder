package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private final List<LocalDate> listDateMock = generateListDateMock();

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
        Assertions.assertEquals(JAN, full(1));
        Assertions.assertEquals(FEV, full(2));
        Assertions.assertEquals(MAR, full(3));
        Assertions.assertEquals(ABR, full(4));
        Assertions.assertEquals(MAI, full(5));
        Assertions.assertEquals(JUN, full(6));
        Assertions.assertEquals(JUL, full(7));
        Assertions.assertEquals(AGO, full(8));
        Assertions.assertEquals(SET, full(9));
        Assertions.assertEquals(OUT, full(10));
        Assertions.assertEquals(NOV, full(11));
        Assertions.assertEquals(DEZ, full(12));
    }

    @Test
    void shouldGetNameMonthFullCapitalize() {
        Assertions.assertEquals("Janeiro", fullC(1));
        Assertions.assertEquals("Fevereiro", fullC(2));
        Assertions.assertEquals("Março", fullC(3));
        Assertions.assertEquals("Abril", fullC(4));
        Assertions.assertEquals("Maio", fullC(5));
        Assertions.assertEquals("Junho", fullC(6));
        Assertions.assertEquals("Julho", fullC(7));
        Assertions.assertEquals("Agosto", fullC(8));
        Assertions.assertEquals("Setembro", fullC(9));
        Assertions.assertEquals("Outubro", fullC(10));
        Assertions.assertEquals("Novembro", fullC(11));
        Assertions.assertEquals("Dezembro", fullC(12));
    }

    @Test
    void shouldGetNameMonthShort() {
        Assertions.assertEquals("jan.", shorter(1));
        Assertions.assertEquals("fev.", shorter(2));
        Assertions.assertEquals("mar.", shorter(3));
        Assertions.assertEquals("abr.", shorter(4));
        Assertions.assertEquals("mai.", shorter(5));
        Assertions.assertEquals("jun.", shorter(6));
        Assertions.assertEquals("jul.", shorter(7));
        Assertions.assertEquals("ago.", shorter(8));
        Assertions.assertEquals("set.", shorter(9));
        Assertions.assertEquals("out.", shorter(10));
        Assertions.assertEquals("nov.", shorter(11));
        Assertions.assertEquals("dez.", shorter(12));
    }

    @Test
    void shouldGetNameMonthShortCapitalize() {
        Assertions.assertEquals("Jan.", shorterC(1));
        Assertions.assertEquals("Fev.", shorterC(2));
        Assertions.assertEquals("Mar.", shorterC(3));
        Assertions.assertEquals("Abr.", shorterC(4));
        Assertions.assertEquals("Mai.", shorterC(5));
        Assertions.assertEquals("Jun.", shorterC(6));
        Assertions.assertEquals("Jul.", shorterC(7));
        Assertions.assertEquals("Ago.", shorterC(8));
        Assertions.assertEquals("Set.", shorterC(9));
        Assertions.assertEquals("Out.", shorterC(10));
        Assertions.assertEquals("Nov.", shorterC(11));
        Assertions.assertEquals("Dez.", shorterC(12));
    }

    @Test
    void shouldNextDayOfWeek() {
        Assertions.assertEquals(
                LocalDate.of(2022, 06, 13),
                DateUtils.nextDayOfWeek(LocalDate.of(2022, 06, 10), DayOfWeek.MONDAY));
        Assertions.assertEquals(
                LocalDate.of(2022, 06, 17),
                DateUtils.nextDayOfWeek(LocalDate.of(2022, 06, 10), DayOfWeek.FRIDAY));
        Assertions.assertEquals(
                LocalDate.of(2022, 07, 05),
                DateUtils.nextDayOfWeek(LocalDate.of(2022, 06, 30), DayOfWeek.TUESDAY));
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
        Assertions.assertEquals(1, DateUtils.getMonthOrdinalByNamePT(JAN));
        Assertions.assertEquals(2, DateUtils.getMonthOrdinalByNamePT(FEV));
        Assertions.assertEquals(3, DateUtils.getMonthOrdinalByNamePT(MAR));
        Assertions.assertEquals(4, DateUtils.getMonthOrdinalByNamePT(ABR));
        Assertions.assertEquals(5, DateUtils.getMonthOrdinalByNamePT(MAI));
        Assertions.assertEquals(6, DateUtils.getMonthOrdinalByNamePT(JUN));
        Assertions.assertEquals(7, DateUtils.getMonthOrdinalByNamePT(JUL));
        Assertions.assertEquals(8, DateUtils.getMonthOrdinalByNamePT(AGO));
        Assertions.assertEquals(9, DateUtils.getMonthOrdinalByNamePT(SET));
        Assertions.assertEquals(10, DateUtils.getMonthOrdinalByNamePT(OUT));
        Assertions.assertEquals(11, DateUtils.getMonthOrdinalByNamePT(NOV));
        Assertions.assertEquals(12, DateUtils.getMonthOrdinalByNamePT(DEZ));
    }

    private void checkMonth(int value, String monthName) {
        Assertions.assertEquals(
                value, DateUtils.getMonthByNamePT(StringUtils.capitalize(monthName)).getValue());
        Assertions.assertEquals(
                value, DateUtils.getMonthByNamePT(monthName.toLowerCase()).getValue());
        Assertions.assertEquals(
                value, DateUtils.getMonthByNamePT(monthName.toUpperCase()).getValue());
    }

    @Test
    void shouldGetNameMonthPTByOrdinal() {
        Assertions.assertEquals(JAN, DateUtils.getNameMonthPtByOrdinal(1));
        Assertions.assertEquals(FEV, DateUtils.getNameMonthPtByOrdinal(2));
        Assertions.assertEquals(MAR, DateUtils.getNameMonthPtByOrdinal(3));
        Assertions.assertEquals(ABR, DateUtils.getNameMonthPtByOrdinal(4));
        Assertions.assertEquals(MAI, DateUtils.getNameMonthPtByOrdinal(5));
        Assertions.assertEquals(JUN, DateUtils.getNameMonthPtByOrdinal(6));
        Assertions.assertEquals(JUL, DateUtils.getNameMonthPtByOrdinal(7));
        Assertions.assertEquals(AGO, DateUtils.getNameMonthPtByOrdinal(8));
        Assertions.assertEquals(SET, DateUtils.getNameMonthPtByOrdinal(9));
        Assertions.assertEquals(OUT, DateUtils.getNameMonthPtByOrdinal(10));
        Assertions.assertEquals(NOV, DateUtils.getNameMonthPtByOrdinal(11));
        Assertions.assertEquals(DEZ, DateUtils.getNameMonthPtByOrdinal(12));
    }

    @Test
    void shouldFormatDDMMMM() {
        Assertions.assertEquals("01 de janeiro", DateUtils.formatDDMMMM(LocalDate.of(2022, 1, 1)));
        Assertions.assertEquals(
                "02 de fevereiro", DateUtils.formatDDMMMM(LocalDate.of(2022, 2, 2)));
        Assertions.assertEquals("03 de março", DateUtils.formatDDMMMM(LocalDate.of(2022, 3, 3)));
        Assertions.assertEquals("04 de abril", DateUtils.formatDDMMMM(LocalDate.of(2022, 4, 4)));
        Assertions.assertEquals("05 de maio", DateUtils.formatDDMMMM(LocalDate.of(2022, 5, 5)));
        Assertions.assertEquals("06 de junho", DateUtils.formatDDMMMM(LocalDate.of(2022, 6, 6)));
        Assertions.assertEquals("07 de julho", DateUtils.formatDDMMMM(LocalDate.of(2022, 7, 7)));
        Assertions.assertEquals("08 de agosto", DateUtils.formatDDMMMM(LocalDate.of(2022, 8, 8)));
        Assertions.assertEquals("09 de setembro", DateUtils.formatDDMMMM(LocalDate.of(2022, 9, 9)));
        Assertions.assertEquals(
                "10 de outubro", DateUtils.formatDDMMMM(LocalDate.of(2022, 10, 10)));
        Assertions.assertEquals(
                "11 de novembro", DateUtils.formatDDMMMM(LocalDate.of(2022, 11, 11)));
        Assertions.assertEquals(
                "12 de dezembro", DateUtils.formatDDMMMM(LocalDate.of(2022, 12, 12)));
    }

    @Test
    void shouldValidDayOfWeek() {
        Assertions.assertTrue(DateUtils.validDayOfWeek("segunda"));
        Assertions.assertTrue(DateUtils.validDayOfWeek("terça"));
        Assertions.assertTrue(DateUtils.validDayOfWeek("quarta"));
        Assertions.assertTrue(DateUtils.validDayOfWeek("quinta"));
        Assertions.assertTrue(DateUtils.validDayOfWeek("sexta"));
        Assertions.assertTrue(DateUtils.validDayOfWeek("sábado"));
        Assertions.assertTrue(DateUtils.validDayOfWeek("domingo"));
        Assertions.assertFalse(DateUtils.validDayOfWeek("abc"));
        Assertions.assertFalse(DateUtils.validDayOfWeek("domingooooooo"));
        Assertions.assertFalse(DateUtils.validDayOfWeek("sabado-feira"));
    }

    @Test
    void shouldParseToLocalDate() {
        Assertions.assertEquals(LocalDate.of(2022, 1, 1), DateUtils.parse("01-01-2022"));
        Assertions.assertEquals(LocalDate.of(2022, 12, 31), DateUtils.parse("31-12-2022"));
    }

    @Test
    void shouldFormatLocalDate() {
        Assertions.assertEquals("01/01/2022", DateUtils.format(LocalDate.of(2022, 1, 1)));
        Assertions.assertEquals("31/12/2022", DateUtils.format(LocalDate.of(2022, 12, 31)));
    }

    @Test
    void shouldFormatDDMMM() {
        Assertions.assertEquals("01/jan", DateUtils.formatDDMMM(LocalDate.of(2022, 1, 1)));
        Assertions.assertEquals("02/fev", DateUtils.formatDDMMM(LocalDate.of(2022, 2, 2)));
        Assertions.assertEquals("03/mar", DateUtils.formatDDMMM(LocalDate.of(2022, 3, 3)));
        Assertions.assertEquals("04/abr", DateUtils.formatDDMMM(LocalDate.of(2022, 4, 4)));
        Assertions.assertEquals("05/mai", DateUtils.formatDDMMM(LocalDate.of(2022, 5, 5)));
        Assertions.assertEquals("06/jun", DateUtils.formatDDMMM(LocalDate.of(2022, 6, 6)));
        Assertions.assertEquals("07/jul", DateUtils.formatDDMMM(LocalDate.of(2022, 7, 7)));
        Assertions.assertEquals("08/ago", DateUtils.formatDDMMM(LocalDate.of(2022, 8, 8)));
        Assertions.assertEquals("09/set", DateUtils.formatDDMMM(LocalDate.of(2022, 9, 9)));
        Assertions.assertEquals("10/out", DateUtils.formatDDMMM(LocalDate.of(2022, 10, 10)));
        Assertions.assertEquals("11/nov", DateUtils.formatDDMMM(LocalDate.of(2022, 11, 11)));
        Assertions.assertEquals("12/dez", DateUtils.formatDDMMM(LocalDate.of(2022, 12, 12)));
    }

    @Test
    void extractDateByDayOfWeekShouldTuesday() {
        var listDates = DateUtils.extractDateByDayOfWeek(listDateMock, DayOfWeek.TUESDAY);
        var listDatesExpeted =
                List.of(
                        LocalDate.of(2022, 1, 4),
                        LocalDate.of(2022, 1, 11),
                        LocalDate.of(2022, 1, 18),
                        LocalDate.of(2022, 1, 25));
        Assertions.assertEquals(listDatesExpeted, listDates);
    }

    @Test
    void extractDateByDayOfWeekShouldWednesday() {
        var listDates = DateUtils.extractDateByDayOfWeek(listDateMock, DayOfWeek.WEDNESDAY);
        var listDatesExpeted =
                List.of(
                        LocalDate.of(2022, 1, 5),
                        LocalDate.of(2022, 1, 12),
                        LocalDate.of(2022, 1, 19),
                        LocalDate.of(2022, 1, 26));
        Assertions.assertEquals(listDatesExpeted, listDates);
    }

    @Test
    void dextractDateByDayOfWeekShouldSaturday() {
        var listDates = DateUtils.extractDateByDayOfWeek(listDateMock, DayOfWeek.SATURDAY);
        var listDatesExpeted =
                List.of(
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 8),
                        LocalDate.of(2022, 1, 15),
                        LocalDate.of(2022, 1, 22),
                        LocalDate.of(2022, 1, 29));
        Assertions.assertEquals(listDatesExpeted, listDates);
    }

    @Test
    void extractDateByDayOfWeekShouldFriday() {
        var listDates = DateUtils.extractDateByDayOfWeek(listDateMock, DayOfWeekEnum.SEXTA);
        var listDatesExpeted =
                List.of(
                        LocalDate.of(2022, 1, 7),
                        LocalDate.of(2022, 1, 14),
                        LocalDate.of(2022, 1, 21),
                        LocalDate.of(2022, 1, 28));
        Assertions.assertEquals(listDatesExpeted, listDates);
    }

    @Test
    void extractDateByDayOfWeekShouldSunday() {
        var listDates = DateUtils.extractDateByDayOfWeek(listDateMock, DayOfWeekEnum.DOMINGO);
        var listDatesExpeted =
                List.of(
                        LocalDate.of(2022, 1, 2),
                        LocalDate.of(2022, 1, 9),
                        LocalDate.of(2022, 1, 16),
                        LocalDate.of(2022, 1, 23),
                        LocalDate.of(2022, 1, 30));
        Assertions.assertEquals(listDatesExpeted, listDates);
    }
}
