package br.com.bvilela.listbuilder.dto.designacao;

import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.InputListDTO;
import com.google.gson.annotations.SerializedName;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class FileInputDataDesignacaoReaderDTO {

    @NotNull(message = MessageConfig.READER_WATCHTOWER_REQUIRED)
    @SerializedName("asentinela")
    private @Valid InputListDTO watchtower;

    @NotNull(message = MessageConfig.READER_BIBLESTUDY_REQUIRED)
    @SerializedName("estudoBiblico")
    private @Valid InputListDTO bibleStudy;
}
