package com.bruno.listbuilder.service.vidacrista;

import java.nio.file.Path;
import java.util.List;

import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface VidaCristaWriterService {

	Path writerPDF(List<VidaCristaExtractWeekDTO> list) throws ListBuilderException;
	
}
