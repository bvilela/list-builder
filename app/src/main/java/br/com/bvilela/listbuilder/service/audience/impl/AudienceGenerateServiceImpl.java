package br.com.bvilela.listbuilder.service.audience.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.audience.AudienceWriterService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.validator.AudienceValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ASSISTENCIA")
@RequiredArgsConstructor
public class AudienceGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;
    private final DateService dateService;
    private final AudienceWriterService writerService;
    private final SendNotificationService notificationService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.ASSISTENCIA;
    }

    @Override
    @SneakyThrows
    public void generateList() {
        try {
            logInit(log);

            var dto = getFileInputDataDTO(properties, FileInputDataAudienceDTO.class);

            var dateServiceInputDto = AudienceValidator.validAndConvertData(dto);

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
