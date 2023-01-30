package com.bruno.listbuilder.service.assistencia;

import java.time.LocalDate;
import java.util.List;

import com.bruno.listbuilder.exception.ListBuilderException;

public interface AssistenciaWriterService {

	void writerPDF(List<LocalDate> listDates) throws ListBuilderException;
	
}
