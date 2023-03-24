package br.com.bvilela.listbuilder.service.assistencia;

import java.time.LocalDate;
import java.util.List;

import br.com.bvilela.listbuilder.exception.ListBuilderException;

public interface AssistenciaWriterService {

	void writerPDF(List<LocalDate> listDates) throws ListBuilderException;
	
}
