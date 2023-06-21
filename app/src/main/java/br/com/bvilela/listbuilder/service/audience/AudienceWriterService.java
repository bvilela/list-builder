package br.com.bvilela.listbuilder.service.audience;

import java.time.LocalDate;
import java.util.List;

public interface AudienceWriterService {

    void writerPDF(List<LocalDate> listDates);
}
