package br.com.bvilela.listbuilder.service.vidacrista;

import java.nio.file.Path;
import java.util.List;

import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface VidaCristaWriterService {

	Path writerPDF(List<VidaCristaExtractWeekDTO> list) throws ListBuilderException;
	
}
