package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.dto.audience.AudienceInputDTO;
import br.com.bvilela.listbuilder.dto.designation.input.DesignationInputDTO;
import br.com.bvilela.listbuilder.dto.util.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import java.time.LocalDate;
import java.util.List;

public interface DateService {

    List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout);

    List<LocalDate> generateAudienceListDates(
            AudienceInputDTO dto, AudienceWriterLayoutEnum layoutEnum);

    List<LocalDate> generateDesignationListDates(DesignationInputDTO dto);
}
