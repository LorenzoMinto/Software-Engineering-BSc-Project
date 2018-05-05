package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;

public class UseToolCardPlayerMove extends PlayerMove {

    private ToolCard toolcard;

    public UseToolCardPlayerMove(ToolCard toolcard) {
        this.toolcard = toolcard;
    }

    public ToolCard getToolcard() {
        return toolcard;
    }
}
