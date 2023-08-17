package br.com.bvilela.listbuilder.dto.discourse;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class InputAllThemesDiscourseDTO {

    @NotEmpty(message = "Temas não pode ser vazio")
    @SerializedName("temas")
    Map<Integer, String> themes;
}
