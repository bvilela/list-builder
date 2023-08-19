package br.com.bvilela.listbuilder.builder.clearing;

import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemLayout2DTO;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FinalListLimpezaDtoBuilder {

    public static ClearingWriterDTO createMockLayout1() {
        var group = "Person1, Person2, Person3";
        var item = new ClearingWriterItemDTO(LocalDate.now(), "label", group);
        return ClearingWriterDTO.builder().items(List.of(item)).build();
    }

    public static ClearingWriterDTO createMockLayout2() {
        var item =
                ClearingWriterItemLayout2DTO.builder()
                        .group("Person1, Person2, Person3")
                        .date1(LocalDate.now().minusDays(1))
                        .label1("label1")
                        .date2(LocalDate.now())
                        .label2("label2")
                        .build();
        return ClearingWriterDTO.builder().itemsLayout2(List.of(item)).build();
    }
}
