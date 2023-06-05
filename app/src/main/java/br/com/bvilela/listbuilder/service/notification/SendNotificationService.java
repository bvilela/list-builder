package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import java.time.LocalDate;
import java.util.List;

public interface SendNotificationService {

    void limpeza(FinalListLimpezaDTO dto, int idLayout);

    void assistencia(List<LocalDate> dates);

    void vidaCrista(List<VidaCristaExtractWeekDTO> weeks);

    void designacao(DesignacaoWriterDTO dto);
}
