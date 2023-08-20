package br.com.bvilela.listbuilder.dto.designation.input;

import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.util.BaseInputDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import br.com.bvilela.listbuilder.validator.DesignationValidator;
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
public class DesignationInputDTO extends BaseInputDTO {

    @NotNull(message = MessageConfig.PRESIDENT_REQUIRED)
    @SerializedName("presidente")
    private @Valid InputListDTO president;

    @NotNull(message = MessageConfig.READER_REQUIRED)
    @SerializedName("leitor")
    private @Valid DesignationInputReaderDTO reader;

    @NotNull(message = MessageConfig.AUDIOVIDEO_REQUIRED)
    @SerializedName("audioVideo")
    private @Valid InputListDTO audioVideo;

    @NotEmpty(message = MessageConfig.INDICATOR_REQUIRED)
    @SerializedName("indicador")
    private List<@Valid @NotBlank(message = MessageConfig.INDICATOR_ELEMENT_REQUIRED) String>
            indicator;

    @NotEmpty(message = MessageConfig.MICROPHONE_REQUIRED)
    @SerializedName("microfone")
    private List<@Valid @NotBlank(message = MessageConfig.MICROPHONE_ELEMENT_REQUIRED) String>
            microphone;

    public void validate() {
        DesignationValidator.validateData(this);
    }
}
