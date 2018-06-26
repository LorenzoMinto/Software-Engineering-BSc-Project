package it.polimi.se2018.utils;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test for {@link XMLFileFinder} class
 *
 * @author Federico Haag
 */
public class XMLFileFinderTest {

    /**
     * Tests the method {@link XMLFileFinder#getFilesNames(String)}
     * @see XMLFileFinder#getFilesNames(String)
     */
    @Test
    public void testGetFilesNames() {
        try{
            assertTrue(XMLFileFinder.getFilesNames("assets/persistency/").contains("GlobalRankings"));
        } catch (IOException e) {
            fail();
        }

        try{
            XMLFileFinder.getFilesNames("assets/notadirectory/");
            fail();
        } catch (IOException e) {
            fail();
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Tests the method {@link XMLFileFinder#getFileDocument(String)}
     * @see XMLFileFinder#getFileDocument(String)
     */
    @Test
    public void testGetFileDocument() {
        try {
            assertEquals(true, XMLFileFinder.getFileDocument("assets/persistency/GlobalRankings.xml") != null);
        } catch (Exception e) {
            fail();
        }
    }
}