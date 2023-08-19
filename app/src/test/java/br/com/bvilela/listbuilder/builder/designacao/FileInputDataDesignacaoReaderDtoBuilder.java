package br.com.bvilela.listbuilder.builder.designacao;

import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputReaderDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;

public class FileInputDataDesignacaoReaderDtoBuilder {

    private DesignationInputReaderDTO target;

    public FileInputDataDesignacaoReaderDtoBuilder() {
        this.target = new DesignationInputReaderDTO();
    }

    public static FileInputDataDesignacaoReaderDtoBuilder create() {
        return new FileInputDataDesignacaoReaderDtoBuilder();
    }

    public DesignationInputReaderDTO build() {
        return target;
    }

    public FileInputDataDesignacaoReaderDtoBuilder withRandomData() {
        this.withWatchtower(
                FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
        this.withBibleStudy(
                FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
        return this;
    }

    public FileInputDataDesignacaoReaderDtoBuilder withWatchtower(InputListDTO watchtower) {
        this.target.setWatchtower(watchtower);
        return this;
    }

    public FileInputDataDesignacaoReaderDtoBuilder withBibleStudy(InputListDTO bibleStudy) {
        this.target.setBibleStudy(bibleStudy);
        return this;
    }
}
