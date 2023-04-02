package br.com.bvilela.listbuilder.service.limpeza.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.DateService;
import br.com.bvilela.listbuilder.service.GroupService;
import br.com.bvilela.listbuilder.service.NotificationService;
import br.com.bvilela.listbuilder.service.limpeza.LimpezaWriterService;
import br.com.bvilela.listbuilder.validator.LimpezaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("LIMPEZA")
@RequiredArgsConstructor
public class LimpezaGenerateServiceImpl implements BaseGenerateService {

	private final AppProperties properties;

	private final LimpezaWriterService writerService;
	private final DateService dateService;
	private final GroupService groupService;
	private final NotificationService notificationService;
	@Override
	public ListTypeEnum getListType() {
		return ListTypeEnum.LIMPEZA;
	}

	@Override
	public void generateList() throws ListBuilderException {
		try {
			logInit(log);
			
			var dto = getFileInputDataDTO(properties, FileInputDataLimpezaDTO.class);

			var dateServiceInputDto = LimpezaValidator.validAndConvertData(dto);

			var listDates = dateService.generateListDatesLimpeza(dateServiceInputDto, properties.getLayoutLimpeza());
			var listGroups = groupService.generateListGroupsLimpeza(dto, listDates, properties.getLayoutLimpeza());
			var listGenerated = generateFinalList(listDates, listGroups, properties.getLayoutLimpeza());

			writerService.writerPDF(listGenerated, dto.getFooterMessage(), dto.getHeaderMessage(),
					properties.getLayoutLimpeza());
			
			notificationService.limpeza(listGenerated, properties.getLayoutLimpeza());

			logFinish(log);

		} catch (Exception e) {
			throw defaultListBuilderException(e);
		}
	}

	private FinalListLimpezaDTO generateFinalList(List<ItemDateDTO> listDates, List<String> listGroups,
                                                  int layout) throws ListBuilderException {

		if (listDates.isEmpty()) {
			throw new ListBuilderException("Lista de Datas e/ou Lista de Grupos VAZIA!");
		}

		if (layout == 2) {
			return generateFinalListLayout2(listDates, listGroups);
		}
		return generateFinalListLayout1(listDates, listGroups);
	}

	private FinalListLimpezaDTO generateFinalListLayout1(List<ItemDateDTO> listDates, List<String> listGroups)
			throws ListBuilderException {
		var list = new ArrayList<FinalListLimpezaItemDTO>();

		for (int i = 0; i < listDates.size(); i++) {
			var label = getLabel(listDates.get(i), true);
			var item = new FinalListLimpezaItemDTO(listDates.get(i).getDate(), label, listGroups.get(i));
			list.add(item);
		}

		return FinalListLimpezaDTO.builder().items(list).build();
	}

	private FinalListLimpezaDTO generateFinalListLayout2(List<ItemDateDTO> listDates, List<String> listGroups)
			throws ListBuilderException {

		var list = new ArrayList<FinalListLimpezaItemLayout2DTO>();

		Map<Integer, List<ItemDateDTO>> mapDatesByOrdinal = listDates.stream()
				.collect(Collectors.groupingBy(ItemDateDTO::getOrdinal));

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

			// @formatter:off
			var item = FinalListLimpezaItemLayout2DTO.builder()
					.withGroup(listGroups.get(i))
					.withDate1(date1).withLabel1(label1)
					.withDate2(date2).withLabel2(label2).build();
			// @formatter:on

			list.add(item);
		}

		return FinalListLimpezaDTO.builder().itemsLayout2(list).build();
	}

	public String getLabel(ItemDateDTO item, boolean showLabelMessage) throws ListBuilderException {
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
