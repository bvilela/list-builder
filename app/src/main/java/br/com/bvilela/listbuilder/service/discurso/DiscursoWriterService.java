package br.com.bvilela.listbuilder.service.discurso;

import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.nio.file.Path;

public interface DiscursoWriterService {

    Path writerPDF(FileInputDataDiscursoDTO dto) throws ListBuilderException;
}
