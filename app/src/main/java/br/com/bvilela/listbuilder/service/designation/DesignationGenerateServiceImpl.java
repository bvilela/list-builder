package br.com.bvilela.listbuilder.service.designation;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.GroupService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.util.DateUtils;
import br.com.bvilela.listbuilder.util.FileUtils;
import br.com.bvilela.listbuilder.validator.DesignationValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service("DESIGNACAO")
@RequiredArgsConstructor
public class DesignationGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;

    private final DateService dateService;
    private final GroupService groupService;
    private final DesignationWriterService writerService;
    private final DesignationCounterService counterService;
    private final SendNotificationService notificationService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.DESIGNACAO;
    }

    @Override
    public void generateList() {
        try {
            log.info(logInitMessage());

            var dto = getFileInputDataDTO(properties, DesignationInputDTO.class);

            dto.validate();

            var listAllDates = dateService.generateDesignationListDates(dto);

            var listDatesMidweek =
                    DateUtils.extractDateByDayOfWeek(listAllDates, dto.getMeetingDayMidweekEnum());
            var listDatesWeekend =
                    DateUtils.extractDateByDayOfWeek(listAllDates, dto.getMeetingDayWeekendEnum());

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

            var dtoWriter = new DesignationWriterDTO();
            dtoWriter.setPresident(listPresident);
            dtoWriter.setReaderWatchtower(listReaderWatchtower);
            dtoWriter.setReaderBibleStudy(listReaderBibleStudy);
            dtoWriter.setAudioVideo(listAudioVideo);
            dtoWriter.setIndicator(listIndicator);
            dtoWriter.setMicrophone(listMicrophone);

            DesignationValidator.checkDtoWriter(dtoWriter);

            var pathDocx = writerService.writerDocx(dtoWriter);

            var fileSB = counterService.countNumberActiviesByName(dtoWriter);

            var newFile = new File(pathDocx.toString().replace(".docx", ".txt"));
            FileUtils.writeTxtFile(newFile, fileSB);

            notificationService.designation(dtoWriter);

            log.info(logFinishMessage());

        } catch (Exception ex) {
            log.error(logErrorMessage(ex));
            throw ex;
        }
    }

    @SneakyThrows
    private void adjustListReadersByPresident(
            List<DesignationWriterItemDTO> listReaderWatchtower,
            List<DesignationWriterItemDTO> listPresident) {

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
            List<DesignationWriterItemDTO> listPresident,
            List<DesignationWriterItemDTO> listReaderWatchtower) {
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
            List<DesignationWriterItemDTO> listAudioVideo,
            List<DesignationWriterItemDTO> listReaderWatchtower,
            List<DesignationWriterItemDTO> listReaderBibleStudy) {

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
            List<DesignationWriterItemDTO> listReaderWatchtower,
            List<DesignationWriterItemDTO> listReaderBibleStudy,
            List<DesignationWriterItemDTO> listAudioVideo) {

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

    private void changeName(List<DesignationWriterItemDTO> list, int index) {
        try {
            var currentName = list.get(index).getName();
            list.get(index).setName(list.get(index + 1).getName());
            list.get(index + 1).setName(currentName);

        } catch (IndexOutOfBoundsException e) {
            list.get(index).setName(list.get(0).getName());
        }
    }

    private List<DesignationWriterItemDTO> getListPresidentAndReader(
            List<DesignationWriterItemDTO> listPresidents,
            List<DesignationWriterItemDTO> listReaderWatchtower,
            List<DesignationWriterItemDTO> listReaderBibleStudy) {
        List<DesignationWriterItemDTO> list =
                Stream.of(listPresidents, listReaderWatchtower, listReaderBibleStudy)
                        .flatMap(Collection::stream)
                        .toList();
        list = new ArrayList<>(list);
        return list;
    }

    private List<DesignationWriterItemDTO> addAudioVideo(
            List<DesignationWriterItemDTO> listBase,
            List<DesignationWriterItemDTO> listAudioVideo) {

        for (DesignationWriterItemDTO item : listAudioVideo) {
            var names = item.getName().split(" e ");
            listBase.add(new DesignationWriterItemDTO(item.getDate(), names[0]));
        }
        return listBase;
    }

    private List<DesignationWriterItemDTO> addIndicator(
            List<DesignationWriterItemDTO> listBase, List<DesignationWriterItemDTO> listIndicator) {

        for (DesignationWriterItemDTO item : listIndicator) {
            var names = item.getName().split(" e ");
            listBase.add(new DesignationWriterItemDTO(item.getDate(), names[0]));
            listBase.add(new DesignationWriterItemDTO(item.getDate(), names[1]));
        }
        return listBase;
    }
}
