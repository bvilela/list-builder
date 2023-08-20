package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.enuns.NotifyDesignationEntityEnum;
import br.com.bvilela.listbuilder.exception.ListBuilderException;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NotifyPropertiesTest {

    @InjectMocks private NotifyProperties properties;

    PropertiesTestUtils propertiesUtils;

    @BeforeEach
    public void setup() {
        propertiesUtils = new PropertiesTestUtils(properties);
    }

    @Test
    void isNotifyActive() {
        propertiesUtils.setNotifyActive(true);
        assertTrue(properties.isActive());
    }

    @Test
    void isNotifyInactive() {
        propertiesUtils.setNotifyActive(false);
        assertTrue(properties.notifyInactive());
    }

    @Test
    void getNotifyName() {
        propertiesUtils.setNotifyName("John");
        assertEquals("John", properties.getName());
        assertDoesNotThrow(() -> properties.checkNotifyNameFilled());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void checkNotifyNameFilled(String name) {
        propertiesUtils.setNotifyName(name);
        assertThrows(ListBuilderException.class, () -> properties.checkNotifyNameFilled());
    }

    @Test
    void getNotifyDesignationTypeActive() {
        var expectedList = List.of(NotifyDesignationEntityEnum.READER.getLabel());
        propertiesUtils.setNotifyDesignationTypeActive(expectedList);
        assertEquals(expectedList, properties.getDesignationTypeActive());
    }

    @Test
    void notifyCleaningPreMeeting() {
        propertiesUtils.setNotifyCleaningPreMeeting(true);
        assertTrue(properties.isCleaningPreMeeting());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "teste", "guarta", "cesta"})
    void checkChristianLifeMeetingDayEnumFilled(String value) {
        propertiesUtils.setNotifyChristianLifeMeetingDay(value);
        assertThrows(ListBuilderException.class, () -> properties.getChristianLifeMeetingDayEnum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"segunda", "terça", "quaRTA", "QUINTA", "SexTA", "Sábado", "domingO"})
    void getChristianLifeMeetingDayEnumValid(String value) {
        propertiesUtils.setNotifyChristianLifeMeetingDay(value);
        assertDoesNotThrow(() -> properties.getChristianLifeMeetingDayEnum());
    }
}
