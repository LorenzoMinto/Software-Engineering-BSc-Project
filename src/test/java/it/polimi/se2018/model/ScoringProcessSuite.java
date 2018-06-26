package it.polimi.se2018.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for Cards. The whole scoring process is tested here.
 *
 * @author Jacopo Pio Gargano
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({RowsColumnsPublicObjectiveCardTest.class, DiagonalsPublicObjectiveCardTest.class,
        SetPublicObjectiveCardTest.class, PrivateObjectiveCardTest.class})
public class ScoringProcessSuite {
    //This class is empty, it's only used as a holder for above annotations
}
