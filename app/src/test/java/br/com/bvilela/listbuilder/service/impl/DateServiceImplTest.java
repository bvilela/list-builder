package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.builder.FileInputDataAudienceDtoBuilder;
import br.com.bvilela.listbuilder.builder.designacao.FileInputDataDesignacaoDtoBuilder;
import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import br.com.bvilela.listbuilder.dto.util.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DateServiceImplTest {

    @InjectMocks private DateServiceImpl service;

    /** CreateItemDate */
    private ItemDateDTO cid(int month, int day) {
        return new ItemDateDTO(LocalDate.of(2022, month, day));
    }

    /** CreateItemDate */
    private ItemDateDTO cid(int month, int day, String message) {
        return new ItemDateDTO(LocalDate.of(2022, month, day), message);
    }

    /** CreateItemDate */
    private List<ItemDateDTO> cid(int ordinal, int month1, int day1, int month2, int day2) {
        return List.of(
                new ItemDateDTO(ordinal, LocalDate.of(2022, month1, day1)),
                new ItemDateDTO(ordinal, LocalDate.of(2022, month2, day2)));
    }

    /** CreateItemDate */
    private List<ItemDateDTO> cid(
            int ordinal, int month1, int day1, String ex1, int month2, int day2, String ex2) {
        return List.of(
                new ItemDateDTO(ordinal, LocalDate.of(2022, month1, day1), ex1),
                new ItemDateDTO(ordinal, LocalDate.of(2022, month2, day2), ex2));
    }

    @Test
    void validGenerateListDatesCase1() {
        var expected =
                List.of(
                        cid(4, 2),
                        cid(4, 5),
                        cid(4, 9),
                        cid(4, 12),
                        cid(4, 16),
                        cid(4, 19),
                        cid(4, 23),
                        cid(4, 26),
                        cid(4, 30));
        var fileInputData = new ClearingInputDTO();
        fileInputData.setLastDate("29-03-2022");
        fileInputData.setMeetingDayMidweek("terca");
        fileInputData.setMeetingDayWeekend("sabado");
        var input = new DateServiceInputDTO(fileInputData, null, null);
        var dto = service.generateListDatesLimpeza(input, 1);
        assertEquals(expected, dto);
        Assertions.assertFalse(fileInputData.toString().isBlank());
        Assertions.assertFalse(input.toString().isBlank());
    }

    @Test
    void validGenerateListDatesCase2() {
        var expected =
                List.of(
                        cid(4, 2),
                        cid(4, 5),
                        cid(4, 9),
                        cid(4, 15, "exception1"),
                        cid(4, 16),
                        cid(4, 19),
                        cid(4, 23),
                        cid(4, 26),
                        cid(4, 30));
        var fileInputData = new ClearingInputDTO();
        fileInputData.setLastDate("29-03-2022");
        fileInputData.setMeetingDayMidweek("terca");
        fileInputData.setMeetingDayWeekend("sabado");
        var remove = List.of(LocalDate.of(2022, 4, 12));
        var add = Map.of(LocalDate.of(2022, 4, 15), "exception1");
        var input = new DateServiceInputDTO(fileInputData, remove, add);
        var dto = service.generateListDatesLimpeza(input, 1);
        assertEquals(expected, dto);
        Assertions.assertFalse(expected.get(1).toString().isBlank());
    }

    @Test
    void validGenerateListDatesTwoDateCase1() {
        var expected = new ArrayList<ItemDateDTO>();
        expected.addAll(cid(1, 4, 1, 4, 2));
        expected.addAll(cid(2, 4, 4, 4, 5));
        expected.addAll(cid(3, 4, 8, 4, 9));
        expected.addAll(cid(4, 4, 14, "exception1", 4, 15, "exception1"));
        expected.add(new ItemDateDTO(5, LocalDate.of(2022, 4, 16)));
        expected.addAll(cid(6, 4, 18, 4, 19));
        expected.addAll(cid(7, 4, 22, 4, 23));
        expected.addAll(cid(8, 4, 25, 4, 26));
        expected.addAll(cid(9, 4, 29, 4, 30));
        var fileInputData = new ClearingInputDTO();
        fileInputData.setLastDate("29-03-2022");
        fileInputData.setMeetingDayMidweek("terca");
        fileInputData.setMeetingDayWeekend("sabado");
        var remove = List.of(LocalDate.of(2022, 4, 12));
        var add = Map.of(LocalDate.of(2022, 4, 15), "exception1");
        var input = new DateServiceInputDTO(fileInputData, remove, add);
        var dto = service.generateListDatesLimpeza(input, 2);
        assertEquals(expected, dto);
    }

    @Test
    void generateListDatesAudienceLayoutFullStartMonth() {
        var dates =
                List.of(
                        "02/04", "05/04", "09/04", "12/04", "16/04", "19/04", "23/04", "26/04",
                        "30/04", "03/05", "07/05", "10/05", "14/05", "17/05", "21/05", "24/05",
                        "28/05", "31/05");
        var expected = TestUtils.createListLocalDates(dates, 2022);

        var fileInputDTO =
                new FileInputDataAudienceDtoBuilder()
                        .withLastDate("29-03-2022")
                        .withMeetingDayMidweek("terca")
                        .withMeetingDayWeekend("sabado")
                        .build();
        var dto = service.generateAudienceListDates(fileInputDTO, AudienceWriterLayoutEnum.FULL);
        assertEquals(expected, dto);
        Assertions.assertNotNull(fileInputDTO.toString());
    }

    @Test
    void generateListDatesAudienceLayoutFullMiddleMonth() {
        var dates =
                List.of(
                        "11/06", "14/06", "18/06", "21/06", "25/06", "28/06", "02/07", "05/07",
                        "09/07", "12/07", "16/07", "19/07", "23/07", "26/07", "30/07");
        var expected = TestUtils.createListLocalDates(dates, 2022);

        var fileInputDTO =
                new FileInputDataAudienceDtoBuilder()
                        .withLastDate("07-06-2022")
                        .withMeetingDayMidweek("terca")
                        .withMeetingDayWeekend("sabado")
                        .build();
        var dto = service.generateAudienceListDates(fileInputDTO, AudienceWriterLayoutEnum.FULL);
        assertEquals(expected, dto);
        Assertions.assertNotNull(fileInputDTO.toString());
    }

    @Test
    void generateListDatesAudienceLayoutCompact() {
        var dates =
                List.of(
                        "04/01", "08/01", "11/01", "15/01", "18/01", "22/01", "25/01", "29/01",
                        "01/02", "05/02", "08/02", "12/02", "15/02", "19/02", "22/02", "26/02",
                        "01/03", "05/03", "08/03", "12/03", "15/03", "19/03", "22/03", "26/03",
                        "29/03");
        var expected = TestUtils.createListLocalDates(dates, 2023);

        var fileInputDTO =
                new FileInputDataAudienceDtoBuilder()
                        .withLastDate("31-12-2022")
                        .withMeetingDayMidweek("quarta")
                        .withMeetingDayWeekend("domingo")
                        .build();
        var dto = service.generateAudienceListDates(fileInputDTO, AudienceWriterLayoutEnum.COMPACT);
        assertEquals(expected, dto);
        Assertions.assertNotNull(fileInputDTO.toString());
    }

    @Test
    void generateDesignationListDatesCase1() {
        var dates =
                List.of(
                        "01/11", "05/11", "08/11", "12/11", "15/11", "19/11", "22/11", "26/11",
                        "29/11", "03/12");
        var expected = TestUtils.createListLocalDates(dates, 2022);

        var fileInputDTO =
                new FileInputDataDesignacaoDtoBuilder()
                        .withLastDate("29-10-2022")
                        .withMeetingDayMidweek("terca")
                        .withMeetingDayWeekend("sabado")
                        .build();
        var list = service.generateDesignationListDates(fileInputDTO);
        assertEquals(expected, list);
        Assertions.assertNotNull(fileInputDTO.toString());
    }

    @Test
    void generateDesignationListDatesCase2() {
        var dates =
                List.of(
                        "02/11", "06/11", "09/11", "13/11", "16/11", "20/11", "23/11", "27/11",
                        "30/11", "04/12");
        var expected = TestUtils.createListLocalDates(dates, 2022);

        var fileInputDTO =
                new FileInputDataDesignacaoDtoBuilder()
                        .withLastDate("29-10-2022")
                        .withMeetingDayMidweek("quarta")
                        .withMeetingDayWeekend("domingo")
                        .build();
        var list = service.generateDesignationListDates(fileInputDTO);
        assertEquals(expected, list);
    }

    @Test
    void generateDesignationListDatesCase3() {
        var dates = List.of("05/10", "09/10", "12/10", "16/10", "19/10", "23/10", "26/10", "30/10");
        var expected = TestUtils.createListLocalDates(dates, 2022);

        var fileInputDTO =
                new FileInputDataDesignacaoDtoBuilder()
                        .withLastDate("02-10-2022")
                        .withMeetingDayMidweek("quarta")
                        .withMeetingDayWeekend("domingo")
                        .build();
        var list = service.generateDesignationListDates(fileInputDTO);
        assertEquals(expected, list);
    }

    @Test
    void getNextDateCase1() {
        var baseDate = LocalDate.of(2023, 1, 4);
        var expectedDate = LocalDate.of(2023, 1, 7);
        var midweekDay = DayOfWeekEnum.QUARTA;
        var weekendDay = DayOfWeekEnum.SABADO;
        var nextDate = service.getNextDate(midweekDay, weekendDay, baseDate);
        assertEquals(expectedDate, nextDate);
    }

    @Test
    void getNextDateCase2() {
        var baseDate = LocalDate.of(2023, 1, 1);
        var expectedDate = LocalDate.of(2023, 1, 5);
        var midweekDay = DayOfWeekEnum.QUINTA;
        var weekendDay = DayOfWeekEnum.DOMINGO;
        var nextDate = service.getNextDate(midweekDay, weekendDay, baseDate);
        assertEquals(expectedDate, nextDate);
    }

    @Test
    void getNextDateCase3() {
        var baseDate = LocalDate.of(2022, 12, 31);
        var expectedDate = LocalDate.of(2023, 1, 5);
        var midweekDay = DayOfWeekEnum.QUINTA;
        var weekendDay = DayOfWeekEnum.DOMINGO;
        var nextDate = service.getNextDate(midweekDay, weekendDay, baseDate);
        assertEquals(expectedDate, nextDate);
    }

    @Test
    void getNextDateCase4() {
        var baseDate = LocalDate.of(2022, 12, 29);
        var expectedDate = LocalDate.of(2023, 1, 1);
        var midweekDay = DayOfWeekEnum.QUINTA;
        var weekendDay = DayOfWeekEnum.DOMINGO;
        var nextDate = service.getNextDate(midweekDay, weekendDay, baseDate);
        assertEquals(expectedDate, nextDate);
    }
}
