package br.com.bvilela.listbuilder.dto.designation.writer;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DesignationWriterDTO {

    @NotEmpty(message = "Lista de Presidente vazia!")
    private List<DesignationWriterItemDTO> president;

    @NotEmpty(message = "Lista de Leitor de A Sentinela vazia!")
    private List<DesignationWriterItemDTO> readerWatchtower;

    @NotEmpty(message = "Lista de Leitor do Estudo Bíblico vazia!")
    private List<DesignationWriterItemDTO> readerBibleStudy;

    @NotEmpty(message = "Lista de AudioVideo vazia!")
    private List<DesignationWriterItemDTO> audioVideo;

    @NotEmpty(message = "Lista de Indicador vazia!")
    private List<DesignationWriterItemDTO> indicator;

    @NotEmpty(message = "Lista de Microfone Volante vazia!")
    private List<DesignationWriterItemDTO> microphone;
}
