package br.com.bvilela.listbuilder.dto.discourse.writer;

import br.com.bvilela.listbuilder.dto.discourse.input.InputDiscourseItemDTO;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscourseWriterItemDTO {

    private LocalDate date;
    @Setter private String president;
    private String themeTitle;
    private String speaker;
    private String congregation;

    public DiscourseWriterItemDTO(InputDiscourseItemDTO dto) {
        this.date = dto.getDateConverted();
        this.president = null;
        this.themeTitle = dto.getThemeTitle();
        this.speaker = dto.getSpeaker();
        this.congregation = dto.getCongregation();
    }
}
