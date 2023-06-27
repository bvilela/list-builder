package br.com.bvilela.listbuilder.service.notification.impl;

import br.com.bvilela.lib.model.CalendarEvent;
import br.com.bvilela.listbuilder.config.NotifyProperties;
import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import br.com.bvilela.listbuilder.service.notification.NotifyAudienceService;
import br.com.bvilela.listbuilder.service.notification.NotifyUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyAudienceServiceImpl implements NotifyAudienceService {

    private final NotifyProperties properties;

    @Override
    public CalendarEvent createEvent(List<LocalDate> dates) {
        if (properties.notifyInactive()) {
            return null;
        }

        var dateNotify = dates.get(dates.size() - 1);
        return NotifyUtils.createDoNextListEvent(ListTypeEnum.ASSISTENCIA, dateNotify);
    }
}
