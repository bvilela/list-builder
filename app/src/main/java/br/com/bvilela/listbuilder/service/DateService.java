package br.com.bvilela.listbuilder.service;

import java.time.LocalDate;
import java.util.List;

import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;

public interface DateService {
		
	List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout);
	
	List<LocalDate> generateListDatesAssistencia(DateServiceInputDTO dto);
	
	List<LocalDate> generateListDatesDesignacao(DateServiceInputDTO dto);
	
}
