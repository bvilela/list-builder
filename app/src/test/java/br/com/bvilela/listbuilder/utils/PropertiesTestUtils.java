package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;

public class PropertiesTestUtils {

    private AppProperties appProperties;
    private NotifyProperties notifyProperties;

    public PropertiesTestUtils(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public PropertiesTestUtils(NotifyProperties notifyProperties) {
        this.notifyProperties = notifyProperties;
    }

    @SneakyThrows
    public void setInputDir(String value) {
        FieldUtils.writeField(appProperties, "inputDir", value, true);
    }

    @SneakyThrows
    public void setOutputDir(String value) {
        FieldUtils.writeField(appProperties, "outputDir", value, true);
    }

    @SneakyThrows
    public void setLayoutLimpeza(int layoutLimpeza) {
        FieldUtils.writeField(appProperties, "layoutLimpeza", layoutLimpeza, true);
    }

    @SneakyThrows
    public void setNotifyActive(boolean value) {
        FieldUtils.writeField(notifyProperties, "notifyActive", value, true);
    }

    @SneakyThrows
    public void setNotifyName(String name) {
        FieldUtils.writeField(notifyProperties, "notifyName", name, true);
    }

    @SneakyThrows
    public void setNotifyDesignationTypeActive(List<String> list) {
        FieldUtils.writeField(notifyProperties, "notifyDesignationTypeActive", list, true);
    }

    @SneakyThrows
    public void setNotifyCleaningPreMeeting(boolean value) {
        FieldUtils.writeField(notifyProperties, "notifyCleaningPreMeeting", value, true);
    }

    @SneakyThrows
    public void setLayoutAudience(AudienceWriterLayoutEnum audienceWriterLayoutEnum) {
        FieldUtils.writeField(
                appProperties, "layoutAudience", audienceWriterLayoutEnum.name(), true);
    }
}
