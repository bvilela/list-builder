package br.com.bvilela.listbuilder.builder.designacao;

import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;

public class DesignacaoWriterItemDtoBuilder {

    private DesignationWriterItemDTO target;

    public DesignacaoWriterItemDtoBuilder() {
        this.target = new DesignationWriterItemDTO();
    }

    public static DesignacaoWriterItemDtoBuilder create() {
        return new DesignacaoWriterItemDtoBuilder();
    }

    public DesignationWriterItemDTO build() {
        return target;
    }

    public DesignacaoWriterItemDtoBuilder withRandomData() {
        this.withDate(LocalDate.now());
        this.withName(RandomStringUtils.randomAlphabetic(10));
        return this;
    }

    public DesignacaoWriterItemDtoBuilder withRandomTwoPeopleData() {
        this.withDate(LocalDate.now());
        this.withName(
                RandomStringUtils.randomAlphabetic(10)
                        .concat(" e ")
                        .concat(RandomStringUtils.randomAlphabetic(10)));
        return this;
    }

    public DesignacaoWriterItemDtoBuilder withDate(LocalDate date) {
        this.target.setDate(date);
        return this;
    }

    public DesignacaoWriterItemDtoBuilder withName(String name) {
        this.target.setName(name);
        return this;
    }
}
