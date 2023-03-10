package com.bruno.listbuilder.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bruno.listbuilder.builder.FileInputDataLimpezaDtoBuilder;
import com.bruno.listbuilder.builder.designacao.DesignacaoWriterItemDtoBuilder;
import com.bruno.listbuilder.builder.designacao.FileInputDataDesignacaoDtoBuilder;
import com.bruno.listbuilder.config.MessageConfig;
import com.bruno.listbuilder.dto.ItemDateDTO;
import com.bruno.listbuilder.dto.designacao.FileInputDataDesignacaoListDTO;
import com.bruno.listbuilder.dto.designacao.writer.DesignacaoWriterItemDTO;
import com.bruno.listbuilder.enuns.DesignacaoEntityEnum;
import com.bruno.listbuilder.exception.ListBuilderException;

@SpringBootApplication
class GroupServiceImplTest {

	@InjectMocks
	private GroupServiceImpl service;
	
	private final List<ItemDateDTO> listItemDate = List.of(
			new ItemDateDTO(1, LocalDate.now()), new ItemDateDTO(2, LocalDate.now().plusDays(1)), 
			new ItemDateDTO(1, LocalDate.now().plusDays(1)), new ItemDateDTO(2, LocalDate.now().plusDays(1)));
	
	private final List<LocalDate> listLocalDate = List.of(LocalDate.now(), LocalDate.now().plusDays(1),
			LocalDate.now().plusDays(1), LocalDate.now().plusDays(1));

