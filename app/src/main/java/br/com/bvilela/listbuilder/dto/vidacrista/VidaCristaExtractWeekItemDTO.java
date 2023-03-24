package br.com.bvilela.listbuilder.dto.vidacrista;

import java.util.List;

import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@NoArgsConstructor
public class VidaCristaExtractWeekItemDTO {

	private String title;
	private VidaCristaExtractItemType type;
	private List<String> participants;
	
	public VidaCristaExtractWeekItemDTO(String title, VidaCristaExtractItemType type) {
		this.title = title;
		this.type = type;
	}
	
}
