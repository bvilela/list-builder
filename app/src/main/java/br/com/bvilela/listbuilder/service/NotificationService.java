package br.com.bvilela.listbuilder.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import com.bvilela.lib.exception.GoogleCalendarLibException;

public interface NotificationService {
	
	public void limpeza(FinalListLimpezaDTO dto, int idLayout) throws IOException, GoogleCalendarLibException, ListBuilderException;
	
	public void assistencia(List<LocalDate> list) throws IOException, GoogleCalendarLibException, ListBuilderException;

	public void vidaCrista(List<VidaCristaExtractWeekDTO> listWeeks) throws IOException, GoogleCalendarLibException, ListBuilderException;

	public void designacao(DesignacaoWriterDTO dtoWriter) throws IOException, GoogleCalendarLibException, ListBuilderException;
	
}
