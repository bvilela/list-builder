package br.com.bvilela.listbuilder.dto.discurso;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileInputDataDiscursoDTO {
	
	@SerializedName("enviar")
	private List<FileInputDataDiscursoItemDTO> send;
	
	@SerializedName("receber")
	private List<FileInputDataDiscursoItemDTO> receive;
	
}
