package br.com.bvilela.listbuilder.builder.designacao;

import br.com.bvilela.listbuilder.dto.InputListDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoReaderDTO;

public class FileInputDataDesignacaoReaderDtoBuilder {

    private FileInputDataDesignacaoReaderDTO target;

    public FileInputDataDesignacaoReaderDtoBuilder() {
        this.target = new FileInputDataDesignacaoReaderDTO();
    }

    public static FileInputDataDesignacaoReaderDtoBuilder create() {
        return new FileInputDataDesignacaoReaderDtoBuilder();
    }

    public FileInputDataDesignacaoReaderDTO build() {
        return target;
    }

    public FileInputDataDesignacaoReaderDtoBuilder withRandomData() {
        this.withWatchtower(
                FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
        this.withBibleStudy(
                FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
        return this;
    }

    public FileInputDataDesignacaoReaderDtoBuilder withWatchtower(
            InputListDTO watchtower) {
        this.target.setWatchtower(watchtower);
        return this;
    }

    public FileInputDataDesignacaoReaderDtoBuilder withBibleStudy(
            InputListDTO bibleStudy) {
        this.target.setBibleStudy(bibleStudy);
        return this;
    }
}
