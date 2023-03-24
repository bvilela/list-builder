package br.com.bvilela.listbuilder.service.limpeza;

import java.nio.file.Path;

import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface LimpezaWriterService {

	Path writerPDF(FinalListLimpezaDTO dto, String footerMessage, String headerMessage, int layout) throws ListBuilderException;
	
}
