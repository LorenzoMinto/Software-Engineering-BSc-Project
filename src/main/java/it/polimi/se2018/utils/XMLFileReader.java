package it.polimi.se2018.utils;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Federico Haag
 */
public class XMLFileReader {

    private XMLFileReader(){
        //Just for preventing instantiation
    }

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

    public static Document getFileDocument(String path) throws Exception{
        File file = new File(path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(file);
    }
}
