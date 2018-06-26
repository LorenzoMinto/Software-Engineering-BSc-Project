package it.polimi.se2018.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for Controller States. All the states are tested here.
 *
 * @author Jacopo Pio Gargano
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ChangeDiceValueControllerStateTest.class, ChangeDiceValueUnitaryControllerStateTest.class,
        ChooseFromTrackControllerStateTest.class, ControllerStateManagerTest.class, DraftControllerStateTest.class,
        EndControllerStateTest.class, MoveControllerStateTest.class, PlaceControllerStateTest.class,
        StartControllerStateTest.class, ToolCardControllerStateTest.class})
public class ControllerStatesSuite {
    //This class is empty, it's only used as a holder for above annotations
}