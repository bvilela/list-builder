package br.com.bvilela.listbuilder.dto.designacao;

import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.BaseFileInputDataDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class FileInputDataDesignacaoDTO extends BaseFileInputDataDTO {

    @NotNull(message = MessageConfig.PRESIDENT_REQUIRED)
    @SerializedName("presidente")
    private @Valid FileInputDataDesignacaoListDTO president;

    @NotNull(message = MessageConfig.READER_REQUIRED)
    @SerializedName("leitor")
    private @Valid FileInputDataDesignacaoReaderDTO reader;

    @NotNull(message = MessageConfig.AUDIOVIDEO_REQUIRED)
    @SerializedName("audioVideo")
    private @Valid FileInputDataDesignacaoListDTO audioVideo;

    @NotEmpty(message = MessageConfig.INDICATOR_REQUIRED)
    @SerializedName("indicador")
    private List<@Valid @NotBlank(message = MessageConfig.INDICATOR_ELEMENT_REQUIRED) String>
            indicator;

    @NotEmpty(message = MessageConfig.MICROPHONE_REQUIRED)
    @SerializedName("microfone")
    private List<@Valid @NotBlank(message = MessageConfig.MICROPHONE_ELEMENT_REQUIRED) String>
            microphone;
}
