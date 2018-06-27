package it.polimi.se2018.view;

import it.polimi.se2018.controller.RankingRecord;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

//TODO: commentare questa classe
public class SagradaSceneController extends View implements Initializable {
    private Scene loginScene;

    //WAITING LIST

    private boolean isOnWaitingList = true;
    private WaitingRoomView waitingRoomView;

    @FXML HBox backPaneBox;

    private List<Image> cards = new ArrayList<>();
    private int cardsCarouselCurrentIndex = 0;

    @FXML private AnchorPane blackAnchorPane;
    @FXML private HBox blackPane;

    @FXML private TextArea playerTerminal;
    @FXML private FlowPane dynamicChoicesPane;

    //WINDOW PATTERNS DISPLAY
    private List<WindowPatternPlayerView> wpViews;
    private WindowPatternPlayerView userWindowPatternView;

    @FXML private HBox windowPatternsBox;

    //DRAFTPOOL DISPLAY
    @FXML private FlowPane draftPoolPane;
    @FXML private HBox currentDraftedPane;
    @FXML private ChoiceBox diceValuePicker;

    private Button selectedDiceButton = null;
    private int selectedTrackSlotNumber = -1;
    private List<Button> dicesButtons = new ArrayList<>();


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

    //TRACK COMPONENTS

    private List<Node> trackVisibleComponents = new ArrayList<>();
    @FXML private HBox trackHBox;
    @FXML private GridPane trackGridPane;
    @FXML private Button trackImageButton;
    @FXML private Button trackBackButton;

    private List<HBox> trackHBoxes = new ArrayList<>();
    @FXML private HBox trackHBox10;
    @FXML private HBox trackHBox9;
    @FXML private HBox trackHBox8;
    @FXML private HBox trackHBox7;
    @FXML private HBox trackHBox6;
    @FXML private HBox trackHBox5;
    @FXML private HBox trackHBox4;
    @FXML private HBox trackHBox3;
    @FXML private HBox trackHBox2;
    @FXML private HBox trackHBox1;

    private List<Button> trackDiceButtons = new ArrayList<>();
    private Button trackSelectedDiceButton = null;


    //TOOLCARDS COMPONENTS
    private List<Node> toolCardsVisibleComponents = new ArrayList<>();

    @FXML private GridPane toolCardsGridPane;

    @FXML private Button toolCards1Button;
    @FXML private Button toolCards2Button;
    @FXML private Button toolCards3Button;

    @FXML private HBox toolCardsPlayerHBox;

    @FXML private Button toolCards1FavorTokensButton;
    @FXML private Button toolCards2FavorTokensButton;
    @FXML private Button toolCards3FavorTokensButton;

    @FXML private Button toolCardsPlayerFavorTokensButton;

    @FXML private Button toolCardsBackButton;


    //WINDOWPATTERNS
    private List<Node> windowPatternsVisibleComponents = new ArrayList<>();
    @FXML private HBox windowPatternsHBox;
    @FXML private VBox windowPatternsVBox;
    @FXML private Button windowPatternsPrivateObjectiveCardImage;

    private List<Button> windowPatternsImages = new ArrayList<>();
    @FXML private Button windowPatterns1Image;
    @FXML private Button windowPatterns2Image;
    @FXML private Button windowPatterns3Image;
    @FXML private Button windowPatterns4Image;

    @FXML private Button windowPatterns1FavorTokens;
    @FXML private Button windowPatterns2FavorTokens;
    @FXML private Button windowPatterns3FavorTokens;
    @FXML private Button windowPatterns4FavorTokens;

    //IMAGES
    private String favorTokensImagePath = "src/main/resources/images/FavorToken.jpg";
    private String trackPath = "src/main/resources/images/Track.jpg";


// DO NOT DELETE THIS COMMENT
//
// File file = new File("src/main/resources/images/toolcard1.png");
// Image toolcard = new Image(file.toURI().toString());


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        diceValuePicker.getItems().addAll("1","2","3","4","5", "6");
        diceValuePicker.setDisable(true);

        Image cardsCarouselDefaultCardImage = getImageFromPath("src/main/resources/images/CardsBack.jpg");

        Image favorTokensImage = getImageFromPath(favorTokensImagePath);
        Image cardsCarouselPreviousImage = getImageFromPath("src/main/resources/images/Previous.jpg");
        Image cardsCarouselNextImage = getImageFromPath("src/main/resources/images/Next.jpg");

        setImageWithHeightAndWidth(cardsCarouselCardImageView, cardsCarouselDefaultCardImage, cardsCarouselCardHBox);

