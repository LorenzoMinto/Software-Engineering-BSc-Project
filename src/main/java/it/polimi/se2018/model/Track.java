package it.polimi.se2018.model;

import java.util.List;

public class Track {

    private List<TrackSlot> slots;

    //Creates new TrackSlot with list of dices passed
    public void processDraftPool(List<Dice> dices) {
        TrackSlot nextSlot = new TrackSlot(dices);
        slots.add(nextSlot);
    }

    //Removes dice from corresponding TrackSlot
    public boolean takeDice(Dice dice, int slotNumber){
        if (slotNumber >= slots.size()) {
            //TrackSlotIndexOutOfBound
            return false;
        }
        TrackSlot slot = slots.get(slotNumber);
        return slot.removeDice(dice);
    }

    //Returns false if slotNumber indexed is not found, else adds dice to corresponding TrackSlot
    public boolean putDice(Dice dice, int slotNumber){
        if (slotNumber >= slots.size()) {
            return false;
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.addDice(dice);
        return true;
    }

}