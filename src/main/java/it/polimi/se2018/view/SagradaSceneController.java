package it.polimi.se2018.view;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Controller class for SagradaScene fxml scene. Displays the main game interface.
 *
 * @author Lorenzo Minto
 * @author Jacopo Gargano
 */
public class SagradaSceneController extends View implements Initializable {
    private static final String PARAM_DICE = "dice";
    private static final String PARAM_VALUE = "value";
    private static final String PARAM_SLOT_NUMBER = "slotNumber";
    private static final String PARAM_ROW_FROM = "rowFrom";
    private static final String PARAM_COL_FROM = "colFrom";
    private static final String PARAM_ROW_TO = "rowTo";
    private static final String PARAM_COL_TO = "colTo";
    private static final String ERROR_SELECT_TO_AND_FROM_CELL_TO_MAKE_THE_MOVE = "Select TO and FROM cell to make the move.";
    private static final String PARAM_WINDOW_PATTERN = "windowPattern";

    private static final String PARAM_TOOLCARD = "toolCard";
    private static final String PARAM_MOVE = "move";
    private static final String PARAM_NICKNAME = "nickname";
    private static final String SRC_MAIN_RESOURCES_IMAGES_DICES = "images/Dices/";

    //WAITING LIST

    /**
     * Flag that is set to true when WaitingRoomView is showing so that console messages can be redirected to it
     */
    private boolean isOnWaitingList = true;

    /**
     * View used to show the Server's Waiting Room status before the start of the game
     */
    private WaitingRoomView waitingRoomView;

    /**
     * HBox that contains the waitingRoomView
     */
    @FXML HBox backPaneBox;

    /**
     * The list of cards shown in the card carousel
     */
    private List<Image> cards = new ArrayList<>();
    /**
     * The index of the current card shown in the card carousel
     */
    private int cardsCarouselCurrentIndex = 0;

    @FXML private AnchorPane blackAnchorPane;

    /**
     * The TextArea where all the server's game message are displayed
     */
    @FXML private TextArea playerTerminal;
    /**
     * The FlowPane that contains the buttons for the user interactions possible at the moment
     */
    @FXML private FlowPane dynamicChoicesPane;

    //WINDOW PATTERNS DISPLAY
    /**
     * The List of {@link WindowPatternPlayerView}s, representing all the players' window patterns
     */
    private List<WindowPatternPlayerView> wpViews;
    /**
     * The {@link WindowPatternPlayerView} representing the user's window pattern
     */
    private WindowPatternPlayerView userWindowPatternView;

    /**
     * The HBox that contains the {@link WindowPatternPlayerView}s
     */
    @FXML private HBox windowPatternsBox;

    //DRAFTPOOL DISPLAY
    /**
     * FlowPane that contains the draft pool dices.
     */
    @FXML private FlowPane draftPoolPane;

    /**
     * The HBox below the draftPoolPane that contains the current drafted dice
     */
    @FXML private HBox currentDraftedPane;

    /**
     * The ChoiceBox that allows, when active, to choose a value for the drafted dice
     */
    @FXML private ChoiceBox diceValuePicker;

    /**
     * The Button that references to the currently selected dice in the draft pool, not yet drafted
     */
    private Button selectedDiceButton = null;

    /**
     * The track slot number of the currently selected dice from the track
     */
    private int selectedTrackSlotNumber = -1;

    /**
     * List of Buttons representing the draftpool dices
     */
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
    /**
     * The visible components for the toolcards display
     */
    private List<Node> toolCardsVisibleComponents = new ArrayList<>();

    /**
     * The GridPane fixing the toolcards display layout
     */
    @FXML private GridPane toolCardsGridPane;

    /**
     * The Button representing the first toolcard in the toolcard display
     */
    @FXML private Button toolCards1Button;
    /**
     * The Button representing the second toolcard in the toolcard display
     */
    @FXML private Button toolCards2Button;
    /**
     * The Button representing the third toolcard in the toolcard display
     */
    @FXML private Button toolCards3Button;

    @FXML private HBox toolCardsPlayerHBox;

