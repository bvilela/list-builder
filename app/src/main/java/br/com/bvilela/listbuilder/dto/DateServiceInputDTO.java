package br.com.bvilela.listbuilder.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import br.com.bvilela.listbuilder.utils.DateUtils;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DateServiceInputDTO {

	private LocalDate lastDate;
	private DayOfWeekEnum midweekDayWeekEnum;
	private DayOfWeekEnum weekendDayWeekEnum;
	private List<LocalDate> removeFromList;
	private Map<LocalDate, String> addToList;

	public DateServiceInputDTO(FileInputDataLimpezaDTO dto, List<LocalDate> removeFromList,
                               Map<LocalDate, String> addToList) {
		baseFileInputData(dto);
		this.removeFromList = removeFromList;
		this.addToList = addToList;
	}
	
	public DateServiceInputDTO(BaseFileInputDataDTO dto) {
		baseFileInputData(dto);
	}
	
	private void baseFileInputData(BaseFileInputDataDTO dto) {
		this.lastDate = DateUtils.parse(dto.getLastDate());
		this.midweekDayWeekEnum = DayOfWeekEnum.valueOf(dto.getMeetingDayMidweek().toUpperCase());
		this.weekendDayWeekEnum = DayOfWeekEnum.valueOf(dto.getMeetingDayWeekend().toUpperCase());
	}
	
	public DateServiceInputDTO(LocalDate lastDate, DateServiceInputDTO dto) {
		this.lastDate = lastDate;
		this.midweekDayWeekEnum = dto.getMidweekDayWeekEnum();
		this.weekendDayWeekEnum = dto.getWeekendDayWeekEnum();
	}

}
