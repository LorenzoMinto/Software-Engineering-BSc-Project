package it.polimi.se2018.utils;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test for {@link FileFinder} class
 *
 * @author Federico Haag
 */
public class FileFinderTest {

    /**
     * Tests the method {@link FileFinder#getFilesNamesInDirectory(String)}
     * @see FileFinder#getFilesNamesInDirectory(String)
     */
    @Test
    public void testGetFilesNames() {
        try{
            assertTrue(new FileFinder().getFilesNamesInDirectory("assets/persistency/").contains("GlobalRankings"));
        } catch (IOException e) {
            fail();
        }

        try{
            new FileFinder().getFilesNamesInDirectory("assets/notadirectory/");
            fail();
        } catch (IOException e) {
            fail();
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Tests the method {@link FileFinder#getFileDocument(String)}
     * @see FileFinder#getFileDocument(String)
     */
    @Test
    public void testGetFileDocument() {
        try {
            assertEquals(true, new FileFinder().getFileDocument("assets/persistency/GlobalRankings.xml") != null);
        } catch (Exception e) {
            fail();
        }
    }
}