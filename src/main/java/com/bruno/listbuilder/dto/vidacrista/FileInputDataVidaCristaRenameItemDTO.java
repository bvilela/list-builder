package com.bruno.listbuilder.dto.vidacrista;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class FileInputDataVidaCristaRenameItemDTO {
	
	@NotNull(message = "Numero da Semana é obrigatório")
	@SerializedName("semana")
	private Integer weekIndex;
	
	@NotBlank(message = "Nome Original do Item é obrigatório")
	@SerializedName("nomeOriginal")
	private String originalName;
	
	@SerializedName("nomeNovo")
	private String newName;
	
}
