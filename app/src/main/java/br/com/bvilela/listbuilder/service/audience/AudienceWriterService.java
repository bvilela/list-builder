package br.com.bvilela.listbuilder.service.audience;

import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import java.time.LocalDate;
import java.util.List;

public interface AudienceWriterService {

    void writerPDF(List<LocalDate> listDates, AudienceWriterLayoutEnum layoutEnum);
}
