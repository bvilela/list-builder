package br.com.bvilela.listbuilder.service.designacao;

import java.nio.file.Path;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface DesignacaoWriterService {

	Path writerPDF(DesignacaoWriterDTO dto) throws ListBuilderException;
	
	Path writerDocx(DesignacaoWriterDTO dto) throws ListBuilderException;
	
}
