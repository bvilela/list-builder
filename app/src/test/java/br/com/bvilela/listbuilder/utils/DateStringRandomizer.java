package br.com.bvilela.listbuilder.utils;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.time.LocalDateRandomizer;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateStringRandomizer implements Randomizer<String> {

    @Override
    public String getRandomValue() {
        var locale = new Locale("pt", "BR");
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(locale);
        return new LocalDateRandomizer().getRandomValue().format(formatter);
    }

}
