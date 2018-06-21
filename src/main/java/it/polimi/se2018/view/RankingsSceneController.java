package it.polimi.se2018.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;
//TODO: commentare questa classe
public class RankingsSceneController {

    @FXML Label resultLabel;
    @FXML Button exitButton;
    @FXML Button newGameButton;
    @FXML ListView rankingListView;

    public static final ObservableList localRanking =
            FXCollections.observableArrayList();
    public static final ObservableList globalRanking =
            FXCollections.observableArrayList();

    public void setLocalRanking(List<String> players) {
        localRanking.addAll(players);
        rankingListView.setItems(localRanking);
        rankingListView.setCellFactory((Callback<ListView<String>, ListCell<String>>) list -> new RankingCell());
    }

    public void setWinner(boolean isWinner) {
        if (isWinner) {
            resultLabel.setText("Congratulations! You won.");
        } else {
            resultLabel.setText("Nah, try again. You lost.");
        }
    }

    public void setGlobalRanking(List<String> players) {
        globalRanking.addAll(players);
        rankingListView.setItems(globalRanking);
    }

    static class RankingCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Label nameLabel = new Label(item);
            if (item != null) {
                setGraphic(nameLabel);
            }
        }
    }


}
