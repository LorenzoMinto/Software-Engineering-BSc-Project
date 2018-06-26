package it.polimi.se2018.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for Decorators.
 *
 * @author Jacopo Pio Gargano
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AdjacentColorPlacementRuleDecoratorTest.class, AdjacentDicePlacementRuleDecoratorTest.class,
        AdjacentValuePlacementRuleDecoratorTest.class, BorderPlacementRuleDecoratorTest.class,
        ColorPlacementRuleDecoratorTest.class, ColorRestrictionPlacementRuleDecoratorTest.class,
        EmptyPlacementRuleTest.class, NotAdjacentDicePlacementRuleDecoratorTest.class,
        ValuePlacementRuleDecoratorTest.class})
public class DecoratorsSuite {
    //This class is empty, it's only used as a holder for above annotations
}