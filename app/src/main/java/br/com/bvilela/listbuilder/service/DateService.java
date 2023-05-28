package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import java.time.LocalDate;
import java.util.List;

public interface DateService {

    List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout);

    List<LocalDate> generateListDatesAssistencia(DateServiceInputDTO dto);

    List<LocalDate> generateListDatesDesignacao(DateServiceInputDTO dto);
}
