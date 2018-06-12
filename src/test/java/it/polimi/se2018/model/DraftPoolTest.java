package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for {@link DraftPool} class
 *
 * @author Federico Haag
 */
public class DraftPoolTest {

    private static DraftPool draftPool;

    /**
     * Initializes a draft pool

     */
    @Before
    public void initializeDraftPool() {
        draftPool = new DraftPool(new ArrayList<>());
    }

    /**
     * Tests the constructor of {@link DraftPool}
     * @see DraftPool#DraftPool(List)
     */
    @Test
    public void testConstructor(){
        List<Dice> dices = new ArrayList<>();
        dices.add(new Dice(DiceColor.RED));
        dices.add(new Dice(DiceColor.YELLOW));

        draftPool = new DraftPool(dices);
        assertNotNull(draftPool);
    }

    /**
     * Tests the impossibility of creating a {@link DraftPool} with a null list of dice
     * @see DraftPool#DraftPool(List)
     */
    @Test
    public void testConstructorWithNullDices(){
        try{
            draftPool = new DraftPool(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests putting a dice in the die bag and retrieving it
     * @see DraftPool#getDices()
     */
    @Test
    public void testPutAndGetDice(){
        draftPool.putDice(new Dice(DiceColor.RED, 1));
        List<Dice> dices = draftPool.getDices();
        assertEquals(1, dices.size());
        assertEquals(new Dice(DiceColor.RED, 1), dices.get(0));
    }

    /**
     * Tests the impossibility of putting a null dice in the draft pool
     * @see DraftPool#putDice(Dice)
     */
    @Test
    public void testPutNullDice(){
        try {
            draftPool.putDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests rerolling the dices in the draft pool
     * @see DraftPool#reRoll()
     */
    @Test
    public void testReroll() {
        List<Dice> dices = new ArrayList<>();

        Dice dice1 = new Dice(DiceColor.RED,3);
        Dice dice2 = new Dice(DiceColor.BLUE,1);
        dices.add(dice1);
        dices.add(dice2);

        draftPool = new DraftPool(dices);
        draftPool.reRoll();


        for (Dice dice : draftPool.getDices()){
            if(dice.getColor()== DiceColor.BLUE  || dice.getColor()== DiceColor.RED
                    && dice.getValue()>= 1 && dice.getValue()<= 6) {
                assertEquals(dices.size(), draftPool.getDices().size());
            }else {
                fail();
            }
        }
    }

    /**
     * Tests drafting a dice from the draft pool. Checks that the drafted dice is removed from the draft pool
     * @see DraftPool#draftDice(Dice)
     */
    @Test
    public void testDraftDice() {
        Dice dice = new Dice(DiceColor.RED,3);
        draftPool.putDice(dice);

        draftPool.draftDice(dice);
        assertEquals(0,draftPool.getDices().size());
    }

    /**
     * Tests drafting from the draft pool a dice that is not in it
     * @see DraftPool#draftDice(Dice)
     */
    @Test
    public void testDraftNotInDraftPool(){
        Dice dice = new Dice(DiceColor.RED, 1);
        draftPool.putDice(new Dice(DiceColor.YELLOW, 2));

        assertFalse(draftPool.draftDice(dice));
    }
}