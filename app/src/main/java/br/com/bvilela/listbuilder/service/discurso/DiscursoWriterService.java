package br.com.bvilela.listbuilder.service.discurso;

import java.nio.file.Path;

import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface DiscursoWriterService {

	Path writerPDF(FileInputDataDiscursoDTO dto) throws ListBuilderException;
	
}
