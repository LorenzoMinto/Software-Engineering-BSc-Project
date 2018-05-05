package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;

public class UseToolcardPlayerMove extends PlayerMove {
    private ToolCard toolcard;

    @Override
    public void perform(ControllerState controllerState) {

    }

    public ToolCard getToolcard() {
        return toolcard;
    }
}
