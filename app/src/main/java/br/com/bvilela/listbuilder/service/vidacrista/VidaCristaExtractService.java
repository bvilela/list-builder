package br.com.bvilela.listbuilder.service.vidacrista;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface VidaCristaExtractService {

	String getUrlMeetingWorkbook(LocalDate lastDate);

	List<VidaCristaExtractWeekDTO> extractWeeksBySite(String url) throws IOException, ListBuilderException;

	void extractWeekItemsBySite(List<VidaCristaExtractWeekDTO> list) throws IOException, ListBuilderException;

}
