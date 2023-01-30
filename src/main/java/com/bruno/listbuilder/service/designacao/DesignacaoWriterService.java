package com.bruno.listbuilder.service.designacao;

import java.nio.file.Path;

import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import com.bruno.listbuilder.exception.ListBuilderException;

public interface DesignacaoWriterService {

	Path writerPDF(DesignacaoWriterDTO dto) throws ListBuilderException;
	
	Path writerDocx(DesignacaoWriterDTO dto) throws ListBuilderException;
	
}
