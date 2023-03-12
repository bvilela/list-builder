package com.bruno.listbuilder.service.assistencia.impl;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssistenciaGenerateServiceImpl implements BaseGenerateService {

	private final AppProperties properties;
	private final DateService dateService;
	private final AssistenciaWriterService writerService;
	private final NotificationService notificationService;

	@Override
	public ListTypeEnum getListType() {
		return ListTypeEnum.ASSISTENCIA;
	}

	@Override
	public void generateList() throws ListBuilderException {
		try {
			logInit(log);
			
			var dto = getFileInputDataDTO(properties, FileInputDataAssistenciaDTO.class);
			
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
