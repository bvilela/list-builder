package br.com.bvilela.listbuilder.dto.clearing.writer;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ClearingWriterItemDTO {
    private LocalDate date;
    private String label;
    private String group;
}
