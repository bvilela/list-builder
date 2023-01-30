package com.bruno.listbuilder.dto.limpeza;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class FinalListLimpezaDTO {

	List<FinalListLimpezaItemDTO> items;
	
	List<FinalListLimpezaItemLayout2DTO> itemsLayout2;
	
}
