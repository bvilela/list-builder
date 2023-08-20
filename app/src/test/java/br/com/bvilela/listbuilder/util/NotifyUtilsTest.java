package br.com.bvilela.listbuilder.util;

import br.com.bvilela.listbuilder.service.notification.NotifyUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NotifyUtilsTest {

    @DisplayName("Check if List of String contains specific name")
    @ParameterizedTest(name = "Name \"{0}\"")
    @ValueSource(strings = {"Name1", "name2", "NAME3"})
    void listContains(String name) {
        var listNames = List.of("Name1", "Name2", "Name3", "Name4");
        Assertions.assertTrue(NotifyUtils.containsName(listNames, name));
    }

    @DisplayName("Check if List of String NOT contains specific name")
    @ParameterizedTest(name = "Name \"{0}\"")
    @ValueSource(strings = {"Name1", "name2", "NAME3"})
    void noListContains(String name) {
        var listNames = List.of("NameA", "NameB", "NameC", "NameD");
        Assertions.assertFalse(NotifyUtils.containsName(listNames, name));
    }

    @DisplayName("Check if String contains specific name")
    @ParameterizedTest(name = "Name \"{0}\"")
    @ValueSource(strings = {"name1", "nAME2"})
    void stringContains(String name) {
        var listNames = "Name1 e NAME2";
        Assertions.assertTrue(NotifyUtils.containsName(listNames, name));
    }

    @DisplayName("Check if String NOT contains specific name")
    @ParameterizedTest(name = "Name \"{0}\"")
    @ValueSource(strings = {"Name1", "Name2"})
    void stringNoContains(String name) {
        var listNames = "NameA e NAMEB";
        Assertions.assertFalse(NotifyUtils.containsName(listNames, name));
    }
}
