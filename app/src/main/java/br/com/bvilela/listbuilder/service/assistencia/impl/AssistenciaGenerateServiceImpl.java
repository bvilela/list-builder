package br.com.bvilela.listbuilder.service.assistencia.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.NotificationService;
import br.com.bvilela.listbuilder.service.assistencia.AssistenciaWriterService;
import br.com.bvilela.listbuilder.validator.AssistenciaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ASSISTENCIA")
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
