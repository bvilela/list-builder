package br.com.bvilela.listbuilder.dto.discourse.input;

import br.com.bvilela.listbuilder.dto.discourse.writer.DiscourseWriterDTO;
import br.com.bvilela.listbuilder.dto.discourse.writer.DiscourseWriterItemDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiscourseInputDTO {

    @SerializedName("presidente")
    private InputListDTO president;

    @SerializedName("enviar")
    private List<DiscourseInputItemDTO> send;

    @SerializedName("receber")
    private List<DiscourseInputItemDTO> receive;

    public DiscourseWriterDTO convertToWriterDto() {
        var writerDto = new DiscourseWriterDTO();

        if (this.send != null) {
            writerDto.setSend(this.send.stream().map(DiscourseWriterItemDTO::new).toList());
        }
        if (this.receive != null) {
            writerDto.setReceive(this.receive.stream().map(DiscourseWriterItemDTO::new).toList());
        }
        return writerDto;
    }
}
