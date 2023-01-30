package com.bruno.listbuilder.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bvilela.lib.exception.GoogleCalendarLibException;

public interface NotificationService {
	
	public void limpeza(FinalListLimpezaDTO dto, int idLayout) throws IOException, GoogleCalendarLibException, ListBuilderException;
	
	public void assistencia(List<LocalDate> list) throws IOException, GoogleCalendarLibException, ListBuilderException;

	public void vidaCrista(List<VidaCristaExtractWeekDTO> listWeeks) throws IOException, GoogleCalendarLibException, ListBuilderException;

	public void designacao(DesignacaoWriterDTO dtoWriter) throws IOException, GoogleCalendarLibException, ListBuilderException;
	
}
