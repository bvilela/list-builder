package com.bruno.listbuilder.service.designacao;

import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;

public interface DesignacaoCounterService {

	StringBuilder countNumberActiviesByName(DesignacaoWriterDTO dto);

}
