package br.com.bvilela.listbuilder.service.designacao;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import java.nio.file.Path;

public interface DesignacaoWriterService {

    Path writerPDF(DesignacaoWriterDTO dto);

    Path writerDocx(DesignacaoWriterDTO dto);
}
