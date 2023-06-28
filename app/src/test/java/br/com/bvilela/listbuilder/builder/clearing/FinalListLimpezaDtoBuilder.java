package br.com.bvilela.listbuilder.builder.clearing;

import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FinalListLimpezaDtoBuilder {

    public static FinalListLimpezaDTO createMockLayout1() {
        var group = "Person1, Person2, Person3";
        var item = new FinalListLimpezaItemDTO(LocalDate.now(), "label", group);
        return FinalListLimpezaDTO.builder().items(List.of(item)).build();
    }

    public static FinalListLimpezaDTO createMockLayout2() {
        var item =
                FinalListLimpezaItemLayout2DTO.builder()
                        .group("Person1, Person2, Person3")
                        .date1(LocalDate.now().minusDays(1))
                        .label1("label1")
                        .date2(LocalDate.now())
                        .label2("label2")
                        .build();
        return FinalListLimpezaDTO.builder().itemsLayout2(List.of(item)).build();
    }
}