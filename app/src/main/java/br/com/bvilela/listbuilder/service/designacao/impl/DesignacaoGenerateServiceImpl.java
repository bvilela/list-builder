package br.com.bvilela.listbuilder.service.designacao.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.GroupService;
import br.com.bvilela.listbuilder.service.designacao.DesignacaoCounterService;
import br.com.bvilela.listbuilder.service.designacao.DesignacaoWriterService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.utils.DateUtils;
import br.com.bvilela.listbuilder.utils.FileUtils;
import br.com.bvilela.listbuilder.validator.DesignacaoValidator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("DESIGNACAO")
@RequiredArgsConstructor
public class DesignacaoGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;

    private final DateService dateService;
    private final GroupService groupService;
    private final DesignacaoWriterService writerService;
    private final DesignacaoCounterService counterService;
    private final SendNotificationService notificationService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.DESIGNACAO;
    }

    @Override
    @SneakyThrows
    public void generateList() {
        try {
            logInit(log);

            var dto = getFileInputDataDTO(properties, FileInputDataDesignacaoDTO.class);

            DesignacaoValidator.validAndConvertData(dto);

            var dateServiceInputDTO = new DateServiceInputDTO(dto);
            var listAllDates = dateService.generateListDatesDesignacao(dateServiceInputDTO);

            if (listAllDates.isEmpty()) {
                throw new ListBuilderException(MessageConfig.LIST_DATE_EMPTY);
            }

            var listDatesMidweek =
                    DateUtils.extractDateByDayOfWeek(
                            listAllDates, dateServiceInputDTO.getMidweekDayWeekEnum());
            var listDatesWeekend =
                    DateUtils.extractDateByDayOfWeek(
                            listAllDates, dateServiceInputDTO.getWeekendDayWeekEnum());

            var listPresident = groupService.generateListPresident(dto, listDatesWeekend);
            var listReaderWatchtower =
                    groupService.generateListReaderWatchtower(dto, listDatesWeekend);
            var listReaderBibleStudy =
                    groupService.generateListReaderBibleStudy(dto, listDatesMidweek);
            var listAudioVideo = groupService.generateListAudioVideo(dto, listAllDates);

            var listPresidentAndReader =
                    getListPresidentAndReader(
                            listPresident, listReaderWatchtower, listReaderBibleStudy);

            listPresidentAndReader = addAudioVideo(listPresidentAndReader, listAudioVideo);
            var listIndicator =
                    groupService.generateListIndicator(dto, listAllDates, listPresidentAndReader);

            listPresidentAndReader = addIndicator(listPresidentAndReader, listIndicator);
            var listMicrophone =
                    groupService.generateListMicrophone(dto, listAllDates, listPresidentAndReader);

            adjustListReadersByPresident(listReaderWatchtower, listPresident);
            adjustListAudioVideoByReaders(
                    listAudioVideo, listReaderWatchtower, listReaderBibleStudy);

            var dtoWriter = new DesignacaoWriterDTO();
            dtoWriter.setPresident(listPresident);
            dtoWriter.setReaderWatchtower(listReaderWatchtower);
            dtoWriter.setReaderBibleStudy(listReaderBibleStudy);
            dtoWriter.setAudioVideo(listAudioVideo);
            dtoWriter.setIndicator(listIndicator);
            dtoWriter.setMicrophone(listMicrophone);

            DesignacaoValidator.checkDtoWriter(dtoWriter);

            var pathDocx = writerService.writerDocx(dtoWriter);

            var fileSB = counterService.countNumberActiviesByName(dtoWriter);

            var newFile = new File(pathDocx.toString().replace(".docx", ".txt"));
            FileUtils.writeTxtFile(newFile, fileSB);

            notificationService.designacao(dtoWriter);

            logFinish(log);

        } catch (Exception e) {
            throw defaultListBuilderException(e);
        }
    }

    @SneakyThrows
    private void adjustListReadersByPresident(
            List<DesignacaoWriterItemDTO> listReaderWatchtower,
            List<DesignacaoWriterItemDTO> listPresident) {

        final String logMessage = "Lista Leitores A Sentinela baseada na lista de Presidentes";
        try {
            log.info("Ajustando {}", logMessage);
            executeAdjustListReadersByPresident(listPresident, listReaderWatchtower);
            log.info("Lista Leitores A Sentinela ajustada com sucesso!");

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao ajustar %s", logMessage);
        }
    }

    private void executeAdjustListReadersByPresident(
            List<DesignacaoWriterItemDTO> listPresident,
            List<DesignacaoWriterItemDTO> listReaderWatchtower) {
        for (int i = 0; i < listReaderWatchtower.size(); i++) {
            var readerToValid = listReaderWatchtower.get(i);

            var namePresidentThisDate =
                    listPresident.stream()
                            .filter(e -> e.getDate().equals(readerToValid.getDate()))
                            .findFirst();
            if (namePresidentThisDate.isPresent()
                    && readerToValid
                            .getName()
                            .equalsIgnoreCase(namePresidentThisDate.get().getName())) {
                changeName(listReaderWatchtower, i);
            }
        }
    }

    @SneakyThrows
    private void adjustListAudioVideoByReaders(
            List<DesignacaoWriterItemDTO> listAudioVideo,
            List<DesignacaoWriterItemDTO> listReaderWatchtower,
            List<DesignacaoWriterItemDTO> listReaderBibleStudy) {

        final String logMessage = "Lista Lista AudioVideo baseada na lista de Leitores";
        try {
            log.info("Ajustando {}", logMessage);
            executeAdjustListAudioVideoByReaders(
                    listReaderWatchtower, listReaderBibleStudy, listAudioVideo);
            log.info("Lista AudioVideo ajustada com sucesso!");

        } catch (Exception e) {
            throw new ListBuilderException("Erro ao ajustar %s", logMessage);
        }
    }

    private void executeAdjustListAudioVideoByReaders(
            List<DesignacaoWriterItemDTO> listReaderWatchtower,
            List<DesignacaoWriterItemDTO> listReaderBibleStudy,
            List<DesignacaoWriterItemDTO> listAudioVideo) {

        var listAllReaders =
                Stream.concat(listReaderWatchtower.stream(), listReaderBibleStudy.stream())
                        .toList();
        for (int i = 0; i < listAudioVideo.size(); i++) {
            var audioVideoToValid = listAudioVideo.get(i);

            var nameReaderThisDate =
                    listAllReaders.stream()
                            .filter(e -> e.getDate().equals(audioVideoToValid.getDate()))
                            .findFirst();
            var nameAudioVideoPrincipal = audioVideoToValid.getName().split(" e ")[0];

            if (nameReaderThisDate.isPresent()
                    && nameAudioVideoPrincipal.equalsIgnoreCase(
                            nameReaderThisDate.get().getName())) {
                changeName(listAudioVideo, i);
            }
        }
    }

    private void changeName(List<DesignacaoWriterItemDTO> list, int index) {
        try {
            var currentName = list.get(index).getName();
            list.get(index).setName(list.get(index + 1).getName());
            list.get(index + 1).setName(currentName);

        } catch (IndexOutOfBoundsException e) {
            list.get(index).setName(list.get(0).getName());
        }
    }

    private List<DesignacaoWriterItemDTO> getListPresidentAndReader(
            List<DesignacaoWriterItemDTO> listPresidents,
            List<DesignacaoWriterItemDTO> listReaderWatchtower,
            List<DesignacaoWriterItemDTO> listReaderBibleStudy) {
        List<DesignacaoWriterItemDTO> list =
                Stream.of(listPresidents, listReaderWatchtower, listReaderBibleStudy)
                        .flatMap(Collection::stream)
                        .toList();
        list = new ArrayList<>(list);
        return list;
    }

    private List<DesignacaoWriterItemDTO> addAudioVideo(
            List<DesignacaoWriterItemDTO> listBase, List<DesignacaoWriterItemDTO> listAudioVideo) {

        for (DesignacaoWriterItemDTO item : listAudioVideo) {
            var names = item.getName().split(" e ");
            listBase.add(new DesignacaoWriterItemDTO(item.getDate(), names[0]));
        }
        return listBase;
    }

    private List<DesignacaoWriterItemDTO> addIndicator(
            List<DesignacaoWriterItemDTO> listBase, List<DesignacaoWriterItemDTO> listIndicator) {

        for (DesignacaoWriterItemDTO item : listIndicator) {
            var names = item.getName().split(" e ");
            listBase.add(new DesignacaoWriterItemDTO(item.getDate(), names[0]));
            listBase.add(new DesignacaoWriterItemDTO(item.getDate(), names[1]));
        }
        return listBase;
    }
}
