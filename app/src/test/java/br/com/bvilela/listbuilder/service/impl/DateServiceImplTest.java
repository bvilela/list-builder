package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.dto.ItemDateDTO;
import br.com.bvilela.listbuilder.dto.assistencia.FileInputDataAssistenciaDTO;
import br.com.bvilela.listbuilder.dto.limpeza.FileInputDataLimpezaDTO;
import br.com.bvilela.listbuilder.dto.DateServiceInputDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
class DateServiceImplTest {

	@InjectMocks
	private DateServiceImpl service;

	@BeforeEach
	public void setup() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		service = new DateServiceImpl();
	}
	
	/** CreateItemDate*/
	private ItemDateDTO cid(int month, int day) {
		return new ItemDateDTO(LocalDate.of(2022, month, day));
	}
	
	/** CreateItemDate*/
	private ItemDateDTO cid(int month, int day, String ex) {
		return new ItemDateDTO(LocalDate.of(2022, month, day), ex);
	}
	
	/** CreateItemDate*/
	private List<ItemDateDTO> cid(int ordinal, int month1, int day1, int month2, int day2) {
		return List.of(
			new ItemDateDTO(ordinal, LocalDate.of(2022, month1, day1)), 
			new ItemDateDTO(ordinal, LocalDate.of(2022, month2, day2))
		);
	}
	
	/** CreateItemDate*/
	private List<ItemDateDTO> cid(int ordinal, int month1, int day1, String ex1, int month2, int day2, String ex2) {
		return List.of(
			new ItemDateDTO(ordinal, LocalDate.of(2022, month1, day1), ex1), 
			new ItemDateDTO(ordinal, LocalDate.of(2022, month2, day2), ex2)
		);
	}
	
	private List<LocalDate> listLocalDate(int[][] matrix) {
		var list = new ArrayList<LocalDate>();
		for (int i = 0; i < matrix.length; i++) {
			list.add(LocalDate.of(2022, matrix[i][0], matrix[i][1]));
		}
		return list;
	}

	@Test
	void validGenerateListDatesCase1() {
		// @formatter:off
		var expected = List.of(
			cid(4, 2),  cid(4, 5),  cid(4, 9), 
			cid(4, 12), cid(4, 16), cid(4, 19),
			cid(4, 23), cid(4, 26), cid(4, 30)
		);
		// @formatter:on		
		var fileInputData = new FileInputDataLimpezaDTO();
		fileInputData.setLastDate("29-03-2022");
		fileInputData.setMeetingDayMidweek("terca");
		fileInputData.setMeetingDayWeekend("sabado");
		var input = new DateServiceInputDTO(fileInputData, null, null);
		var dto = service.generateListDatesLimpeza(input, 1);
		Assertions.assertEquals(expected, dto);
		Assertions.assertFalse(fileInputData.toString().isBlank());
		Assertions.assertFalse(input.toString().isBlank());
	}

	@Test
	void validGenerateListDatesCase2() {
		// @formatter:off
		var expected = List.of(
			cid(4, 2), cid(4, 5), cid(4, 9),
			cid(4, 15, "exception1"), cid(4, 16), cid(4, 19), 
			cid(4, 23), cid(4, 26), cid(4, 30)
		);
		// @formatter:on		
		var fileInputData = new FileInputDataLimpezaDTO();
		fileInputData.setLastDate("29-03-2022");
		fileInputData.setMeetingDayMidweek("terca");
		fileInputData.setMeetingDayWeekend("sabado");
		var remove = List.of(LocalDate.of(2022, 4, 12));
		var add = Map.of(LocalDate.of(2022, 4, 15), "exception1");
		var input = new DateServiceInputDTO(fileInputData, remove, add);
		var dto = service.generateListDatesLimpeza(input, 1);
		Assertions.assertEquals(expected, dto);
		Assertions.assertFalse(expected.get(1).toString().isBlank());
	}

	@Test
	void validGenerateListDatesTwoDateCase1() {
		// @formatter:off
		var expected = new ArrayList<ItemDateDTO>();
		expected.addAll(cid(1, 4, 1, 4, 2));
		expected.addAll(cid(2, 4, 4, 4, 5));
		expected.addAll(cid(3, 4, 8, 4, 9));
		expected.addAll(cid(4, 4, 14, "exception1", 4, 15, "exception1"));
		expected.add(new ItemDateDTO(5, LocalDate.of(2022, 4, 16)));
		expected.addAll(cid(6, 4, 18, 4, 19));
		expected.addAll(cid(7, 4, 22, 4, 23));
		expected.addAll(cid(8, 4, 25, 4, 26));
		expected.addAll(cid(9, 4, 29, 4, 30));
		// @formatter:on
		var fileInputData = new FileInputDataLimpezaDTO();
		fileInputData.setLastDate("29-03-2022");
		fileInputData.setMeetingDayMidweek("terca");
		fileInputData.setMeetingDayWeekend("sabado");
		var remove = List.of(LocalDate.of(2022, 4, 12));
		var add = Map.of(LocalDate.of(2022, 4, 15), "exception1");
		var input = new DateServiceInputDTO(fileInputData, remove, add);
		var dto = service.generateListDatesLimpeza(input, 2);
		Assertions.assertEquals(expected, dto);
	}
	
	@Test
	void validGenerateListDatesAssistenciaMidMonth() {
		// @formatter:off
		var matrix = new int[][]{
			{6, 11}, {6, 14}, {6, 18}, {6, 21},
			{6, 25}, {6, 28}, {7, 2},  {7, 5},
			{7, 9},  {7, 12}, {7, 16}, {7, 19},
			{7, 23}, {7, 26}, {7, 30}
		};
		var expected = listLocalDate(matrix);
		
		// @formatter:on
		var fileInputData = new FileInputDataAssistenciaDTO();
		fileInputData.setLastDate("07-06-2022");
		fileInputData.setMeetingDayMidweek("terca");
		fileInputData.setMeetingDayWeekend("sabado");
		var input = new DateServiceInputDTO(fileInputData);
		var dto = service.generateListDatesAssistencia(input);
		Assertions.assertEquals(expected, dto);
	}
	
	@Test
	void validGenerateListDatesAssistenciaInitMonth() {
		// @formatter:off
		var matrix = new int[][]{
			{4, 2},  {4, 5},  {4, 9},  {4, 12},
			{4, 16}, {4, 19}, {4, 23}, {4, 26},
			{4, 30}, {5, 3},  {5, 7},  {5, 10},
			{5, 14}, {5, 17}, {5, 21}, {5, 24},
			{5, 28}, {5, 31}
		};
		var expected = listLocalDate(matrix);
		
		// @formatter:on
		var fileInputData = new FileInputDataAssistenciaDTO();
		fileInputData.setLastDate("29-03-2022");
		fileInputData.setMeetingDayMidweek("terca");
		fileInputData.setMeetingDayWeekend("sabado");
		var input = new DateServiceInputDTO(fileInputData);
		var dto = service.generateListDatesAssistencia(input);
		Assertions.assertEquals(expected, dto);
		Assertions.assertNotNull(fileInputData.toString());
		Assertions.assertNotNull(input.toString());
	}
	
	
	@Test
	void validGenerateListDatesDesignacaoCase1() {
		// @formatter:off
		var matrix = new int[][]{
			{11, 1},  {11, 5},  {11, 8},  {11, 12},
			{11, 15}, {11, 19}, {11, 22},  {11, 26},
			{11, 29}, {12, 3}
		};
		var expected = listLocalDate(matrix);
		
		// @formatter:on
		var fileInputData = new FileInputDataAssistenciaDTO();
		fileInputData.setLastDate("29-10-2022");
		fileInputData.setMeetingDayMidweek("terca");
		fileInputData.setMeetingDayWeekend("sabado");
		var input = new DateServiceInputDTO(fileInputData);
		var list = service.generateListDatesDesignacao(input);
		Assertions.assertEquals(expected, list);
		Assertions.assertNotNull(fileInputData.toString());
		Assertions.assertNotNull(input.toString());
	}
	
	@Test
	void validGenerateListDatesDesignacaoCase2() {
		// @formatter:off
		var matrix = new int[][]{
			{11, 2},  {11, 6},  {11, 9},  {11, 13},
			{11, 16}, {11, 20}, {11, 23},  {11, 27},
			{11, 30}, {12, 4}
		};
		var expected = listLocalDate(matrix);
		
		// @formatter:on
		var fileInputData = new FileInputDataAssistenciaDTO();
		fileInputData.setLastDate("29-10-2022");
		fileInputData.setMeetingDayMidweek("quarta");
		fileInputData.setMeetingDayWeekend("domingo");
		var input = new DateServiceInputDTO(fileInputData);
		var list = service.generateListDatesDesignacao(input);
		Assertions.assertEquals(expected, list);
	}
	
	@Test
	void validGenerateListDatesDesignacaoCase3() {
		// @formatter:off
		var matrix = new int[][]{
			{10, 5},  {10, 9},  {10, 12},  {10, 16},
			{10, 19}, {10, 23}, {10, 26},  {10, 30}
		};
		var expected = listLocalDate(matrix);
		
		// @formatter:on
		var fileInputData = new FileInputDataAssistenciaDTO();
		fileInputData.setLastDate("02-10-2022");
		fileInputData.setMeetingDayMidweek("quarta");
		fileInputData.setMeetingDayWeekend("domingo");
		var input = new DateServiceInputDTO(fileInputData);
		var list = service.generateListDatesDesignacao(input);
		Assertions.assertEquals(expected, list);
	}
}
