package com.bruno.listbuilder.service;

import java.time.LocalDate;
import java.util.List;

import com.bruno.listbuilder.dto.DateServiceInputDTO;
import com.bruno.listbuilder.dto.ItemDateDTO;

public interface DateService {
		
	List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout);
	
	List<LocalDate> generateListDatesAssistencia(DateServiceInputDTO dto);
	
	List<LocalDate> generateListDatesDesignacao(DateServiceInputDTO dto);
	
}
