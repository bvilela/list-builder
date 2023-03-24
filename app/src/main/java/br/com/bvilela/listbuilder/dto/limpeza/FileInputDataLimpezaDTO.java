package br.com.bvilela.listbuilder.dto.limpeza;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import br.com.bvilela.listbuilder.dto.BaseFileInputDataDTO;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class FileInputDataLimpezaDTO extends BaseFileInputDataDTO {
	
	@NotNull(message = "Último grupo não informado!")
	@SerializedName("ultimoGrupo")
	private Integer lastGroup;

	@NotNull(message = "Grupos está vazio!")
	@SerializedName("gruposLimpeza")
	private Map<Integer, String> groups;
	
	@SerializedName("mensagemCabecalho")
	private String headerMessage;
	
	@SerializedName("mensagemRodape")
	private String footerMessage;
	
	@SerializedName("removerLista")
	private List<String> removeFromList;
	
	@SerializedName("adicionarLista")
	private Map<String, String> addToList;
	
}
