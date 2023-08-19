package br.com.bvilela.listbuilder.dto.christianlife.extract;

import br.com.bvilela.listbuilder.enuns.ChristianLifeExtractItemTypeEnum;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@NoArgsConstructor
public class ChristianLifeExtractWeekItemDTO {

    private String title;
    private ChristianLifeExtractItemTypeEnum type;
    private List<String> participants;

    public ChristianLifeExtractWeekItemDTO(String title, ChristianLifeExtractItemTypeEnum type) {
        this.title = title;
        this.type = type;
    }
}
