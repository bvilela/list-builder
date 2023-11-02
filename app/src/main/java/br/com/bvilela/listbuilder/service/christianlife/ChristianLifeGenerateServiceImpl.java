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
import br.com.bvilela.listbuilder.util.ChristianLifeUtils;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.validator.ChristianLifeValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    public void generateList() {
        try {
            log.info(logInitMessage());

            var dto = getFileInputDataDTO(properties, ChristianLifeInputDTO.class);

            ChristianLifeValidator.validInputDto(dto);
            this.abbreviationMap = new LinkedHashMap<>(dto.getAbbreviations());

            var url = extractService.getUrlMeetingWorkbook(dto.getLastDateConverted());
            var listWeeks = extractService.extractWeeksBySite(url);

            listWeeks = adjustListByLastDate(listWeeks, dto);

            extractService.extractWeekItemsBySite(listWeeks);

            renameItems(listWeeks, dto.getRenameItems());

            includeBibleStudyReader(listWeeks);

            populateExtractListWithParticipants(listWeeks, dto.getParticipants());

            writerService.writerPDF(listWeeks);

            notificationService.christianLife(listWeeks);

            log.info(logFinishMessage());

        } catch (Exception ex) {
            log.error(logErrorMessage(ex));
            throw ex;
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

        var participantsIterator = participantsOfWeek.iterator();

        week.getItems().stream()
                .filter(i -> i.getType().isHasParticipants())
                .forEach(i -> setParticipantsInVidaCristaWeekItem(i, participantsIterator));
    }

    @SneakyThrows
    private void setParticipantsInVidaCristaWeekItem(
            ChristianLifeExtractWeekItemDTO item, Iterator<String> participantsIterator) {

        try {
            String participant = getAbbreviation(participantsIterator.next());
            log.debug("Item {}: {}", item.getTitle(), participant);
            item.setParticipants(List.of(participant));
        } catch (NoSuchElementException e) {
            throw new ListBuilderException(
                    "Quantidade de Participantes Informada no Arquivo de Entrada MENOR "
                            + "que o necessário para a semana");
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

    private void includeBibleStudyReader(List<ChristianLifeExtractWeekDTO> listWeeks) {
        if (!properties.isChristianlifeAddBibleStudyReader()) {
            return;
        }
        listWeeks.forEach(this::includeBibleStudyReaderInWeekDto);
    }

    private void includeBibleStudyReaderInWeekDto(ChristianLifeExtractWeekDTO week) {
        if (week.isSkip()) {
            return;
        }

        var bibleStudyOptional =
                week.getItems().stream().filter(ChristianLifeUtils::isBibleStudy).findFirst();

        bibleStudyOptional.ifPresent(
                bibleStudy -> {
                    var bibleStudyIndex = week.getItems().indexOf(bibleStudy);
                    var item =
                            new ChristianLifeExtractWeekItemDTO(
                                    "Leitor", ChristianLifeExtractItemTypeEnum.BIBLE_STUDY_READER);
                    week.getItems().add(bibleStudyIndex + 1, item);
                });
    }
}
