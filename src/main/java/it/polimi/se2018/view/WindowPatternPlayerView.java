package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;

/**
 * Custom Pane that displays a player's window pattern and available favour tokens, while allowing the player
 * to interact with the window pattern grid in order to set his moves
 *
 * @author Lorenzo Minto
 */
public class WindowPatternPlayerView extends Pane {

    /**
     * The player's username
     */
    private String nickname;

    /**
     * The label that shows the player's username
     */
    @FXML private Label nicknameLabel;
    /**
     * The label that shows the player's available favour tokens
     */
    @FXML private Label favourTokensLabel;
    /**
     * The GridPane representing the window pattern's cells
     */
    @FXML private GridPane wpGrid;
    /**
     * The Label that shows the window pattern's title
     */
    @FXML private Label titleLabel;
    /**
     * The user icon ImageView marking the user's playerView
     */
    @FXML private ImageView userIcon;

    /**
     * The matrix of cell buttons in the window pattern grid
     */
    private Pane[][] gridDiceButtons;
    /**
     * The x coordinate of the selected cell, or of the selected origin cell if canMoveSelect is set to true
     */
    private int xSelected = -1;

    /**
     * The y coordinate of the selected cell, or of the selected origin cell if canMoveSelect is set to true
     */
    private int ySelected = -1;


    /**
     * Flag set to true if WindowPatternPlayerView is in Move Selection mode
     */
    private boolean canMoveSelect = false;
    /**
     * The x coordinate of the selected destination cell
     */
    private int xDestSelected = -1;
    /**
     * The y coordinate of the selected destination cell
     */
    private int yDestSelected = -1;

    /**
     * Class constructor.
     */
    WindowPatternPlayerView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WindowPatternPlayerView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new BadBehaviourRuntimeException(exception);
        }

    }

    /**
     * Sets the nickname property to the player's username
     *
     * @param nickname the player's username
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nicknameLabel.setText(nickname);
    }

    /**
     * Sets the WindowPattern represented by this PlayerView and handles its representation of the wpGrid
     *
     * @param wp the window pattern to be represented
     */
    public void setWindowPattern(WindowPattern wp) {
        titleLabel.setText(wp.getTitle());

        gridDiceButtons = new Pane[wp.getNumberOfRows()][wp.getNumberOfColumns()];
        Cell[][] pattern = wp.getPattern();
        for (int i=0; i<wp.getNumberOfRows(); i++) {
            for (int j=0; j<wp.getNumberOfColumns(); j++) {
                Pane dice = new Pane();
                wpGrid.add(dice, j, i);
                Image cellBack = new Image(getClass().getClassLoader().getResourceAsStream("images/Cells/" + pattern[i][j].getCellConstraintsToString() + ".jpg"));
                dice.setBackground(new Background(new BackgroundFill(new ImagePattern(cellBack), CornerRadii.EMPTY, Insets.EMPTY)));
                dice.setOnMouseClicked(e -> mouseEventHandler(wp, dice));

                gridDiceButtons[i][j] = dice;
            }
        }
    }

    /**
     * Event Handler for mouse events on the window pattern grid
     *
     * @param wp the window pattern
     * @param dice the grid cell pressed
     */
    private void mouseEventHandler(WindowPattern wp, Pane dice) {
        int x = 0;
        int y = 0;
        for (int i1 = 0; i1 <wp.getNumberOfRows(); i1++) {
            for (int j1 = 0; j1 <wp.getNumberOfColumns(); j1++) {
                if (dice == gridDiceButtons[i1][j1]) {
                    x = i1;
                    y = j1;
                }
            }
        }

        handleCanMoveSelection(x, y);
        visualizeSelection();
    }

    /**
     * Sets the x/ySelected properties and x/yDestSelected properties making sure to comply with
     * the canMoveSelect flag
     *
     * @param x
     * @param y
     */
    private void handleCanMoveSelection(int x, int y) {
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
    }

    /**
     * Updates window pattern visualization where needed, in the case something was placed or moved
     *
     * @param wp the window pattern to be represented
     */
    public void updateWindowPattern(WindowPattern wp) {
        Cell[][] pattern = wp.getPattern();
        for (int i = 0; i < wp.getNumberOfRows(); i++) {
            for (int j = 0; j < wp.getNumberOfColumns(); j++) {
                Dice dice = pattern[i][j].getDice();
                if (dice != null) {
                    Image diceImage = new Image(getClass().getClassLoader().getResourceAsStream("images/Dices/" + dice.toString() + ".jpg"));
                    gridDiceButtons[i][j].setBackground(new Background(new BackgroundFill(new ImagePattern(diceImage), CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    Image cellBack = new Image(getClass().getClassLoader().getResourceAsStream("images/Cells/" + pattern[i][j].getCellConstraintsToString() + ".jpg"));
                    gridDiceButtons[i][j].setBackground(new Background(new BackgroundFill(new ImagePattern(cellBack), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                gridDiceButtons[i][j].setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                        BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        }
    }

    /**
     * Sets the player's favour tokens
     *
     * @param ft the player's favour tokens
     */
    public void setFavourTokens(int ft) {
        this.favourTokensLabel.setText(String.valueOf(ft));
    }

    /**
     * Sets this view as the user view by showing the user icon on the bottom right
     */
    public void setThisAsUser() {
        this.userIcon.setVisible(true);
    }

    /**
     * Sets this view as the current player by highlighting the border red
     *
     * @param t boolean
     */
    public void setThisAsCurrentPlayer(boolean t) {
        if (t) {
            this.setBorder(new Border(new BorderStroke(Color.RED,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            this.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                    BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    /**
     * Returns the player's username
     *
     * @return the player's username
     */
    public String getNickname() { return nickname; }

    /**
     * Returns the x coordinate of the selected cell
     *
     * @return the x coordinate of the selected cell
     */
    public int getxSelected() { return xSelected; }

    /**
     * Returns the y coordinate of the selected cell
     *
     * @return the y coordinate of the selected cell
     */
    public int getySelected() { return ySelected; }

    /**
     * Returns the x coordinate of the selected destination cell
     *
     * @return the x coordinate of the selected destination cell
     */
    public int getxDestSelected() { return xDestSelected; }

    /**
     * Returns the y coordinate of the selected destination cell
     *
     * @return the y coordinate of the selected destination cell
     */
    public int getyDestSelected() { return yDestSelected; }

    /**
     * Cleans the current selection the window pattern grid
     */
    public void cleanSelection() {
        this.xSelected = -1;
        this.ySelected = -1;
        this.yDestSelected = -1;
        this.xDestSelected = -1;
        visualizeSelection();
    }

    /**
     * Renders the current selection/double selection on the window pattern grid
     */
    private void visualizeSelection() {
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

    /**
     * Enables double selection
     *
     * @param canMoveSelect boolean
     */
    public void enableMoveSelection(boolean canMoveSelect) { this.canMoveSelect = canMoveSelect; }
}
