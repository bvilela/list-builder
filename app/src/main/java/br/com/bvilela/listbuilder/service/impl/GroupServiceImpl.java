package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import br.com.bvilela.listbuilder.enuns.DesignationEntityEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.GroupService;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Override
    @SneakyThrows
    public List<String> generateListGroupsLimpeza(
            ClearingInputDTO dto, List<ItemDateDTO> listDates, int layout) {
        if (layout == 2) {
            var newList =
                    listDates.stream().collect(Collectors.groupingBy(ItemDateDTO::getOrdinal));
            return generateListGroupsBase(dto, newList.size());
        } else {
            return generateListGroupsBase(dto, listDates.size());
        }
    }

    @SneakyThrows
    private List<String> generateListGroupsBase(ClearingInputDTO dto, int numberOfDates) {

        var mapGroups = dto.getGroups();
        if (Objects.isNull(mapGroups.get(dto.getLastGroup()))) {
            throw new ListBuilderException(MessageConfig.LAST_GROUP_INVALID);
        }

        var listGeneratedGroups = new ArrayList<String>();
        var indexGroup = dto.getLastGroup();
        for (int i = 0; i < numberOfDates; i++) {
            if (indexGroup.equals(mapGroups.size())) {
                indexGroup = 1;
            } else {
                indexGroup++;
            }
            listGeneratedGroups.add(mapGroups.get(indexGroup));
        }
        return listGeneratedGroups;
    }

    @Override
    @SneakyThrows
    public List<DesignationWriterItemDTO> generateListPresident(
            DesignationInputDTO dto, List<LocalDate> listDatesWeekend) {
        var entity = DesignationEntityEnum.PRESIDENT;
        try {
            return generateSequenceListDesignacao(dto.getPresident(), listDatesWeekend, entity);
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignationWriterItemDTO> generateListReaderWatchtower(
            DesignationInputDTO dto, List<LocalDate> listDatesWeekend) {
        var entity = DesignationEntityEnum.READER_WATCHTOWER;
        try {
            return generateSequenceListDesignacao(
                    dto.getReader().getWatchtower(), listDatesWeekend, entity);
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignationWriterItemDTO> generateListReaderBibleStudy(
            DesignationInputDTO dto, List<LocalDate> listDatesMidweek) {
        var entity = DesignationEntityEnum.READER_BIBLESTUDY;
        try {
            return generateSequenceListDesignacao(
                    dto.getReader().getBibleStudy(), listDatesMidweek, entity);
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignationWriterItemDTO> generateListAudioVideo(
            DesignationInputDTO dto, List<LocalDate> listDatesAll) {

        var entity = DesignationEntityEnum.AUDIO_VIDEO;
        try {
            var list = generateSequenceListDesignacao(dto.getAudioVideo(), listDatesAll, entity);

            String lastHelper = null;
            SecureRandom rand = new SecureRandom();
            for (DesignationWriterItemDTO item : list) {
                final String lastHelperFinal = lastHelper;
                var listPossibleNames =
                        dto.getAudioVideo().getList().stream()
                                .filter(
                                        e ->
                                                !e.equals(item.getName())
                                                        && !e.equals(lastHelperFinal))
                                .toList();
                var helper = listPossibleNames.get(rand.nextInt(listPossibleNames.size()));
                lastHelper = helper;
                item.setName(item.getName().concat(" e ").concat(helper));
            }
            return list;
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignationWriterItemDTO> generateListIndicator(
            DesignationInputDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignationWriterItemDTO> anotherLists) {

        var entity = DesignationEntityEnum.INDICATOR;
        try {

            return generateListDesignacaoBaseIndicatorMicrophone(
                    dto.getIndicator(), listDatesAll, anotherLists);

        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignationWriterItemDTO> generateListMicrophone(
            DesignationInputDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignationWriterItemDTO> anotherLists) {

        var entity = DesignationEntityEnum.MICROPHONE;
        try {

            return generateListDesignacaoBaseIndicatorMicrophone(
                    dto.getMicrophone(), listDatesAll, anotherLists);

        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @SneakyThrows
    private List<DesignationWriterItemDTO> generateSequenceListDesignacao(
            InputListDTO dto, List<LocalDate> listDates, DesignationEntityEnum entity) {

        var indexLast = dto.getList().indexOf(dto.getLast());
        if (indexLast < 0) {
            throw new ListBuilderException(MessageConfig.LAST_INVALID, entity.getLabel());
        }

        List<DesignationWriterItemDTO> listWriterItems = new ArrayList<>();
        for (LocalDate listDate : listDates) {
            indexLast = indexLast == dto.getList().size() - 1 ? 0 : ++indexLast;
            listWriterItems.add(
                    new DesignationWriterItemDTO(listDate, dto.getList().get(indexLast)));
        }

        return listWriterItems;
    }

    private List<DesignationWriterItemDTO> generateListDesignacaoBaseIndicatorMicrophone(
            List<String> listNames,
            List<LocalDate> listDatesAll,
            List<DesignationWriterItemDTO> anotherLists) {

        int indexLast = 0;
        List<DesignationWriterItemDTO> listWriterItems = new ArrayList<>();

        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < listDatesAll.size(); i++) {

            final int finalIndex = i;
            List<String> listNotPossibleNames =
                    anotherLists.stream()
                            .filter(e -> e.getDate().equals(listDatesAll.get(finalIndex)))
                            .map(e -> e.getName().split(" e ")[0])
                            .toList();
            var listPossibleNames =
                    listNames.stream().filter(e -> !listNotPossibleNames.contains(e)).toList();
            listPossibleNames = new ArrayList<>(listPossibleNames);
            var name1 = listPossibleNames.get(rand.nextInt(listPossibleNames.size()));
            listPossibleNames.remove(name1);
            var name2 = listPossibleNames.get(rand.nextInt(listPossibleNames.size()));
            var names = name1 + " e " + name2;
            listWriterItems.add(new DesignationWriterItemDTO(listDatesAll.get(i), names));
            indexLast = indexLast == listNames.size() - 1 ? 0 : ++indexLast;
        }
        return listWriterItems;
    }

    private ListBuilderException newListBuilderException(
            DesignationEntityEnum entity, Exception exception) {
        return new ListBuilderException("%s: %s", entity.getLabel(), exception.getMessage());
    }
}