        setImageWithHeightAndWidth(cardsCarouselFavorTokensImageView, favorTokensImage, cardsCarouselFavorTokensStackPane);

        setImageWithHeightAndWidth(cardsCarouselPreviousImageView, cardsCarouselPreviousImage, cardsCarouselPreviousHBox);

        setImageWithHeightAndWidth(cardsCarouselNextImageView, cardsCarouselNextImage, cardsCarouselNextHBox);

        cardsCarouselVisibleComponents.add(cardsCarouselCardImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselFavorTokensImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselNextImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselPreviousImageView);
        cardsCarouselVisibleComponents.add(cardsCarouselToolCardsButton);
        cardsCarouselVisibleComponents.add(cardsCarouselPublicsButton);
        cardsCarouselVisibleComponents.add(cardsCarouselPrivateButton);
        cardsCarouselVisibleComponents.add(cardsCarouselFavorTokensValue);

        cardsCarouselCurrentIndex = 0;

        cardsCarouselVisibleComponents.forEach(component->component.setVisible(false));
        cardsCarouselCardImageView.setVisible(true);


        trackHBoxes.add(trackHBox1);
        trackHBoxes.add(trackHBox2);
        trackHBoxes.add(trackHBox3);
        trackHBoxes.add(trackHBox4);
        trackHBoxes.add(trackHBox5);
        trackHBoxes.add(trackHBox6);
        trackHBoxes.add(trackHBox7);
        trackHBoxes.add(trackHBox8);
        trackHBoxes.add(trackHBox9);
        trackHBoxes.add(trackHBox10);

        trackVisibleComponents.add(trackHBox1);
        trackVisibleComponents.add(trackHBox2);
        trackVisibleComponents.add(trackHBox3);
        trackVisibleComponents.add(trackHBox4);
        trackVisibleComponents.add(trackHBox5);
        trackVisibleComponents.add(trackHBox6);
        trackVisibleComponents.add(trackHBox7);
        trackVisibleComponents.add(trackHBox8);
        trackVisibleComponents.add(trackHBox9);
        trackVisibleComponents.add(trackHBox10);
        trackVisibleComponents.add(trackGridPane);
        trackVisibleComponents.add(trackHBox);
        trackVisibleComponents.add(trackImageButton);
        trackVisibleComponents.add(trackBackButton);


        disable(trackVisibleComponents);


