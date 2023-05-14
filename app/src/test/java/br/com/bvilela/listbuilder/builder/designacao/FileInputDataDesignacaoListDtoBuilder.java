package br.com.bvilela.listbuilder.builder.designacao;

import java.util.List;

import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoListDTO;
import org.apache.commons.lang3.RandomStringUtils;

public class FileInputDataDesignacaoListDtoBuilder {

    private FileInputDataDesignacaoListDTO target;

    public FileInputDataDesignacaoListDtoBuilder() {
        this.target = new FileInputDataDesignacaoListDTO();
    }

    public static FileInputDataDesignacaoListDtoBuilder create() {
        return new FileInputDataDesignacaoListDtoBuilder();
    }

    public FileInputDataDesignacaoListDTO build() {
        return target;
    }

    public FileInputDataDesignacaoListDtoBuilder withRandomData() {
        var list =
                List.of(
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10));
        this.withList(list);
        this.withLast(list.get(0));
        return this;
    }

    public FileInputDataDesignacaoListDtoBuilder withList(List<String> list) {
        this.target.setList(list);
        return this;
    }

    public FileInputDataDesignacaoListDtoBuilder withLast(String last) {
        this.target.setLast(last);
        return this;
    }
}
