package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private int number;
    private Player[] players;
    private DraftPool draftPool;
    private DiceBag diceBag;

    public Round(int number, Player[] players, DiceBag diceBag) {
        this.number = number;
        this.players = players;
        this.diceBag = diceBag;
        this.draftPool = new DraftPool(diceBag.getDices(players.length*2+1));
    }

    public List<Dice> emptyDraftPool(){ return new ArrayList<>(draftPool.dices); }

    public boolean removeDiceFromDraftPool(Dice dice) {
        return draftPool.takeDice(dice);
    }


}
