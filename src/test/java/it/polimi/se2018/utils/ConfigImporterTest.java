package it.polimi.se2018.utils;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ConfigImporterTest {

    @Test
    public void getProperties() {
        ConfigImporter configImporter = new ConfigImporter();
        Properties p = null;
        try {
            p = configImporter.getProperties();
        } catch (NoConfigParamFoundException e) {
            fail();
        }
        p.getProperty("numberOfRounds").equals("10");
        p.getProperty("minNumberOfPlayers").equals("2");
        p.getProperty("amountOfCouplesOfPatternsPerPlayer").equals("2");
    }
}