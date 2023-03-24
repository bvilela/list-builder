package br.com.bvilela.listbuilder.service.designacao;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;

public interface DesignacaoCounterService {

	StringBuilder countNumberActiviesByName(DesignacaoWriterDTO dto);

}
