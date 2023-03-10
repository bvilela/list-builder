package com.bruno.listbuilder.service.limpeza.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.ItemDateDTO;
import com.bruno.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemDTO;
import com.bruno.listbuilder.dto.limpeza.FinalListLimpezaItemLayout2DTO;
import com.bruno.listbuilder.enuns.DayOfWeekEnum;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.BaseGenerateService;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.service.DateService;
import com.bruno.listbuilder.service.GroupService;
import com.bruno.listbuilder.service.NotificationService;
import com.bruno.listbuilder.service.limpeza.LimpezaWriterService;
import com.bruno.listbuilder.validator.LimpezaValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LimpezaGenerateServiceImpl implements BaseGenerateService {

	private AppProperties properties;

	private LimpezaWriterService writerService;
	private DateService dateService;
	private GroupService groupService;
	private NotificationService notificationService;
	private ConvertImageService convertImageService;

	public LimpezaGenerateServiceImpl(AppProperties properties, DateService dateService,
			GroupService groupService, LimpezaWriterService writerService,
			NotificationService notificationService, ConvertImageService convertImageService) {
		this.properties = properties;
		this.dateService = dateService;
		this.groupService = groupService;
		this.writerService = writerService;
		this.notificationService = notificationService;
		this.convertImageService = convertImageService;
	}

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

			var pathPdf = writerService.writerPDF(listGenerated, dto.getFooterMessage(), dto.getHeaderMessage(),
					properties.getLayoutLimpeza());
			
			convertImageService.convertToImage(pathPdf);

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
				showLabel = true;
				date2 = dates.get(1).getDate();
				label2 = getLabel(dates.get(1), showLabel);
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
				return String.format("%s - Ap??s a Reuni??o", dayWeekEnum.getName());
			}
		}
		return dayWeekEnum.getName();
	}

}
