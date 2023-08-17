package br.com.bvilela.listbuilder.builder;

import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseDTO;
import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseItemDTO;
import java.util.List;

public class FileInputDataDiscursoDtoBuilder {

    private InputDiscourseDTO target;

    public FileInputDataDiscursoDtoBuilder() {
        this.target = new InputDiscourseDTO();
    }

    public static FileInputDataDiscursoDtoBuilder create() {
        return new FileInputDataDiscursoDtoBuilder();
    }

    public InputDiscourseDTO build() {
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

    public FileInputDataDiscursoDtoBuilder withSend(List<InputDiscourseItemDTO> send) {
        this.target.setSend(send);
        return this;
    }

    public FileInputDataDiscursoDtoBuilder withReceive(List<InputDiscourseItemDTO> receive) {
        this.target.setReceive(receive);
        return this;
    }
}
