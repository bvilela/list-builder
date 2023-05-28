package br.com.bvilela.listbuilder.service.assistencia;

import java.time.LocalDate;
import java.util.List;

public interface AssistenciaWriterService {

    void writerPDF(List<LocalDate> listDates);
}
