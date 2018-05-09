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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ToolCardsManager {

    private static final String PATH = "assets/toolcards/";

    private List<String> availableToolCards;

    public ToolCardsManager() throws NoToolCardsFoundInFileSystemException{
        try{
            this.availableToolCards = XMLFileReader.getFilesNames(PATH);
        } catch (IOException e) {
            throw new NoToolCardsFoundInFileSystemException();
        }
    }

    public List<ToolCard> getRandomToolCards(int quantity) throws BadFormattedToolCardFileException{

        List<ToolCard> toolCards = new ArrayList<>();
        List<String> usedToolCards = new ArrayList<>();

        if( availableToolCards.size() >= quantity ){

            Random r = new Random();

            for(int i=0; i<quantity; i++){

                //Choose randomly one of the available toolcards
                int randomIndex = r.nextInt(availableToolCards.size());
                String randomToolCardID = availableToolCards.get(randomIndex);

                //Add the selected toolcard to a list tracking all the selected patterns
                usedToolCards.add(randomToolCardID);

                //Removes the selected toolcard from the available to avoid double choise
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
        }

        return toolCards;
    }

    private ToolCard loadToolCardFromFileSystem(String toolCardID) throws BadFormattedToolCardFileException{

        try{

            File file = new File(PATH.concat(toolCardID).concat(".xml"));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            //Parse from xml the number of rows of the pattern
            String toolCardImageURL = document.getElementsByTagName("imageURL").item(0).getTextContent();
            String toolCardDescription = document.getElementsByTagName("description").item(0).getTextContent();
            int neededTokens = Integer.parseInt( document.getElementsByTagName("neededTokens").item(0).getTextContent() );

            //Placement Rules PARSING

            PlacementRule placementRule = new EmptyPlacementRule();
            NodeList placementRules = document.getElementsByTagName("placementRule");
            for(int i=0; i<placementRules.getLength(); i++){

                NamedNodeMap a = placementRules.item(i).getAttributes();

                //Parse from xml the decoratorName constraint
                String decoratorName = a.getNamedItem("decoratorName").getNodeValue();

                /*Creates a PlacementRule decorator of the type specified in "decoratorName"
                **and then decorates it with the previous rules (default is EmptyPlacementRule*/
                placementRule = (PlacementRule) Class.forName(decoratorName).getConstructor(PlacementRule.class).newInstance(placementRule);
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

            return new ToolCard(toolCardID,toolCardDescription,toolCardImageURL,neededTokens,controllerStateRules,placementRule);

        } catch (Exception e){
            throw new BadFormattedToolCardFileException();
        }

    }
}
