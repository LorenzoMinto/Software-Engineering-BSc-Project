package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import javafx.application.Platform;
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
//TODO: commentare questa classe
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
    @FXML private GridPane patternGrid;

    private Pane[][] gridDiceButtons;
    private int xSelected = -1;
    private int ySelected = -1;

    private boolean canMoveSelect = false;
    private int xDestSelected = -1;
    private int yDestSelected = -1;

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
        titleLabel.setText(windowPattern.getTitle());

        gridDiceButtons = new Pane[wp.getNumberOfRows()][wp.getNumberOfColumns()];
        Cell[][] pattern = wp.getPattern();
        for (int i=0; i<wp.getNumberOfRows(); i++) {
            for (int j=0; j<wp.getNumberOfColumns(); j++) {
                Pane dice = new Pane();
                wpGrid.add(dice, j, i);
                Image cellBack = new Image((new File("src/main/resources/images/Cells/"+pattern[i][j].getCellConstraintsToString()+".jpg")).toURI().toString());
                dice.setBackground(new Background(new BackgroundFill(new ImagePattern(cellBack), CornerRadii.EMPTY, Insets.EMPTY)));
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
                        if (canMoveSelect) {
                            //this handles the double selection
                            if (xSelected != -1 && ySelected != -1 && xDestSelected != -1 && yDestSelected != -1) {
                                xSelected = x;
                                ySelected = y;
                                xDestSelected = -1;
                                yDestSelected = -1;
                            } else if (xSelected != -1 && ySelected != -1) {
                                xDestSelected = x;
                                yDestSelected = y;
                            } else {
                                xSelected = x;
                                ySelected = y;
                            }
                        } else {
                            xSelected = x;
                            ySelected = y;
                        }
                        visualizeSelection();
                    }
                });

                gridDiceButtons[i][j] = dice;
            }
        }
    }

    public void updateWindowPattern(WindowPattern wp) {
        Cell[][] pattern = wp.getPattern();
        for (int i = 0; i < wp.getNumberOfRows(); i++) {
            for (int j = 0; j < wp.getNumberOfColumns(); j++) {
                Dice dice = pattern[i][j].getDice();
                if (dice != null) {
                    Image diceImage = new Image((new File("src/main/resources/images/Dices/" + dice.toString() + ".jpg")).toURI().toString());
                    gridDiceButtons[i][j].setBackground(new Background(new BackgroundFill(new ImagePattern(diceImage), CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    Image cellBack = new Image((new File("src/main/resources/images/Cells/" + pattern[i][j].getCellConstraintsToString() + ".jpg")).toURI().toString());
                    gridDiceButtons[i][j].setBackground(new Background(new BackgroundFill(new ImagePattern(cellBack), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                gridDiceButtons[i][j].setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                        BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
    public int getxDestSelected() { return xDestSelected; }
    public int getyDestSelected() { return yDestSelected; }

    public void cleanSelection() {
        this.xSelected = -1;
        this.ySelected = -1;
        this.yDestSelected = -1;
        this.xDestSelected = -1;
        visualizeSelection();
    }

    void visualizeSelection() {
        for (int i=0; i<gridDiceButtons.length; i++) {
            for (int j=0; j<gridDiceButtons[0].length; j++) {
                if (i==xSelected && j==ySelected) {
                    gridDiceButtons[i][j].setBorder(new Border(new BorderStroke(Color.BLACK,
                            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
                } else if (i==xDestSelected && j==yDestSelected) {
                    gridDiceButtons[i][j].setBorder(new Border(new BorderStroke(Color.BLACK,
                            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
                } else {
                    gridDiceButtons[i][j].setBorder(new Border(new BorderStroke(Color.BLACK,
                            BorderStrokeStyle.NONE, CornerRadii.EMPTY, new BorderWidths(4))));
                }
            }
        }
    }

    public void enableMoveSelection(boolean canMoveSelect) { this.canMoveSelect = canMoveSelect; }
}
