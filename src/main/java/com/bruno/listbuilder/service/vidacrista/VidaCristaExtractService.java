package com.bruno.listbuilder.service.vidacrista;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface VidaCristaExtractService {

	String getUrlMeetingWorkbook(LocalDate lastDate);

	List<VidaCristaExtractWeekDTO> extractWeeksBySite(String url) throws IOException, ListBuilderException;

	void extractWeekItemsBySite(List<VidaCristaExtractWeekDTO> list) throws IOException, ListBuilderException;

}
