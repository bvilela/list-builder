package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.listbuilder.config.AppProperties;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.enuns.AudienceWriterLayoutEnum;

import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class PropertiesTestUtils {

    private AppProperties appProperties;
    private NotifyProperties notifyProperties;

    public PropertiesTestUtils(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public PropertiesTestUtils(NotifyProperties notifyProperties) {
        this.notifyProperties = notifyProperties;
    }

    public void setInputDir(String value) {
        setField(appProperties, "inputDir", value);
    }

    public void setOutputDir(String value) {
        setField(appProperties, "outputDir", value);
    }

    public void setLayoutLimpeza(int layoutLimpeza) {
        setField(appProperties, "layoutLimpeza", layoutLimpeza);
    }

    public void setNotifyActive(boolean value) {
        setField(notifyProperties, "active", value);
    }

    public void setNotifyName(String name) {
        setField(notifyProperties, "name", name);
    }

    public void setNotifyDesignationTypeActive(List<String> list) {
        setField(notifyProperties, "designationTypeActive", list);
    }

    public void setNotifyCleaningPreMeeting(boolean value) {
        setField(notifyProperties, "cleaningPreMeeting", value);
    }

    public void setNotifyChristianLifeMeetingDay(String value) {
        setField(notifyProperties, "christianLifeMeetingDay", value);
    }

    public void setLayoutAudience(AudienceWriterLayoutEnum audienceWriterLayoutEnum) {
        setField(appProperties, "layoutAudience", audienceWriterLayoutEnum.name());
    }
}