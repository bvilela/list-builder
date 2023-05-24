package br.com.bvilela.listbuilder.service.assistencia;

import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.LocalDate;
import java.util.List;

public interface AssistenciaWriterService {

    void writerPDF(List<LocalDate> listDates) throws ListBuilderException;
}
