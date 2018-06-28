package it.polimi.se2018.view;

import it.polimi.se2018.controller.RankingRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controller class for RankingScene fxml scene. Displays local and global rankings to user, gives the option
 * to close the application.
 *
 * @author Lorenzo Minto
 */
public class RankingsSceneController {

    /**
     * Result label showing textually the outcome of the game
     */
    @FXML Label resultLabel;
    /**
     * Button used to exit the game
     */
    @FXML Button exitButton;
    /**
     * Ranking List View showing the users scores sorted top to bottom.
     */
    @FXML ListView rankingListView;
    /**
     * Button used to switch between the Local Ranking and the Global Ranking
     */
    @FXML Button localityButton;

    /**
     * Flag set to true if rankingListView is displaying Local Rankings, false otherwise
     */
    private boolean showingLocal = true;

    /**
     * Observable list representing the local rankings
     */
    private static final ObservableList localRanking =
            FXCollections.observableArrayList();
    /**
     * Observable list representing the global rankings
     */
    private static final ObservableList globalRanking =
            FXCollections.observableArrayList();

    /**
     * Setter for localRankings property
     * @param records ranking records list to be set as localRankings
     */
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

    /**
     * Setter for winner property
     * @param isWinner boolean
     */
    public void setWinner(boolean isWinner) {
        if (isWinner) {
            resultLabel.setText("Congratulations! You won.");
        } else {
            resultLabel.setText("Nah, try again. You lost.");
        }
    }

    /**
     * Setter for globalRankings property
     * @param records ranking records list to be set as globalRankings
     */
    public void setGlobalRanking(List<RankingRecord> records) {
        records.sort(Comparator.comparing(RankingRecord::getGamesWon));
        Collections.reverse(records);

        globalRanking.addAll(records);
    }


    /**
     * Locality button event handler, implements the switching logic between Local and Global
     */
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


    /**
     * Exit button event handler, closes the application
     */
    @FXML public void handleExitEvent() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


}
