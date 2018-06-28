package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.FileFinder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages creation and distribution of Tool Cards
 *
 * @author Federico Haag
 */
public class ToolCardManager {

    /**
     * The file system path to find toolCards .xml files
     */
    private static final String PATH = "toolcards/";

    /**
     * String used as message of BadBehaviourRuntimeException in getRandomToolCards
     */
    private static final String CANT_CREATE_THE_REQUESTED_NUMBER_OF_TOOL_CARDS = "Can't create the requested number of toolCards.";

    /**
     * String used as message of IllegalArgumentException in getRandomToolCards
     */
    private static final String CANT_GET_A_NEGATIVE_NUMBER_OF_RANDOM_TOOL_CARDS = "Can't get a negative number of random toolCards";

    /**
     * List of all the toolCards that can be distributed in the current game
     */
    private List<String> availableToolCardsIDs;

    /**
     * Default {@link PlacementRule}
     */
    private PlacementRule defaultPlacementRule;

    /**
     * FileFinder
     */
    private FileFinder fileFinder = new FileFinder();

    /**
     * Constructor of the class. Checks if there are toolCards than can be loaded
     * from file system and if yes loads them.
     *
     * @param defaultPlacementRule the default placement rule to apply to toolCards that in the xml file
     *                             does not include specifications about what should be placement rule
     * @throws NoToolCardsFoundInFileSystemException if no toolCards .xml files can be loaded
     */
    public ToolCardManager(PlacementRule defaultPlacementRule){
        this.defaultPlacementRule = defaultPlacementRule;

        try{
            List<String> fileNames = fileFinder.getFilesNamesInDirectory(PATH);
            this.availableToolCardsIDs = fileNames.stream().map(FileFinder::getXMLFileName).collect(Collectors.toList());
        } catch (Exception e) {
            throw new NoToolCardsFoundInFileSystemException();
        }
    }

    /**
     * Returns the requested quantity of toolCards, if there are enough available.
     * It should not happen that the method is called if no toolCards are available;
     * due to this reason a RuntimeException is thrown in that case.
     *
     * @param quantity the amount of toolCards requested
     * @return the requested quantity of toolCards, if there are enough available
     * @throws BadFormattedToolCardFileException if during the loading of a toolCard it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    public List<ToolCard> getRandomToolCards(int quantity){
        if(quantity < 0){ throw new IllegalArgumentException(CANT_GET_A_NEGATIVE_NUMBER_OF_RANDOM_TOOL_CARDS);}

        List<ToolCard> toolCards = new ArrayList<>();

        if( availableToolCardsIDs.size() >= quantity ){

            Random r = new Random();

            for(int i=0; i<quantity; i++){

                //Choose randomly one of the available toolCards
                int randomIndex = r.nextInt(availableToolCardsIDs.size());
                String randomToolCardID = availableToolCardsIDs.get(randomIndex);

                //Remove the selected toolCard from the available ones to avoid double choice
                availableToolCardsIDs.remove(randomToolCardID);

                //Load the randomly selected pattern
                ToolCard randomToolCard = loadToolCardFromFileSystem(randomToolCardID);

                //The successfully loaded pattern is added in a list that will be returned at the end of bulk loading
                toolCards.add(randomToolCard);
            }
        } else {
            throw new BadBehaviourRuntimeException(CANT_CREATE_THE_REQUESTED_NUMBER_OF_TOOL_CARDS);
        }

        return toolCards;
    }

    /**
     * Loads from file the specified toolCard loading all its properties in a new {@link ToolCard} class.
     *
     * @param toolCardID the ID String representing the toolCard to be loaded
     * @return the requested {@link ToolCard}
     * @throws BadFormattedToolCardFileException if during the loading of a toolCard it comes out that
     * the file is not correctly formatted. This error is not handlable in this context so it is thrown to the caller.
     */
    private ToolCard loadToolCardFromFileSystem(String toolCardID){

        Properties params = new Properties();
        params.put("id", toolCardID);

        try{

            Document document = fileFinder.getFileDocument(PATH.concat(toolCardID).concat(".xml"));

            //Parse from xml the properties of the toolCard
            params.put("title", document.getElementsByTagName("title").item(0).getTextContent());
            params.put("imageURL", document.getElementsByTagName("imageURL").item(0).getTextContent());
            params.put("description", document.getElementsByTagName("description").item(0).getTextContent());
            params.put("neededTokens", document.getElementsByTagName("neededTokens").item(0).getTextContent());
            params.put("tokensUsageMultiplier", document.getElementsByTagName("tokensUsageMultiplier").item(0).getTextContent());

            //Move counter PARSING
            Node moveCounter = document.getElementsByTagName("moveCounter").item(0);
            HashSet<Integer> possibleMovesCountSet = new HashSet<>();
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

            return new ToolCard(params,controllerStateRules,placementRule, possibleMovesCountSet);

        } catch (Exception e){
            throw new BadFormattedToolCardFileException();
        }
    }
}
