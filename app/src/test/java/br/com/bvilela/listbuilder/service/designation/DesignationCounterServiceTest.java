package br.com.bvilela.listbuilder.service.designation;

import br.com.bvilela.listbuilder.builder.designation.DesignationWriterDtoBuilder;
import br.com.bvilela.listbuilder.dto.designation.writer.DesignationWriterItemDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class DesignationCounterServiceTest {

    @InjectMocks private DesignationCounterService service;

    private static final String PERSON_1 = "Person 1";
    private static final String PERSON_2 = "Person 2";
    private static final String PERSON_3 = "Person 3";
    private static final String PERSON_4 = "Person 4";

    @Test
    void shouldCountNumberActiviesByName() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        Assertions.assertDoesNotThrow(() -> service.countNumberActiviesByName(dto));
    }

    @Test
    void shouldCount() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        List<DesignationWriterItemDTO> listAux = new ArrayList<>();
        addItem(listAux, PERSON_1, 3);
        addItem(listAux, PERSON_2, 2);
        addItem(listAux, PERSON_3, 1);
        addItem(listAux, PERSON_4, 1);
        dto.setPresident(listAux);
        var map = service.count(dto.getPresident());
        Assertions.assertEquals(3, map.get(PERSON_1));
        Assertions.assertEquals(2, map.get(PERSON_2));
        Assertions.assertEquals(1, map.get(PERSON_3));
        Assertions.assertEquals(1, map.get(PERSON_4));
    }

    @Test
    void shouldCountSplit() {
        var dto = DesignationWriterDtoBuilder.create().withRandomData().build();
        List<DesignationWriterItemDTO> listAux = new ArrayList<>();
        addItemTwoPeople(listAux, PERSON_1, PERSON_4, 4);
        addItemTwoPeople(listAux, PERSON_2, PERSON_3, 1);
        addItemTwoPeople(listAux, PERSON_3, PERSON_3, 2);
        addItemTwoPeople(listAux, PERSON_4, PERSON_1, 1);
        dto.setAudioVideo(listAux);

        // valid Person1
        var map1 = service.countWithSplit(dto.getAudioVideo(), 0);
        Assertions.assertEquals(4, map1.get(PERSON_1));
        Assertions.assertEquals(1, map1.get(PERSON_2));
        Assertions.assertEquals(2, map1.get(PERSON_3));
        Assertions.assertEquals(1, map1.get(PERSON_4));

        // valid Person2
        var map2 = service.countWithSplit(dto.getAudioVideo(), 1);
        Assertions.assertEquals(1, map2.get(PERSON_1));
        Assertions.assertNull(map2.get(PERSON_2));
        Assertions.assertEquals(3, map2.get(PERSON_3));
        Assertions.assertEquals(4, map2.get(PERSON_4));
    }

    @Test
    void shouldMergeMapCase1() {
        var map1 = Map.of(PERSON_1, 1l, PERSON_2, 2l, PERSON_3, 3l, PERSON_4, 4l);
        var map2 = Map.of(PERSON_1, 1l, PERSON_4, 4l);

        // valid Person1
        var mergedMap = service.mergeMap(map1, map2);
        Assertions.assertEquals(2, mergedMap.get(PERSON_1));
        Assertions.assertEquals(2, mergedMap.get(PERSON_2));
        Assertions.assertEquals(3, mergedMap.get(PERSON_3));
        Assertions.assertEquals(8, mergedMap.get(PERSON_4));
    }

    @Test
    void shouldMergeMapCase2() {
        var map1 = Map.of(PERSON_1, 1l, PERSON_3, 3l);
        var map2 = Map.of(PERSON_1, 1l, PERSON_4, 4l);

        // valid Person1
        var mergedMap = service.mergeMap(map1, map2);
        Assertions.assertEquals(2, mergedMap.get(PERSON_1));
        Assertions.assertNull(mergedMap.get(PERSON_2));
        Assertions.assertEquals(3, mergedMap.get(PERSON_3));
        Assertions.assertEquals(4, mergedMap.get(PERSON_4));
    }

    private void addItem(List<DesignationWriterItemDTO> listAux, String name, int number) {
        for (int i = 0; i < number; i++) {
            listAux.add(new DesignationWriterItemDTO(LocalDate.now(), name));
        }
    }

    private void addItemTwoPeople(
            List<DesignationWriterItemDTO> listAux, String name1, String name2, int number) {
        for (int i = 0; i < number; i++) {
            listAux.add(
                    new DesignationWriterItemDTO(
                            LocalDate.now(), name1.concat(" e ").concat(name2)));
        }
    }
}
