package it.polimi.se2018.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLFileReader {

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
}
