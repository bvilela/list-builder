package br.com.bvilela.listbuilder.builder.designation;

import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputReaderDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;

public class DesignationInputReaderDtoBuilder {

    private DesignationInputReaderDTO target;

    public DesignationInputReaderDtoBuilder() {
        this.target = new DesignationInputReaderDTO();
    }

    public static DesignationInputReaderDtoBuilder create() {
        return new DesignationInputReaderDtoBuilder();
    }

    public DesignationInputReaderDTO build() {
        return target;
    }

    public DesignationInputReaderDtoBuilder withRandomData() {
        this.withWatchtower(
                InputListDtoBuilder.create().withRandomData().build());
        this.withBibleStudy(
                InputListDtoBuilder.create().withRandomData().build());
        return this;
    }

    public DesignationInputReaderDtoBuilder withWatchtower(InputListDTO watchtower) {
        this.target.setWatchtower(watchtower);
        return this;
    }

    public DesignationInputReaderDtoBuilder withBibleStudy(InputListDTO bibleStudy) {
        this.target.setBibleStudy(bibleStudy);
        return this;
    }
}
