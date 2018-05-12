package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.view.View;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class ToolCardControllerState extends ControllerState {

    public ToolCardControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = TOOLCARD_ONLY;
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        if (controller.canUseSpecificToolCard(toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            view.showMessage("Can't use this toolcard.");
        }
    }
}
