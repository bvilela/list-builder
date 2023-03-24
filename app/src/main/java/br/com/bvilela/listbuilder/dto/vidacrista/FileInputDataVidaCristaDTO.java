package br.com.bvilela.listbuilder.dto.vidacrista;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import br.com.bvilela.listbuilder.config.MessageConfig;
import com.bvilela.utils.annotation.gson.NotSerialized;
import com.bvilela.utils.annotation.javax.ValidParseDate;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileInputDataVidaCristaDTO {

	@SerializedName("abreviacoes")
	private Map<String, String> abbreviations;

	@ValidParseDate(message = MessageConfig.LAST_DATE_INVALID, pattern = "dd-MM-yyyy", parse = true, messageRequired = MessageConfig.LAST_DATE_REQUIRED)
	@SerializedName("ultimaData")
	private String lastDate;

	@NotSerialized
	private LocalDate lastDateConverted;

	@NotEmpty(message = MessageConfig.PARTICIPANTS_REQUIRED)
	@SerializedName("participantes")
	private List<List<String>> participants;
	
	@SerializedName("removerSemanaLista")
	private Map<String, String> removeWeekFromList;
	
	@NotSerialized
	private Map<LocalDate, String> removeWeekFromListConverted;
	
	@SerializedName("renomearItem")
	private List<@Valid FileInputDataVidaCristaRenameItemDTO> renameItems;
	
}
