package com.bruno.listbuilder.builder;

import java.util.List;

import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoItemDTO;

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
		this.withReceive(List.of(
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build()
			));
		this.withSend(List.of(
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build(),
				FileInputDataDiscursoItemDtoBuilder.create().withRandomData().build()
			));
		return this;
	}

    private FileInputDataDiscursoDtoBuilder withSend(List<FileInputDataDiscursoItemDTO> send) {
        this.target.setSend(send);
        return this;
    }
    
    private FileInputDataDiscursoDtoBuilder withReceive(List<FileInputDataDiscursoItemDTO> receive) {
        this.target.setReceive(receive);
        return this;
    }
	
}
