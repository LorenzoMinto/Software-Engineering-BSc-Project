package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.XMLFileReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Manages creation and distribution of Tool Cards
 *
 * @author Federico Haag
 */
public class ToolCardsManager {

    /**
     * The file system path to find toolcards .xml files
     */
    private static final String PATH = "assets/toolcards/";

    /**
     * List of all the toolcards that can be distributed in the current game
     */
    private List<String> availableToolCards;

    private PlacementRule defaultPlacementRule;

    /**
     * Constructor of the class. Checks if there are toolcards than can be loaded
     * from file system and if yes loads them.
     *
     * @throws NoToolCardsFoundInFileSystemException if no toolcards .xml files can be loaded
     */
    public ToolCardsManager(PlacementRule defaultPlacementRule){
        this.defaultPlacementRule = defaultPlacementRule;

        try{
            this.availableToolCards = XMLFileReader.getFilesNames(PATH);
        } catch (IOException e) {
            throw new NoToolCardsFoundInFileSystemException();
        }
    }

    /**
     * Returns the requested quantity of toolcards, if there are enough available.
     * It should not happen that the method is called if no toolcards are available;
     * due to this reason a RuntimeException is thrown in that case.
     *
     * @param quantity the amount of toolcards requested
     * @return the requested quantity of toolcards, if there are enough available
     * @throws BadFormattedToolCardFileException if during the loading of a toolcard it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    public List<ToolCard> getRandomToolCards(int quantity){

        List<ToolCard> toolCards = new ArrayList<>();
        List<String> usedToolCards = new ArrayList<>();

        /*if( availableToolCards.size() >= quantity ){

            Random r = new Random();

            for(int i=0; i<quantity; i++){

                //Choose randomly one of the available toolcards
                int randomIndex = r.nextInt(availableToolCards.size());
                String randomToolCardID = availableToolCards.get(randomIndex);

                //Add the selected toolcard to a list tracking all the selected patterns
                usedToolCards.add(randomToolCardID);

                //Removes the selected toolcard from the available to avoid double choice
                availableToolCards.remove(randomToolCardID);

                //Load the randomly selected pattern
                ToolCard randomToolCard;
                try {

                    randomToolCard = loadToolCardFromFileSystem(randomToolCardID);

                } catch (BadFormattedToolCardFileException e){

                    //re-insert the pattern to the available ones before rethrowing to the caller
                    availableToolCards.addAll(usedToolCards);

                    throw new BadFormattedToolCardFileException();
                }

                //The successfully loaded pattern is added in a list that will be returned at the end of bulk loading
                toolCards.add(randomToolCard);
            }
        } else {
            throw new BadBehaviourRuntimeException("Can't create the requested number of toolcards. This error is not handlable at all");
        }*/

        return toolCards;
    }

    /**
     * Loads from file the specified toolcard loading all its properties in a new {@link ToolCard} class.
     *
     * @param toolCardID the ID String representing the toolCard to be loaded
     * @return the requested {@link ToolCard}
     * @throws BadFormattedToolCardFileException if during the loading of a toolcard it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    private ToolCard loadToolCardFromFileSystem(String toolCardID){

        try{

            Document document = XMLFileReader.getFileDocument(PATH.concat(toolCardID).concat(".xml"));

            //Parse from xml the number of rows of the pattern
            String toolCardTitle = document.getElementsByTagName("title").item(0).getTextContent();
            String toolCardImageURL = document.getElementsByTagName("imageURL").item(0).getTextContent();
            String toolCardDescription = document.getElementsByTagName("description").item(0).getTextContent();
            int neededTokens = Integer.parseInt( document.getElementsByTagName("neededTokens").item(0).getTextContent() );
            int tokensUsageMultiplier = Integer.parseInt( document.getElementsByTagName("tokensUsageMultiplier").item(0).getTextContent() );

            //Placement Rules PARSING

            PlacementRule placementRule;
            NodeList placementRules = document.getElementsByTagName("placementRule");

            if(placementRules.getLength()==0){
                //If no placement rules are specified in the file, the default one is applied
                placementRule = this.defaultPlacementRule;

            } else {
                //The placement rule is built by decorating additional rules according to the "Decorator Pattern"
                placementRule = new EmptyPlacementRule();
                for(int i=0; i<placementRules.getLength(); i++){

                    NamedNodeMap a = placementRules.item(i).getAttributes();

                    //Parse from xml the decoratorName constraint
                    String decoratorName = a.getNamedItem("decoratorName").getNodeValue();

                    /*Creates a PlacementRule decorator of the type specified in "decoratorName"
                     **and then decorates it with the previous rules (default is EmptyPlacementRule*/
                    placementRule = (PlacementRule) Class.forName(decoratorName).getConstructor(PlacementRule.class).newInstance(placementRule);
                }
            }

            //ControllerStates transitions PARSING

            HashMap<String,String> controllerStateRules = new HashMap<>();

            NodeList controllerStateRulesTags = document.getElementsByTagName("controllerStateRule");
            for(int i=0; i<controllerStateRulesTags.getLength(); i++){

                NamedNodeMap a = controllerStateRulesTags.item(i).getAttributes();

                String prevState = a.getNamedItem("prevState").getNodeValue();
                String nextState = a.getNamedItem("nextState").getNodeValue();

                controllerStateRules.put(prevState,nextState);
            }

            return new ToolCard(toolCardID,toolCardTitle,toolCardDescription,toolCardImageURL,neededTokens,tokensUsageMultiplier,controllerStateRules,placementRule);

        } catch (Exception e){
            throw new BadFormattedToolCardFileException();
        }

    }
}
