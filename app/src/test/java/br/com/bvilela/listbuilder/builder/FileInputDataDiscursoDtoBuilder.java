package br.com.bvilela.listbuilder.builder;

import java.util.List;

import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import br.com.bvilela.listbuilder.dto.discurso.FileInputDataDiscursoItemDTO;

public class FileInputDataDiscursoDtoBuilder {

    private FileInputDataDiscursoDTO target;

    public FileInputDataDiscursoDtoBuilder() {
        this.target = new FileInputDataDiscursoDTO();
    }

    public static FileInputDataDiscursoDtoBuilder create() {
        return new FileInputDataDiscursoDtoBuilder();
    }

    public FileInputDataDiscursoDTO build() {
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

    public FileInputDataDiscursoDtoBuilder withSend(List<FileInputDataDiscursoItemDTO> send) {
        this.target.setSend(send);
        return this;
    }

    public FileInputDataDiscursoDtoBuilder withReceive(List<FileInputDataDiscursoItemDTO> receive) {
        this.target.setReceive(receive);
        return this;
    }
}