    /**
     * The Button representing the first toolcard's needed favour tokens
     */
    @FXML private Button toolCards1FavorTokensButton;
    /**
     * The Button representing the second toolcard's needed favour tokens
     */
    @FXML private Button toolCards2FavorTokensButton;
    /**
     * The Button representing the third toolcard's needed favour tokens
     */
    @FXML private Button toolCards3FavorTokensButton;

    /**
     * The Button representing the player's available favour tokens
     */
    @FXML private Button toolCardsPlayerFavorTokensButton;

    /**
     * The Button used to exit the toolcard display
     */
    @FXML private Button toolCardsBackButton;


    //WINDOWPATTERNS
    private List<Node> windowPatternsVisibleComponents = new ArrayList<>();
    @FXML private HBox windowPatternsHBox;
    @FXML private VBox windowPatternsVBox;
    @FXML private Button windowPatternsPrivateObjectiveCardImage;

    private List<HBox> windowPatternsImageBoxes = new ArrayList<>();
    @FXML private HBox windowPatterns1ImageBox;
    @FXML private HBox windowPatterns2ImageBox;
    @FXML private HBox windowPatterns3ImageBox;
    @FXML private HBox windowPatterns4ImageBox;

    @FXML private Button windowPatterns1FavorTokens;
    @FXML private Button windowPatterns2FavorTokens;
    @FXML private Button windowPatterns3FavorTokens;
    @FXML private Button windowPatterns4FavorTokens;

    //HISTORY COMPONENTS
    @FXML TextArea historyTerminal;
    @FXML ScrollPane historyScrollPane;


    /**
     * Initializes the scene's fundamental structure
     *
     * @param location the url location
     * @param resources the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        diceValuePicker.getItems().addAll("1","2","3","4","5", "6");
        diceValuePicker.setDisable(true);

        Image cardsCarouselDefaultCardImage = getImageFromPath("images/CardsBack.jpg");

        String favorTokensImagePath = "images/FavorToken.jpg";
        Image favorTokensImage = getImageFromPath(favorTokensImagePath);
        Image cardsCarouselPreviousImage = getImageFromPath("images/Previous.jpg");
        Image cardsCarouselNextImage = getImageFromPath("images/Next.jpg");

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
        toolCards2FavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));
        toolCards3FavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));
        toolCardsPlayerFavorTokensButton.setBackground(getBackgroundFromImage(favorTokensImage));

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


        windowPatternsImageBoxes.add(windowPatterns1ImageBox);
        windowPatternsImageBoxes.add(windowPatterns2ImageBox);
        windowPatternsImageBoxes.add(windowPatterns3ImageBox);
        windowPatternsImageBoxes.add(windowPatterns4ImageBox);

        windowPatterns1FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));
        windowPatterns2FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));
        windowPatterns3FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));
        windowPatterns4FavorTokens.setBackground(getBackgroundFromImage(favorTokensImage));

        windowPatternsVisibleComponents.add(windowPatternsHBox);
        windowPatternsVisibleComponents.add(windowPatternsVBox);
        windowPatternsVisibleComponents.add(windowPatternsPrivateObjectiveCardImage);
        windowPatternsVisibleComponents.add(windowPatterns1ImageBox);
        windowPatternsVisibleComponents.add(windowPatterns2ImageBox);
        windowPatternsVisibleComponents.add(windowPatterns3ImageBox);
        windowPatternsVisibleComponents.add(windowPatterns4ImageBox);
        windowPatternsVisibleComponents.add(windowPatterns1FavorTokens);
        windowPatternsVisibleComponents.add(windowPatterns2FavorTokens);
        windowPatternsVisibleComponents.add(windowPatterns3FavorTokens);
        windowPatternsVisibleComponents.add(windowPatterns4FavorTokens);

        disable(windowPatternsVisibleComponents);


        enableBlackAnchorPane();
        blackAnchorPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");

        historyTerminal.prefWidthProperty().bind(historyScrollPane.prefWidthProperty());
        historyTerminal.prefHeightProperty().bind(historyScrollPane.heightProperty());
        historyTerminal.appendText("---------HISTORY----------\n");
    }

    /**
     * Enables and sets to visible the given list of nodes
     *
     * @param visibleComponents the list of nodes
     */
    private void enable(List<Node> visibleComponents) {
        visibleComponents.forEach(component -> component.setVisible(true));
        visibleComponents.forEach(component -> component.setOpacity(1));
        visibleComponents.forEach(component -> component.setDisable(false));

    }

