package br.com.bvilela.listbuilder.dto.designacao;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.bvilela.listbuilder.config.MessageConfig;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class FileInputDataDesignacaoReaderDTO {
	
	@NotNull(message = MessageConfig.READER_WATCHTOWER_REQUIRED)
	@SerializedName("asentinela")
	private @Valid FileInputDataDesignacaoListDTO watchtower;
	
	@NotNull(message = MessageConfig.READER_BIBLESTUDY_REQUIRED)
	@SerializedName("estudoBiblico")
	private @Valid FileInputDataDesignacaoListDTO bibleStudy;

}
