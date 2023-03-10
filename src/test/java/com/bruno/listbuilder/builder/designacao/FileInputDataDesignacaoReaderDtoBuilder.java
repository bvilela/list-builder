package com.bruno.listbuilder.builder.designacao;

import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoListDTO;
import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoReaderDTO;

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
		this.withWatchtower(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
		this.withBibleStudy(FileInputDataDesignacaoListDtoBuilder.create().withRandomData().build());
		return this;
	}
	
    public FileInputDataDesignacaoReaderDtoBuilder withWatchtower(FileInputDataDesignacaoListDTO watchtower) {
        this.target.setWatchtower(watchtower);
        return this;
    }
    
    public FileInputDataDesignacaoReaderDtoBuilder withBibleStudy(FileInputDataDesignacaoListDTO bibleStudy) {
        this.target.setBibleStudy(bibleStudy);
        return this;
    }
	
}
