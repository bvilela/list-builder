package br.com.bvilela.listbuilder.service.vidacrista;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.nio.file.Path;
import java.util.List;

public interface VidaCristaWriterService {

    Path writerPDF(List<VidaCristaExtractWeekDTO> list) throws ListBuilderException;
}
