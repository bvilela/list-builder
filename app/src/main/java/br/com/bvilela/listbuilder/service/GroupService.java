package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import java.time.LocalDate;
import java.util.List;

public interface GroupService {

    List<String> generateListGroupsLimpeza(
            FileInputDataLimpezaDTO dto, List<ItemDateDTO> listDates, int layout);

    List<DesignacaoWriterItemDTO> generateListPresident(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesWeekend);

    List<DesignacaoWriterItemDTO> generateListReaderWatchtower(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesWeekend);

    List<DesignacaoWriterItemDTO> generateListReaderBibleStudy(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesMidweek);

    List<DesignacaoWriterItemDTO> generateListAudioVideo(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesAll);

    List<DesignacaoWriterItemDTO> generateListIndicator(
            FileInputDataDesignacaoDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists);

    List<DesignacaoWriterItemDTO> generateListMicrophone(
            FileInputDataDesignacaoDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists);
}
