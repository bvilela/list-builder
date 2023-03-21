package com.bruno.listbuilder.dto.designacao.writer;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DesignacaoWriterDTO {

	private List<DesignacaoWriterItemDTO> president;
	
	private List<DesignacaoWriterItemDTO> readerWatchtower;
	
	private List<DesignacaoWriterItemDTO> readerBibleStudy;
	
	private List<DesignacaoWriterItemDTO> audioVideo;
	
	private List<DesignacaoWriterItemDTO> indicator;
	
	private List<DesignacaoWriterItemDTO> microphone;
	
}
