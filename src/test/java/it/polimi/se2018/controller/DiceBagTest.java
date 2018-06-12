package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.DiceColor;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link DiceBag} class
 *
 * @author Federico Haag
 */
public class DiceBagTest {

    private static DiceBag diceBag;
    private static final int numberOfDicesPerColor = 18;

    /**
     * Initializes the dice bag
     * @see DiceBag#DiceBag(int)
     */
    @BeforeClass
    public static void initializeDiceBag(){
        diceBag = new DiceBag(numberOfDicesPerColor);
    }

    /**
     * Tests the constructor with allowed and not allowed parameters
     * @see DiceBag#DiceBag(int)
     */
    @Test
    public void testConstructor(){
        new DiceBag(10);
        try{
            new DiceBag(-1);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of the dice
     * @see DiceBag#getDices(int)
     */
    @Test
    public void testGetDices(){
        List<Dice> dices = diceBag.getDices(numberOfDicesPerColor);
        assertEquals(numberOfDicesPerColor, dices.size());
        for (Dice dice: dices) {
            assertNotNull(dice);
        }
    }

    /**
     * Tests the impossibility of retrieving a negative number of dice
     * @see DiceBag#getDices(int)
     */
    @Test
    public void testGetNegativeQuantityOfDices(){
        try{
            diceBag.getDices(-1);
            fail();
        }catch (BadBehaviourRuntimeException e){
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of retrieving more dices than available
     * @see DiceBag#getDices(int)
     */
    @Test
    public void testGetMoreDicesThanAvailable(){
        try{
            diceBag.getDices(numberOfDicesPerColor*(DiceColor.values().length-1));
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests dice bag creates all different color dices if there is only 1 dice per color
     * @see DiceBag#getDices(int)
     */
    @Test
    public void testHasAllDifferentColorDices() {
        DiceBag diceBag = new DiceBag(1);
        List<Dice> dices = diceBag.getDices(DiceColor.values().length-1);

        for (Dice dice: dices) {
            dice.setValue(3);
        }

        assertTrue(
                dices.contains(new Dice(RED,3))&&
                         dices.contains(new Dice(GREEN,3))&&
                         dices.contains(new Dice(BLUE,3))&&
                         dices.contains(new Dice(PURPLE,3))&&
                         dices.contains(new Dice(YELLOW,3))
        );
    }

    /**
     *Tests adding a dice to the dice bag
     * @see DiceBag#addDice(Dice)
     */
    @Test
    public void testAddDice() {
        DiceBag diceBag = new DiceBag(0);
        Dice dice = new Dice(RED,4);
        diceBag.addDice(dice);
        assertTrue(diceBag.getDices(1).get(0).getColor()== RED);
    }

    /**
     * Tests adding a null dice to the dice bag
     * @see DiceBag#addDice(Dice)
     */
    @Test
    public void testAddNullDice(){
        try {
            diceBag.addDice(null);
        }catch (NullPointerException e){}
    }

}