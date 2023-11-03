package br.com.bvilela.listbuilder.utils;

import org.jeasy.random.api.Randomizer;

import java.util.Random;

public class WeekDayStringRandomizer implements Randomizer<String> {
    @Override
    public String getRandomValue() {
        String[] validDays = {"domingo", "segunda", "terca", "quarta", "quinta", "sexta", "sabado"};
        return validDays[new Random().nextInt(validDays.length)];
    }
}
