package br.com.bvilela.listbuilder.dto.vidacrista;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VidaCristaExtractWeekDTO {

    @ToString.Exclude private String link;

    @ToString.Exclude private String labelDate;

    private LocalDate date1;

    private LocalDate date2;

    private List<VidaCristaExtractWeekItemDTO> items;

    @Builder.Default @ToString.Exclude private boolean skip = false;

    private String skipMessage;
}
