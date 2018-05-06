package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;

import java.util.List;

public class ToolCardsFactory {

    private static ToolCardsFactory instance = null;

    private ToolCardsFactory(){}

    public static ToolCardsFactory getInstance(){
        if(instance == null) {
            instance = new ToolCardsFactory();
        }
        return instance;
    }

    public List<ToolCard> getRandomToolCards(int quantity){
        //TODO: implement this method
    }

    private ToolCard loadToolCardFromFileSystem(String toolCardID){
        //TODO: implement this method
    }
}
