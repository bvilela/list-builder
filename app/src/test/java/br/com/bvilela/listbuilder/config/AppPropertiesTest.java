package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AppPropertiesTest {

    private static final String OUTPUT_DIR_VALUE = "outputDirValue";
    private static final String INPUT_DIR_VALUE = "inputDirValue";
    private static final int LAYOUT_LIMPEZA = 1;

    @InjectMocks private AppProperties appProperties;

    @BeforeEach
    public void setup() {
        final var propertiesUtils = new PropertiesTestUtils(appProperties);
        propertiesUtils.setInputDir(INPUT_DIR_VALUE);
        propertiesUtils.setOutputDir(OUTPUT_DIR_VALUE);
        propertiesUtils.setLayoutLimpeza(LAYOUT_LIMPEZA);
    }

    @Test
    void shouldGetValues() {
        assertEquals(INPUT_DIR_VALUE, appProperties.getInputDir());
        assertEquals(OUTPUT_DIR_VALUE, appProperties.getOutputDir());
        assertEquals(LAYOUT_LIMPEZA, appProperties.getLayoutLimpeza());
    }
}