    /**
     * Sets to invisible and disabled the passed list of nodes
     *
     * @param visibleComponents the list of nodes
     */
    private void disable(List<Node> visibleComponents) {
        visibleComponents.forEach(component -> component.setVisible(false));
        visibleComponents.forEach(component -> component.setOpacity(0));
        visibleComponents.forEach(component -> component.setDisable(true));
    }

    /**
     * Generates a Background object for a given image
     *
     * @param image the background image
     * @return a Background object
     */
    private Background getBackgroundFromImage(Image image) {
        return new Background(new BackgroundFill(new ImagePattern(image), CornerRadii.EMPTY, Insets.EMPTY));
    }

    /**
     * Generates a Border object for a given color
     *
     * @param color the wanted color
     * @return a Border object
     */
    private Border getBorderWithColor(Color color) {
        return new Border(new BorderStroke(color,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5)));
    }

    /**
     * Gets the Image object for a given URL
     *
     * @param path the asset path
     * @return an Image object for the wanted asset
     */
    private Image getImageFromPath(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    /**
     * Brings to front and displays the WaitingRoomView
     *
     */
    public void showWaitingRoom() {
        waitingRoomView = new WaitingRoomView();
        EventHandler<ActionEvent> handler = event -> handleExitEvent();
        waitingRoomView.setExitHandler(handler);

        Platform.runLater(() -> {
            backPaneBox.getChildren().clear();
            backPaneBox.getChildren().add(waitingRoomView);
            backPaneBox.toFront();

            Move m = Move.JOIN;
            Button join = new Button(m.getTextualREP());
            join.setOnAction(event -> checkID(m));
            waitingRoomView.addPermissions(join);
        });
    }

    /**
     * Event handler for the NEXT button on the card carousel
     */
    @FXML
    public void handleCardCarouselNext(){
        if(cardsCarouselCurrentIndex == cards.size()-1){
            cardsCarouselCurrentIndex = 0;
        }else {
            cardsCarouselCurrentIndex ++;
        }
        updateCardCarousel();
    }

    /**
     * Event handler for the NEXT button on the card carousel
     */
    @FXML
    public void handleCardCarouselPrevious(){
        if(cardsCarouselCurrentIndex == 0){
            cardsCarouselCurrentIndex = cards.size()-1;
        }else {
            cardsCarouselCurrentIndex --;
        }
        updateCardCarousel();
    }

    /**
     * Updates the card carousel display
     */
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

    /**
     * Sets image to imageView preserving the ratio of the pane
     *
     * @param imageView the ImageView
     * @param image the image to set on the imageView
     * @param pane the pane of which to maintain the ratio
     */
    private void setImageWithHeightAndWidth(ImageView imageView, Image image, Pane pane) {
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());
    }

    /**
     * Event handler for the toolcard bookmark button on the card carousel
     */
    public void onCardCarouselToolCardsButtonPressed() {
        cardsCarouselCurrentIndex = 0;
        updateCardCarousel();
    }

    /**
     * Event handler for the public objectives bookmark button on the card carousel
     */
    public void onCardCarouselPublicsButtonPressed(){
        cardsCarouselCurrentIndex = drawnToolCards.size();
        updateCardCarousel();
    }

    /**
     * Event handler for the private objective bookmark button on the card carousel
     */
    public void onCardCarouselPrivateButtonPressed(){
        cardsCarouselCurrentIndex = drawnToolCards.size() + drawnPublicObjectiveCards.size();
        updateCardCarousel();
    }

    /**
     * Event handler for the back button on the track display
     */
    public void onTrackBackButtonPressed(){
        disable(trackVisibleComponents);
        disableBlackAnchorPane();
    }

    /**
     * Event handler for the back button on the toolcard display
     */
    public void onToolCardsBackButtonPressed(){
        disable(toolCardsVisibleComponents);
        disableBlackAnchorPane();
    }

    @Override
    void handleAddedEvent(Message m) {
        super.handleAddedEvent(m);
        waitingRoomView.setWaitingPlayers(waitingRoomPlayers);
    }

    @Override
    void handleRemovedEvent(Message m) {
        super.handleRemovedEvent(m);
        waitingRoomView.setWaitingPlayers(waitingRoomPlayers);
    }

    /**
     * Callback that links a button move permission to the method that handle its behaviour
     *
     * @param move the move to be handled
     */
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
                handleReturnDiceToDraftpoolMove();
                break;
            case MOVE_DICE:
                handleMoveDiceMove();
                break;
            case BACK_GAME:
                handleBackGameMove();
                break;
            case LEAVE:
                handleLeaveWaitingRoomMove();
                break;
            case JOIN:
                handleJoinGameMove();
                break;
            default:
                break;
        }
    }

    @Override
    void handleJoinGameMove() {
        if (waitingRoomView.getUsername().isEmpty()) {
            printOnConsole("You have to choose a username first!");
            return;
        }
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put(PARAM_MOVE,Move.JOIN);
            params.put(PARAM_NICKNAME,waitingRoomView.getUsername());
            setPlayer(waitingRoomView.getUsername());
            notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
        } catch (NetworkingException e) {
            showError(e.getMessage());
        }
    }

    @Override
    void handleUseToolCardMove() {


        Platform.runLater(() -> backPaneBox.toBack());

        enableBlackAnchorPane();

        enable(toolCardsVisibleComponents);

        Platform.runLater(() -> {
            double toolCardsProportions = 0.712;

            //Retrieving ToolCards images
            ToolCard toolCard = drawnToolCards.get(0);
            Image toolCardImage = getImageFromPath(toolCard.getImageURL());
            toolCards1Button.setBackground(getBackgroundFromImage(toolCardImage));
            toolCards1Button.prefWidthProperty().bind(toolCardsGridPane.widthProperty().divide(4));
            toolCards1Button.prefHeightProperty().bind(toolCards1Button.prefWidthProperty().divide(toolCardsProportions));
            toolCards1FavorTokensButton.setText(String.valueOf(toolCard.getNeededTokens()));
            toolCards1FavorTokensButton.prefWidthProperty().bind(toolCards1Button.prefWidthProperty().divide(4));
            toolCards1FavorTokensButton.prefHeightProperty().bind(toolCards1FavorTokensButton.prefWidthProperty());

            toolCard = drawnToolCards.get(1);
            toolCardImage = getImageFromPath(toolCard.getImageURL());
            toolCards2Button.setBackground(getBackgroundFromImage(toolCardImage));
            toolCards2Button.prefWidthProperty().bind(toolCardsGridPane.widthProperty().divide(4));
            toolCards2Button.prefHeightProperty().bind(toolCards2Button.prefWidthProperty().divide(toolCardsProportions));
            toolCards2FavorTokensButton.setText(String.valueOf(toolCard.getNeededTokens()));
            toolCards2FavorTokensButton.prefWidthProperty().bind(toolCards2Button.prefWidthProperty().divide(4));
            toolCards2FavorTokensButton.prefHeightProperty().bind(toolCards2FavorTokensButton.prefWidthProperty());

            toolCard = drawnToolCards.get(2);
            toolCardImage = getImageFromPath(toolCard.getImageURL());
            toolCards3Button.setBackground(getBackgroundFromImage(toolCardImage));
            toolCards3Button.prefWidthProperty().bind(toolCardsGridPane.widthProperty().divide(4));
            toolCards3Button.prefHeightProperty().bind(toolCards3Button.prefWidthProperty().divide(toolCardsProportions));
            toolCards3FavorTokensButton.setText(String.valueOf(toolCard.getNeededTokens()));
            toolCards3FavorTokensButton.prefWidthProperty().bind(toolCards3Button.prefWidthProperty().divide(4));
            toolCards3FavorTokensButton.prefHeightProperty().bind(toolCards3FavorTokensButton.prefWidthProperty());

            toolCardsPlayerFavorTokensButton.setText(String.valueOf(getPlayerFavourTokens()));
            toolCardsPlayerFavorTokensButton.prefWidthProperty().bind(toolCards1Button.prefWidthProperty().divide(4));
            toolCardsPlayerFavorTokensButton.prefHeightProperty().bind(toolCardsPlayerFavorTokensButton.prefWidthProperty());
        });

    }

    /**
     * Event handler for the first toolcard button on the toolcard display
     */
    public void onToolCards1ButtonPressed(){
        cardsCarouselCurrentIndex = 0;

        onToolCardButtonPressed(0);
    }



    /**
     * Event handler for the second toolcard button on the toolcard display
     */
    public void onToolCards2ButtonPressed(){
        cardsCarouselCurrentIndex = 1;

        onToolCardButtonPressed(1);
    }

    /**
     * Event handler for the third toolcard button on the toolcard display
     */
    public void onToolCards3ButtonPressed(){
        cardsCarouselCurrentIndex = 2;

        onToolCardButtonPressed(2);
    }

    private void onToolCardButtonPressed(int i) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put(PARAM_MOVE, Move.USE_TOOLCARD);
            params.put(PARAM_TOOLCARD, drawnToolCards.get(i));
            notifyGame(new Message(ControllerBoundMessageType.MOVE, params));
        } catch (NetworkingException e) {
            printOnConsole(e.getMessage());
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
            showInformation("Drafted dice: " + draftedDice);
            try {
                HashMap<String,Object> params = new HashMap<>();
                params.put(PARAM_MOVE,Move.DRAFT_DICE_FROM_DRAFTPOOL);
                params.put(PARAM_DICE,draftedDice);
                notifyGame(new Message(ControllerBoundMessageType.MOVE, params));
            } catch (NetworkingException e) {
                printOnConsole(e.getMessage());
            }
        } else {
            showError("You have not selected a dice from the draft pool yet!");
        }
    }

    /**
     * Gets the Dice object corresponding to the given button in a list of dice
     *
     * @param btn the dice button
     * @param dices the list of dice
     * @return the Dice object corresponding to the dice button
     */
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
            showInformation("Trying to place dice on: " + x + " " + y);
            HashMap<String,Object> params = new HashMap<>();
            params.put("row",x);
            params.put("col",y);
            params.put(PARAM_MOVE,Move.PLACE_DICE_ON_WINDOWPATTERN);
            try {
                notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
            } catch (NetworkingException e) {
                printOnConsole(e.getMessage());
            }
        } else {
            showError("No cell was selected!");
        }
        Platform.runLater(() -> userWindowPatternView.cleanSelection());
    }

    @Override
    void handleChangeDraftedDiceValueMove() {
        if (diceValuePicker.getValue() == null) {
            showError("You have to choose a new value for the dice.");
        } else {
            int newDiceValue = Integer.parseInt((String) diceValuePicker.getValue());
            try {
                HashMap<String,Object> params = new HashMap<>();
                params.put(PARAM_MOVE,Move.CHANGE_DRAFTED_DICE_VALUE);
                params.put(PARAM_VALUE,newDiceValue);
                notifyGame(new Message(ControllerBoundMessageType.MOVE, params));
            } catch (NetworkingException e) {
                printOnConsole(e.getMessage());
            }
            diceValuePicker.setDisable(true);
        }

    }

    @Override
    void handleChooseDiceFromTrackMove() {
        super.handleChooseDiceFromTrackMove();

        if (trackSelectedDiceButton != null && selectedTrackSlotNumber >-1) {
            Dice trackChosenDice = getDiceForDiceButton(trackSelectedDiceButton, track.getDicesFromSlotNumber(selectedTrackSlotNumber));
            showInformation("Selected dice: " + trackChosenDice);

            HashMap<String,Object> params = new HashMap<>();
            params.put(PARAM_SLOT_NUMBER, selectedTrackSlotNumber);
            params.put(PARAM_DICE,trackChosenDice);
            params.put(PARAM_MOVE,Move.CHOOSE_DICE_FROM_TRACK);
            try {
                notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
            } catch (NetworkingException e) {
                printOnConsole(e.getMessage());
            }

        } else {
            showError("You have not selected a dice from the track yet!");
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
            params.put(PARAM_ROW_FROM,row);
            params.put(PARAM_COL_FROM,col);
            params.put(PARAM_ROW_TO,rowDest);
            params.put(PARAM_COL_TO,colDest);
            params.put(PARAM_MOVE,Move.MOVE_DICE);
            try {
                notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
            } catch (NetworkingException e) {
                printOnConsole(e.getMessage());
            }

        } else {
            showError(ERROR_SELECT_TO_AND_FROM_CELL_TO_MAKE_THE_MOVE);
        }
        Platform.runLater(userWindowPatternView::cleanSelection);
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
            dice.prefWidthProperty().bind(currentDraftedPane.widthProperty().multiply(0.3));
            dice.prefHeightProperty().bind(dice.prefWidthProperty());
            Image diceImage = getImageFromPath(SRC_MAIN_RESOURCES_IMAGES_DICES +draftedDice.toString()+".jpg");
            dice.setBackground(getBackgroundFromImage(diceImage));
            Platform.runLater(() -> {
                currentDraftedPane.getChildren().clear();
                currentDraftedPane.getChildren().add(dice);
            });
        } else {
            Platform.runLater(() -> currentDraftedPane.getChildren().clear());
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
        } catch (IOException e) {
            printOnConsole(e.getMessage());
        }
        Scene rankingsScene = new Scene(root, thisStage.getWidth(), thisStage.getHeight());
        Platform.runLater(() -> thisStage.setScene(rankingsScene));

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
        Platform.runLater(() -> backPaneBox.toBack());

        enableBlackAnchorPane();

        enable(windowPatternsVisibleComponents);

        Platform.runLater(() -> {

            Stage thisStage = (Stage) playerTerminal.getScene().getWindow();

            Image privateObjectiveCardImage = getImageFromPath(privateObjectiveCard.getImageURL());
            windowPatternsPrivateObjectiveCardImage.setBackground(getBackgroundFromImage(privateObjectiveCardImage));
            windowPatternsPrivateObjectiveCardImage.prefHeightProperty().bind(thisStage.heightProperty().divide(2));
            windowPatternsPrivateObjectiveCardImage.prefWidthProperty().bind(windowPatternsPrivateObjectiveCardImage.prefHeightProperty().multiply(0.7));

            windowPatternsHBox.prefHeightProperty().bind(windowPatternsVBox.heightProperty().divide(2));

            WindowPattern windowPattern = drawnWindowPatterns.get(0);
            WindowPatternView wpv = new WindowPatternView(windowPattern, event -> handleWindowPatterSelected(0));
            windowPatterns1ImageBox.getChildren().add(wpv);
            windowPatterns1FavorTokens.setText(String.valueOf(windowPattern.getDifficulty()));

            WindowPattern windowPattern1 = drawnWindowPatterns.get(1);
            WindowPatternView wpv1 = new WindowPatternView(windowPattern1, event -> handleWindowPatterSelected(1));
            windowPatterns2ImageBox.getChildren().add(wpv1);
            windowPatterns2FavorTokens.setText(String.valueOf(windowPattern1.getDifficulty()));

            WindowPattern windowPattern2 = drawnWindowPatterns.get(2);
            WindowPatternView wpv2 = new WindowPatternView(windowPattern2, event -> handleWindowPatterSelected(2));
            windowPatterns3ImageBox.getChildren().add(wpv2);
            windowPatterns3FavorTokens.setText(String.valueOf(windowPattern2.getDifficulty()));

            WindowPattern windowPattern3 = drawnWindowPatterns.get(3);
            WindowPatternView wpv3 = new WindowPatternView(windowPattern3, event -> handleWindowPatterSelected(3));
            windowPatterns4ImageBox.getChildren().add(wpv3);
            windowPatterns4FavorTokens.setText(String.valueOf(windowPattern3.getDifficulty()));
        });
    }

    private void handleWindowPatterSelected(int i) {
        WindowPattern windowPattern1 = drawnWindowPatterns.get(i);
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put(PARAM_MOVE,Move.CHOOSE_WINDOW_PATTERN);
            params.put(PARAM_WINDOW_PATTERN,windowPattern1.copy());
            notifyGame(new Message(ControllerBoundMessageType.MOVE, params));
        } catch (NetworkingException e) {
            printOnConsole(e.getMessage());
        }
        hasChosenWindowPattern();
        printOnConsole(windowPattern1.getTitle() +" chosen.");
        Label waitLabel = new Label("Your window pattern was assigned. Waiting for the other players to pick theirs.");
        waitLabel.setFont(new Font(24));
        windowPatternsBox.getChildren().add(waitLabel);
    }


    /**
     * Event handler for the Track Button, shows the track display allowing track dice selection
     */
    public void handleTrackButtonPressedEvent(){

        Platform.runLater(() -> backPaneBox.toBack());

        enableBlackAnchorPane();

        enable(trackVisibleComponents);

        Platform.runLater(() -> {
            String trackPath = "images/Track.jpg";
            Image trackImage = getImageFromPath(trackPath);
            trackImageButton.setBackground(getBackgroundFromImage(trackImage));
            trackImageButton.prefHeightProperty().bind(trackHBox.heightProperty());

            for (HBox hBox: trackHBoxes) {
                hBox.getChildren().clear();
                try {
                    for (Dice dice : track.getDicesFromSlotNumber(trackHBoxes.indexOf(hBox))) {
                        Button trackSlotDice = new Button();
                        trackSlotDice.setId(dice.toString());
                        trackDiceButtons.add(trackSlotDice);

                        Image diceImage = getImageFromPath(SRC_MAIN_RESOURCES_IMAGES_DICES + dice + ".jpg");
                        trackSlotDice.setBackground(getBackgroundFromImage(diceImage));

                        trackSlotDice.setPrefHeight(50);
                        trackSlotDice.setPrefWidth(50);

                        trackSlotDice.setOnAction(event -> handleTrackSlotDicePressedEvent(hBox, trackSlotDice));

                        hBox.getChildren().add(trackSlotDice);
                    }
                }catch (ValueOutOfBoundsException e){
                    //DO NOTHING
                }
            }
        });
    }

    @Override
    void handleAbortedEvent() {
        super.handleAbortedEvent();
        segueToExitScreenWithMessage("The game was aborted by the server.");
    }

    /**
     * Event handler for track slots dice pressed
     *
     * @param hBox the track slot hbox
     * @param trackSlotDice the track slot dice pressed
     */
    private void handleTrackSlotDicePressedEvent(HBox hBox, Button trackSlotDice) {
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
    }

    /**
     * Event handler for the Exit button, segues to exit screen
     */
    public void handleExitEvent() {
        handleQuitMove();
        segueToExitScreenWithMessage("You have quit the game.");
    }

    /**
     * Segues to the exit screen with the given message
     *
     * @param s the message to be displayed
     */
    private void segueToExitScreenWithMessage(String s) {
        Stage thisStage = (Stage) playerTerminal.getScene().getWindow();

        URL fxmlUrl = getClass().getClassLoader().getResource("fxml/ExitScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Parent root = new Pane();
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            printOnConsole(e.getMessage());
        }
        Scene exitScene = new Scene(root, thisStage.getWidth(), thisStage.getHeight());
        Platform.runLater(() -> thisStage.setScene(exitScene));

        ExitSceneController exitSceneController = fxmlLoader.getController();

        Platform.runLater(() -> exitSceneController.setMessage(s));
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

    @Override
    public void handleHistoryMessageEvent(Message m) {
        super.handleHistoryMessageEvent(m);

        Platform.runLater(() -> {
            String sss = lastMovePlayer.concat(lastMove.getRepresentationOfMove(lastMoveParams)).concat("\n");
            historyTerminal.appendText(sss);
        });
    }


    /**
     * Disables and sets to invisible the choose window pattern display
     */
    private void hasChosenWindowPattern() {
        disable(windowPatternsVisibleComponents);
        disableBlackAnchorPane();
        disable(trackVisibleComponents);
    }

    /**
     * Enables and sets to visible the BlackAnchorPane
     */
    private void enableBlackAnchorPane() {
        blackAnchorPane.setOpacity(1);
        blackAnchorPane.setDisable(false);
        blackAnchorPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
    }


    /**
     * Disables and sets to invisible the BlackAnchorPane
     */
    private void disableBlackAnchorPane() {
        blackAnchorPane.setOpacity(0);
        blackAnchorPane.setDisable(true);
    }


    @Override
    void showInformation(String message) {
        printOnConsole(message);
    }

    @Override
    void showError(String message) { printOnConsole("ERROR: "+message);}

    /**
     * Sets up the card carousel and updates it
     */
    private void setupCards() {
        updateCards();
        cardsCarouselVisibleComponents.forEach(component-> component.setVisible(true));
        updateCardCarousel();
    }

    /**
     * Updates the card carousel displaying
     */
    private void updateCards() {
        if(drawnToolCards.isEmpty() || drawnPublicObjectiveCards.isEmpty()){
            throw new BadBehaviourRuntimeException("Cards shouldn't be empty");}
        //getting the cards images
        drawnToolCards.forEach(card
                -> cards.add(getImageFromPath(card.getImageURL())));
        drawnPublicObjectiveCards.forEach(card
                -> cards.add(getImageFromPath(card.getImageURL())));
        cards.add(getImageFromPath(privateObjectiveCard.getImageURL()));

        updateCardCarousel();
    }

    /**
     * Updates the draftpool displaying
     */
    private void updateDraftPool() {
        if (!dicesButtons.isEmpty()) {
            dicesButtons.clear();
            Platform.runLater(() -> draftPoolPane.getChildren().clear());
        }
        for (Dice d: draftPoolDices) {
            Button dice = new Button();
            dice.prefWidthProperty().bind(draftPoolPane.widthProperty().multiply(0.25));
            dice.prefHeightProperty().bind(dice.prefWidthProperty());
            dice.setId(d.toString());

            Image diceImage = getImageFromPath(SRC_MAIN_RESOURCES_IMAGES_DICES +d.toString()+".jpg");
            dice.setBackground(getBackgroundFromImage(diceImage));

            dice.setOnAction(e -> {
                selectedDiceButton = dice;
                for (Button d1 : dicesButtons) {
                    if (d1 == selectedDiceButton) {
                        d1.setBorder(getBorderWithColor(Color.BLACK));
                    } else {
                        d1.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN,
                                BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    }
                }
            });

            Platform.runLater(() -> draftPoolPane.getChildren().add(dice));

            dicesButtons.add(dice);
        }
    }


    /**
     * Sets up the WindowPatternViews for each player
     */
    private void setupWindowPatterns() {
        wpViews = new ArrayList<>();
        int i = 0;

        Platform.runLater(() -> windowPatternsBox.getChildren().clear());

        for (WindowPattern wp: windowPatterns) {
            String nickname = players.get(i);
            WindowPatternPlayerView wpView = new WindowPatternPlayerView();

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

    /**
     * Updates the WindowPatternView for each player
     */
    private void updateWindowPatterns() {
        int i = 0;
        for (WindowPattern wp: windowPatterns) {
            WindowPatternPlayerView wpv = getWPViewById(wp.getTitle());
            Integer favourTokens = playersFavourTokens.get(i);

            Platform.runLater(() -> {
                if (wpv != null) {
                    wpv.updateWindowPattern(wp);
                    wpv.setFavourTokens(favourTokens);
                }
            });
            i++;
        }

        //clears currently drafted dice. This is always called after someone's placing.
        Platform.runLater(() -> currentDraftedPane.getChildren().clear());
    }

    /**
     * Returns the WindowPatternView in wpViews with the given title
     *
     * @param title the given title string
     * @return a WindowPatternView object
     */
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
            Platform.runLater(this::enableAuxiliaryComponents);

        }
    }

    /**
     * Enables the auxiliary components to the current presented permissions
     */
    private void enableAuxiliaryComponents() {
        if (userWindowPatternView != null) {
            userWindowPatternView.enableMoveSelection(false);
        }
        if (getPermissions().contains(Move.MOVE_DICE)) {
            userWindowPatternView.enableMoveSelection(true);
        } else if (getPermissions().contains(Move.CHANGE_DRAFTED_DICE_VALUE)) {
            diceValuePicker.setDisable(false);
        }
    }

    /**
     * Highlights WindowPatternPlayerView of current player with a red border
     */
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
        hasChosenWindowPattern();
    }


    @Override
    void notifyGameVariablesChanged() {
        super.notifyGameVariablesChanged();
        updateWindowPatterns();
        updateDraftPool();
    }

    /**
     * Prints the string message on the playerTerminal, when not in waitingRoomView, otherwise it forwards
     * the message to the waitingRoomView
     *
     * @param s the message string
     */
    private void printOnConsole(String s) {
        String ss = "\n"+s;
        if (!isOnWaitingList) {
            Platform.runLater(() -> playerTerminal.appendText(ss));
        } else {
            Platform.runLater(() -> waitingRoomView.forwardMessage(s));
        }
    }

}
