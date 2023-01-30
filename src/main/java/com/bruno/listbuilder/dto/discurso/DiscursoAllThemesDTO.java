package com.bruno.listbuilder.dto.discurso;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DiscursoAllThemesDTO {

	@NotEmpty(message = "Temas não pode ser vazio")
	@SerializedName("temas")
	Map<Integer, String> themes;
	
}
