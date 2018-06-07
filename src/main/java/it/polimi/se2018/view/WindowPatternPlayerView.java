package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.File;
import java.io.IOException;

public class WindowPatternPlayerView extends Pane {
    private Player player;

    private String nickname;
    private int favourTokens;
    private WindowPattern windowPattern;

    @FXML private Label nicknameLabel;
    @FXML private Label favourTokensLabel;
    @FXML private GridPane wpGrid;
    @FXML private Label titleLabel;
    @FXML private ImageView userIcon;

    private Pane[][] gridDiceButtons;
    private int xSelected;
    private int ySelected;

    public WindowPatternPlayerView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WindowPatternPlayerView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nicknameLabel.setText(nickname);
    }

    public void setWindowPattern(WindowPattern wp) {
        this.windowPattern = wp;
        Image wpjpg = new Image((new File(windowPattern.getImageURL()).toURI().toString()));
        wpGrid.setBackground(new Background(new BackgroundFill(new ImagePattern(wpjpg), CornerRadii.EMPTY, Insets.EMPTY)));
        titleLabel.setText(windowPattern.getTitle());

        gridDiceButtons = new Pane[wp.getNumberOfRows()][wp.getNumberOfColumns()];
        Cell[][] pattern = wp.getPattern();
        for (int i=0; i<wp.getNumberOfRows(); i++) {
            for (int j=0; j<wp.getNumberOfColumns(); j++) {
                Pane dice = new Pane();
                wpGrid.add(dice, j, i);
                dice.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        int x = 0;
                        int y = 0;
                        for (int i=0; i<wp.getNumberOfRows(); i++) {
                            for (int j=0; j<wp.getNumberOfColumns(); j++) {
                                if (dice == gridDiceButtons[i][j]) {
                                    x = i;
                                    y = j;
                                }
                            }
                        }
                        xSelected = x;
                        ySelected = y;

                        //NOTE: this is a test
                        Image diceImage = new Image((new File("src/main/resources/images/Dices/"+(new Dice(DiceColor.RED)).toString()+".jpg")).toURI().toString());
                        gridDiceButtons[x][y].setBackground(new Background(new BackgroundFill(new ImagePattern(diceImage), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                });

                gridDiceButtons[i][j] = dice;
            }
        }
    }

    public void updateWindowPattern(WindowPattern wp) {
        Cell[][] pattern = wp.getPattern();
        for (int i=0; i<wp.getNumberOfRows(); i++) {
            for (int j=0; j<wp.getNumberOfColumns(); j++) {
                Dice dice = pattern[i][j].getDice();
                if (dice!=null) {
                    Image diceImage = new Image((new File("src/main/resources/images/Dices/"+dice.toString()+".jpg")).toURI().toString());
                    gridDiceButtons[i][j].setBackground(new Background(new BackgroundFill(new ImagePattern(diceImage), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }

    public void setFavourTokens(int ft) {
        this.favourTokens = ft;
        this.favourTokensLabel.setText(String.valueOf(ft));
    }


    public void setThisAsUser() {
        this.userIcon.setVisible(true);
    }

    public void setThisAsCurrentPlayer(boolean t) {
        if (t) {
            this.setBorder(new Border(new BorderStroke(Color.RED,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            this.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                    BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    public String getNickname() { return nickname; }
    public int getxSelected() { return xSelected; }
    public int getySelected() { return ySelected; }
}
