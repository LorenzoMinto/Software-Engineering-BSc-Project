package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.XMLFileFinder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Manages creation and distribution of Tool Cards
 *
 * @author Federico Haag
 */
public class ToolCardManager {

    /**
     * The file system path to find toolcards .xml files
     */
    private static final String PATH = "assets/toolcards/";

    /**
     * List of all the toolcards that can be distributed in the current game
     */
    private List<String> availableToolCards;

    /**
     * Default {@link PlacementRule}
     */
    private PlacementRule defaultPlacementRule;

    /**
     * Constructor of the class. Checks if there are toolcards than can be loaded
     * from file system and if yes loads them.
     *
     * @param defaultPlacementRule the default placement rule to apply to toolcards that in the xml file
     *                             does not include specifications about what should be placement rule
     * @throws NoToolCardsFoundInFileSystemException if no toolcards .xml files can be loaded
     */
    public ToolCardManager(PlacementRule defaultPlacementRule){
        this.defaultPlacementRule = defaultPlacementRule;

        try{
            this.availableToolCards = XMLFileFinder.getFilesNames(PATH);
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
        if(quantity < 0){ throw new IllegalArgumentException("Can't get a negative number of random toolcards");}

        List<ToolCard> toolCards = new ArrayList<>();

        if( availableToolCards.size() >= quantity ){

            Random r = new Random();

            for(int i=0; i<quantity; i++){

                //Choose randomly one of the available toolcards
                int randomIndex = r.nextInt(availableToolCards.size());
                String randomToolCardID = availableToolCards.get(randomIndex);

                //Remove the selected toolcard from the available ones to avoid double choice
                availableToolCards.remove(randomToolCardID);

                //Load the randomly selected pattern
                ToolCard randomToolCard = loadToolCardFromFileSystem(randomToolCardID);

                //The successfully loaded pattern is added in a list that will be returned at the end of bulk loading
                toolCards.add(randomToolCard);
            }
        } else {
            throw new BadBehaviourRuntimeException("Can't create the requested number of toolcards. Controller should not ask for so much cards. This error is not handlable at all");
        }

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

        Properties params = new Properties();
        params.put("id", toolCardID);

        try{

            Document document = XMLFileFinder.getFileDocument(PATH.concat(toolCardID).concat(".xml"));

            //Parse from xml the properties of the toolcard
            params.put("title", document.getElementsByTagName("title").item(0).getTextContent());
            params.put("imageURL", document.getElementsByTagName("imageURL").item(0).getTextContent());
            params.put("description", document.getElementsByTagName("description").item(0).getTextContent());
            params.put("neededTokens", document.getElementsByTagName("neededTokens").item(0).getTextContent());
            params.put("tokensUsageMultiplier", document.getElementsByTagName("tokensUsageMultiplier").item(0).getTextContent());

            //Move counter PARSING
            Node moveCounter = document.getElementsByTagName("moveCounter").item(0);
            Set<Integer> possibleMovesCountSet = new HashSet<Integer>();
            if (moveCounter != null ) {
                NamedNodeMap attributes = moveCounter.getAttributes();
                String quantifier = attributes.getNamedItem("quantifier").getNodeValue();
                String maximumQuantity = attributes.getNamedItem("count").getNodeValue();
                if (quantifier.equals("upto")) {
                    for (int i=0; i<= Integer.parseInt(maximumQuantity); i++) {
                        possibleMovesCountSet.add(i);
                    }
                } else {
                    possibleMovesCountSet.add(Integer.parseInt(maximumQuantity));
                }
            }
            params.put("possibleMovesCountSet", possibleMovesCountSet);

            //Placement Rules PARSING
            PlacementRule placementRule;
            NodeList placementRules = document.getElementsByTagName("placementRule");

            if(placementRules.getLength()==0){
                //If no placement rules are specified in the file, set the default one
                placementRule = this.defaultPlacementRule;

            } else {
                //Build the placement rule by decorating it with additional rules, following the Decorator Pattern
                placementRule = new EmptyPlacementRule();
                for(int i=0; i<placementRules.getLength(); i++){

                    NamedNodeMap attributes = placementRules.item(i).getAttributes();

                    //Parse from xml the decoratorName constraint
                    String decoratorName = attributes.getNamedItem("decoratorName").getNodeValue();

                    /*
                    Creates a PlacementRule decorator of the specified type in "decoratorName"
                    and then decorates it with the previous rules (default is EmptyPlacementRule)
                    */

                    Class<?> currentClass = Class.forName(PlacementRule.class.getPackage().getName()+"."+decoratorName);
                    Constructor<?> currentConstructor = currentClass.getConstructor(PlacementRule.class);
                    placementRule = (PlacementRule) currentConstructor.newInstance(placementRule);
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

            return new ToolCard(params,controllerStateRules,placementRule);

        } catch (Exception e){
            throw new BadFormattedToolCardFileException();
        }

    }
}
