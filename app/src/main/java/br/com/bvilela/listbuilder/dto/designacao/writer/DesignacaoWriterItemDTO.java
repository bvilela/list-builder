package br.com.bvilela.listbuilder.dto.designacao.writer;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DesignacaoWriterItemDTO {
	
	private LocalDate date;
	private String name;

}
