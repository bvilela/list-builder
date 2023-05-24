package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.LocalDate;
import java.util.List;

public interface GroupService {

    List<String> generateListGroupsLimpeza(
            FileInputDataLimpezaDTO dto, List<ItemDateDTO> listDates, int layout)
            throws ListBuilderException;

    List<DesignacaoWriterItemDTO> generateListPresident(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesWeekend)
            throws ListBuilderException;

    List<DesignacaoWriterItemDTO> generateListReaderWatchtower(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesWeekend)
            throws ListBuilderException;

    List<DesignacaoWriterItemDTO> generateListReaderBibleStudy(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesMidweek)
            throws ListBuilderException;

    List<DesignacaoWriterItemDTO> generateListAudioVideo(
            FileInputDataDesignacaoDTO dto, List<LocalDate> listDatesAll)
            throws ListBuilderException;

    List<DesignacaoWriterItemDTO> generateListIndicator(
            FileInputDataDesignacaoDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists)
            throws ListBuilderException;

    List<DesignacaoWriterItemDTO> generateListMicrophone(
            FileInputDataDesignacaoDTO dto,
            List<LocalDate> listDatesAll,
            List<DesignacaoWriterItemDTO> anotherLists)
            throws ListBuilderException;
}
