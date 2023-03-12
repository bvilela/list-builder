package com.bruno.listbuilder.service.designacao.impl;

import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterDTO;
import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import com.bruno.listbuilder.service.designacao.DesignacaoCounterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class DesignacaoCounterServiceImpl implements DesignacaoCounterService {
	
	private static final int COLUMN_SIZE = 11;
	private static final int MIN_NAME_SIZE_COLUMN = 12;
	private static final int NUMBER_COLUMNS_WITHOUT_NAMES = 9;
	
	private record CounterDto(Map<String, Long> readerGeral, Map<String, Long> readerBibleStudy,
			Map<String, Long> readerWatchtower, Map<String, Long> audioVideoGeral,
			Map<String, Long> audioVideoPrincipal, Map<String, Long> audioVideoHelper, Map<String, Long> president,
			Map<String, Long> indicator, Map<String, Long> microphone, Map<String, Long> indicatorAndMicrophone) {		
	}

	@Override
	public StringBuilder countNumberActiviesByName(DesignacaoWriterDTO dto) {
		
		// Readers
		var readerBibleStudy = count(dto.getReaderBibleStudy());
		var readerWatchtower = count(dto.getReaderWatchtower());
		var readerGeneral = mergeMap(readerBibleStudy, readerWatchtower);
		
		// AudioVideo
		var audioVideoPrincipal = countWithSplit(dto.getAudioVideo(), 0);
		var audioVideoHelper = countWithSplit(dto.getAudioVideo(), 1);
		var audioVideoGeneral = mergeMap(audioVideoPrincipal, audioVideoHelper);
		
		// President
		var president = count(dto.getPresident());
		
		// Indicator
		var indicatorName1 = countWithSplit(dto.getIndicator(), 0);
		var indicatorName2 = countWithSplit(dto.getIndicator(), 1);
		var indicatorGeneral = mergeMap(indicatorName1, indicatorName2);
		
		// Microphone
		var microphoneName1 = countWithSplit(dto.getMicrophone(), 0);
		var microphoneName2 = countWithSplit(dto.getMicrophone(), 1);
		var microphoneGeneral = mergeMap(microphoneName1, microphoneName2);
		
		// Indicator and Microphone
		var indicatorAndMicrophone = mergeMap(indicatorGeneral, microphoneGeneral);
		
		var countersDto = new CounterDto(readerGeneral, readerBibleStudy, readerWatchtower, audioVideoGeneral,
				audioVideoPrincipal, audioVideoHelper, president, indicatorGeneral, microphoneGeneral,
				indicatorAndMicrophone);
		
		return printCounterTable(countersDto);
	}

	private StringBuilder printCounterTable(CounterDto dto) {
		
		var listNames = getListNames(dto);
		
		int maxNameSize = getMaxNameSize(listNames);
		
		var divider = "|" + StringUtils.leftPad("-", NUMBER_COLUMNS_WITHOUT_NAMES * (COLUMN_SIZE + 1) + maxNameSize, "-") + "|";
		
		StringBuilder fileSB = new StringBuilder();
		
		// Title
		addLineInLogAndFile(fileSB, divider);
		var titleLine = getTitleLine(divider);
		addLineInLogAndFile(fileSB, titleLine);
		addLineInLogAndFile(fileSB, divider);
		
		// Header Line 1
		var lineHeader1 = getLineHeader1(maxNameSize);
		addLineInLogAndFile(fileSB, lineHeader1);

		// Header Line 2
		var lineHeader2 = getLineHeader2(maxNameSize);
		addLineInLogAndFile(fileSB, lineHeader2);
		
		// Divider Header/Data
		var divider2 = getDividerHeaderData(maxNameSize);
		addLineInLogAndFile(fileSB, divider2);
		
		for (String name : listNames) {
			var lineData = getLineData(dto, name, maxNameSize);
			addLineInLogAndFile(fileSB, lineData);
		}
		
		addLineInLogAndFile(fileSB, divider);
		
		return fileSB;
	}

	private List<String> getListNames(CounterDto dto) {
		var allList = Stream.of(dto.readerGeral, dto.audioVideoGeral, dto.indicatorAndMicrophone, dto.president).toList();
		HashSet<String> setNames = new HashSet<>();
		allList.stream().forEach(map -> map.forEach((k, v) -> setNames.add(k)));
		return setNames.stream().sorted().toList();
	}
	
	private int getMaxNameSize(List<String> listNames) {
		int maxNameSize = listNames.stream().mapToInt(String::length).max().getAsInt() + 2;
		
		if (maxNameSize < MIN_NAME_SIZE_COLUMN) {
			return MIN_NAME_SIZE_COLUMN;
		}
		return maxNameSize;
	}
	
	private String getTitleLine(String divider) {
		var titleLine = StringUtils.center("Contagem Participações", (divider.length() - 2), " ");
		return String.format("|%s|", titleLine);
	}
	
	private String getLineHeader1(int maxNameSize) {
		var size = COLUMN_SIZE * 3 + 2;
		var labelName = StringUtils.center("Nome", maxNameSize, " ");
		var subtitle1LineC1 = StringUtils.center("Outros", size, " ");
		var subtitle1LineC2 = StringUtils.center("Audio e Video", size, " ");
		var subtitle1LineC3 = StringUtils.center("Leitores", size, " ");		
		return String.format("|%s|%s|%s|%s|", labelName, subtitle1LineC1, subtitle1LineC2, subtitle1LineC3);
	}

	private String getLineHeader2(int maxNameSize) {
		// Indicator e Microphone 
		var indicator = StringUtils.center("Indicador", COLUMN_SIZE, " ");
		var microphone = StringUtils.center("Microfone", COLUMN_SIZE, " ");
		var indicatorAndMicrophone = StringUtils.center("Ind/Mic", COLUMN_SIZE, " ");

		// AudioVideo
		var audioVideoPrimary = StringUtils.center("Principal", COLUMN_SIZE, " ");
		var audioVideoSecond = StringUtils.center("Ajudante", COLUMN_SIZE, " ");
		var audioVideoGeral = StringUtils.center("Geral", COLUMN_SIZE, " ");
		
		// Readers
		var readerWatch = StringUtils.center("Sentinela", COLUMN_SIZE, " ");
		var readerBibleStudy = StringUtils.center("Livro", COLUMN_SIZE, " ");
		var readersGeral = StringUtils.center("Geral", COLUMN_SIZE, " ");
		var publicador = StringUtils.center("Publicador", maxNameSize, " ");
		
		return String.format("|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", publicador, indicator, microphone, indicatorAndMicrophone,
				audioVideoPrimary, audioVideoSecond, audioVideoGeral, readerBibleStudy, readerWatch, readersGeral);
	}
	
	private String getLineData(CounterDto dto, String name, int maxNameSize) {
		var labelPubName = StringUtils.center(name, maxNameSize, " ");
		var labelInd = getLabel(dto.indicator, name);
		var labelMic = getLabel(dto.microphone, name);
		var labelIndMic = getLabel(dto.indicatorAndMicrophone, name);

		var labelAVPrimary = getLabel(dto.audioVideoPrincipal, name);
		var labelAVSecond = getLabel(dto.audioVideoHelper, name);
		var labelAVGeral = getLabel(dto.audioVideoGeral, name);

		var labelReaderBook = getLabel(dto.readerBibleStudy, name);
		var labelReaderWatch = getLabel(dto.readerWatchtower, name);
		var labelReaderGeral = getLabel(dto.readerGeral, name);

		return String.format("|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", labelPubName, labelInd, labelMic, labelIndMic, labelAVPrimary,
				labelAVSecond, labelAVGeral, labelReaderBook, labelReaderWatch, labelReaderGeral);		
	}
	
	private String getLabel(Map<String, Long> map, String key) {
		var value = map.get(key);
		var valueString = Objects.isNull(value) ? "-" : value.toString();
		return StringUtils.center(String.valueOf(valueString), COLUMN_SIZE, " ");		
	}

	public Map<String, Long> count(List<DesignacaoWriterItemDTO> list) {
		return list.stream().collect(Collectors.groupingBy(DesignacaoWriterItemDTO::getName,
				Collectors.mapping(DesignacaoWriterItemDTO::getName, Collectors.counting())));
	}
	
	public Map<String, Long> countWithSplit(List<DesignacaoWriterItemDTO> list, int index) {
		return list.stream().collect(Collectors.groupingBy(e -> e.getName().split(" e ")[index],
				Collectors.mapping(t -> t.getName().split(" e ")[index], Collectors.counting())));
	}
	
	public Map<String, Long> mergeMap(Map<String, Long> map1, Map<String, Long> map2) {
		Map<String, Long> mergedMap = new HashMap<>();
		var map2Aux = new HashMap<String, Long>(map2);
		map1.forEach((key, value) -> {
			var valueMap2 = map2Aux.remove(key);
			var safeValueMap2 = Objects.isNull(valueMap2) ? 0 : valueMap2; 
			mergedMap.put(key, value + safeValueMap2);
		});
		
		if (!map2Aux.isEmpty()) {
			map2Aux.forEach(mergedMap::put);
		}
		
		return mergedMap;
	}
	
	private void addLineInLogAndFile(StringBuilder sb, String value) {
		log.info(value);
		sb.append(value);
		sb.append(System.lineSeparator());
	}
	
	private String getDividerHeaderData(int maxNameSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		sb.append(StringUtils.leftPad("-", maxNameSize, "-"));
		sb.append("|");
		for (int i = 0; i < NUMBER_COLUMNS_WITHOUT_NAMES; i++) {
			sb.append(StringUtils.leftPad("-", COLUMN_SIZE, "-"));
			sb.append("|");
		}
		return sb.toString();
	}
	
}
