package it.polimi.se2018.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the access to xml files (toolcards and windowpatterns).
 *
 * @author Federico Haag
 */
public final class XMLFileReader {

    /**
     * Private constructor just for preventing instantiation
     */
    private XMLFileReader(){}

    /**
     * Given a path returns the list of all .xml files in that path as a list of
     * filenames (just the name, no path, no extension - is obviously .xml).
     *
     * @param path the path to explore
     * @return the list of all .xml files in that path as a list of filenames
     * (just the name, no path, no extension - is obviously .xml)
     * @throws IOException if something during reading from file system fails
     */
    public static List<String> getFilesNames(String path) throws IOException{

        List<String> filesNames = new ArrayList<>();

        //Parse the directory in order to find all patterns .xml files
        File dir = new File(path);

        if( !dir.isDirectory() ){
            throw new IllegalArgumentException("Asked to load files from path that is not a directory");
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));

        //Exception is thrown if no files are found in the folder (could happen even due to IO error)
        if(files==null) throw new IOException();

        //Add the found files to the list
        for(File file : files){

            //fileName is filename without ".xml"
            String fileName = file.getName().substring(0, file.getName().length() - 4 );

            //Add fileName to filesNames list
            filesNames.add( fileName );
        }

        return filesNames;
    }

    /**
     * Returns the Document instance of the given file (by path name)
     *
     * @param path pathname of the file
     * @return the Document instance of the given file (by path name)
     * @throws IOException if something in opening the file fails
     * @throws ParserConfigurationException if build of a new document fails
     * @throws SAXException if parsing of the document fails
     */
        public static Document getFileDocument(String path) throws IOException, ParserConfigurationException, SAXException{
        File file = new File(path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(file);
    }
}
