package it.polimi.se2018.view;

import it.polimi.se2018.controller.RankingRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//TODO: commentare questa classe
public class RankingsSceneController {

    @FXML Label resultLabel;
    @FXML Button exitButton;
    @FXML Button newGameButton;
    @FXML ListView rankingListView;
    @FXML Button localityButton;

    private boolean showingLocal = true;

    public static final ObservableList localRanking =
            FXCollections.observableArrayList();
    public static final ObservableList globalRanking =
            FXCollections.observableArrayList();

    public void setLocalRanking(List<RankingRecord> records) {
        localRanking.addAll(records);
        rankingListView.setItems(localRanking);

        rankingListView.setCellFactory(new Callback<ListView<RankingRecord>, ListCell<RankingRecord>>() {
            @Override
            public ListCell<RankingRecord> call(ListView<RankingRecord> p) {

                return new ListCell<RankingRecord>() {
                    @Override
                    protected void updateItem(RankingRecord t, boolean bln) {
                        super.updateItem(t, bln);

                        if (t != null) {
                            setText(t.toString());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }

    public void setWinner(boolean isWinner) {
        if (isWinner) {
            resultLabel.setText("Congratulations! You won.");
        } else {
            resultLabel.setText("Nah, try again. You lost.");
        }
    }

    public void setGlobalRanking(List<RankingRecord> records) {
        globalRanking.addAll(records);
    }


    public void handleLocalityButtonPressedEvent(){
        if (showingLocal) {
            showingLocal = false;
            localityButton.setText("Show Local");
            rankingListView.setItems(globalRanking);
        } else {
            showingLocal = true;
            localityButton.setText("Show Global");
            rankingListView.setItems(localRanking);
        }
    }


}
