package com.bruno.listbuilder.dto;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
public class ItemDateDTO {
	
	private Integer ordinal;
	
	private LocalDate date;
	
	private String message;
	
	public boolean isException() {
		return Objects.nonNull(message) && !message.isBlank();
	}
	
	public ItemDateDTO(LocalDate date) {
		this.date = date;
	}
	
	public ItemDateDTO(LocalDate date, String message) {
		this.date = date;
		this.message = message;
	}
	
	public ItemDateDTO(int ordinal, ItemDateDTO dto) {
		this.ordinal = ordinal;
		this.date = dto.getDate();
		this.message = dto.getMessage();
	}
	
	public ItemDateDTO(int ordinal, LocalDate date) {
		this.ordinal = ordinal;
		this.date = date;
	}

}
