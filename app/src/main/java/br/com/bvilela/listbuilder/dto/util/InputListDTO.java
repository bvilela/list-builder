package br.com.bvilela.listbuilder.dto.util;

import br.com.bvilela.listbuilder.config.MessageConfig;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class InputListDTO {

    @NotEmpty(message = MessageConfig.LIST_REQUIRED)
    @SerializedName("lista")
    private List<@Valid @NotBlank(message = MessageConfig.LIST_ELEMENT_REQUIRED) String> list;

    @NotBlank(message = MessageConfig.LAST_REQUIRED)
    @SerializedName("ultimo")
    private String last;
}
