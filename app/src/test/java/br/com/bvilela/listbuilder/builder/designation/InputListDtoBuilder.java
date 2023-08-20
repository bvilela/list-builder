package br.com.bvilela.listbuilder.builder.designation;

import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class InputListDtoBuilder {

    private InputListDTO target;

    public InputListDtoBuilder() {
        this.target = new InputListDTO();
    }

    public static InputListDtoBuilder create() {
        return new InputListDtoBuilder();
    }

    public InputListDTO build() {
        return target;
    }

    public InputListDtoBuilder withRandomData() {
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

    public InputListDtoBuilder withList(List<String> list) {
        this.target.setList(list);
        return this;
    }

    public InputListDtoBuilder withLast(String last) {
        this.target.setLast(last);
        return this;
    }
}
