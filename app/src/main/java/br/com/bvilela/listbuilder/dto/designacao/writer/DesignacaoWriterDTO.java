package br.com.bvilela.listbuilder.dto.designacao.writer;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DesignacaoWriterDTO {

    @NotEmpty(message = "Lista de Presidente vazia!")
    private List<DesignacaoWriterItemDTO> president;

    @NotEmpty(message = "Lista de Leitor de A Sentinela vazia!")
    private List<DesignacaoWriterItemDTO> readerWatchtower;

    @NotEmpty(message = "Lista de Leitor do Estudo Bíblico vazia!")
    private List<DesignacaoWriterItemDTO> readerBibleStudy;

    @NotEmpty(message = "Lista de AudioVideo vazia!")
    private List<DesignacaoWriterItemDTO> audioVideo;

    @NotEmpty(message = "Lista de Indicador vazia!")
    private List<DesignacaoWriterItemDTO> indicator;

    @NotEmpty(message = "Lista de Microfone Volante vazia!")
    private List<DesignacaoWriterItemDTO> microphone;
}
