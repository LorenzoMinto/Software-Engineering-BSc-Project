package it.polimi.se2018.utils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Test for {@link ConfigImporter} class
 *
 * @author Federico Haag
 */
public class ConfigImporterTest {

    private static ConfigImporter configImporter;

    /**
     * Initializes the Config Importer
     */
    @BeforeClass
    public static void initializeConfigImporter(){
       configImporter = new ConfigImporter();
    }

    /**
     * Tests the retrieval of the properties by the Config Importer
     * @see ConfigImporter#getProperties()
     */
    @Test
    public void testGetProperties() {
        Properties p = null;
        try {
            p = configImporter.getProperties();
        } catch (NoConfigParamFoundException e) {
            fail();
        }

        assertNotNull(p);
    }

    /**
     * Tests the retrieval of some specific properties by the Config Importer
     * @see ConfigImporter#getProperties()
     */
    @Test
    public void testGetSpecificProperties() {
        Properties p = null;
        try {
            p = configImporter.getProperties();
        } catch (NoConfigParamFoundException e) {
            fail();
        }

        assertEquals("10", p.getProperty("numberOfRounds"));
        assertEquals("2", p.getProperty("minNumberOfPlayers"));
        assertEquals("2", p.getProperty("amountOfCouplesOfPatternsPerPlayer"));
    }
}