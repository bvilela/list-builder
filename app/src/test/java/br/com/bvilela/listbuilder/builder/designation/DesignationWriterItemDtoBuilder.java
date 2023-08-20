package br.com.bvilela.listbuilder.builder.designation;

import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;

public class DesignationWriterItemDtoBuilder {

    private DesignationWriterItemDTO target;

    public DesignationWriterItemDtoBuilder() {
        this.target = new DesignationWriterItemDTO();
    }

    public static DesignationWriterItemDtoBuilder create() {
        return new DesignationWriterItemDtoBuilder();
    }

    public DesignationWriterItemDTO build() {
        return target;
    }

    public DesignationWriterItemDtoBuilder withRandomData() {
        this.withDate(LocalDate.now());
        this.withName(RandomStringUtils.randomAlphabetic(10));
        return this;
    }

    public DesignationWriterItemDtoBuilder withRandomTwoPeopleData() {
        this.withDate(LocalDate.now());
        this.withName(
                RandomStringUtils.randomAlphabetic(10)
                        .concat(" e ")
                        .concat(RandomStringUtils.randomAlphabetic(10)));
        return this;
    }

    public DesignationWriterItemDtoBuilder withDate(LocalDate date) {
        this.target.setDate(date);
        return this;
    }

    public DesignationWriterItemDtoBuilder withName(String name) {
        this.target.setName(name);
        return this;
    }
}
