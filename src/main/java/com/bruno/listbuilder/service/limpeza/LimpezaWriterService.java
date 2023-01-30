package com.bruno.listbuilder.service.limpeza;

import java.nio.file.Path;

import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface LimpezaWriterService {

	Path writerPDF(FinalListLimpezaDTO dto, String footerMessage, String headerMessage, int layout) throws ListBuilderException;
	
}
