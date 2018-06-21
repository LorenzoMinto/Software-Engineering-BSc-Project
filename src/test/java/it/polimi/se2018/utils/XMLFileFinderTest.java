package it.polimi.se2018.utils;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class XMLFileFinderTest {

    /**
     * Tests the method {@link XMLFileFinder#getFilesNames(String)}
     * @see XMLFileFinder#getFilesNames(String)
     */
    @Test
    public void testGetFilesNames() {
        try{
            assertEquals(true,XMLFileFinder.getFilesNames("assets/persistency/").contains("GlobalRankings"));
        } catch (IOException e) {
            fail();
        }

        try{
            assertEquals(true,XMLFileFinder.getFilesNames("assets/notadirectory/").contains("GlobalRankings"));
            fail();
        } catch (IOException e) {
            fail();
        }
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