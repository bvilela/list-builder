package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.enuns.NotifDesignacaoEntityEnum;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

class NotifyDesignationServiceImplTest {

    @InjectMocks private NotifyDesignationServiceImpl service;

    @InjectMocks private NotifyProperties notifProperties;

    @BeforeEach
    @SneakyThrows
    void setupBeforeEach() {
        MockitoAnnotations.openMocks(this);
        FieldUtils.writeField(notifProperties, "notifActive", true, true);
        setNotifDesignationTypeActive(Collections.emptyList());
        service = new NotifyDesignationServiceImpl(notifProperties);
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
        setNotifName(dto.getPresident().get(0).getName());
        setNotifDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.PRESIDENT.getLabel()));
        var events = service.getNotifyPresident(dto);
        Assertions.assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyPresidentActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifName("notFound");
        setNotifDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.PRESIDENT.getLabel()));
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
        setNotifName(dto.getReaderWatchtower().get(0).getName());
        setNotifDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.READER.getLabel()));
        var events = service.getNotifyReader(dto);
        Assertions.assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifName("notFound");
        setNotifDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.READER.getLabel()));
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
        setNotifName(audioVideoFirstName);
        setNotifDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.AUDIO_VIDEO.getLabel()));
        var events = service.getNotifyAudioVideo(dto);
        Assertions.assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        setNotifName("notFound");
        setNotifDesignationTypeActive(List.of(NotifDesignacaoEntityEnum.AUDIO_VIDEO.getLabel()));
        var events = service.getNotifyAudioVideo(dto);
        Assertions.assertTrue(CollectionUtils.isEmpty(events));
    }

    @SneakyThrows
    private void setNotifName(String name) {
        FieldUtils.writeField(notifProperties, "notifName", name, true);
    }

    @SneakyThrows
    private void setNotifDesignationTypeActive(List<String> list) {
        FieldUtils.writeField(notifProperties, "notifDesignationTypeActive", list, true);
    }


}
