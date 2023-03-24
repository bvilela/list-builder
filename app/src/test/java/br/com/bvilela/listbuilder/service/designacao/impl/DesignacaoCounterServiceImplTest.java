package br.com.bvilela.listbuilder.service.designacao.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.bvilela.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;

@SpringBootApplication
class DesignacaoCounterServiceImplTest {

	@InjectMocks
	private DesignacaoCounterServiceImpl service;
	
	private final String p1 = "Person 1";
	private final String p2 = "Person 2";
	private final String p3 = "Person 3";
	private final String p4 = "Person 4";

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		service = new DesignacaoCounterServiceImpl();
	}
	
	@Test
	void shouldCountNumberActiviesByName() {
		var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		Assertions.assertDoesNotThrow(() -> service.countNumberActiviesByName(dto));
	}
	
	@Test
	void shouldCount() {
		var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		List<DesignacaoWriterItemDTO> listAux = new ArrayList<>();
		addItem(listAux, p1, 3);
		addItem(listAux, p2, 2);
		addItem(listAux, p3, 1);
		addItem(listAux, p4, 1);
		dto.setPresident(listAux);
		var map = service.count(dto.getPresident());
		Assertions.assertEquals(3, map.get(p1));
		Assertions.assertEquals(2, map.get(p2));
		Assertions.assertEquals(1, map.get(p3));
		Assertions.assertEquals(1, map.get(p4));
	}
	
	@Test
	void shouldCountSplit() {
		var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
		List<DesignacaoWriterItemDTO> listAux = new ArrayList<>();
		addItemTwoPeople(listAux, p1, p4, 4);
		addItemTwoPeople(listAux, p2, p3, 1);
		addItemTwoPeople(listAux, p3, p3, 2);
		addItemTwoPeople(listAux, p4, p1, 1);
		dto.setAudioVideo(listAux);
		
		// valid Person1
		var map1 = service.countWithSplit(dto.getAudioVideo(), 0);
		Assertions.assertEquals(4, map1.get(p1));
		Assertions.assertEquals(1, map1.get(p2));
		Assertions.assertEquals(2, map1.get(p3));
		Assertions.assertEquals(1, map1.get(p4));
		
		// valid Person2
		var map2 = service.countWithSplit(dto.getAudioVideo(), 1);
		Assertions.assertEquals(1, map2.get(p1));
		Assertions.assertNull(map2.get(p2));
		Assertions.assertEquals(3, map2.get(p3));
		Assertions.assertEquals(4, map2.get(p4));
	}
	
	@Test
	void shouldMergeMapCase1() {
		var map1 = Map.of(p1, 1l, p2, 2l, p3, 3l, p4, 4l);
		var map2 = Map.of(p1, 1l, p4, 4l);
		
		// valid Person1
		var mergedMap = service.mergeMap(map1, map2);
		Assertions.assertEquals(2, mergedMap.get(p1));
		Assertions.assertEquals(2, mergedMap.get(p2));
		Assertions.assertEquals(3, mergedMap.get(p3));
		Assertions.assertEquals(8, mergedMap.get(p4));
	}
	
	@Test
	void shouldMergeMapCase2() {
		var map1 = Map.of(p1, 1l, p3, 3l);
		var map2 = Map.of(p1, 1l, p4, 4l);
		
		// valid Person1
		var mergedMap = service.mergeMap(map1, map2);
		Assertions.assertEquals(2, mergedMap.get(p1));
		Assertions.assertNull(mergedMap.get(p2));
		Assertions.assertEquals(3, mergedMap.get(p3));
		Assertions.assertEquals(4, mergedMap.get(p4));
	}
	
	private void addItem(List<DesignacaoWriterItemDTO> listAux, String name, int number) {
		for (int i = 0; i < number; i++) {
			listAux.add(new DesignacaoWriterItemDTO(LocalDate.now(), name));
		}
	}
	
	private void addItemTwoPeople(List<DesignacaoWriterItemDTO> listAux, String name1, String name2, int number) {
		for (int i = 0; i < number; i++) {
			listAux.add(new DesignacaoWriterItemDTO(LocalDate.now(), name1.concat(" e ").concat(name2)));
		}
	}
	
}