        toolCards1FavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));
        toolCards1FavorTokensButton.setPrefWidth(70);
        toolCards1FavorTokensButton.setPrefHeight(70);

        toolCards2FavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));
        toolCards2FavorTokensButton.setPrefWidth(70);
        toolCards2FavorTokensButton.setPrefHeight(70);

        toolCards3FavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));
        toolCards3FavorTokensButton.setPrefWidth(70);
        toolCards3FavorTokensButton.setPrefHeight(70);

        toolCardsPlayerFavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));
        toolCardsPlayerFavorTokensButton.setPrefWidth(85);
        toolCardsPlayerFavorTokensButton.setPrefHeight(85);

        toolCardsVisibleComponents.add(toolCardsGridPane);
        toolCardsVisibleComponents.add(toolCards1Button);
        toolCardsVisibleComponents.add(toolCards2Button);
        toolCardsVisibleComponents.add(toolCards3Button);
        toolCardsVisibleComponents.add(toolCards1FavorTokensButton);
        toolCardsVisibleComponents.add(toolCards2FavorTokensButton);
        toolCardsVisibleComponents.add(toolCards3FavorTokensButton);
        toolCardsVisibleComponents.add(toolCardsPlayerHBox);
        toolCardsVisibleComponents.add(toolCardsBackButton);

        disable(toolCardsVisibleComponents);


        windowPatternsImages.add(windowPatterns1Image);
        windowPatternsImages.add(windowPatterns2Image);
        windowPatternsImages.add(windowPatterns3Image);
        windowPatternsImages.add(windowPatterns4Image);

        windowPatterns1FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));
        windowPatterns2FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));
        windowPatterns3FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));
        windowPatterns4FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));

        windowPatternsVisibleComponents.add(windowPatternsHBox);
        windowPatternsVisibleComponents.add(windowPatternsVBox);
        windowPatternsVisibleComponents.add(windowPatternsPrivateObjectiveCardImage);
        windowPatternsVisibleComponents.add(windowPatterns1Image);
        windowPatternsVisibleComponents.add(windowPatterns2Image);
        windowPatternsVisibleComponents.add(windowPatterns3Image);
        windowPatternsVisibleComponents.add(windowPatterns4Image);
        windowPatternsVisibleComponents.add(windowPatterns1FavorTokens);
        windowPatternsVisibleComponents.add(windowPatterns2FavorTokens);
        windowPatternsVisibleComponents.add(windowPatterns3FavorTokens);
        windowPatternsVisibleComponents.add(windowPatterns4FavorTokens);

        disable(windowPatternsVisibleComponents);


        disableBlackAnchorPane();
        disableBlackHBox();








        //setting a nice background
        //Image backgroundImage = new Image((new File("src/main/resources/images/SagradaBackground.jpg")).toURI().toString());
        //cardsCarouselGridPane.setBackground(new Background(new BackgroundFill(new ImagePattern(backgroundImage), CornerRadii.EMPTY, Insets.EMPTY)));
        //backgroundPane.setBackground(new Background(new BackgroundFill(new ImagePattern(velvetBackground), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void disable(List<Node> visibleComponents) {
        visibleComponents.forEach(component -> component.setVisible(false));
        visibleComponents.forEach(component -> component.setDisable(true));
    }

    private Background getBackgroundFromImage(Image image) {
        return new Background(new BackgroundFill(new ImagePattern(image), CornerRadii.EMPTY, Insets.EMPTY));
    }

    private Border getBorderWithColor(Color color) {
        return new Border(new BorderStroke(color,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5)));
    }

    private Image getImageFromPath(String path) {
        return new Image((new File(path).toURI().toString()));
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    public void showWaitingRoom(String username) {
        waitingRoomView = new WaitingRoomView(username);
        EventHandler<ActionEvent> handler = event -> handleExitEvent();
        waitingRoomView.setExitHandler(handler);

        Platform.runLater(() -> {
            backPaneBox.getChildren().add(waitingRoomView);
            backPaneBox.toFront();

            Move m = Move.JOIN_GAME;
            Button join = new Button(m.getTextualREP());
            join.setOnAction(event -> checkID(m));
            waitingRoomView.addPermissions(join);
        });
    }

    @FXML
    public void handleCardCarouselNext(){
        if(cardsCarouselCurrentIndex == cards.size()-1){
            cardsCarouselCurrentIndex = 0;
        }else {
            cardsCarouselCurrentIndex ++;
        }
        updateCardCarousel();
    }

    @FXML
    public void handleCardCarouselPrevious(){
        if(cardsCarouselCurrentIndex == 0){
            cardsCarouselCurrentIndex = cards.size()-1;
        }else {
            cardsCarouselCurrentIndex --;
        }
        updateCardCarousel();
    }

    private void updateCardCarousel() {
        Platform.runLater(() -> {
            cardsCarouselFavorTokensValue.setDisable(true);
            setImageWithHeightAndWidth(
                    cardsCarouselCardImageView,
                    cards.get(cardsCarouselCurrentIndex),
                    cardsCarouselCardHBox);

            if(cardsCarouselCurrentIndex<drawnToolCards.size()) {
                cardsCarouselFavorTokensValue.setText(String.valueOf(drawnToolCards.get(cardsCarouselCurrentIndex).getNeededTokens()));
            }else{
                cardsCarouselFavorTokensValue.setText("");
            }
            cardsCarouselFavorTokensValue.setDisable(false);
        });
    }

    private void setImageWithHeightAndWidth(ImageView imageView, Image image, Pane pane) {
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());
    }

    public void onCardCarouselToolCardsButtonPressed() {
        cardsCarouselCurrentIndex = 0;
        updateCardCarousel();
    }

    public void onCardCarouselPublicsButtonPressed(){
        cardsCarouselCurrentIndex = drawnToolCards.size();
        updateCardCarousel();
    }

    public void onCardCarouselPrivateButtonPressed(){
        cardsCarouselCurrentIndex = drawnToolCards.size() + drawnPublicObjectiveCards.size();
        updateCardCarousel();
    }

    public void onTrackBackButtonPressed(){
        disable(trackVisibleComponents);
        disableBlackAnchorPane();
    }

    public void onToolCardsBackButtonPressed(){
        disable(toolCardsVisibleComponents);
        disableBlackAnchorPane();
    }

    @Override
    void handleAddedEvent(Message m) {

    }

    @Override
    void handleRemovedEvent(Message m) {

    }

    private void checkID(Move move){
        switch (move) {
            case END_TURN:
                handleEndTurnMove();
                break;
            case END_EFFECT:
                handleEndEffectMove();
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                handleDraftDiceFromDraftPoolMove();
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                handlePlaceDiceOnWindowPatternMove();
                break;
            case USE_TOOLCARD:
                handleUseToolCardMove();
                break;
            case INCREMENT_DRAFTED_DICE:
                handleIncrementDraftedDiceMove();
                break;
            case DECREMENT_DRAFTED_DICE:
                handleDecrementDraftedDiceMove();
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                handleChangeDraftedDiceValueMove();
                break;
            case CHOOSE_DICE_FROM_TRACK:
                handleChooseDiceFromTrackMove();
                break;
            case RETURN_DICE_TO_DRAFTPOOL:
                handleReturnDiceFromTrackMove();
                break;
            case MOVE_DICE:
                handleMoveDiceMove();
                break;
            case BACK_GAME:
                printOnConsole("Trying to reconnect to the the game...");
                handleBackGameMove();
                break;
            case LEAVE:
                System.out.println("Called to leave the waiting room.");
                handleLeaveWaitingRoomMove();
                break;
            case JOIN_GAME:
                System.out.println("Calling join game");
                handleJoinGameMove();
                break;
            default:
                break;
        }
    }

    @Override
    void handleJoinGameMove() {
        try {
            sendMessage(new Message(ControllerBoundMessageType.JOIN_WR,Message.fastMap("nickname", getPlayerID())));
        } catch (NetworkingException e) {
            errorMessage(e.getMessage());
        }
    }

    @Override
    void handleUseToolCardMove() {

        Platform.runLater(() -> {
            enableBlackAnchorPane();
            blackAnchorPane.setOpacity(0.93);
            disableBlackHBox();
            enable(toolCardsVisibleComponents);

            //Retrieving ToolCards images
            ToolCard toolCard = drawnToolCards.get(0);
            Image toolCardImage = getImageFromPath(toolCard.getImageURL());
            toolCards1Button.setBackground(getBackgroundFromImage(toolCardImage));
            toolCards1FavorTokensButton.setText(String.valueOf(toolCard.getNeededTokens()));

            toolCard = drawnToolCards.get(1);
            toolCardImage = getImageFromPath(toolCard.getImageURL());
            toolCards2Button.setBackground(getBackgroundFromImage(toolCardImage));
            toolCards2FavorTokensButton.setText(String.valueOf(toolCard.getNeededTokens()));

            toolCard = drawnToolCards.get(2);
            toolCardImage = getImageFromPath(toolCard.getImageURL());
            toolCards3Button.setBackground(getBackgroundFromImage(toolCardImage));
            toolCards3FavorTokensButton.setText(String.valueOf(toolCard.getNeededTokens()));

            toolCardsPlayerFavorTokensButton.setText(String.valueOf(getPlayerFavourTokens()));
        });
    }

    private void enable(List<Node> visibleComponents) {
        visibleComponents.forEach(component -> component.setVisible(true));
        visibleComponents.forEach(component -> component.setOpacity(1));
        visibleComponents.forEach(component -> component.setDisable(false));

    }

    public void onToolCards1ButtonPressed(){
        cardsCarouselCurrentIndex = 0;

        try {
            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("toolCard", drawnToolCards.get(0))));
        } catch (NetworkingException e) {
            //TODO: implementa
        }
        Platform.runLater(() -> {
            updateCardCarousel();
            disable(toolCardsVisibleComponents);
            disableBlackAnchorPane();
        });
    }



    public void onToolCards2ButtonPressed(){
        cardsCarouselCurrentIndex = 1;

        try {
            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("toolCard", drawnToolCards.get(1))));
        } catch (NetworkingException e) {
            //TODO: implementa
        }
        Platform.runLater(() -> {
            updateCardCarousel();
            disable(toolCardsVisibleComponents);
            disableBlackAnchorPane();
        });
    }

    public void onToolCards3ButtonPressed(){
        cardsCarouselCurrentIndex = 2;

        try {
            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("toolCard", drawnToolCards.get(2))));
        } catch (NetworkingException e) {
            //TODO: implementa
        }
        Platform.runLater(() -> {
            updateCardCarousel();
            disable(toolCardsVisibleComponents);
            disableBlackAnchorPane();
        });
    }


    @Override
    void handleDraftDiceFromDraftPoolMove() {
        super.handleDraftDiceFromDraftPoolMove();
        if (selectedDiceButton != null) {
            Dice draftedDice = getDiceForDiceButton(selectedDiceButton, draftPoolDices);
            showMessage("Drafted dice: " + draftedDice);
            try {
                sendMessage(new Message(ControllerBoundMessageType.DRAFT_DICE_FROM_DRAFTPOOL,Message.fastMap("dice", draftedDice.copy())));
            } catch (NetworkingException e) {
                //TODO: implementa
            }
        } else {
            errorMessage("You have not selected a dice from the draft pool yet!");
        }
    }

    private Dice getDiceForDiceButton(Button btn, List<Dice> dices) {
        String id = btn.getId();
        for (Dice dice: dices) {
            if (dice.toString().equals(id)) {
                return dice;
            }
        }
        return null;
    }

    @Override
    void handlePlaceDiceOnWindowPatternMove() {
        super.handlePlaceDiceOnWindowPatternMove();
        int x = userWindowPatternView.getxSelected();
        int y = userWindowPatternView.getySelected();

        if (x != -1  && y != -1) {
            showMessage("Trying to place dice on: " + String.valueOf(x) + " " + String.valueOf(y));
            HashMap<String,Object> params = new HashMap<>();
            params.put("row",x);
            params.put("col",y);
            try {
                sendMessage(new Message(ControllerBoundMessageType.PLACE_DICE,params));
            } catch (NetworkingException e) {
                //TODO: implementa
            }
        } else {
            errorMessage("No cell was selected!");
        }
        Platform.runLater(() -> {
            userWindowPatternView.cleanSelection();
        });
    }

    @Override
    void handleChangeDraftedDiceValueMove() {
        if (diceValuePicker.getValue() == null) {
            errorMessage("You have to choose a new value for the dice.");
        } else {
            int newDiceValue = Integer.parseInt((String) diceValuePicker.getValue());
            try {
                sendMessage(new Message(ControllerBoundMessageType.CHOOSE_DICE_VALUE,Message.fastMap("value", newDiceValue)));
            } catch (NetworkingException e) {
                //TODO: implementa
            }
            diceValuePicker.setDisable(true);
        }
    }

    @Override
    void handleChooseDiceFromTrackMove() {
        super.handleChooseDiceFromTrackMove();

        if (trackSelectedDiceButton != null && selectedTrackSlotNumber >-1) {
            Dice trackChosenDice = getDiceForDiceButton(trackSelectedDiceButton, track.getDicesFromSlotNumber(selectedTrackSlotNumber));
            showMessage("Selected dice: " + trackChosenDice);

            HashMap<String,Object> params = new HashMap<>();
            params.put("slotNumber", selectedTrackSlotNumber);
            params.put("dice",trackChosenDice);

            try {
                sendMessage(new Message(ControllerBoundMessageType.CHOOSE_DICE_FROM_TRACK,params));
            } catch (NetworkingException e) {
                //TODO: implementa
            }

        } else {
            errorMessage("You have not selected a dice from the track yet!");
        }

    }

    @Override
    void handleMoveDiceMove() {
        super.handleMoveDiceMove();
        HashMap<String,Object> params = new HashMap<>();
        int row = userWindowPatternView.getxSelected();
        int col = userWindowPatternView.getySelected();
        int rowDest = userWindowPatternView.getxDestSelected();
        int colDest = userWindowPatternView.getyDestSelected();

        if (row != -1  && col != -1  && rowDest != -1 && colDest != -1) {
            params.put("rowFrom",row);
            params.put("colFrom",col);
            params.put("rowTo",rowDest);
            params.put("colTo",colDest);

            try {
                sendMessage(new Message(ControllerBoundMessageType.MOVE_DICE,params));
            } catch (NetworkingException e) {
                //TODO: implementa
            }

        } else {
            errorMessage("Select TO and FROM cell to make the move.");
        }
        Platform.runLater(() -> {
            userWindowPatternView.cleanSelection();
        });
    }

    @Override
    public void handleLeaveWaitingRoomMove() {
        super.handleLeaveWaitingRoomMove();
    }


    //EVENTS

    @Override
    void handleUpdatedWindowPatternEvent(Message m) {
        super.handleUpdatedWindowPatternEvent(m);
        updateWindowPatterns();
    }

    @Override
    void handleChangedDraftPoolEvent(Message m) {
        super.handleChangedDraftPoolEvent(m);
        updateDraftPool();
    }

    @Override
    void handleDraftedDiceEvent(Message m) {
        super.handleDraftedDiceEvent(m);
        if (draftedDice != null) {
            Button dice = new Button();
            dice.setPrefWidth(100);
            dice.setPrefHeight(100);
            Image diceImage = getImageFromPath("src/main/resources/images/Dices/"+draftedDice.toString()+".jpg");
            dice.setBackground(getBackgroundFromImage(diceImage));
            Platform.runLater(() -> {
                currentDraftedPane.getChildren().clear();
                currentDraftedPane.getChildren().add(dice);
            });
        } else {
            Platform.runLater(() -> {
                currentDraftedPane.getChildren().clear();
            });
        }
    }

    @Override
    void handleRankingsEvent(Message m) {
        super.handleRankingsEvent(m);
        //get current scene's stage
        Stage thisStage = (Stage) playerTerminal.getScene().getWindow();

        URL fxmlUrl = getClass().getClassLoader().getResource("fxml/RankingsScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Parent root = new Pane();
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {}
        Scene rankingsScene = new Scene(root, thisStage.getWidth(), thisStage.getHeight());
        Platform.runLater(() -> {
            thisStage.setScene(rankingsScene);
        });

        RankingsSceneController rankingsController = fxmlLoader.getController();

        Platform.runLater(() -> {
            rankingsController.setLocalRanking(rankings);
            rankingsController.setGlobalRanking(globalRankings);
            rankingsController.setWinner(getPlayerID().equals(getWinnerID()));
        });
    }

    @Override
    void handleGiveWindowPatternsEvent(Message m) {
        super.handleGiveWindowPatternsEvent(m);

        //taking out of the game the waiting room view
        isOnWaitingList = false;
        Platform.runLater(() -> {
            backPaneBox.toBack();
        });

        enableBlackAnchorPane();

        disable(trackVisibleComponents);

        enable(windowPatternsVisibleComponents);

        Platform.runLater(() -> {
            double widthProportion = 4.8;
            double heightWidthWindowPatternProportion = 0.8;

            Image privateObjectiveCardImage = getImageFromPath(privateObjectiveCard.getImageURL());
            windowPatternsPrivateObjectiveCardImage.setBackground(getBackgroundFromImage(privateObjectiveCardImage));
            windowPatternsPrivateObjectiveCardImage.prefHeightProperty().bind(windowPatternsVBox.heightProperty().divide(2));
            windowPatternsPrivateObjectiveCardImage.prefWidthProperty().bind(windowPatternsPrivateObjectiveCardImage.heightProperty().multiply(0.712));

            windowPatternsHBox.prefHeightProperty().bind(windowPatternsVBox.heightProperty().divide(2));

            WindowPattern windowPattern = drawnWindowPatterns.get(0);
            Image windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns1Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns1Image.prefWidthProperty().bind(windowPatternsHBox.widthProperty().divide(widthProportion));
            windowPatterns1Image.prefHeightProperty().bind(windowPatterns1Image.prefWidthProperty().multiply(heightWidthWindowPatternProportion));
            windowPatterns1FavorTokens.setText(String.valueOf(windowPattern.getDifficulty()));

            windowPattern = drawnWindowPatterns.get(1);
            windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns2Image.prefWidthProperty().bind(windowPatternsHBox.widthProperty().divide(widthProportion));
            windowPatterns2Image.prefHeightProperty().bind(windowPatterns2Image.prefWidthProperty().multiply(heightWidthWindowPatternProportion));
            windowPatterns2Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns2FavorTokens.setText(String.valueOf(drawnWindowPatterns.get(1).getDifficulty()));

            windowPattern = drawnWindowPatterns.get(2);
            windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns3Image.prefWidthProperty().bind(windowPatternsHBox.widthProperty().divide(widthProportion));
            windowPatterns3Image.prefHeightProperty().bind(windowPatterns3Image.prefWidthProperty().multiply(heightWidthWindowPatternProportion));
            windowPatterns3Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns3FavorTokens.setText(String.valueOf(drawnWindowPatterns.get(2).getDifficulty()));

            windowPattern = drawnWindowPatterns.get(3);
            windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns4Image.prefWidthProperty().bind(windowPatternsHBox.widthProperty().divide(widthProportion));
            windowPatterns4Image.prefHeightProperty().bind(windowPatterns4Image.prefWidthProperty().multiply(heightWidthWindowPatternProportion));
            windowPatterns4Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns4FavorTokens.setText(String.valueOf(drawnWindowPatterns.get(3).getDifficulty()));

            for (Button wp: windowPatternsImages) {
                wp.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        WindowPattern windowPattern = drawnWindowPatterns.get(windowPatternsImages.indexOf(wp));
                        try {
                            sendMessage(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN,Message.fastMap("windowPattern",windowPattern.copy())));
                        } catch (NetworkingException e) {
                            //TODO: implementa
                        }
                        hasChosenWindowPattern();
                        printOnConsole(windowPattern.getTitle() +" chosen.");
                    }
                });
            }
        });
    }


    public void handleTrackButtonPressedEvent(){

        Image trackImage = getImageFromPath(trackPath);
        trackImageButton.setBackground(getBackgroundFromImage(trackImage));
        trackImageButton.prefHeightProperty().bind(trackHBox.heightProperty());

        enableBlackAnchorPane();
        disableBlackHBox();

        enable(trackVisibleComponents);

        Platform.runLater(() -> {
            for (HBox hBox: trackHBoxes) {
                hBox.getChildren().clear();
                try {
                    for (Dice dice : track.getDicesFromSlotNumber(trackHBoxes.indexOf(hBox))) {
                        Button trackSlotDice = new Button();
                        trackSlotDice.setId(dice.toString());
                        trackDiceButtons.add(trackSlotDice);

                        Image diceImage = getImageFromPath("src/main/resources/images/Dices/" + dice + ".jpg");
                        trackSlotDice.setBackground(getBackgroundFromImage(diceImage));

                        trackSlotDice.setPrefHeight(50);
                        trackSlotDice.setPrefWidth(50);

                        trackSlotDice.setOnAction(event -> {
                            trackSelectedDiceButton = trackSlotDice;
                            selectedTrackSlotNumber = trackHBoxes.indexOf(hBox);
                            for (Button d: trackDiceButtons) {
                                if (d == trackSelectedDiceButton) {
                                    d.setBorder(getBorderWithColor(Color.WHITE));
                                } else {
                                    d.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                                            BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                                }
                            }
                        });

                        hBox.getChildren().add(trackSlotDice);
                    }
                }catch (ValueOutOfBoundsException e){}
            }
        });
    }

    public void handleExitEvent() {
        Stage stage = (Stage) playerTerminal.getScene().getWindow();
        Platform.runLater(() -> backPaneBox.getChildren().clear());
        stage.setFullScreen(false);
        stage.setScene(loginScene);
    }

    @Override
    void handlePlayerAddedToWREvent(Message m) {
        super.handlePlayerAddedToWREvent(m);
        waitingRoomView.setWaitingPlayers(waitingRoomPlayers);
    }

    @Override
    void handlePlayerRemovedFromWREvent(Message m) {
        super.handlePlayerRemovedFromWREvent(m);
        waitingRoomView.setWaitingPlayers(waitingRoomPlayers);
    }

    @Override
    public void handleUsedToolCardEvent(Message m) {
        super.handleUsedToolCardEvent(m);
        updateWindowPatterns();
        updateCardCarousel();
    }


    private void hasChosenWindowPattern() {
        disable(windowPatternsVisibleComponents);
        disableBlackAnchorPane();
        disableBlackHBox();
        disable(trackVisibleComponents);
    }

    private void enableBlackAnchorPane() {
        blackAnchorPane.setOpacity(0.8);
        blackAnchorPane.setDisable(false);
        disableBlackHBox();
    }


    private void disableBlackAnchorPane() {
        blackAnchorPane.setOpacity(0);
        blackAnchorPane.setDisable(true);
    }

    private void enableBlackHBox() {
        blackPane.setOpacity(0.8);
        blackPane.setDisable(false);
    }

    private void disableBlackHBox() {
        blackPane.setOpacity(0);
        blackPane.setDisable(true);
    }


    @Override
    void showMessage(String message) {
        printOnConsole(message);
    }

    @Override
    void errorMessage(String message) { printOnConsole("ERROR: "+message);}

    private void setupCards() {
        updateCards();
        cardsCarouselVisibleComponents.forEach(component-> component.setVisible(true));
        updateCardCarousel();
    }

    private void updateCards() {
        if(drawnToolCards.isEmpty() || drawnPublicObjectiveCards.isEmpty() || getPrivateObjectiveCard() == null){
            throw new BadBehaviourRuntimeException("Cards shouldn't be empty");}
        //getting the cards images
        drawnToolCards.forEach(card
                -> cards.add(getImageFromPath(card.getImageURL())));
        drawnPublicObjectiveCards.forEach(card
                -> cards.add(getImageFromPath(card.getImageURL())));
        cards.add(getImageFromPath(privateObjectiveCard.getImageURL()));

        updateCardCarousel();
    }

    private void updateTrack() {
    }

    private void updateDraftPool() {
        if (!dicesButtons.isEmpty()) {
            dicesButtons.clear();
            Platform.runLater(() -> draftPoolPane.getChildren().clear());
        }
        System.out.println(draftPoolDices);
        for (Dice d: draftPoolDices) {
            Button dice = new Button();
            dice.prefWidthProperty().bind(draftPoolPane.widthProperty().multiply(0.3));
            dice.prefHeightProperty().bind(dice.prefWidthProperty());
            dice.setId(d.toString());

            Image diceImage = getImageFromPath("src/main/resources/images/Dices/"+d.toString()+".jpg");
            dice.setBackground(getBackgroundFromImage(diceImage));

            dice.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    selectedDiceButton = dice;
                    for (Button d: dicesButtons) {
                        if (d == selectedDiceButton) {
                            d.setBorder(getBorderWithColor(Color.BLACK));
                        } else {
                            d.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                                    BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                        }
                    }
                }
            });

            Platform.runLater(() -> draftPoolPane.getChildren().add(dice));

            dicesButtons.add(dice);
        }
    }

    private void updatePlayers() {
    }

    private void setupWindowPatterns() {
        wpViews = new ArrayList<>();
        int i = 0;
        for (WindowPattern wp: windowPatterns) {
            String nickname = players.get(i);
            WindowPatternPlayerView wpView = new WindowPatternPlayerView();
            trackImageButton.prefHeightProperty().bind(trackHBox.heightProperty());
            wpView.prefHeightProperty().bind(windowPatternsBox.heightProperty());
            wpView.setFavourTokens(playersFavourTokens.get(i));
            wpView.setNickname(nickname);
            wpView.setWindowPattern(wp);
            if (nickname.equals(getPlayerID())) {
                wpView.setThisAsUser();
                userWindowPatternView = wpView;
            }
            wpView.setId(wp.getTitle());
            wpViews.add(wpView);
            i += 1;

            Platform.runLater(() -> windowPatternsBox.getChildren().add(wpView));
        }
    }

    private void updateWindowPatterns() {
        int i = 0;
        for (WindowPattern wp: windowPatterns) {
            WindowPatternPlayerView wpv = getWPViewById(wp.getTitle());
            Integer favourTokens = playersFavourTokens.get(i);

            Platform.runLater(() -> {
                wpv.updateWindowPattern(wp);
                wpv.setFavourTokens(favourTokens);
            });
            i++;
        }

        //clears currently drafted dice. This is always called after someone's placing.
        Platform.runLater(() -> currentDraftedPane.getChildren().clear());
    }

    private WindowPatternPlayerView getWPViewById(String title) {
        for (WindowPatternPlayerView wpv : wpViews) {
            if (wpv.getId().equals(title)) {
                return wpv;
            }
        }
        return null;
    }

    @Override
    void notifyPermissionsChanged() {
        if (isOnWaitingList) {
            waitingRoomView.resetPermissions();
            for (Move m: getPermissions()) {
                Button button = new Button(m.getTextualREP());
                button.setId(m.toString());
                button.setOnAction(event -> checkID(m));

                waitingRoomView.addPermissions(button);
            }

        } else {

            if (getPermissions().isEmpty()) {
                //TODO: add label here to remind user is not his turn
                Platform.runLater(() -> dynamicChoicesPane.getChildren().clear());
            } else {
                Set<Move> permissions = getPermissions();
                Platform.runLater(() -> dynamicChoicesPane.getChildren().clear());
                for (Move m: permissions) {
                    Button button = new Button(m.getTextualREP());
                    button.setId(m.toString());

                    button.setOnAction(event -> checkID(m));
                    Platform.runLater(() -> dynamicChoicesPane.getChildren().add(button));
                }
            }
            Platform.runLater(() -> {
                if (userWindowPatternView != null) {
                    userWindowPatternView.enableMoveSelection(false);
                }
                if (getPermissions().contains(Move.MOVE_DICE)) {
                    userWindowPatternView.enableMoveSelection(true);
                    System.out.println("Move selection enabled.");
                } else if (getPermissions().contains(Move.CHANGE_DRAFTED_DICE_VALUE)) {
                    diceValuePicker.setDisable(false);
                }
            });

        }
    }

    private void highlightCurrentPlayer() {
        Platform.runLater(() -> {
            for (WindowPatternPlayerView wpView: wpViews) {
                wpView.setThisAsCurrentPlayer(playingPlayerID.equals(wpView.getNickname()));
            }
        });
    }

    @Override
    void notifyNewTurn(){
        super.notifyNewTurn();
        highlightCurrentPlayer();
    }

    @Override
    void notifyNewRound(){
        super.notifyNewRound();
        updateDraftPool();
        updateWindowPatterns();
    }

    @Override
    void notifyGameStarted() {
        super.notifyGameStarted();
        printOnConsole("The game has started.");
        updateDraftPool();
        setupWindowPatterns();
        setupCards();
    }


    @Override
    void notifyGameVariablesChanged() {
        super.notifyGameVariablesChanged();
        updateWindowPatterns();
        updateTrack();
        updateDraftPool();
        updatePlayers();
    }

    protected void printOnConsole(String s) {
        String ss = "\n"+s;
        if (!isOnWaitingList) {
            Platform.runLater(() -> playerTerminal.appendText(ss));
        } else {
            Platform.runLater(() -> waitingRoomView.forwardMessage(s));
        }
    }

}
