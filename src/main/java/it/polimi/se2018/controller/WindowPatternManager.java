package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.XMLFileReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the loading and creation of {@link WindowPattern}
 *
 * @author Federico Haag
 */
public class WindowPatternManager {

    /**
     * The file system path to find windowpatterns .xml files
     */
    private static final String PATH = "assets/patterns/";

    /**
     * List of all the toolcards that can be distributed in the current game
     */
    private List<String> availablePatterns;

    /**
     * Constructor of the class. Checks if there are window patterns than can be loaded
     * from file system and if yes loads them.
     */
    public WindowPatternManager() {

        try{
            this.availablePatterns = XMLFileReader.getFilesNames(PATH);
        } catch (IOException e){
            throw new NoPatternsFoundInFileSystemException();
        }

    }

    /**
     * Returns if there are available window patterns
     *
     * @return if there are available window patterns
     */
    public boolean hasAvailablePatterns(){
        return !(availablePatterns.isEmpty());
    }

    /**
     * Returns a list of available window patterns
     *
     * @return list of available window patterns
     */
    public List<String> getAvailablePatternsNames() {

        return new ArrayList<>(availablePatterns);
    }

    /**
     * Loads from file the specified toolcard loading all its properties in a new {@link WindowPattern} class.
     *
     * @param patternID the ID String representing the window pattern to be loaded
     * @return the requested {@link ToolCard}
     * @throws BadFormattedPatternFileException if during the loading of a window pattern it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    private WindowPattern loadPatternFromFileSystem(String patternID) throws BadFormattedPatternFileException {

        try {

            Cell[][] pattern;

            Document document = XMLFileReader.getFileDocument(PATH.concat(patternID).concat(".xml"));

            //Parse from xml the number of rows of the pattern
            int rows = Integer.parseInt( document.getElementsByTagName("rows").item(0).getTextContent() );

            //Parse from xml the number of columns of the pattern
            int cols = Integer.parseInt( document.getElementsByTagName("cols").item(0).getTextContent() );

            //Parse from xml the difficulty of the pattern
            int diff = Integer.parseInt( document.getElementsByTagName("difficulty").item(0).getTextContent() );

            //Creates the pattern
            pattern = new Cell[rows][cols];
            for(int i=0; i<rows; i++){
                for(int j=0; j<cols; j++){
                    pattern[i][j] = new Cell();
                }
            }

            //Parse from xml the list of constraints
            NodeList constraints = document.getElementsByTagName("constraint");
            for(int i=0; i<constraints.getLength(); i++){

                NamedNodeMap a = constraints.item(i).getAttributes();

                //Parse from xml the constraint location (row,col)
                int row = Integer.parseInt( a.getNamedItem("row").getNodeValue() );
                int col = Integer.parseInt( a.getNamedItem("col").getNodeValue() );

                //Parse from xml proper constraint features (value,color)
                int value = Integer.parseInt( a.getNamedItem("value").getNodeValue() );
                DiceColors color = DiceColors.valueOf( a.getNamedItem("color").getNodeValue() );

                //Set constraints to corresponding pattern cell
                pattern[row][col] = new Cell(value,color);
            }

            return new WindowPattern(patternID,diff,rows,cols,pattern);

        } catch (Exception e) {
            //Bad formatting of xml is caught and method returns false
            throw new BadFormattedPatternFileException();
        }
    }

    /**
     * Returns a list of the requested quantity of Window Patterns
     *
     * @param quantity the amount of patterns that are asked to be created
     * @return the list of the requested quantity of Window Patterns
     * @throws BadFormattedPatternFileException if during the loading of a window pattern it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    public List<WindowPattern> getPatterns(int quantity) throws BadFormattedPatternFileException {
        List<WindowPattern> patterns = new ArrayList<>();
        List<String> usedPatterns = new ArrayList<>();

        if( availablePatterns.size() >= quantity ){

            Random r = new Random();

            for(int i=0; i<quantity; i++){

                //Choose randomly one of the available patterns
                int randomIndex = r.nextInt(availablePatterns.size());
                String randomPatternID = availablePatterns.get(randomIndex);

                //Add the selected pattern to a list tracking all the selected patterns
                usedPatterns.add(randomPatternID);

                //Removes the selected pattern from the available to avoid double choise
                availablePatterns.remove(randomPatternID);

                //Load the randomly selected pattern
                WindowPattern randomPattern;
                try {

                    randomPattern = loadPatternFromFileSystem(randomPatternID);

                } catch (BadFormattedPatternFileException e){

                    //re-insert the pattern to the available ones before rethrowing to the caller
                    availablePatterns.addAll(usedPatterns);

                    throw new BadFormattedPatternFileException();
                }

                //The successfully loaded pattern is added in a list that will be returned at the end of bulk loading
                patterns.add(randomPattern);
            }
        } else {
            throw new RuntimeException("Cant create the number of window pattern requested. This error is not handlable at all");
        }

        return patterns;

    }

}
