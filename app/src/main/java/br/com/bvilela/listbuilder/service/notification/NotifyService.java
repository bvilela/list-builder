package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import java.time.LocalDate;
import java.util.List;

public interface NotifyService {

    void limpeza(FinalListLimpezaDTO dto, int idLayout);

    void assistencia(List<LocalDate> list);

    void vidaCrista(List<VidaCristaExtractWeekDTO> listWeeks);

    void designacao(DesignacaoWriterDTO dtoWriter);
}
