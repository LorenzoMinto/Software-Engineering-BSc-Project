package it.polimi.se2018.view;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.Client;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

public class SagradaSceneController extends View implements Initializable {
    private Client client;
    private List<Image> cards = new ArrayList<>();
    private static final int numberOfToolCards = 3;
    private static final int numberOfPublicObjectiveCards = 3;
    private int cardCarouselCurrentIndex;

    @FXML private HBox blackPane;

    @FXML private TextArea playerTerminal;
    @FXML private FlowPane dynamicChoicesPane;
    @FXML private FlowPane draftPoolPane;

    //WINDOW PATTERNS DISPLAY
    private List<WindowPatternPlayerView> wpViews;

    @FXML private HBox windowPatternsBox;


    //CARDS CAROUSEL COMPONENTS
    private List<Node> cardsCarouselVisibleComponents = new ArrayList<>();

    @FXML private HBox cardsCarouselCardHBox;
    @FXML private ImageView cardsCarouselCardImageView;

    @FXML private StackPane cardsCarouselFavorTokensStackPane;
    @FXML private ImageView cardsCarouselFavorTokensImageView;
    @FXML private Label cardsCarouselFavorTokensValue;

    @FXML private HBox cardsCarouselPreviousHBox;
    @FXML private ImageView cardsCarouselPreviousImageView;

    @FXML private HBox cardsCarouselNextHBox;
    @FXML private ImageView cardsCarouselNextImageView;
    @FXML private GridPane cardsCarouselGridPane;

    @FXML private Button cardsCarouselToolCardsButton;
    @FXML private Button cardsCarouselPublicsButton;
    @FXML private Button cardsCarouselPrivateButton;


// DO NOT DELETE THIS COMMENT
//
// File file = new File("src/main/resources/images/toolcard1.png");
// Image toolcard = new Image(file.toURI().toString());


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cardsCarouselVisibleComponents.add(cardsCarouselCardImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselFavorTokensImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselNextImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselPreviousImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselToolCardsButton);
        cardsCarouselVisibleComponents.add(cardsCarouselPublicsButton);
        cardsCarouselVisibleComponents.add(cardsCarouselPrivateButton);
        cardsCarouselVisibleComponents.add(cardsCarouselFavorTokensValue);

        cardCarouselCurrentIndex = 0;

        cardsCarouselVisibleComponents.forEach(component->component.setVisible(false));
        cardsCarouselCardImageView.setVisible(true);

        disableBlackPane();

        Image cardsCarouselDefaultCard = (new Image((new File("src/main/resources/images/CardsBack.jpg")).toURI().toString()));

        setImageWithHeightAndWidth(cardsCarouselCardImageView, cardsCarouselDefaultCard, cardsCarouselCardHBox);

        //setting favor tokens image and next and previous buttons
        setImageWithHeightAndWidth(cardsCarouselFavorTokensImageView,
                new Image((new File("src/main/resources/images/FavorToken.jpg")).toURI().toString()),
                cardsCarouselFavorTokensStackPane);

        setImageWithHeightAndWidth(cardsCarouselPreviousImageView,
                new Image((new File("src/main/resources/images/Previous.jpg")).toURI().toString()),
                cardsCarouselPreviousHBox);

        setImageWithHeightAndWidth(cardsCarouselNextImageView,
                new Image((new File("src/main/resources/images/Next.jpg")).toURI().toString()),
                cardsCarouselNextHBox);


