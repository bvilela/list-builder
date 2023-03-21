package com.bruno.listbuilder.service;

import java.time.LocalDate;
import java.util.List;

import com.bruno.listbuilder.dto.ItemDateDTO;
import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import com.bruno.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface GroupService {

	List<String> generateListGroupsLimpeza(FileInputDataLimpezaDTO dto, List<ItemDateDTO> listDates, int layout)
			throws ListBuilderException;

	List<DesignacaoWriterItemDTO> generateListPresident(FileInputDataDesignacaoDTO dto,
			List<LocalDate> listDatesWeekend) throws ListBuilderException;

	List<DesignacaoWriterItemDTO> generateListReaderWatchtower(FileInputDataDesignacaoDTO dto,
			List<LocalDate> listDatesWeekend) throws ListBuilderException;

	List<DesignacaoWriterItemDTO> generateListReaderBibleStudy(FileInputDataDesignacaoDTO dto,
			List<LocalDate> listDatesMidweek) throws ListBuilderException;

	List<DesignacaoWriterItemDTO> generateListAudioVideo(FileInputDataDesignacaoDTO dto,
			List<LocalDate> listDatesAll) throws ListBuilderException;

	List<DesignacaoWriterItemDTO> generateListIndicator(FileInputDataDesignacaoDTO dto,
			List<LocalDate> listDatesAll, List<DesignacaoWriterItemDTO> anotherLists) throws ListBuilderException;

	List<DesignacaoWriterItemDTO> generateListMicrophone(FileInputDataDesignacaoDTO dto,
			List<LocalDate> listDatesAll, List<DesignacaoWriterItemDTO> anotherLists) throws ListBuilderException;
	
}
