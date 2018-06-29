package it.polimi.se2018.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the access to xml files (toolCards and windowPatterns).
 *
 * @author Federico Haag
 */
public final class FileFinder {

    /**
     * Private constructor just for preventing instantiation
     */
    public FileFinder(){
        //do nothing
    }

    /**
     * Given a path returns the list of all .xml files in that path as a list of
     * filenames (just the name, no path, no extension - is obviously .xml).
     *
     * @param path the path to explore
     * @return the list of all .xml files in that path as a list of filenames
     * (just the name, no path, no extension - is obviously .xml)
     * @throws IOException if something during reading from file system fails
     */
    public List<String> getFilesNamesInDirectory(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try( InputStream input = getClass().getClassLoader().getResourceAsStream(path.concat("index")) ){

            BufferedReader reader = new BufferedReader( new InputStreamReader(input));

            while(reader.ready()){
                filenames.add( reader.readLine() );
            }

        } catch (Exception e){
            throw new IOException();
        }
        return filenames;
    }

    /**
     * Given a path returns the relative input stream
     * @param path given path to access
     * @return given path relative input stream
     */
    public InputStream getResourceAsStream(String path){
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    /**
     * Returns the Document instance of the given file (by path name)
     *
     * @param path pathname of the file
     * @return the Document instance of the given file (by path name)
     * @throws IOException if something in opening the file fails
     * @throws ParserConfigurationException if build of a new document fails
     * @throws SAXException if parsing of the document fails
     * @throws URISyntaxException if something getting the url of the document fails
     */
    public Document getFileDocument(String path) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(getClass().getClassLoader().getResource(path).toURI().toString());
    }

    /**
     * Returns the given filename string without (if it has it) the end ".xml"
     * @param filename the filename to convert
     * @return the given filename string without (if it has it) the end ".xml"
     */
    public static String getXMLFileName(String filename){

        if(filename.endsWith(".xml")){
            return filename.substring(0,filename.length()-4);
        } else {
            return filename;
        }
    }
}
