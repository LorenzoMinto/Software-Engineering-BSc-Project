package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.FileFinder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the loading and creation of {@link WindowPattern}
 *
 * @author Federico Haag
 */
public class WindowPatternManager {

    private static final Random RANDOM = new Random();

    /**
     * The file system path to find windowPatterns .xml files
     */
    private static final String PATH = "patterns/";

    /**
     * String used as message of BadBehaviourRuntimeException in getPairsOfPatterns()
     */
    private static final String CANT_CREATE_THE_NUMBER_OF_WINDOW_PATTERN_REQUESTED = "Cant create the number of window pattern requested.";

    /**
     * String used as message of IllegalArgumentException in getPairsOfPatterns()
     */
    private static final String CANT_GET_A_NEGATIVE_NUMBER_OF_COUPLES_OF_WINDOW_PATTERNS = "Can't get a negative number of couples of windowPatterns.";

    /**
     * List of all the toolCards that can be distributed in the current game
     */
    private List<String> availablePatternsIDs;

    /**
     * FileFinder
     */
    private FileFinder fileFinder = new FileFinder();

    /**
     * Constructor of the class. Checks if there are window patterns than can be loaded
     * from file system and if yes loads them.
     */
    public WindowPatternManager() {

        try{

            List<String> fileNames = fileFinder.getFilesNamesInDirectory(PATH);
            this.availablePatternsIDs = fileNames.stream().map(FileFinder::getXMLFileName).collect(Collectors.toList());
        } catch (Exception e){
            throw new NoPatternsFoundInFileSystemException();
        }

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
        if(numberOfPairs < 0){ throw new IllegalArgumentException(CANT_GET_A_NEGATIVE_NUMBER_OF_COUPLES_OF_WINDOW_PATTERNS);}

        Set<WindowPattern> couplesOfPatterns = new HashSet<>();

        if( availablePatternsIDs.size() >= numberOfPairs * 2 ){

            for(int i=0; i<numberOfPairs; i++){

                //Choose randomly one of the available patterns
                int randomIndex = RANDOM.nextInt(availablePatternsIDs.size());
                String randomPatternID = availablePatternsIDs.get(randomIndex);
                String randomPartnerPatternID = getPartnerPatternID(randomPatternID);

                //Removes the selected pattern from the available to avoid double choise
                availablePatternsIDs.remove(randomPatternID);
                availablePatternsIDs.remove(randomPartnerPatternID);

                //Load the randomly selected pattern
                WindowPattern randomPattern = loadPatternFromFileSystem(randomPatternID.concat(".xml"));
                WindowPattern randomPartnerPattern = loadPatternFromFileSystem(randomPartnerPatternID.concat(".xml"));

                //The successfully loaded patterns are added in a list that will be returned at the end of bulk loading
                couplesOfPatterns.add(randomPattern);
                couplesOfPatterns.add(randomPartnerPattern);
            }
        } else {
            throw new BadBehaviourRuntimeException(CANT_CREATE_THE_NUMBER_OF_WINDOW_PATTERN_REQUESTED);
        }

        return couplesOfPatterns;
    }

    /**
     * Loads from file the specified toolCard loading all its properties in a new {@link WindowPattern} class.
     *
     * @param patternID the ID String representing the window pattern to be loaded
     * @return the requested {@link ToolCard}
     * @throws BadFormattedPatternFileException if during the loading of a window pattern it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    private WindowPattern loadPatternFromFileSystem(String patternID) {

        try {

            Cell[][] pattern;

            Document document = fileFinder.getFileDocument(PATH.concat(patternID));

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
            Document document = fileFinder.getFileDocument(PATH.concat(patternID).concat(".xml"));
            return document.getElementsByTagName("partnerID").item(0).getTextContent();
        } catch( Exception e ){
            //Bad formatting of xml is caught and method returns false
            throw new BadFormattedPatternFileException();
        }
    }
}
