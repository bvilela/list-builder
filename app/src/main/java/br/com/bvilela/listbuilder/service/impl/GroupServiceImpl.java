package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.dto.InputListDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import br.com.bvilela.listbuilder.enuns.DesignacaoEntityEnum;
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
            FileInputDataLimpezaDTO dto, List<ItemDateDTO> listDates, int layout) {
        if (layout == 2) {
            var newList =
                    listDates.stream().collect(Collectors.groupingBy(ItemDateDTO::getOrdinal));
            return generateListGroupsBase(dto, newList.size());
        } else {
            return generateListGroupsBase(dto, listDates.size());
        }
    }

    @SneakyThrows
    private List<String> generateListGroupsBase(FileInputDataLimpezaDTO dto, int numberOfDates) {

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
    public List<DesignacaoWriterItemDTO> generateListPresident(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesWeekend) {
        var entity = DesignacaoEntityEnum.PRESIDENT;
        try {
            return generateSequenceListDesignacao(dto.getPresident(), listDatesWeekend, entity);
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignacaoWriterItemDTO> generateListReaderWatchtower(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesWeekend) {
        var entity = DesignacaoEntityEnum.READER_WATCHTOWER;
        try {
            return generateSequenceListDesignacao(
                    dto.getReader().getWatchtower(), listDatesWeekend, entity);
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignacaoWriterItemDTO> generateListReaderBibleStudy(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesMidweek) {
        var entity = DesignacaoEntityEnum.READER_BIBLESTUDY;
        try {
            return generateSequenceListDesignacao(
                    dto.getReader().getBibleStudy(), listDatesMidweek, entity);
        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignacaoWriterItemDTO> generateListAudioVideo(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesAll) {

        var entity = DesignacaoEntityEnum.AUDIO_VIDEO;
        try {
            var list = generateSequenceListDesignacao(dto.getAudioVideo(), listDatesAll, entity);

            String lastHelper = null;
            SecureRandom rand = new SecureRandom();
            for (DesignacaoWriterItemDTO item : list) {
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
    public List<DesignacaoWriterItemDTO> generateListIndicator(
            FileInputDataDesignacaoDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists) {

        var entity = DesignacaoEntityEnum.INDICATOR;
        try {

            return generateListDesignacaoBaseIndicatorMicrophone(
                    dto.getIndicator(), listDatesAll, anotherLists);

        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @Override
    @SneakyThrows
    public List<DesignacaoWriterItemDTO> generateListMicrophone(
            FileInputDataDesignacaoDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists) {

        var entity = DesignacaoEntityEnum.MICROPHONE;
        try {

            return generateListDesignacaoBaseIndicatorMicrophone(
                    dto.getMicrophone(), listDatesAll, anotherLists);

        } catch (Exception e) {
            throw newListBuilderException(entity, e);
        }
    }

    @SneakyThrows
    private List<DesignacaoWriterItemDTO> generateSequenceListDesignacao(
            InputListDTO dto,
            List<LocalDate> listDates,
            DesignacaoEntityEnum entity) {

        var indexLast = dto.getList().indexOf(dto.getLast());
        if (indexLast < 0) {
            throw new ListBuilderException(MessageConfig.LAST_INVALID, entity.getLabel());
        }

        List<DesignacaoWriterItemDTO> listWriterItems = new ArrayList<>();
        for (LocalDate listDate : listDates) {
            indexLast = indexLast == dto.getList().size() - 1 ? 0 : ++indexLast;
            listWriterItems.add(
                    new DesignacaoWriterItemDTO(listDate, dto.getList().get(indexLast)));
        }

        return listWriterItems;
    }

    private List<DesignacaoWriterItemDTO> generateListDesignacaoBaseIndicatorMicrophone(
            List<String> listNames,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists) {

        int indexLast = 0;
        List<DesignacaoWriterItemDTO> listWriterItems = new ArrayList<>();

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
            listWriterItems.add(new DesignacaoWriterItemDTO(listDatesAll.get(i), names));
            indexLast = indexLast == listNames.size() - 1 ? 0 : ++indexLast;
        }
        return listWriterItems;
    }

    private ListBuilderException newListBuilderException(
            DesignacaoEntityEnum entity, Exception exception) {
        return new ListBuilderException("%s: %s", entity.getLabel(), exception.getMessage());
    }
}
