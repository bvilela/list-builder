package br.com.bvilela.listbuilder.service.vidacrista;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import java.time.LocalDate;
import java.util.List;

public interface VidaCristaExtractService {

    String getUrlMeetingWorkbook(LocalDate lastDate);

    List<VidaCristaExtractWeekDTO> extractWeeksBySite(String url);

    void extractWeekItemsBySite(List<VidaCristaExtractWeekDTO> list);
}
