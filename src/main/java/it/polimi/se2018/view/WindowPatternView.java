package it.polimi.se2018.view;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;

class WindowPatternView extends Pane {

    @FXML private GridPane gridWp;
    @FXML private Button buttonLayer;
    @FXML private Pane backPane;

    private Pane[][] gridCellPanes;

    WindowPatternView(WindowPattern wp, EventHandler<ActionEvent> e) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WindowPatternView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new BadBehaviourRuntimeException(exception);
        }

        gridCellPanes = new Pane[wp.getNumberOfRows()][wp.getNumberOfColumns()];
        Cell[][] pattern = wp.getPattern();
        for (int i=0; i<wp.getNumberOfRows(); i++) {
            for (int j=0; j<wp.getNumberOfColumns(); j++) {
                Pane dice = new Pane();
                gridWp.add(dice, j, i);
                Image cellBack = new Image(getClass().getClassLoader().getResourceAsStream("images/Cells/" + pattern[i][j].getCellConstraintsToString() + ".jpg"));
                dice.setBackground(new Background(new BackgroundFill(new ImagePattern(cellBack), CornerRadii.EMPTY, Insets.EMPTY)));

                gridCellPanes[i][j] = dice;
            }
        }

        buttonLayer.setOnAction(e);

        gridWp.setAlignment(Pos.BOTTOM_CENTER);
        gridWp.prefWidthProperty().bind(backPane.widthProperty());
        gridWp.prefHeightProperty().bind(gridWp.prefWidthProperty().multiply(0.8));
    }
}
