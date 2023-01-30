package com.bruno.listbuilder.service.discurso;

import java.nio.file.Path;

import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface DiscursoWriterService {

	Path writerPDF(FileInputDataDiscursoDTO dto) throws ListBuilderException;
	
}
