package br.com.bvilela.listbuilder.service;

import br.com.bvilela.listbuilder.exception.listtype.InvalidListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.RequiredListTypeException;
import br.com.bvilela.listbuilder.exception.listtype.ServiceListTypeNotFoundException;

public interface OrchestratorService {

	BaseGenerateService validateAndGetServiceByListType()
			throws InvalidListTypeException, RequiredListTypeException, ServiceListTypeNotFoundException;

}
