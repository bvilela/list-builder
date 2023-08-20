package br.com.bvilela.listbuilder.builder.discourse;

import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputDTO;
import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputItemDTO;
import java.util.List;

public class DiscourseInputDtoBuilder {

    private DiscourseInputDTO target;

    public DiscourseInputDtoBuilder() {
        this.target = new DiscourseInputDTO();
    }

    public static DiscourseInputDtoBuilder create() {
        return new DiscourseInputDtoBuilder();
    }

    public DiscourseInputDTO build() {
        return target;
    }

    public DiscourseInputDtoBuilder withRandomData() {
        this.withReceive(
                List.of(
                        DiscourseInputItemDtoBuilder.create().withRandomData().build(),
                        DiscourseInputItemDtoBuilder.create().withRandomData().build(),
                        DiscourseInputItemDtoBuilder.create().withRandomData().build(),
                        DiscourseInputItemDtoBuilder.create().withRandomData().build()));
        this.withSend(
                List.of(
                        DiscourseInputItemDtoBuilder.create().withRandomData().build(),
                        DiscourseInputItemDtoBuilder.create().withRandomData().build(),
                        DiscourseInputItemDtoBuilder.create().withRandomData().build(),
                        DiscourseInputItemDtoBuilder.create().withRandomData().build()));
        return this;
    }

    public DiscourseInputDtoBuilder withSend(List<DiscourseInputItemDTO> send) {
        this.target.setSend(send);
        return this;
    }

    public DiscourseInputDtoBuilder withReceive(List<DiscourseInputItemDTO> receive) {
        this.target.setReceive(receive);
        return this;
    }
}
