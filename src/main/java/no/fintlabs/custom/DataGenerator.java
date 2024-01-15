package no.fintlabs.custom;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

public class DataGenerator {

    public static String getRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        return random.ints(length, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());
    }

    public static Identifikator getIdentifikator() {
        Identifikator identifikator = new Identifikator();
        identifikator.setGyldighetsperiode(getPeriode());
        identifikator.setIdentifikatorverdi(getRandomString(10));
        return identifikator;
    }

    public static Periode getPeriode() {
        Periode periode = new Periode();
        periode.setBeskrivelse(getRandomString(10));
        periode.setStart(new Date());
        periode.setSlutt(new Date());
        return periode;
    }

}
