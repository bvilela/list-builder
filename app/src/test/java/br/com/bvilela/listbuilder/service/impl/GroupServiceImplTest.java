package br.com.bvilela.listbuilder.service.impl;

import br.com.bvilela.listbuilder.builder.clearing.ClearingInputDtoBuilder;
import br.com.bvilela.listbuilder.builder.designation.DesignationWriterItemDtoBuilder;
import br.com.bvilela.listbuilder.builder.designation.DesignationInputDtoBuilder;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import br.com.bvilela.listbuilder.dto.util.InputListDTO;
import br.com.bvilela.listbuilder.dto.util.ItemDateDTO;
import br.com.bvilela.listbuilder.enuns.DesignationEntityEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @InjectMocks private GroupServiceImpl service;

    private final List<ItemDateDTO> listItemDate =
            List.of(
                    new ItemDateDTO(1, LocalDate.now()),
                            new ItemDateDTO(2, LocalDate.now().plusDays(1)),
                    new ItemDateDTO(1, LocalDate.now().plusDays(1)),
                            new ItemDateDTO(2, LocalDate.now().plusDays(1)));

    private final List<LocalDate> listLocalDate =
            List.of(
                    LocalDate.now(),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1));


    // --------------------- LIMPEZA --------------------- \\
    @Test
    void shouldGenerateListGroupExceptionLastGroupInvalid() {
        var dto = ClearingInputDtoBuilder.create().withLastGroupInvalid().build();
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () ->
                                service.generateListGroupsLimpeza(
                                        dto, List.of(new ItemDateDTO(LocalDate.now())), 1));
        Assertions.assertEquals(MessageConfig.LAST_GROUP_INVALID, exception.getMessage());
    }

    @Test
    void shouldGenerateListGroupLayout1Success() {
        var dto = ClearingInputDtoBuilder.create().withSuccess().build();
        dto.setLastDate("1");
        var list = service.generateListGroupsLimpeza(dto, listItemDate, 1);
        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(4, list.size());
        Assertions.assertTrue(list.get(0).contains("Group 2"));
        Assertions.assertTrue(list.get(1).contains("Group 3"));
        Assertions.assertTrue(list.get(2).contains("Group 4"));
        Assertions.assertTrue(list.get(3).contains("Group 5"));
    }

    @Test
    void shouldGenerateListGroupLayout2Success() {
        var dto = ClearingInputDtoBuilder.create().withSuccess().build();
        dto.setLastGroup(dto.getGroups().size());
        var list = service.generateListGroupsLimpeza(dto, listItemDate, 2);
        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.get(0).contains("Group 1"));
        Assertions.assertTrue(list.get(1).contains("Group 2"));
    }

    // --------------------- PRESIDENT --------------------- \\
    @Test
    void shouldgenerateListDesignacaoPresidentException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListPresident(null, null));
        Assertions.assertTrue(
                exception.getMessage().contains(DesignationEntityEnum.PRESIDENT.getLabel()));
    }

    @Test
    void shouldgenerateListDesignacaoPresidentLastInvalidException() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        dto.getPresident().setLast("Invalid");
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListPresident(dto, listLocalDate));
        Assertions.assertTrue(
                exception
                        .getMessage()
                        .contains(
                                String.format(
                                        MessageConfig.LAST_INVALID,
                                        DesignationEntityEnum.PRESIDENT.getLabel())));
    }

    @Test
    void shouldgenerateListDesignacaoPresidentSuccess() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        var list = service.generateListPresident(dto, listLocalDate);
        validListGenerated(dto.getPresident(), list);
    }

    //	 --------------------- READER WATCHTOWER --------------------- \\
    @Test
    void generateListDesignacaoReaderWatchtowerException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListReaderWatchtower(null, null));
        Assertions.assertTrue(
                exception
                        .getMessage()
                        .contains(DesignationEntityEnum.READER_WATCHTOWER.getLabel()));
    }

    @Test
    void shouldgenerateListDesignacaoReaderWatchtowerSuccess() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        var list = service.generateListReaderWatchtower(dto, listLocalDate);
        validListGenerated(dto.getReader().getWatchtower(), list);
    }

    @Test
    void shouldgenerateListDesignacaoReaderWatchtowerLastInvalidException() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        dto.getReader().getWatchtower().setLast("Invalid");
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListReaderWatchtower(dto, listLocalDate));
        Assertions.assertTrue(
                exception
                        .getMessage()
                        .contains(
                                String.format(
                                        MessageConfig.LAST_INVALID,
                                        DesignationEntityEnum.READER_WATCHTOWER.getLabel())));
    }

    //	 --------------------- READER BIBLESTUDY --------------------- \\
    @Test
    void generateListDesignacaoReaderBibleStudyException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListReaderBibleStudy(null, null));
        Assertions.assertTrue(
                exception
                        .getMessage()
                        .contains(DesignationEntityEnum.READER_BIBLESTUDY.getLabel()));
    }

    @Test
    void shouldgenerateListDesignacaoReaderBibleStudySuccess() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        var list = service.generateListReaderBibleStudy(dto, listLocalDate);
        validListGenerated(dto.getReader().getBibleStudy(), list);
    }

    @Test
    void shouldgenerateListDesignacaoReaderBibleStudyLastInvalidException() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        dto.getReader().getBibleStudy().setLast("Invalid");
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListReaderBibleStudy(dto, listLocalDate));
        Assertions.assertTrue(
                exception
                        .getMessage()
                        .contains(
                                String.format(
                                        MessageConfig.LAST_INVALID,
                                        DesignationEntityEnum.READER_BIBLESTUDY.getLabel())));
    }

    //	 --------------------- AUDIOVIDEO --------------------- \\
    @Test
    void generateListDesignacaoAudioVideoException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListAudioVideo(null, null));
        Assertions.assertTrue(
                exception.getMessage().contains(DesignationEntityEnum.AUDIO_VIDEO.getLabel()));
    }

    @Test
    void shouldgenerateListDesignacaoAudioVideoSuccess() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        var list = service.generateListAudioVideo(dto, listLocalDate);
        Assertions.assertEquals(4, list.size());
        Assertions.assertTrue(
                dto.getAudioVideo()
                        .getList()
                        .get(1)
                        .contains(list.get(0).getName().split(" e ")[0]));
        Assertions.assertTrue(
                dto.getAudioVideo()
                        .getList()
                        .get(2)
                        .contains(list.get(1).getName().split(" e ")[0]));
        Assertions.assertTrue(
                dto.getAudioVideo()
                        .getList()
                        .get(3)
                        .contains(list.get(2).getName().split(" e ")[0]));
        Assertions.assertTrue(
                dto.getAudioVideo()
                        .getList()
                        .get(0)
                        .contains(list.get(3).getName().split(" e ")[0]));
    }

    @Test
    void shouldgenerateListDesignacaoAudioVideoLastInvalidException() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        dto.getAudioVideo().setLast("Invalid");
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListAudioVideo(dto, listLocalDate));
        Assertions.assertTrue(
                exception
                        .getMessage()
                        .contains(
                                String.format(
                                        MessageConfig.LAST_INVALID,
                                        DesignationEntityEnum.AUDIO_VIDEO.getLabel())));
    }

    //	 --------------------- INDICATOR --------------------- \\
    @Test
    void generateListDesignacaoIndicatorException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListIndicator(null, null, null));
        Assertions.assertTrue(
                exception.getMessage().contains(DesignationEntityEnum.INDICATOR.getLabel()));
    }

    @Test
    void shouldgenerateListDesignacaoIndicatorSuccess() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        var anotherList = List.of(DesignationWriterItemDtoBuilder.create().withRandomData().build());
        var list = service.generateListIndicator(dto, listLocalDate, anotherList);
        Assertions.assertEquals(4, list.size());
    }

    //	 --------------------- MICROPHONE --------------------- \\
    @Test
    void generateListDesignacaoMicrophoneException() {
        var exception =
                Assertions.assertThrows(
                        ListBuilderException.class,
                        () -> service.generateListMicrophone(null, null, null));
        Assertions.assertTrue(
                exception.getMessage().contains(DesignationEntityEnum.MICROPHONE.getLabel()));
    }

    @Test
    void shouldgenerateListDesignacaoMicrophoneSuccess() {
        var dto = DesignationInputDtoBuilder.create().withRandomData().build();
        var anotherList = List.of(DesignationWriterItemDtoBuilder.create().withRandomData().build());
        var list = service.generateListMicrophone(dto, listLocalDate, anotherList);
        Assertions.assertEquals(4, list.size());
    }

    //	 --------------------- OTHER --------------------- \\
    private void validListGenerated(InputListDTO dto, List<DesignationWriterItemDTO> list) {
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals(dto.getList().get(1), list.get(0).getName());
        Assertions.assertEquals(dto.getList().get(2), list.get(1).getName());
        Assertions.assertEquals(dto.getList().get(3), list.get(2).getName());
        Assertions.assertEquals(dto.getList().get(0), list.get(3).getName());
    }
}
