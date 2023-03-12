package com.bruno.listbuilder.builder;

import com.bruno.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;

public class FileInputDataAssistenciaDtoBuilder {
	
	private static final String LAST_DATE_DEFAULT = "29-03-2022";

	private FileInputDataAssistenciaDTO target;

    public FileInputDataAssistenciaDtoBuilder() {
        this.target = new FileInputDataAssistenciaDTO();
    }

    public static FileInputDataAssistenciaDtoBuilder create() {
        return new FileInputDataAssistenciaDtoBuilder();
    }

    public FileInputDataAssistenciaDTO build() {
        return target;
    }
    
	public FileInputDataAssistenciaDtoBuilder withLastDateNull() {
		return baseLastDate(null);
	}
	
	public FileInputDataAssistenciaDtoBuilder withLastDateEmpty() {
		return baseLastDate("");
	}
	
	public FileInputDataAssistenciaDtoBuilder withLastDateBlank() {
		return baseLastDate(" ");
	}
	
	public FileInputDataAssistenciaDtoBuilder withLastDateInvalid() {
		return baseLastDate("01-13-2022");
	}
    
    public FileInputDataAssistenciaDtoBuilder withMidweekNull() {
    	return baseMidweekDay(null);
	}
    
    public FileInputDataAssistenciaDtoBuilder withMidweekEmpty() {
    	return baseMidweekDay("");
	}
    
    public FileInputDataAssistenciaDtoBuilder withMidweekBlank() {
    	return baseMidweekDay(" ");
	}
    
    public FileInputDataAssistenciaDtoBuilder withMidweekInvalid() {
    	return baseMidweekDay("tercaaa");
	}
    
    public FileInputDataAssistenciaDtoBuilder withWeekendNull() {
    	return baseWeekendDay(null);
	}
    
    public FileInputDataAssistenciaDtoBuilder withWeekendEmpty() {
    	return baseWeekendDay("");
	}
    
    public FileInputDataAssistenciaDtoBuilder withWeekendBlank() {
    	return baseWeekendDay(" ");
	}
    
    public FileInputDataAssistenciaDtoBuilder withWeekendInvalid() {
    	return baseWeekendDay("sabadooo");
	}
    
    public FileInputDataAssistenciaDtoBuilder withSuccess() {
    	return base(LAST_DATE_DEFAULT, "terça", "sábado");
    }
    
    private FileInputDataAssistenciaDtoBuilder baseMidweekDay(String midweekDay) {
    	return base(LAST_DATE_DEFAULT, midweekDay, "sábado");
    }
    
    private FileInputDataAssistenciaDtoBuilder baseWeekendDay(String weekendDay) {
    	return base(LAST_DATE_DEFAULT, "terça", weekendDay);
    } 
    
    private FileInputDataAssistenciaDtoBuilder baseLastDate(String lastDate) {
    	return base(lastDate, "terça", "sábado");
    }
    
    private FileInputDataAssistenciaDtoBuilder base(String lastDate, String dayMidweek, String dayWeekend) {
    	this.withLastDate(lastDate);
    	this.withMeetingDayMidweek(dayMidweek);
    	this.withMeetingDayWeekend(dayWeekend);
        return this;
    }

    private FileInputDataAssistenciaDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }
    
    private FileInputDataAssistenciaDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }
    
    private FileInputDataAssistenciaDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }
	
}
