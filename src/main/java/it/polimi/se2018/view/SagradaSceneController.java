package it.polimi.se2018.view;

import it.polimi.se2018.controller.RankingRecord;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.WindowPattern;
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

import static it.polimi.se2018.model.DiceColor.*;
//TODO: commentare questa classe
public class SagradaSceneController extends View implements Initializable {
    private Scene loginScene;

    private List<Image> cards = new ArrayList<>();
    //TODO: remove this line: private static int playerTokens = 5;
    private static int playerTokens = 5;
    private int cardCarouselCurrentIndex = 0;

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


    //WINDOWPATTERNS
    private List<Node> windowPatternsVisibleComponents = new ArrayList<>();
    @FXML private HBox windowPatternsHBox;

    private Button selectedWindowPattern = null;
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

        cardCarouselCurrentIndex = 0;

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

//    private void setDisableTrackView(boolean disable) {
//        long opacity = 1;
//        if (disable) {
//            opacity = 0;
//        }
//
//        for (HBox hbox: trackHBoxes) {
//            hbox.setOpacity(opacity);
//        }
//
//        trackGridPane.setOpacity(opacity);
//        trackHBox.setOpacity(opacity);
//        trackImageButton.setOpacity(opacity);
//
//
//        trackHBoxes.forEach(hBox -> hBox.setDisable(disable));
//
//        trackGridPane.setDisable(disable);
//        trackHBox.setDisable(disable);
//        trackImageButton.setDisable(disable);
//    }


    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
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
        Platform.runLater(() -> {
            setImageWithHeightAndWidth(
                    cardsCarouselCardImageView,
                    cards.get(cardCarouselCurrentIndex),
                    cardsCarouselCardHBox);

            if(cardCarouselCurrentIndex<drawnToolCards.size()) {
                cardsCarouselFavorTokensValue.setText(String.valueOf(drawnToolCards.get(cardCarouselCurrentIndex).getNeededTokens()));
            }else{
                cardsCarouselFavorTokensValue.setText("");
            }
        });
    }

    private void setImageWithHeightAndWidth(ImageView imageView, Image image, Pane pane) {
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());
    }

    public void onCardCarouselToolCardsButtonPressed() {
        cardCarouselCurrentIndex = 0;
        updateCardCarousel();
    }

    public void onCardCarouselPublicsButtonPressed(){
        cardCarouselCurrentIndex = drawnToolCards.size();
        updateCardCarousel();
    }

    public void onCardCarouselPrivateButtonPressed(){
        cardCarouselCurrentIndex = drawnToolCards.size() + drawnPublicObjectiveCards.size();
        updateCardCarousel();
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
                handleTrackButtonPressedEvent();
                break;
            case MOVE_DICE:
                handleMoveDiceMove();
                break;
            case BACK_GAME:
                handleBackGameMove();
                printOnConsole("Trying to reconnect to the the game...");
                break;
            case LEAVE:
                handleLeaveWaitingRoomMove();
                Stage stage = (Stage) playerTerminal.getScene().getWindow();
                stage.setFullScreen(false);
                stage.setScene(loginScene);
                break;
            default:
                break;
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

            //TODO: set player.getFavorTokens();
            toolCardsPlayerFavorTokensButton.setText(String.valueOf(playerTokens));
        });
    }

    private void enable(List<Node> visibleComponents) {
        visibleComponents.forEach(component -> component.setVisible(true));
        visibleComponents.forEach(component -> component.setOpacity(1));
        visibleComponents.forEach(component -> component.setDisable(false));
    }

    //TODO: player.getFavorTokens();
    public void onToolCards1ButtonPressed(){
        if(playerTokens >= drawnToolCards.get(0).getNeededTokens()){
            cardCarouselCurrentIndex = 0;

            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("toolCard", drawnToolCards.get(0))));
            Platform.runLater(() -> {
                disable(toolCardsVisibleComponents);
                disableBlackAnchorPane();
            });
        }
    }



    //TODO: player.getFavorTokens();
    public void onToolCards2ButtonPressed(){
        if(playerTokens >= drawnToolCards.get(1).getNeededTokens()){
            cardCarouselCurrentIndex = 1;

            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("toolCard", drawnToolCards.get(1))));
            Platform.runLater(() -> {
                disable(toolCardsVisibleComponents);
                disableBlackAnchorPane();
            });
        }
    }

    //TODO: player.getFavorTokens();
    public void onToolCards3ButtonPressed(){
        if(playerTokens >= drawnToolCards.get(2).getNeededTokens()){
            cardCarouselCurrentIndex = 2;

            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("toolCard", drawnToolCards.get(2))));
            Platform.runLater(() -> {
                disable(toolCardsVisibleComponents);
                disableBlackAnchorPane();
            });
        }
    }


    @Override
    void handleDraftDiceFromDraftPoolMove() {
        super.handleDraftDiceFromDraftPoolMove();
        if (selectedDiceButton != null) {
            Dice draftedDice = getDiceForDiceButton(selectedDiceButton);
            showMessage("Drafted dice: " + draftedDice);
            sendMessage(new Message(ControllerBoundMessageType.DRAFT_DICE_FROM_DRAFTPOOL,Message.fastMap("dice", draftedDice.copy())));
        } else {
            errorMessage("You have not selected a dice from the draft pool yet!");
        }
    }

    private Dice getDiceForDiceButton(Button btn) {
        String id = btn.getId();
        for (Dice dice: draftPoolDices) {
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
            sendMessage(new Message(ControllerBoundMessageType.PLACE_DICE,params));
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
            sendMessage(new Message(ControllerBoundMessageType.CHOOSE_DICE_VALUE,Message.fastMap("value", newDiceValue)));
            diceValuePicker.setDisable(true);
        }
    }

    @Override
    void handleChooseDiceFromTrackMove() {

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
            sendMessage(new Message(ControllerBoundMessageType.MOVE_DICE,params));
            userWindowPatternView.enableMoveSelection(false);
        } else {
            errorMessage("Select TO and FROM cell to make the move.");
        }
        Platform.runLater(() -> {
            userWindowPatternView.cleanSelection();
        });
    }

    @Override
    void handleUpdatedWindowPatternEvent(Message m) {
        super.handleUpdatedWindowPatternEvent(m);
        System.out.println("Updating window patterns.");
        updateWindowPatterns();
    }

    @Override
    void handleChangedDraftPoolEvent(Message m) {
        super.handleChangedDraftPoolEvent(m);
        System.out.println("Updating draft pool to " + draftPoolDices.toString());
        updateDraftPool();
    }

    @Override
    void handleDraftedDiceEvent(Message m) {
        super.handleDraftedDiceEvent(m);
        Button dice = new Button();
        dice.setPrefWidth(100);
        dice.setPrefHeight(100);
        Image diceImage = getImageFromPath("src/main/resources/images/Dices/"+draftedDice.toString()+".jpg");
        dice.setBackground(getBackgroundFromImage(diceImage));
        Platform.runLater(() -> {
            currentDraftedPane.getChildren().clear();
            currentDraftedPane.getChildren().add(dice);
        });
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
        //TODO: replace placeholder
        List<String> placeholder = new ArrayList<>();
        for (RankingRecord ranking: rankings) {
            placeholder.add(ranking.getPlayerID());
        }

        Platform.runLater(() -> {
            rankingsController.setLocalRanking(placeholder);
            rankingsController.setWinner(true);
        });
    }

    @Override
    void handleGiveWindowPatternsEvent(Message m) {
        super.handleGiveWindowPatternsEvent(m);
        enableBlackAnchorPane();


        enable(windowPatternsVisibleComponents);


        Platform.runLater(() -> {
            WindowPattern windowPattern = drawnWindowPatterns.get(0);
            Image windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns1Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns1FavorTokens.setText(String.valueOf(windowPattern.getDifficulty()));

            windowPattern = drawnWindowPatterns.get(1);
            windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns2Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns2FavorTokens.setText(String.valueOf(drawnWindowPatterns.get(1).getDifficulty()));

            windowPattern = drawnWindowPatterns.get(2);
            windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns3Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns3FavorTokens.setText(String.valueOf(drawnWindowPatterns.get(2).getDifficulty()));

            windowPattern = drawnWindowPatterns.get(3);
            windowPatternImage = getImageFromPath(windowPattern.getImageURL());
            windowPatterns4Image.setBackground(getBackgroundFromImage(windowPatternImage));
            windowPatterns4FavorTokens.setText(String.valueOf(drawnWindowPatterns.get(3).getDifficulty()));

            for (Button wp: windowPatternsImages) {
                wp.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        WindowPattern windowPattern = drawnWindowPatterns.get(windowPatternsImages.indexOf(wp));
                        setWindowPattern(windowPattern);
                        sendMessage(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN,Message.fastMap("windowPattern",windowPattern.copy())));
                        hasChosenWindowPattern();
                        printOnConsole(windowPattern.getTitle() +" chosen.");
                        disable(windowPatternsVisibleComponents);
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

//        //LIST TO TRY HOW IT IS RENDERED
//        List<Dice> dices = new ArrayList<>();
//        dices.add(new Dice(RED));
//        dices.add(new Dice((BLUE)));
//        dices.add(new Dice((GREEN)));
//        dices.add(new Dice((YELLOW)));
//        dices.add(new Dice((PURPLE)));


        Platform.runLater(() -> {
            for (HBox hBox: trackHBoxes) {

                hBox.getChildren().clear();
                try {
//                    for(Dice dice: dices){
                    for (Dice dice : track.getDicesFromSlotNumber(trackHBoxes.indexOf(hBox))) {
                        Button trackSlotDice = new Button();
                        trackDiceButtons.add(trackSlotDice);

                        Image diceImage = getImageFromPath("src/main/resources/images/Dices/" + dice + ".jpg");
                        trackSlotDice.setBackground(getBackgroundFromImage(diceImage));

                        trackSlotDice.setPrefHeight(50);
                        trackSlotDice.setPrefWidth(50);

                        trackSlotDice.setOnAction(event -> {
                            for (Button button: trackDiceButtons) {
                                if(button.equals(trackSlotDice)){

                                    HashMap<String,Object> params = new HashMap<>();
                                    params.put("slotNumber", trackHBoxes.indexOf(hBox));
                                    params.put("dice",dice.copy());

                                    sendMessage(new Message(ControllerBoundMessageType.CHOOSE_DICE_FROM_TRACK,params));
                                }
                            }
                        });

                        hBox.getChildren().add(trackSlotDice);
                    }
                }catch (ValueOutOfBoundsException e){}
            }
        });
    }

    public void handleTrackImageButtonPressedEvent(){
        disable(trackVisibleComponents);
        disableBlackAnchorPane();
    }


    private void hasChosenWindowPattern() {
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
            dice.setId(d.toString());
            dice.setPrefWidth(80);
            dice.setPrefHeight(80);

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
            //TODO:!!!! The informations of all players are needed at all time, here is missing favourTokens
            String nickname = players.get(i);
            WindowPatternPlayerView wpView = new WindowPatternPlayerView();
            wpView.setFavourTokens(wp.getDifficulty());
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
        for (WindowPattern wp: windowPatterns) {
            WindowPatternPlayerView wpv = getWPViewById(wp.getTitle());
            wpv.updateWindowPattern(wp);
            //TODO: update player's favour tokens here
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
        //TODO: throw exception if this happens
        return new WindowPatternPlayerView();
    }

    @Override
    void notifyPermissionsChanged() {
        System.out.println("Notified of permissions changed.");
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
            if (getPermissions().contains(Move.MOVE_DICE)) {
                userWindowPatternView.enableMoveSelection(true);
                System.out.println("Move selection enabled.");
            } else if (getPermissions().contains(Move.CHANGE_DRAFTED_DICE_VALUE)) {
                diceValuePicker.setDisable(false);
            }
        });
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
        highlightCurrentPlayer();
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
        Platform.runLater(() -> playerTerminal.appendText(ss));
    }

}
