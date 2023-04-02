package br.com.bvilela.listbuilder.service.vidacrista.impl;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.service.BaseGenerateService;
import br.com.bvilela.listbuilder.service.vidacrista.VidaCristaExtractService;
import br.com.bvilela.listbuilder.service.vidacrista.VidaCristaWriterService;
import br.com.bvilela.listbuilder.validator.VidaCristaValidator;
import br.com.bvilela.listbuilder.dto.vidacrista.FileInputDataVidaCristaDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.FileInputDataVidaCristaRenameItemDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import br.com.bvilela.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.enuns.VidaCristaExtractItemType;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.service.NotificationService;
import br.com.bvilela.listbuilder.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@Service("VIDA_CRISTA")
@RequiredArgsConstructor
public class VidaCristaGenerateServiceImpl implements BaseGenerateService {

	private Map<String, String> abbreviationMap;

	private final AppProperties properties;
	private final VidaCristaExtractService extractService;
	private final VidaCristaWriterService writerService;
	private final NotificationService notificationService;

	@Override
	public ListTypeEnum getListType() {
		return ListTypeEnum.VIDA_CRISTA;
	}

	@Override
	public void generateList() throws ListBuilderException {
		try {
			logInit(log);
			
			var dto = getFileInputDataDTO(properties, FileInputDataVidaCristaDTO.class);

			VidaCristaValidator.validInputDto(dto);
			this.abbreviationMap = new LinkedHashMap<>(dto.getAbbreviations());

			var url = extractService.getUrlMeetingWorkbook(dto.getLastDateConverted());
			var listWeeks = extractService.extractWeeksBySite(url);

			listWeeks = adjustListByLastDate(listWeeks, dto);

			extractService.extractWeekItemsBySite(listWeeks);
			
			renameItems(listWeeks, dto.getRenameItems());

			populateExtractListWithParticipants(listWeeks, dto.getParticipants());

			writerService.writerPDF(listWeeks);
			
			notificationService.vidaCrista(listWeeks);
			
			logFinish(log);
			
		} catch (Exception e) {
			throw defaultListBuilderException(e);
		}
	}

	private ArrayList<VidaCristaExtractWeekDTO> adjustListByLastDate(List<VidaCristaExtractWeekDTO> listWeeks,
			FileInputDataVidaCristaDTO dto) {
		var lastDate = dto.getLastDateConverted();
		var mapRemove = dto.getRemoveWeekFromListConverted();
		
		var nextMonth = DateUtils.nextDayOfWeek(lastDate, DayOfWeek.MONDAY).plusMonths(1);
		nextMonth = nextMonth.withDayOfMonth(1);
		var newListWeeks = new ArrayList<VidaCristaExtractWeekDTO>();
		for (VidaCristaExtractWeekDTO item: listWeeks) {
			if (item.getDate1().isAfter(lastDate) && item.getDate1().isBefore(nextMonth)) {
				newListWeeks.add(item);
			}
		}
		
		for (VidaCristaExtractWeekDTO week: newListWeeks) {
			if (Objects.nonNull(mapRemove)) {
				for (Entry<LocalDate, String> remove: mapRemove.entrySet()) {
					if (week.getDate1().compareTo(remove.getKey()) <= 0 && week.getDate2().compareTo(remove.getKey()) >= 0) {
						week.setSkip(true);
						week.setSkipMessage(remove.getValue());
					}
				}
			}
		}
		
		var dates = newListWeeks.stream().map(VidaCristaExtractWeekDTO::getLabelDate).toList();
		log.info("Datas: {}", dates);
		return newListWeeks;
	}

	private void populateExtractListWithParticipants(List<VidaCristaExtractWeekDTO> listWeeks,
			List<List<String>> listParticipants) throws ListBuilderException {
		
		if (listWeeks.size() != listParticipants.size()) {
			throw new ListBuilderException(
				"Quantidade de semanas extraída do site é diferente da quantidade de semanas com participantes");
		}
		
		int weekIndex = 0;
		for (VidaCristaExtractWeekDTO week : listWeeks) {
			if (week.isSkip()) {
				weekIndex++;
				continue;
			}
			log.info("Adicionando Participantes da Semana {}", week.getLabelDate());
			var participantsOfWeek = listParticipants.get(weekIndex);
			addParticipantsOfWeek(week, participantsOfWeek);
			weekIndex++;
		}
	}

	private void addParticipantsOfWeek(VidaCristaExtractWeekDTO week, List<String> participantsOfWeek) {
		int participantsIndex = 0;

		for (VidaCristaExtractWeekItemDTO item : week.getItems()) {

			if (item.getType().isHasParticipants()) {
				String participant = participantsOfWeek.get(participantsIndex);
				item.setParticipants(List.of(getAbbreviation(participant)));
				participantsIndex++;
			}

		}
	}

	private String getAbbreviation(String abbreviate) {
		var name = this.abbreviationMap.get(abbreviate);
		return (name != null && !name.isBlank()) ? name : abbreviate;
	}
	
	private void renameItems(List<VidaCristaExtractWeekDTO> listWeeks,
			List<FileInputDataVidaCristaRenameItemDTO> renameItems) {
		
		if (Objects.isNull(renameItems) || renameItems.isEmpty()) {
			return;
		}

		int weekIndex = 0;
		for (VidaCristaExtractWeekDTO week : listWeeks) {
			if (week.isSkip()) {
				weekIndex++;
				continue;
			}
			
			checkRenameItemFromWeek(week, renameItems, weekIndex);
			weekIndex++;
		}
		
	}

	public void checkRenameItemFromWeek(VidaCristaExtractWeekDTO week,
			List<FileInputDataVidaCristaRenameItemDTO> renameItems, final int weekIndex) {

		var renameItemsThisWeek = renameItems.stream().filter(e -> e.getWeekIndex()-1 == weekIndex).toList();
		if (renameItemsThisWeek.isEmpty()) {
			return;
		}
		
		var itemsCanRenamed = week.getItems().stream()
				.filter(e -> e.getType() == VidaCristaExtractItemType.WITH_PARTICIPANTS).toList();
		
		var listWeekItemToRemove = new ArrayList<VidaCristaExtractWeekItemDTO>();
		for (VidaCristaExtractWeekItemDTO item : itemsCanRenamed) {
			for (FileInputDataVidaCristaRenameItemDTO renameItem : renameItemsThisWeek) {
				
				if (item.getTitle().equalsIgnoreCase(renameItem.getOriginalName())) {
					processRenameOrRemoveItem(item, renameItem, listWeekItemToRemove);
				}
			}
		}
		if (!listWeekItemToRemove.isEmpty()) {
			var auxList = new ArrayList<>(week.getItems());
			auxList.removeAll(listWeekItemToRemove);
			week.setItems(auxList);
		}
	}

	public void processRenameOrRemoveItem(VidaCristaExtractWeekItemDTO item, FileInputDataVidaCristaRenameItemDTO renameItem,
			List<VidaCristaExtractWeekItemDTO> listWeekItemToRemove) {
		var newName = renameItem.getNewName(); 
		if (Objects.isNull(newName) || newName.isBlank()) {
			listWeekItemToRemove.add(item);
		} else {
			item.setTitle(newName);	
		}
	}

}
