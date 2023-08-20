package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputDTO;
import br.com.bvilela.listbuilder.dto.discourse.input.DiscourseInputItemDTO;
import java.util.List;

public class FileInputDataDiscursoDtoBuilder {

    private DiscourseInputDTO target;

    public FileInputDataDiscursoDtoBuilder() {
        this.target = new DiscourseInputDTO();
    }

    public static FileInputDataDiscursoDtoBuilder create() {
        return new FileInputDataDiscursoDtoBuilder();
    }

    public DiscourseInputDTO build() {
        return target;
    }

    public FileInputDataDiscursoDtoBuilder withRandomData() {
        this.withReceive(
                List.of(
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build()));
        this.withSend(
                List.of(
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
                        FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build()));
        return this;
    }

    public FileInputDataDiscursoDtoBuilder withSend(List<DiscourseInputItemDTO> send) {
        this.target.setSend(send);
        return this;
    }

    public FileInputDataDiscursoDtoBuilder withReceive(List<DiscourseInputItemDTO> receive) {
        this.target.setReceive(receive);
        return this;
    }
}
