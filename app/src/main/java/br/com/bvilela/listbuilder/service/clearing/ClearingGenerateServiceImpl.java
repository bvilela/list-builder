package br.com.bvilela.listbuilder.service.clearing;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemDTO;
import br.com.bvilela.listbuilder.dto.clearing.writer.ClearingWriterItemLayout2DTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.GroupService;
import br.com.bvilela.listbuilder.service.notification.SendNotificationService;
import br.com.bvilela.listbuilder.validator.ClearingValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("LIMPEZA")
@RequiredArgsConstructor
public class ClearingGenerateServiceImpl implements BaseGenerateService {

    private final AppProperties properties;

    private final ClearingWriterService writerService;
    private final DateService dateService;
    private final GroupService groupService;
    private final SendNotificationService notificationService;

    @Override
    public ListTypeEnum getListType() {
        return ListTypeEnum.LIMPEZA;
    }

    @Override
    @SneakyThrows
    public void generateList() {
        try {
            logInit(log);

            var dto = getFileInputDataDTO(properties, ClearingInputDTO.class);

            var dateServiceInputDto = ClearingValidator.validAndConvertData(dto);

            var listDates =
                    dateService.generateListDatesLimpeza(
                            dateServiceInputDto, properties.getLayoutLimpeza());
            var listGroups =
                    groupService.generateListGroupsLimpeza(
                            dto, listDates, properties.getLayoutLimpeza());
            var listGenerated =
                    generateFinalList(listDates, listGroups, properties.getLayoutLimpeza());

            writerService.writerPDF(
                    listGenerated,
                    dto.getFooterMessage(),
                    dto.getHeaderMessage(),
                    properties.getLayoutLimpeza());

            notificationService.clearing(listGenerated, properties.getLayoutLimpeza());

            logFinish(log);

        } catch (Exception e) {
            throw defaultListBuilderException(log, e);
        }
    }

    @SneakyThrows
    private ClearingWriterDTO generateFinalList(
            List<ItemDateDTO> listDates, List<String> listGroups, int layout) {

        if (listDates.isEmpty()) {
            throw new ListBuilderException("Lista de Datas e/ou Lista de Grupos VAZIA!");
        }

        if (layout == 2) {
            return generateFinalListLayout2(listDates, listGroups);
        }
        return generateFinalListLayout1(listDates, listGroups);
    }

    @SneakyThrows
    private ClearingWriterDTO generateFinalListLayout1(
            List<ItemDateDTO> listDates, List<String> listGroups) {
        var list = new ArrayList<ClearingWriterItemDTO>();

        for (int i = 0; i < listDates.size(); i++) {
            var label = getLabel(listDates.get(i), true);
            var item =
                    new ClearingWriterItemDTO(listDates.get(i).getDate(), label, listGroups.get(i));
            list.add(item);
        }

        return ClearingWriterDTO.builder().items(list).build();
    }

    @SneakyThrows
    private ClearingWriterDTO generateFinalListLayout2(
            List<ItemDateDTO> listDates, List<String> listGroups) {

        var list = new ArrayList<ClearingWriterItemLayout2DTO>();

        Map<Integer, List<ItemDateDTO>> mapDatesByOrdinal =
                listDates.stream().collect(Collectors.groupingBy(ItemDateDTO::getOrdinal));

        for (int i = 0; i < mapDatesByOrdinal.size(); i++) {
            var dates = mapDatesByOrdinal.get(i + 1);

            boolean showLabel = dates.size() == 1;
            LocalDate date1 = dates.get(0).getDate();
            LocalDate date2 = null;
            String label1 = getLabel(dates.get(0), showLabel);
            String label2 = null;

            if (dates.size() == 2) {
                date2 = dates.get(1).getDate();
                label2 = getLabel(dates.get(1), true);
            }

            var item =
                    ClearingWriterItemLayout2DTO.builder()
                            .group(listGroups.get(i))
                            .date1(date1)
                            .label1(label1)
                            .date2(date2)
                            .label2(label2)
                            .build();

            list.add(item);
        }

        return ClearingWriterDTO.builder().itemsLayout2(list).build();
    }

    @SneakyThrows
    public String getLabel(ItemDateDTO item, boolean showLabelMessage) {
        var dayWeekEnum = DayOfWeekEnum.getByDayOfWeek(item.getDate().getDayOfWeek());

        if (showLabelMessage) {
            if (item.isException()) {
                return String.format("%s - %s", dayWeekEnum.getName(), item.getMessage());
            } else {
                return String.format("%s - Após a Reunião", dayWeekEnum.getName());
            }
        }
        return dayWeekEnum.getName();
    }
}
