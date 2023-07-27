package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.utils.DateUtils;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class DateServiceImpl implements DateService {

    @Override
    public List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout) {
        var list = generateListDates(dto, 1);

        if (layout == 2) {
            List<ItemDateDTO> newList = new ArrayList<>();
            int ordinal = 1;
            for (ItemDateDTO e : list) {
                var before = new ItemDateDTO(ordinal, e.getDate().minusDays(1), e.getMessage());
                if (newList.stream().filter(e2 -> e2.getDate().isEqual(before.getDate())).count()
                        == 0) {
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
    @SneakyThrows
    public List<LocalDate> generateAudienceListDates(
            FileInputDataAudienceDTO fileInputData, AudienceWriterLayoutEnum layoutEnum) {

        var dto = new DateServiceInputDTO(fileInputData);
        var list = generateListDates(dto, layoutEnum.getNumberOfMonth());

        return list.stream().map(ItemDateDTO::getDate).toList();
    }

    @Override
    public List<LocalDate> generateDesignationListDates(FileInputDataDesignacaoDTO fileInputData) {

        var dto = new DateServiceInputDTO(fileInputData);
        var listDates = generateListDates(dto, 1);

        if (lastDateIsMidweek(listDates, dto)) {
            listDates = addWeekendDate(listDates, dto);
        }

        return listDates.stream().map(ItemDateDTO::getDate).toList();
    }

    private boolean lastDateIsMidweek(List<ItemDateDTO> listDates, DateServiceInputDTO dto) {
        var lastDate = getLastDate(listDates);
        return lastDate.getDayOfWeek() == dto.getMidweekDayWeekEnum().getDayOfWeek();
    }

    private List<ItemDateDTO> addWeekendDate(List<ItemDateDTO> listDates, DateServiceInputDTO dto) {
        var lastDate = getLastDate(listDates);
        var additionalDate =
                DateUtils.nextDayOfWeek(lastDate, dto.getWeekendDayWeekEnum().getDayOfWeek());

        listDates = new ArrayList<>(listDates);
        listDates.add(new ItemDateDTO(additionalDate));
        return listDates;
    }

    private static LocalDate getLastDate(List<ItemDateDTO> listDates) {
        return listDates.get(listDates.size() - 1).getDate();
    }

    private List<ItemDateDTO> generateListDates(
            @NotNull DateServiceInputDTO dto, @NotNull @Min(1) int numberOfMonths) {
        var listDates = new LinkedHashSet<ItemDateDTO>();
        var currencyDate =
                getNextDate(
                        dto.getMidweekDayWeekEnum(),
                        dto.getWeekendDayWeekEnum(),
                        dto.getLastDate());
        var lastDateToGenerate = getLastDateToGenerate(dto, currencyDate, numberOfMonths);

        while (!currencyDate.isAfter(lastDateToGenerate)) {
            listDates.add(new ItemDateDTO(currencyDate));
            currencyDate =
                    getNextDate(
                            dto.getMidweekDayWeekEnum(), dto.getWeekendDayWeekEnum(), currencyDate);
        }

        addDatesExceptions(dto, listDates);
        removeDatesExceptions(dto, listDates);

        return listDates.stream().sorted().toList();
    }

    private LocalDate getLastDateToGenerate(
            DateServiceInputDTO dto, LocalDate currencyDate, int numberOfMonths) {
        if (lastDateIsInInitOfCurrencyMonth(dto, currencyDate)) {
            numberOfMonths--;
        }
        return dto.getLastDate()
                .plusMonths(numberOfMonths)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    private boolean lastDateIsInInitOfCurrencyMonth(
            DateServiceInputDTO dto, LocalDate currencyDate) {
        return currencyDate.getMonth().equals(dto.getLastDate().getMonth());
    }

    public LocalDate getNextDate(
            DayOfWeekEnum midweekEnum, DayOfWeekEnum weekendEnum, LocalDate baseDate) {
        if (baseDateIsWeekend(baseDate, midweekEnum)) {
            return DateUtils.nextDayOfWeek(baseDate, midweekEnum.getDayOfWeek());
        }
        return DateUtils.nextDayOfWeek(baseDate, weekendEnum.getDayOfWeek());
    }

    private static boolean baseDateIsWeekend(LocalDate baseDate, DayOfWeekEnum midweekEnum) {
        return baseDate.getDayOfWeek().compareTo(midweekEnum.getDayOfWeek()) > 0;
    }

    private void addDatesExceptions(DateServiceInputDTO dto, Set<ItemDateDTO> listDates) {
        var datesToAdd = getDatesToAdd(dto);
        listDates.addAll(datesToAdd);
    }

    private List<ItemDateDTO> getDatesToAdd(DateServiceInputDTO dto) {
        if (dto.getAddToList() == null) {
            return List.of();
        }
        return dto.getAddToList().entrySet().stream()
                .map(entry -> new ItemDateDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void removeDatesExceptions(DateServiceInputDTO dto, Set<ItemDateDTO> listDates) {
        var datesToRemove = getDatesToRemove(dto);
        datesToRemove.forEach(listDates::remove);
    }

    private List<ItemDateDTO> getDatesToRemove(DateServiceInputDTO dto) {
        if (dto.getRemoveFromList() == null) {
            return List.of();
        }
        return dto.getRemoveFromList().stream().map(ItemDateDTO::new).toList();
    }
}
