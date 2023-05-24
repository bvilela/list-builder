package br.com.bvilela.listbuilder.service.limpeza;

import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.nio.file.Path;

public interface LimpezaWriterService {

    Path writerPDF(FinalListLimpezaDTO dto, String footerMessage, String headerMessage, int layout)
            throws ListBuilderException;
}
