package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import java.time.LocalDate;
import java.util.List;

public interface GroupService {

    List<String> generateListGroupsLimpeza(
            ClearingInputDTO dto, List<ItemDateDTO> listDates, int layout);

    List<DesignationWriterItemDTO> generateListPresident(
            DesignationInputDTO dto, List<LocalDate> listDatesWeekend);

    List<DesignationWriterItemDTO> generateListReaderWatchtower(
            DesignationInputDTO dto, List<LocalDate> listDatesWeekend);

    List<DesignationWriterItemDTO> generateListReaderBibleStudy(
            DesignationInputDTO dto, List<LocalDate> listDatesMidweek);

    List<DesignationWriterItemDTO> generateListAudioVideo(
            DesignationInputDTO dto, List<LocalDate> listDatesAll);

    List<DesignationWriterItemDTO> generateListIndicator(
            DesignationInputDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignationWriterItemDTO> anotherLists);

    List<DesignationWriterItemDTO> generateListMicrophone(
            DesignationInputDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignationWriterItemDTO> anotherLists);
}
