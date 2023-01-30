package com.bruno.listbuilder.service;

import com.bruno.listbuilder.exception.listtype.InvalidListTypeException;
import com.bruno.listbuilder.exception.listtype.RequiredListTypeException;
import com.bruno.listbuilder.exception.listtype.ServiceListTypeNotFoundException;

public interface OrchestratorService {

	BaseGenerateService validateAndGetServiceByListType()
			throws InvalidListTypeException, RequiredListTypeException, ServiceListTypeNotFoundException;

}
