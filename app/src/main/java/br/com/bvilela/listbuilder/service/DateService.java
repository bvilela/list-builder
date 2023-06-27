package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import br.com.bvilela.listbuilder.dto.designacao.FileInputDataDesignacaoDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import java.time.LocalDate;
import java.util.List;

public interface DateService {

    List<ItemDateDTO> generateListDatesLimpeza(DateServiceInputDTO dto, int layout);

    List<LocalDate> generateAudienceListDates(
            FileInputDataAudienceDTO dto, AudienceWriterLayoutEnum layoutEnum);

    List<LocalDate> generateDesignationListDates(FileInputDataDesignacaoDTO dto);
}