        //setting a nice background
        Image backgroundImage = new Image((new File("src/main/resources/images/SagradaBackground.jpg")).toURI().toString());
        cardsCarouselGridPane.setBackground(new Background(new BackgroundFill(new ImagePattern(backgroundImage), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    public void handleCardCarouselNext(){
        if(cardCarouselCurrentIndex == cards.size()-1){
            cardCarouselCurrentIndex = 0;
        }else {
            cardCarouselCurrentIndex ++;
        }
        updateCardCarousel();
    }

    @FXML
    public void handleCardCarouselPrevious(){
        if(cardCarouselCurrentIndex == 0){
            cardCarouselCurrentIndex = cards.size()-1;
        }else {
            cardCarouselCurrentIndex --;
        }
        updateCardCarousel();
    }

    private void updateCardCarousel() {
        setImageWithHeightAndWidth(
                cardsCarouselCardImageView,
                cards.get(cardCarouselCurrentIndex),
                cardsCarouselCardHBox);

        if(cardCarouselCurrentIndex<numberOfToolCards) {
            cardsCarouselFavorTokensValue.setText(String.valueOf(drawnToolCards.get(cardCarouselCurrentIndex).getUsedTokens()));
        }else{
            cardsCarouselFavorTokensValue.setText("");
        }
    }

    private void setImageWithHeightAndWidth(ImageView imageView, Image image, Pane pane) {
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());
    }

    public void handleCardCarouselToolCardsButtonPressed() {
        cardCarouselCurrentIndex = 0;
        updateCardCarousel();
    }

    public void handleCardCarouselPublicsButtonPressed(){
        cardCarouselCurrentIndex = numberOfToolCards;
        updateCardCarousel();
    }

    public void handleCardCarouselPrivateButtonPressed(){
        cardCarouselCurrentIndex = numberOfToolCards + numberOfPublicObjectiveCards;
        updateCardCarousel();
    }

    @Override
    void handleAddedEvent() {

    }

    @Override
    void handleRemovedEvent() {

    }

    private void checkID(Button button){
        //TODO: Button action handling here -> will correspond to the start of a move.
        switch (button.getId()) {
            case "DRAFT_DICE_FROM_DRAFTPOOL":

                break;
            default:
                break;
        }
    }

    @Override
    void handleLeaveWaitingRoomMove() {

    }

    @Override
    void handleBackGameMove() {

    }

    @Override
    void handleEndTurnMove() {

    }

    @Override
    void handleDraftDiceFromDraftPoolMove() {

    }

    @Override
    void handlePlaceDiceOnWindowPatternMove() {

    }

    @Override
    void handleUseToolCardMove() {

    }

    @Override
    void handleIncrementDraftedDiceMove() {

    }

    @Override
    void handleDecrementDraftedDiceMove() {

    }

    @Override
    void handleChangeDraftedDiceValueMove() {

    }

    @Override
    void handleChooseDiceFromTrackMove() {

    }

    @Override
    void handleMoveDiceMove() {

    }

    @Override
    void handleJoinGameMove() {

    }

    @Override
    void handleGiveWindowPatternsEvent(Message m) {
        super.handleGiveWindowPatternsEvent(m);
        enableBlackPane();

        List<ImageView> windowPatternPanes = new ArrayList<>();

        for (WindowPattern pattern: drawnWindowPatterns) {
//            Pane pane = new Pane();
//            pane.setBackground(new Background(new BackgroundFill(new ImagePattern(backgroundImage), CornerRadii.EMPTY, Insets.EMPTY)));
            Image patternImage = new Image((new File(pattern.getImageURL())).toURI().toString());
            ImageView patternImageView = new ImageView(patternImage);
            patternImageView.setOpacity(1);

            patternImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    windowPattern = pattern;
                    hasChosenWindowPattern();
                    printOnConsole(pattern.getTitle() +" chosen.");
                }
            });

//            pane.getChildren().add(patternImageView);
            windowPatternPanes.add(patternImageView);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                blackPane.getChildren().clear();
                windowPatternPanes.forEach(pane -> blackPane.getChildren().add(pane));
            }
        });
    }

    private void hasChosenWindowPattern() {
        disableBlackPane();
        cardsCarouselVisibleComponents.forEach(component-> component.setVisible(true));
    }

    private void enableBlackPane() {
        blackPane.setOpacity(0.8);
        blackPane.setDisable(false);
    }

    private void disableBlackPane() {
        blackPane.setOpacity(0);
        blackPane.setDisable(true);
    }

    @Override
    void showMessage(String message) {
        printOnConsole(message);
    }

    @Override
    void errorMessage(String message) {

    }

    @Override
    void notifyGameVariablesChanged() {
        updateWindowPatterns();
        updateCards();
        updateTrack();
        updateDraftPool();
        updatePlayers();
    }

    private void updateCards() {
        if(drawnToolCards.isEmpty() || drawnPublicObjectiveCards.isEmpty() || getPrivateObjectiveCard() == null){
            throw new BadBehaviourRuntimeException("Cards shouldn't be empty");}
        //getting the cards images
        drawnToolCards.forEach(card
                -> cards.add(new Image((new File(card.getImageURL())).toURI().toString())));
        drawnPublicObjectiveCards.forEach(card
                -> cards.add(new Image((new File(card.getImageURL())).toURI().toString())));
        System.out.println(privateObjectiveCard.toString());
        System.out.println(privateObjectiveCard.getImageURL());
        cards.add(new Image((new File(privateObjectiveCard.getImageURL())).toURI().toString()));

        updateCardCarousel();
    }

    private void updateTrack() {
    }

    private void updateDraftPool() {
        for (Dice d: draftPoolDices) {
            Button dice = new Button();
            dice.setPrefWidth(80);
            dice.setPrefHeight(80);

            Image diceImage = new Image((new File("src/main/resources/images/Dices/"+d.toString()+".jpg")).toURI().toString());
            dice.setBackground(new Background(new BackgroundFill(new ImagePattern(diceImage), CornerRadii.EMPTY, Insets.EMPTY)));

            Platform.runLater(new Runnable() {
                @Override public void run() {
                    draftPoolPane.getChildren().add(dice);
                }
            });
        }
    }

    private void updatePlayers() {
    }

    private void updateWindowPatterns() {
        //SETTING UP WINDOWPATTERNS

        System.out.println("Printing out wps. Total: "+ windowPatterns.size());
        wpViews = new ArrayList<>();
        int i = 0;

        for (WindowPattern wp: windowPatterns) {
            //TODO:!!!! The informations of all players are needed at all time, here is missing favourTokens
             String nickname = players.get(i);
            WindowPatternPlayerView wpView = new WindowPatternPlayerView();
            wpView.setFavourTokens(wp.getDifficulty());
            wpView.setNickname(nickname);
            wpView.setWindowPattern(wp);
            wpViews.add(wpView);
            i += 1;


            Platform.runLater(new Runnable() {
                @Override public void run() {
                    windowPatternsBox.getChildren().add(wpView);
                }
            });
        }

    }

    @Override
    void notifyGameStarted() {
        printOnConsole("The game has started!");
    }

    @Override
    void notifyPermissionsChanged() {
        new Thread(new Runnable() {
            @Override public void run() {
                if (getPermissions().isEmpty()) {
                    //add message? No move available at the moment
                } else {
                    Set<Move> permissions = getPermissions();
                    //TODO: each move should have a literal representation, not hardcoded here
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            dynamicChoicesPane.getChildren().clear();
                            for (Move m : permissions) {
                                Button button = new Button(m.toString());
                                button.setId(m.toString());
                                button.setOnAction(event -> checkID(button));
                                dynamicChoicesPane.getChildren().add(button);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void setClient(Client c) {
        this.client = c;
    }

    protected void printOnConsole(String s) {
        String ss = "\n"+s;
        playerTerminal.appendText(ss);
    }

}
