package br.com.bvilela.listbuilder.dto.limpeza;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class FinalListLimpezaItemDTO {
    private LocalDate date;
    private String label;
    private String group;
}
