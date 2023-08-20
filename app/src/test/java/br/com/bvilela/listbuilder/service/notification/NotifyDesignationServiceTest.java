package br.com.bvilela.listbuilder.service.notification;

import br.com.bvilela.listbuilder.builder.designacao.DesignacaoWriterDtoBuilder;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.enuns.NotifyDesignationEntityEnum;
import br.com.bvilela.listbuilder.utils.PropertiesTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NotifyDesignationServiceTest {

    @InjectMocks private NotifyDesignationService service;

    @InjectMocks private NotifyProperties notifyProperties;

    private PropertiesTestUtils propertiesUtils;

    @BeforeEach
    void setupBeforeEach() {
        propertiesUtils = new PropertiesTestUtils(notifyProperties);
        propertiesUtils.setNotifyActive(true);
        propertiesUtils.setNotifyDesignationTypeActive(Collections.emptyList());
        service = new NotifyDesignationService(notifyProperties);
    }

    @Test
    void getNotifyPresidentInactive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var events = service.createPresidentEvents(dto);
        assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyPresidentActive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        propertiesUtils.setNotifyName(dto.getPresident().get(0).getName());
        propertiesUtils.setNotifyDesignationTypeActive(
                List.of(NotifyDesignationEntityEnum.PRESIDENT.getLabel()));
        var events = service.createPresidentEvents(dto);
        assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyPresidentActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        propertiesUtils.setNotifyName("notFound");
        propertiesUtils.setNotifyDesignationTypeActive(
                List.of(NotifyDesignationEntityEnum.PRESIDENT.getLabel()));
        var events = service.createPresidentEvents(dto);
        assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderInactive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var events = service.createReaderEvents(dto);
        assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderActive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        propertiesUtils.setNotifyName(dto.getReaderWatchtower().get(0).getName());
        propertiesUtils.setNotifyDesignationTypeActive(
                List.of(NotifyDesignationEntityEnum.READER.getLabel()));
        var events = service.createReaderEvents(dto);
        assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyReaderActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        propertiesUtils.setNotifyName("notFound");
        propertiesUtils.setNotifyDesignationTypeActive(
                List.of(NotifyDesignationEntityEnum.READER.getLabel()));
        var events = service.createReaderEvents(dto);
        assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoInactive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var events = service.createAudioVideoEvents(dto);
        assertTrue(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoActive() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        var audioVideoFirstName = dto.getAudioVideo().get(0).getName().split(" e ")[0];
        propertiesUtils.setNotifyName(audioVideoFirstName);
        propertiesUtils.setNotifyDesignationTypeActive(
                List.of(NotifyDesignationEntityEnum.AUDIO_VIDEO.getLabel()));
        var events = service.createAudioVideoEvents(dto);
        assertFalse(CollectionUtils.isEmpty(events));
    }

    @Test
    void getNotifyAudioVideoActiveNotifyNameNotFound() {
        var dto = DesignacaoWriterDtoBuilder.create().withRandomData().build();
        propertiesUtils.setNotifyName("notFound");
        propertiesUtils.setNotifyDesignationTypeActive(
                List.of(NotifyDesignationEntityEnum.AUDIO_VIDEO.getLabel()));
        var events = service.createAudioVideoEvents(dto);
        assertTrue(CollectionUtils.isEmpty(events));
    }
}
