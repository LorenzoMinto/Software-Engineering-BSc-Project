package it.polimi.se2018.model;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WindowPatternManager {

    private static final String PATH = "assets/patterns/";
    private static final String EXTENSION = ".xml";

    private List<String> availablePatterns;

    public WindowPatternManager() throws NoPatternsFoundInFileSystemException {

        availablePatterns = new ArrayList<>();

        //Parse "patterns" directory in order to find all patterns .xml files
        File dir = new File(PATH);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));

        //Exception is thrown if no pattern files are found in the folder (could happen even due to IO error)
        if(files==null) throw new NoPatternsFoundInFileSystemException();

        //Add the found patterns to availablePatterns
        for(File file : files){

            //PatternID is filename without ".xml"
            String patternID = file.getName().substring(0, file.getName().length() - 4 );

            //Adds patternID to list of availablePatterns
            availablePatterns.add( patternID );
        }
    }

    public boolean hasAvailablePatterns(){
        return !(availablePatterns.isEmpty());
    }

    public List<String> getAvailablePatternsNames() {

        return new ArrayList<>(availablePatterns);
    }

    private WindowPattern loadPatternFromFileSystem(String patternID) throws BadFormattedPatternFileException {

        try {

            Cell[][] pattern;

            File file = new File(PATH.concat(patternID).concat(EXTENSION));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

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
        }

        return patterns;

    }

}
