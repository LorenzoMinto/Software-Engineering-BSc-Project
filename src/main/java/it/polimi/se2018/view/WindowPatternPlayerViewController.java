package it.polimi.se2018.view;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.*;

public class WindowPatternPlayerViewController {
    private Player player;

    private String nickname;
    private int favourTokens;

    @FXML private Label nicknameLabel;
    @FXML private Label favourTokensLabel;

    public WindowPatternPlayerViewController(String nickname, int favourTokens, Cell[][] pattern) {
        this.nickname = nickname;
        this.favourTokens = favourTokens;
    }

    @FXML
    public void initialize() {
        nicknameLabel.setText(nickname);
        favourTokensLabel.setText(String.valueOf(favourTokens));
    }
}