	@BeforeEach
	void setupBeforeEach() throws IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		service = new GroupServiceImpl();
	}

	
	// --------------------- LIMPEZA --------------------- \\	
	@Test
	void shouldGenerateListGroupExceptionLastGroupInvalid() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataLimpezaDtoBuilder.create().withLastGroupInvalid().build();
		var ex = assertThrows(ListBuilderException.class,
				() -> service.generateListGroupsLimpeza(dto, List.of(new ItemDateDTO(LocalDate.now())), 1));
		assertEquals(MessageConfig.LAST_GROUP_INVALID, ex.getMessage());
	}
	
	@Test
	void shouldGenerateListGroupLayout1Success() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataLimpezaDtoBuilder.create().withSuccess().build();
		dto.setLastDate("1");
		var list = service.generateListGroupsLimpeza(dto, listItemDate, 1);
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(4, list.size());
		assertTrue(list.get(0).contains("Group 2"));
		assertTrue(list.get(1).contains("Group 3"));
		assertTrue(list.get(2).contains("Group 4"));
		assertTrue(list.get(3).contains("Group 5"));
	}
	
	@Test
	void shouldGenerateListGroupLayout2Success() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataLimpezaDtoBuilder.create().withSuccess().build();
		dto.setLastGroup(dto.getGroups().size());
		var list = service.generateListGroupsLimpeza(dto, listItemDate, 2);
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		assertTrue(list.get(0).contains("Group 1"));
		assertTrue(list.get(1).contains("Group 2"));
	}
	
	
	// --------------------- PRESIDENT --------------------- \\
	@Test
	void shouldgenerateListDesignacaoPresidentException() throws IllegalAccessException, ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListPresident(null, null));
		assertTrue(ex.getMessage().contains(DesignacaoEntityEnum.PRESIDENT.getLabel()));
	}
	
	@Test
	void shouldgenerateListDesignacaoPresidentLastInvalidException() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		dto.getPresident().setLast("Invalid");
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListPresident(dto, listLocalDate));
		assertTrue(ex.getMessage().contains(String.format(MessageConfig.LAST_INVALID, DesignacaoEntityEnum.PRESIDENT.getLabel())));
	}
	
	@Test
	void shouldgenerateListDesignacaoPresidentSuccess() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		var list = service.generateListPresident(dto, listLocalDate);
		validListGenerated(dto.getPresident(), list);
	}
	
	
	//	 --------------------- READER WATCHTOWER --------------------- \\
	@Test
	void generateListDesignacaoReaderWatchtowerException() throws IllegalAccessException, ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListReaderWatchtower(null, null));
		assertTrue(ex.getMessage().contains(DesignacaoEntityEnum.READER_WATCHTOWER.getLabel()));
	}
	
	@Test
	void shouldgenerateListDesignacaoReaderWatchtowerSuccess() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		var list = service.generateListReaderWatchtower(dto, listLocalDate);
		validListGenerated(dto.getReader().getWatchtower(), list);
	}
	
	@Test
	void shouldgenerateListDesignacaoReaderWatchtowerLastInvalidException() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		dto.getReader().getWatchtower().setLast("Invalid");
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListReaderWatchtower(dto, listLocalDate));
		assertTrue(ex.getMessage().contains(String.format(MessageConfig.LAST_INVALID, DesignacaoEntityEnum.READER_WATCHTOWER.getLabel())));
	}
	
	
	//	 --------------------- READER BIBLESTUDY --------------------- \\
	@Test
	void generateListDesignacaoReaderBibleStudyException() throws IllegalAccessException, ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListReaderBibleStudy(null, null));
		assertTrue(ex.getMessage().contains(DesignacaoEntityEnum.READER_BIBLESTUDY.getLabel()));
	}
	
	@Test
	void shouldgenerateListDesignacaoReaderBibleStudySuccess() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		var list = service.generateListReaderBibleStudy(dto, listLocalDate);
		validListGenerated(dto.getReader().getBibleStudy(), list);
	}
	
	@Test
	void shouldgenerateListDesignacaoReaderBibleStudyLastInvalidException() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		dto.getReader().getBibleStudy().setLast("Invalid");
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListReaderBibleStudy(dto, listLocalDate));
		assertTrue(ex.getMessage().contains(String.format(MessageConfig.LAST_INVALID, DesignacaoEntityEnum.READER_BIBLESTUDY.getLabel())));
	}
	
	
	//	 --------------------- AUDIOVIDEO --------------------- \\
	@Test
	void generateListDesignacaoAudioVideoException() throws IllegalAccessException, ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListAudioVideo(null, null));
		assertTrue(ex.getMessage().contains(DesignacaoEntityEnum.AUDIOVIDEO.getLabel()));
	}
	
	@Test
	void shouldgenerateListDesignacaoAudioVideoSuccess() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		var list = service.generateListAudioVideo(dto, listLocalDate);
		assertEquals(4, list.size());
		assertTrue(dto.getAudioVideo().getList().get(1).contains(list.get(0).getName().split(" e ")[0]));
		assertTrue(dto.getAudioVideo().getList().get(2).contains(list.get(1).getName().split(" e ")[0]));
		assertTrue(dto.getAudioVideo().getList().get(3).contains(list.get(2).getName().split(" e ")[0]));
		assertTrue(dto.getAudioVideo().getList().get(0).contains(list.get(3).getName().split(" e ")[0]));
	}
	
	@Test
	void shouldgenerateListDesignacaoAudioVideoLastInvalidException() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		dto.getAudioVideo().setLast("Invalid");
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListAudioVideo(dto, listLocalDate));
		assertTrue(ex.getMessage().contains(String.format(MessageConfig.LAST_INVALID, DesignacaoEntityEnum.AUDIOVIDEO.getLabel())));
	}
	
	
	//	 --------------------- INDICATOR --------------------- \\
	@Test
	void generateListDesignacaoIndicatorException() throws IllegalAccessException, ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListIndicator(null, null, null));
		assertTrue(ex.getMessage().contains(DesignacaoEntityEnum.INDICATOR.getLabel()));
	}
	
	@Test
	void shouldgenerateListDesignacaoIndicatorSuccess() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		var anotherList = List.of(DesignacaoWriterItemDtoBuilder.create().withRandomData().build());
		var list = service.generateListIndicator(dto, listLocalDate, anotherList);
		assertEquals(4, list.size());
	}


	//	 --------------------- MICROPHONE --------------------- \\
	@Test
	void generateListDesignacaoMicrophoneException() throws IllegalAccessException, ListBuilderException {
		var ex = assertThrows(ListBuilderException.class, () -> service.generateListMicrophone(null, null, null));
		assertTrue(ex.getMessage().contains(DesignacaoEntityEnum.MICROPHONE.getLabel()));
	}
	
	@Test
	void shouldgenerateListDesignacaoMicrophoneSuccess() throws IllegalAccessException, ListBuilderException {
		var dto = FileInputDataDesignacaoDtoBuilder.create().withRandomData().build();
		var anotherList = List.of(DesignacaoWriterItemDtoBuilder.create().withRandomData().build());
		var list = service.generateListMicrophone(dto, listLocalDate, anotherList);
		assertEquals(4, list.size());
	}
	
	
	//	 --------------------- OTHER --------------------- \\
	private void validListGenerated(FileInputDataDesignacaoListDTO dto, List<DesignacaoWriterItemDTO> list) {
		assertEquals(4, list.size());
		assertEquals(dto.getList().get(1), list.get(0).getName());
		assertEquals(dto.getList().get(2), list.get(1).getName());
		assertEquals(dto.getList().get(3), list.get(2).getName());
		assertEquals(dto.getList().get(0), list.get(3).getName());
	}
	
}
