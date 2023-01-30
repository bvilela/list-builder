package com.bruno.listbuilder.dto.discurso;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import com.bvilela.utils.annotation.gson.NotSerialized;
import com.bvilela.utils.annotation.javax.ValidParseDate;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class FileInputDataDiscursoItemDTO {
	
	@ValidParseDate(message = "Data inválida: '${validatedValue}' não é uma data válida", 
			pattern = "dd-MM-yyyy", parse = true, messageRequired = "Campo 'Data' é obrigatório!")
	@SerializedName("data")
	private String date;
	
	@NotSerialized
	private LocalDate dateConverted;
	
	@SerializedName("tema_numero")
	private String themeNumber;
	
	@SerializedName("tema_titulo")
	private String themeTitle;
	
	@NotBlank(message = "Campo 'orador' é obrigatório!")
	@SerializedName("orador")
	private String speaker;
	
	@NotBlank(message = "Campo 'congregacao' é obrigatório!")
	@SerializedName("congregacao")
	private String congregation;
	
}
