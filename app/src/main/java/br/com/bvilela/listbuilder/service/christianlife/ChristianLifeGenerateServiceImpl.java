package br.com.bvilela.listbuilder.service.christianlife;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.christianlife.extract.ChristianLifeExtractWeekItemDTO;
import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputDTO;
import br.com.bvilela.listbuilder.dto.christianlife.input.ChristianLifeInputRenameItemDTO;
import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.validator.ChristianLifeValidator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("VIDA_CRISTA")
@RequiredArgsConstructor
public class ChristianLifeGenerateServiceImpl implements BaseGenerateService {

    private Map<String, String> abbreviationMap;

    private final AppProperties properties;
    private final ChristianLifeExtractService extractService;
    private final ChristianLifeWriterService writerService;
    private final SendNotificationService notificationService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.VIDA_CRISTA;
    }

    @Override
    @SneakyThrows
    public void generateList() {
        try {
            logInit(log);

            var dto = getFileInputDataDTO(properties, ChristianLifeInputDTO.class);

            ChristianLifeValidator.validInputDto(dto);
            this.abbreviationMap = new LinkedHashMap<>(dto.getAbbreviations());

            var url = extractService.getUrlMeetingWorkbook(dto.getLastDateConverted());
            var listWeeks = extractService.extractWeeksBySite(url);

            listWeeks = adjustListByLastDate(listWeeks, dto);

            extractService.extractWeekItemsBySite(listWeeks);

            renameItems(listWeeks, dto.getRenameItems());

            populateExtractListWithParticipants(listWeeks, dto.getParticipants());

            writerService.writerPDF(listWeeks);

            notificationService.christianLife(listWeeks);

            logFinish(log);

        } catch (Exception e) {
            throw defaultListBuilderException(log, e);
        }
    }

    public List<ChristianLifeExtractWeekDTO> adjustListByLastDate(
            List<ChristianLifeExtractWeekDTO> listWeeks, ChristianLifeInputDTO dto) {
        var lastDate = dto.getLastDateConverted();

        var nextMonth = DateUtils.nextDayOfWeek(lastDate, DayOfWeek.MONDAY).plusMonths(1);
        nextMonth = nextMonth.withDayOfMonth(1);
        var newListWeeks = new ArrayList<ChristianLifeExtractWeekDTO>();
        for (ChristianLifeExtractWeekDTO item : listWeeks) {
            if (item.getInitialDate().isAfter(lastDate)
                    && item.getInitialDate().isBefore(nextMonth)) {
                newListWeeks.add(item);
            }
        }

        var mapRemove = dto.getRemoveWeekFromListConverted();
        removeWeeksIfNecessaryByInputDTO(newListWeeks, mapRemove);

        var dates = newListWeeks.stream().map(ChristianLifeExtractWeekDTO::getLabelDate).toList();
        log.info("Datas: {}", dates);
        return newListWeeks;
    }

    public void removeWeeksIfNecessaryByInputDTO(
            List<ChristianLifeExtractWeekDTO> listWeeks, Map<LocalDate, String> mapRemove) {
        if (mapRemove == null) {
            return;
        }

        for (ChristianLifeExtractWeekDTO week : listWeeks) {
            for (Entry<LocalDate, String> entry : mapRemove.entrySet()) {
                var dateToRemove = entry.getKey();
                if (!dateToRemove.isBefore(week.getInitialDate())
                        && !dateToRemove.isAfter(week.getEndDate())) {
                    week.setSkip(true);
                    week.setSkipMessage(entry.getValue());
                }
            }
        }
    }

    @SneakyThrows
    private void populateExtractListWithParticipants(
            List<ChristianLifeExtractWeekDTO> listWeeks, List<List<String>> listParticipants) {

        if (listWeeks.size() != listParticipants.size()) {
            throw new ListBuilderException(
                    "Quantidade de semanas extraída do site é diferente da quantidade de semanas com participantes");
        }

        int weekIndex = 0;
        for (ChristianLifeExtractWeekDTO week : listWeeks) {
            if (week.isSkip()) {
                weekIndex++;
                continue;
            }
            log.info("Adicionando Participantes da Semana {}", week.getLabelDate());
            var participantsOfWeek = listParticipants.get(weekIndex);
            addParticipantsOfWeek(week, participantsOfWeek);
            weekIndex++;
        }
    }

    private void addParticipantsOfWeek(
            ChristianLifeExtractWeekDTO week, List<String> participantsOfWeek) {
        int participantsIndex = 0;

        for (ChristianLifeExtractWeekItemDTO item : week.getItems()) {

            if (item.getType().isHasParticipants()) {
                String participant = participantsOfWeek.get(participantsIndex);
                item.setParticipants(List.of(getAbbreviation(participant)));
                participantsIndex++;
            }
        }
    }

    private String getAbbreviation(String abbreviate) {
        var name = this.abbreviationMap.get(abbreviate);
        return name != null && !name.isBlank() ? name : abbreviate;
    }

    private void renameItems(
            List<ChristianLifeExtractWeekDTO> listWeeks,
            List<ChristianLifeInputRenameItemDTO> renameItems) {

        if (Objects.isNull(renameItems) || renameItems.isEmpty()) {
            return;
        }

        int weekIndex = 0;
        for (ChristianLifeExtractWeekDTO week : listWeeks) {
            if (week.isSkip()) {
                weekIndex++;
                continue;
            }

            checkRenameItemFromWeek(week, renameItems, weekIndex);
            weekIndex++;
        }
    }

    public void checkRenameItemFromWeek(
            ChristianLifeExtractWeekDTO week,
            List<ChristianLifeInputRenameItemDTO> renameItems,
            final int weekIndex) {

        var renameItemsThisWeek =
                renameItems.stream().filter(e -> e.getWeekIndex() - 1 == weekIndex).toList();
        if (renameItemsThisWeek.isEmpty()) {
            return;
        }

        var itemsCanRenamed =
                week.getItems().stream()
                        .filter(
                                e ->
                                        e.getType()
                                                == ChristianLifeExtractItemTypeEnum
                                                        .WITH_PARTICIPANTS)
                        .toList();

        var listWeekItemToRemove = new ArrayList<ChristianLifeExtractWeekItemDTO>();
        for (ChristianLifeExtractWeekItemDTO item : itemsCanRenamed) {
            for (ChristianLifeInputRenameItemDTO renameItem : renameItemsThisWeek) {

                if (item.getTitle().equalsIgnoreCase(renameItem.getOriginalName())) {
                    processRenameOrRemoveItem(item, renameItem, listWeekItemToRemove);
                }
            }
        }
        if (!listWeekItemToRemove.isEmpty()) {
            var auxList = new ArrayList<>(week.getItems());
            auxList.removeAll(listWeekItemToRemove);
            week.setItems(auxList);
        }
    }

    public void processRenameOrRemoveItem(
            ChristianLifeExtractWeekItemDTO item,
            ChristianLifeInputRenameItemDTO renameItem,
            List<ChristianLifeExtractWeekItemDTO> listWeekItemToRemove) {
        var newName = renameItem.getNewName();
        if (Objects.isNull(newName) || newName.isBlank()) {
            listWeekItemToRemove.add(item);
        } else {
            item.setTitle(newName);
        }
    }
}
