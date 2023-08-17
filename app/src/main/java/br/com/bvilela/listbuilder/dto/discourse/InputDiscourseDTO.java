package br.com.bvilela.listbuilder.dto.discourse;

import br.com.bvilela.listbuilder.dto.InputListDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InputDiscourseDTO {

    @SerializedName("presidente")
    private InputListDTO president;

    @SerializedName("enviar")
    private List<InputDiscourseItemDTO> send;

    @SerializedName("receber")
    private List<InputDiscourseItemDTO> receive;
}
