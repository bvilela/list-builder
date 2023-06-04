package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.enuns.NotifDesignacaoEntityEnum;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

class NotifyDesignationServiceImplTest {

    @InjectMocks private NotifyDesignationServiceImpl service;

    @InjectMocks private NotifyProperties notifyProperties;

    @BeforeEach
    @SneakyThrows
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        FieldUtils.writeField(notifyProperties, "notifyActive", true, true);
        setNotifyDesignationTypeActive(Collections.emptyList());
        service = new NotifyDesignationServiceImpl(notifyProperties);
    }

    @Test
    void getNotifyPresidentInactive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var events = service.getNotifyPresident(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyPresidentActive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifyName(dto.getPresident().get(0).getName());
        setNotifyDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.PRESIDENT.getLabel()));
        var events = service.getNotifyPresident(dto);
        Assertions.assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyPresidentActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifyName("notFound");
        setNotifyDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.PRESIDENT.getLabel()));
        var events = service.getNotifyPresident(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderInactive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var events = service.getNotifyReader(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderActive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifyName(dto.getReaderWatchtower().get(0).getName());
        setNotifyDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.READER.getLabel()));
        var events = service.getNotifyReader(dto);
        Assertions.assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifyName("notFound");
        setNotifyDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.READER.getLabel()));
        var events = service.getNotifyReader(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoInactive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var events = service.getNotifyAudioVideo(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoActive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var audioVideoFirstName = dto.getAudioVideo().get(0).getName().split(" e ")[0];
        setNotifyName(audioVideoFirstName);
        setNotifyDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.AUDIO_VIDEO.getLabel()));
        var events = service.getNotifyAudioVideo(dto);
        Assertions.assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifyName("notFound");
        setNotifyDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.AUDIO_VIDEO.getLabel()));
        var events = service.getNotifyAudioVideo(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @SneakyThrows
    private void setNotifyName(String name) {
        FieldUtils.writeField(notifyProperties, "notifyName", name, true);
    }

    @SneakyThrows
    private void setNotifyDesignationTypeActive(List<String> list) {
        FieldUtils.writeField(notifyProperties, "notifyDesignationTypeActive", list, true);
    }
}
