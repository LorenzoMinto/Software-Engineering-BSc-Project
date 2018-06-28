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
            assertTrue(new FileFinder().getFilesNamesInDirectory("patterns/").contains("Gravitas.xml"));
        } catch (IOException e) {
            fail();
        }

        try{
            new FileFinder().getFilesNamesInDirectory("notadirectory/");
            fail();
        } catch (IOException e) {
            //assert true
        }
    }

    /**
     * Tests the method {@link FileFinder#getFileDocument(String)}
     * @see FileFinder#getFileDocument(String)
     */
    @Test
    public void testGetFileDocument() {
        try {
            assertEquals(true, new FileFinder().getFileDocument("patterns/Batllo.xml") != null);
        } catch (Exception e) {
            fail();
        }
    }
}