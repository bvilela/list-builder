package com.bruno.listbuilder.service.assistencia.impl;

import org.springframework.stereotype.Service;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.BaseGenerateService;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.assistencia.AssistenciaWriterService;
import com.bruno.listbuilder.validator.AssistenciaValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistenciaGenerateServiceImpl implements BaseGenerateService {

	private AppProperties properties;

	private DateService dateService;
	private AssistenciaWriterService writerService;
	private NotificationService notificationService;

	public AssistenciaGenerateServiceImpl(AppProperties properties, DateService dateService,
			AssistenciaWriterService writerService, NotificationService notificationService) {
		this.properties = properties;
		this.dateService = dateService;
		this.writerService = writerService;
		this.notificationService = notificationService;
	}

	@Override
	public ListTypeEnum getExecutionMode() {
		return ListTypeEnum.ASSISTENCIA;
	}
	
	@Override
	public AppProperties getAppProperties() {
		return this.properties;
	}

	@Override
	public void generateList() throws ListBuilderException {
		try {
			logInit(log);
			
			var dto = getFileInputDataDTO(FileInputDataAssistenciaDTO.class);

			var dateServiceInputDto = AssistenciaValidator.validAndConvertData(dto);

			var listDates = dateService.generateListDatesAssistencia(dateServiceInputDto);
			
			if (listDates.isEmpty()) {
				throw new ListBuilderException(MessageConfig.LIST_DATE_EMPTY);
			}

			writerService.writerPDF(listDates);

			notificationService.assistencia(listDates);

			logFinish(log);

		} catch (Exception e) {
			throw defaultListBuilderException(e);
		}
	}

}
