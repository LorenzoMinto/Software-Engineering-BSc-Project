package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    public Turn(int number, Player player) {
        if(player==null){ throw new IllegalArgumentException("Asked to create a turn giving null player"); }

        this.number = number;
        this.currentPlayer = player;
        this.draftedDice = null;
        this.draftedAndPlaced = false;
        this.usedToolCard = null;
    }

    private int number;

    public Player currentPlayer;

    private boolean draftedAndPlaced;

    private Dice draftedDice;

    private ToolCard usedToolCard;


    //Getters

    public int getNumber() {
        return number;
    }

    public boolean hasDrafted(){
        return !( draftedDice == null );
    }

    public boolean hasDraftedAndPlaced(){
        return draftedAndPlaced;
    }

    public boolean hasUsedToolCard(){
        return !( this.usedToolCard == null );
    }

    public Dice getDraftedDice() { return draftedDice.copy(); }

    //Setters

    public void setDraftedDice(Dice dice){
        this.draftedDice = dice;
    }

    public void setDraftedAndPlaced(){
        if(this.draftedDice==null){ throw new IllegalStateException("Asked to setDraftedAndPlaced but draftedDice is null"); }
        this.draftedAndPlaced = true;
    }

    public void setUsedToolCard(ToolCard card){
        this.usedToolCard = card;
    }

    public void setCurrentDiceAsPlaced(){
        this.draftedAndPlaced = true;
    }

    //Utils

    public boolean isCurrentPlayer(Player player){
        return ( this.currentPlayer.equals(player) );
    }



}
