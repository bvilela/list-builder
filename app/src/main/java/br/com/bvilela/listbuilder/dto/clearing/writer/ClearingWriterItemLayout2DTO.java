package br.com.bvilela.listbuilder.dto.clearing.writer;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ClearingWriterItemLayout2DTO {

    private String group;

    private LocalDate date1;
    private String label1;

    private LocalDate date2;
    private String label2;
}
