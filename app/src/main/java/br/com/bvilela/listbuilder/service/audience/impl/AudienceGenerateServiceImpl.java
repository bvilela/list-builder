package br.com.bvilela.listbuilder.service.audience.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.audience.FileInputDataAudienceDTO;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.audience.AudienceWriterService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import java.util.Map;
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
    private final SendNotificationService notificationService;
    private final AudienceWriterService writerService;

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

            dto.validate();

            var layoutEnum = AudienceWriterLayoutEnum.getByLayout(properties.getLayoutAudience());

            var listDates = dateService.generateAudienceListDates(dto, layoutEnum);

            writerService.writerPDF(listDates, layoutEnum);

            notificationService.audience(listDates);

            logFinish(log);

        } catch (Exception e) {
            throw defaultListBuilderException(log, e);
        }
    }
}
