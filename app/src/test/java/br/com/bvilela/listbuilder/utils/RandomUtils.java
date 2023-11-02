package br.com.bvilela.listbuilder.utils;

import br.com.bvilela.listbuilder.dto.audience.AudienceInputDTO;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

public class RandomUtils {

    private static EasyRandomParameters getBaseParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(FieldPredicates.named("lastDate"), new DateStringRandomizer());
        parameters.randomize(FieldPredicates.named("midweekMeetingDay"), new WeekDayStringRandomizer());
        parameters.randomize(FieldPredicates.named("weekendMeetingDay"), new WeekDayStringRandomizer());
        return parameters;
    }

    public static AudienceInputDTO getMockedAudienceInputDTO() {
        return new EasyRandom(getBaseParameters()).nextObject(AudienceInputDTO.class);
    }

}
