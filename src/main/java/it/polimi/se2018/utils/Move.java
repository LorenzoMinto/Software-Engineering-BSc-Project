package it.polimi.se2018.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for each kind of move that can be performed during game play
 */
public enum Move {
    CHOOSE_WINDOW_PATTERN ("Choose window pattern","has chosen",getChooseWindowPatternParams()),
    END_TURN ("End turn","ended turn.", getEmptyParams()),
    DRAFT_DICE_FROM_DRAFTPOOL ("Draft dice from draft pool", "has drafted", getDraftDiceFromDraftpoolParams()),
    PLACE_DICE_ON_WINDOWPATTERN ("Place dice on windowPattern", "has positioned", getPlaceDiceOnWindowPatternParams()),
    USE_TOOLCARD ("Use toolCard", "has used", getUseToolcardParams()),
    INCREMENT_DRAFTED_DICE ("Increment drafted dice", "has incremented dice.", getEmptyParams()),
    DECREMENT_DRAFTED_DICE ("Decrement drafted dice", "has decremented dice.", getEmptyParams()),
    CHANGE_DRAFTED_DICE_VALUE ("Change drafted dice value", "has changed dice value to",getChangeDiceValueParams()),
    CHOOSE_DICE_FROM_TRACK ("Choose dice from track", "has chosen", getChooseDiceFromTrackParams()),
    RETURN_DICE_TO_DRAFTPOOL ("Return dice to draft pool", "has returned dice to draftpool.", getEmptyParams()),
    MOVE_DICE ("Move dice", "moved", getMoveDiceParams()),
    END_EFFECT ("End toolCard effect", "has ended the active toolcard effect.", getEmptyParams()),
    JOIN("Join waiting room", "joined waiting room.", getEmptyParams()),
    BACK_GAME ("Back to game", "joined back the game.", getEmptyParams()),
    LEAVE ("Leave waiting room", "has left the game.", getEmptyParams()),
    QUIT ("Quit the game", "has quit the game.", getEmptyParams());

    /**
     * A human readable text representation of the move in active form
     */
    private final String activeTextualRepresentation;

    /**
     * A human readable text representation of the move in passive form
     */
    private final String passiveTextualRepresentation;

    /**
     * Params of the message of the move
     */
    private final HashMap<String,String> params;

    /**
     * Constructor for this enum
     * @param textualRep the human readable text representation of the move
     */
    Move(String textualRep, String passiveRep, HashMap<String,String> params){
        this.activeTextualRepresentation = textualRep;
        this.passiveTextualRepresentation = passiveRep;
        this.params = params;
    }

    /**
     * Returns a human readable text representation of the move.
     * Used in CLI for printing moves and in GUI for building buttons.
     *
     * @return a human readable text representation of the move.
     */
    public String getTextualREP(){
        return this.activeTextualRepresentation;
    }

    /**
     * Returns a composed string representing the move made by a player using the given params
     * @param oldparams given params to be evaluated
     * @return a composed string representing the move made by a player using the given params
     */
    public String getRepresentationOfMove(Map<String,Object> oldparams){
        String string = " ";

        string = string.concat(this.passiveTextualRepresentation).concat(" ");

        for(Map.Entry<String,String> entry : this.params.entrySet()){
            string = string.concat(entry.getKey()).concat(": ").concat(oldparams.get(entry.getValue()).toString()).concat(", ");
        }

        return string.substring(0,string.length()-2);
    }

    /**
     * Returns params of move "Choose Window Pattern"
     * @return params of move "Choose Window Pattern"
     */
    private static HashMap<String,String> getChooseWindowPatternParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("","windowPatternName");
        return map;
    }

    /**
     * Returns empty set of params
     * @return empty set of params
     */
    private static HashMap<String,String> getEmptyParams(){
        return new HashMap<>();
    }

    /**
     * Returns params of move "Draft Dice From Draftpool"
     * @return params of move "Draft Dice From Draftpool"
     */
    private static HashMap<String,String> getDraftDiceFromDraftpoolParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("","dice");
        return map;
    }

    /**
     * Returns params of move "Place Dice on Window Pattern"
     * @return params of move "Place Dice on Window Pattern"
     */
    private static HashMap<String,String> getPlaceDiceOnWindowPatternParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("row","row");
        map.put("col","col");
        return map;
    }

    /**
     * Returns params of move "Use Toolcard"
     * @return params of move "Use Toolcard"
     */
    private static HashMap<String,String> getUseToolcardParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("","toolCard");
        return map;
    }

    /**
     * Returns params of move "Change Dice Value"
     * @return params of move "Change Dice Value"
     */
    private static HashMap<String,String> getChangeDiceValueParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("","value");
        return map;
    }

    /**
     * Returns params of move "Choose Dice From Track"
     * @return params of move "Choose Dice From Track"
     */
    private static HashMap<String,String> getChooseDiceFromTrackParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("dice","dice");
        return map;
    }

    /**
     * Returns params of move "Move Dice"
     * @return params of move "Move Dice"
     */
    private static HashMap<String,String> getMoveDiceParams(){
        HashMap<String,String> map = new HashMap<>();
        map.put("from col","colFrom");
        map.put("from row","rowFrom");
        map.put("to col","colTo");
        map.put("to row","rowTo");
        return map;
    }
}
