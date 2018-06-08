package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.XMLFileFinder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.*;

/**
 * Manages the loading and creation of {@link WindowPattern}
 *
 * @author Federico Haag
 */
public class WindowPatternManager {

    /**
     * The file system path to find windowPatterns .xml files
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
            this.availablePatterns = XMLFileFinder.getFilesNames(PATH);
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
     * Returns a list of the requested quantity of Window Patterns
     *
     * @param numberOfPairs the number of pairs of window patterns that must be created
     * @return the list of the requested quantity of Window Patterns
     * @throws BadFormattedPatternFileException if during the loading of a window pattern it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    public Set<WindowPattern> getPairsOfPatterns(int numberOfPairs) {
        if(numberOfPairs < 0){ throw new IllegalArgumentException("Can't get a negative number of couples of windowPatterns");}

        Set<WindowPattern> couplesOfPatterns = new HashSet<>();

        if( availablePatterns.size() >= numberOfPairs * 2 ){

            Random r = new Random();

            for(int i=0; i<numberOfPairs; i++){

                //Choose randomly one of the available patterns
                int randomIndex = r.nextInt(availablePatterns.size());
                String randomPatternID = availablePatterns.get(randomIndex);

                //Removes the selected pattern from the available to avoid double choise
                availablePatterns.remove(randomPatternID);

                //Load the randomly selected pattern
                WindowPattern randomPattern = loadPatternFromFileSystem(randomPatternID);
                WindowPattern randomPartnerPattern = loadPatternFromFileSystem(getPartnerPatternID(randomPatternID));

                //The successfully loaded patterns are added in a list that will be returned at the end of bulk loading
                couplesOfPatterns.add(randomPattern);
                couplesOfPatterns.add(randomPartnerPattern);
            }
        } else {
            throw new BadBehaviourRuntimeException("Cant create the number of window pattern requested. This error is not handlable at all");
        }

        return couplesOfPatterns;
    }

    /**
     * Loads from file the specified toolcard loading all its properties in a new {@link WindowPattern} class.
     *
     * @param patternID the ID String representing the window pattern to be loaded
     * @return the requested {@link ToolCard}
     * @throws BadFormattedPatternFileException if during the loading of a window pattern it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    private WindowPattern loadPatternFromFileSystem(String patternID) {

        try {

            Cell[][] pattern;

            Document document = XMLFileFinder.getFileDocument(PATH.concat(patternID).concat(".xml"));

            //Parse from xml the pattern's id
            String title = document.getElementsByTagName("title").item(0).getTextContent();

            //Parse from xml the number of rows of the pattern
            int rows = Integer.parseInt( document.getElementsByTagName("rows").item(0).getTextContent() );

            String imageURL = document.getElementsByTagName("imageURL").item(0).getTextContent();

            //Parse from xml the number of columns of the pattern
            int cols = Integer.parseInt( document.getElementsByTagName("cols").item(0).getTextContent() );

            //Parse from xml the difficulty of the pattern
            int difficulty = Integer.parseInt( document.getElementsByTagName("difficulty").item(0).getTextContent() );

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

                NamedNodeMap attributes = constraints.item(i).getAttributes();

                //Parse from xml the constraint location (row,col)
                int row = Integer.parseInt( attributes.getNamedItem("row").getNodeValue() );
                int col = Integer.parseInt( attributes.getNamedItem("col").getNodeValue() );

                //Parse from xml proper constraint features (value,color)
                int value = Integer.parseInt( attributes.getNamedItem("value").getNodeValue() );
                DiceColor color = DiceColor.valueOf( attributes.getNamedItem("color").getNodeValue() );

                //Set constraints to corresponding pattern cell
                pattern[row][col] = new Cell(value,color);
            }

            return new WindowPattern(patternID,title,imageURL,difficulty,pattern);

        } catch (Exception e) {
            //Bad formatting of xml is caught and method returns false
            throw new BadFormattedPatternFileException();
        }
    }

    /**
     * Returns the id of the partner pattern of the given pattern
     * @param patternID the id of the pattern to check
     * @return the id of the partner pattern of the given pattern
     */
    private String getPartnerPatternID(String patternID){
        try{
            Document document = XMLFileFinder.getFileDocument(PATH.concat(patternID).concat(".xml"));
            return document.getElementsByTagName("partnerID").item(0).getTextContent();
        } catch( Exception e ){
            //Bad formatting of xml is caught and method returns false
            throw new BadFormattedPatternFileException();
        }
    }



}
