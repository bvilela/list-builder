package br.com.bvilela.listbuilder.builder.designacao;

import br.com.bvilela.listbuilder.dto.InputListDTO;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class FileInputDataDesignacaoListDtoBuilder {

    private InputListDTO target;

    public FileInputDataDesignacaoListDtoBuilder() {
        this.target = new InputListDTO();
    }

    public static FileInputDataDesignacaoListDtoBuilder create() {
        return new FileInputDataDesignacaoListDtoBuilder();
    }

    public InputListDTO build() {
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
