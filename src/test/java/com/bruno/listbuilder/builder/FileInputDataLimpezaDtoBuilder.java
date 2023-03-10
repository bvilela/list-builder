package com.bruno.listbuilder.builder;

import java.util.List;
import java.util.Map;

import com.bruno.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;

public class FileInputDataLimpezaDtoBuilder {
	
	private static final String LAST_DATE_DEFAULT = "29-03-2022";

	private final Map<Integer, String> groupsDefault = 
		Map.of(
    		1, "Group 1 (Person1, Person2 Person3)",
    		2, "Group 2 (Person3, Person4, Person5, Person6)",
    		3, "Group 3 (Person7, Person8, Person9, Person10, Person11)",
    		4, "Group 4 (Person12, Person13, Person14, Person15, Person16)",
    		5, "Group 5 (Person17, Person18, Person19, Person20, Person21)",
    		6, "Group 6 (Person22, Person23, Person24, Person25, Person26, Person27)",
    		7, "Group 7 (Person28, Person29, Person30, Person31, Person32)",
    		8, "Group 8 (Person33, Person34, Person35, Person36)"
    	);

	private FileInputDataLimpezaDTO target;

    public FileInputDataLimpezaDtoBuilder() {
        this.target = new FileInputDataLimpezaDTO();
    }

    public static FileInputDataLimpezaDtoBuilder create() {
        return new FileInputDataLimpezaDtoBuilder();
    }

    public FileInputDataLimpezaDTO build() {
        return target;
    }
    
	public FileInputDataLimpezaDtoBuilder withLastDateNull() {
		return baseLastDate(null);
	}
	
	public FileInputDataLimpezaDtoBuilder withLastDateEmpty() {
		return baseLastDate("");
	}
	
	public FileInputDataLimpezaDtoBuilder withLastDateBlank() {
		return baseLastDate(" ");
	}
	
	public FileInputDataLimpezaDtoBuilder withLastDateInvalid() {
		return baseLastDate("01-13-2022");
	}
    
    public FileInputDataLimpezaDtoBuilder withLastGroupNull() {
    	return baseLastGroup(null);
	}
    
    public FileInputDataLimpezaDtoBuilder withLastGroupInvalid() {
    	return baseLastGroup(0);
	}
    
    public FileInputDataLimpezaDtoBuilder withGroupsNull() {
    	return base(LAST_DATE_DEFAULT, 8, "terça", "sábado", null);
	}
    
    public FileInputDataLimpezaDtoBuilder withMidweekNull() {
    	return baseMidweekDay(null);
	}
    
    public FileInputDataLimpezaDtoBuilder withMidweekEmpty() {
    	return baseMidweekDay("");
	}
    
    public FileInputDataLimpezaDtoBuilder withMidweekBlank() {
    	return baseMidweekDay(" ");
	}
    
    public FileInputDataLimpezaDtoBuilder withMidweekInvalid() {
    	return baseMidweekDay("tercaaa");
	}
    
    public FileInputDataLimpezaDtoBuilder withWeekendNull() {
    	return baseWeekendDay(null);
	}
    
    public FileInputDataLimpezaDtoBuilder withWeekendEmpty() {
    	return baseWeekendDay("");
	}
    
    public FileInputDataLimpezaDtoBuilder withWeekendBlank() {
    	return baseWeekendDay(" ");
	}
    
    public FileInputDataLimpezaDtoBuilder withWeekendInvalid() {
    	return baseWeekendDay("sabadooo");
	}
    
    public FileInputDataLimpezaDtoBuilder withSuccess() {
    	return base(LAST_DATE_DEFAULT, 1, "terça", "sábado", groupsDefault);
    }
    
    private FileInputDataLimpezaDtoBuilder baseMidweekDay(String midweekDay) {
    	return base(LAST_DATE_DEFAULT, 1, midweekDay, "sábado", groupsDefault);
    }
    
    private FileInputDataLimpezaDtoBuilder baseWeekendDay(String weekendDay) {
    	return base(LAST_DATE_DEFAULT, 1, "terça", weekendDay, groupsDefault);
    } 
    
    private FileInputDataLimpezaDtoBuilder baseLastDate(String lastDate) {
    	return base(lastDate, 1, "terça", "sábado", groupsDefault);
    }
    
    private FileInputDataLimpezaDtoBuilder baseLastGroup(Integer lastGroup) {
    	return base(LAST_DATE_DEFAULT, lastGroup, "terça", "sábado", groupsDefault);
    }
    
    private FileInputDataLimpezaDtoBuilder base(String lastDate, Integer lastGroup, String dayMidweek, String dayWeekend, Map<Integer, String> groups) {
    	this.withLastDate(lastDate);
    	this.withLastGroup(lastGroup);
    	this.withMeetingDayMidweek(dayMidweek);
    	this.withMeetingDayWeekend(dayWeekend);
    	this.withGroups(groups);
    	this.withHeaderMessage("Header Message");
    	this.withFooterMessage("Footer Message");
    	this.withRemoveFromList(null);
    	this.withAddToList(null);
        return this;
    }

    private FileInputDataLimpezaDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }
    
    private FileInputDataLimpezaDtoBuilder withLastGroup(Integer lastGroup) {
        this.target.setLastGroup(lastGroup);
        return this;
    }
    
    private FileInputDataLimpezaDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }
    
    private FileInputDataLimpezaDtoBuilder withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
        return this;
    }
    
    private FileInputDataLimpezaDtoBuilder withGroups(Map<Integer, String> groups) {
        this.target.setGroups(groups);
        return this;
    }
    
    private FileInputDataLimpezaDtoBuilder withHeaderMessage(String headerMessage) {
        this.target.setHeaderMessage(headerMessage);
        return this;
    }
    
    private FileInputDataLimpezaDtoBuilder withFooterMessage(String footerMessage) {
        this.target.setFooterMessage(footerMessage);
        return this;
    }
    
    public FileInputDataLimpezaDtoBuilder withRemoveFromList(List<String> removeFromList) {
        this.target.setRemoveFromList(removeFromList);
        return this;
    }
    
    public FileInputDataLimpezaDtoBuilder withAddToList(Map<String, String> addToList) {
        this.target.setAddToList(addToList);
        return this;
    }
	
}
