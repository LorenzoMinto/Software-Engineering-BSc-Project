package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.controller.NoMoreTurnsAvailableException;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Message;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main class of the Model.
 *
 * An instance of this class represent a specific real game going on on the server.
 *
 * This class implements the OBSERVER PATTERN in order to notify all the Views connected.
 *
 * @author Federico Haag
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class Game extends Observable implements Observer{
    /**
     * String passed as message of ValueOutOfBoundsException when is asked to create a game with a negative number of rounds
     */
    private static final String GAME_WITH_NEGATIVE_NUMBER_OF_ROUNDS = "Can't create a game with negative number of rounds.";

    /**
     * String passed as message of ValueOutOfBoundsException when is asked to create a game with a negative number of players
     */
    private static final String GAME_WITH_NEGATIVE_NUMBER_OF_PLAYERS = "Can't create a game with negative number of players.";

    /**
     * String passed as message of IllegalStateException when is asked to assign cards after they were already assigned
     */
    private static final String ASKED_TO_ASSIGN_CARDS_IN_BAD_STATE = "Can't assign cards more than once to the game.";

    /**
     * String passed as message of IllegalArgumentException when is asked to set rankings giving as a reference a null object
     */
    private static final String NULL_RANKINGS = "Can't set rankings to null.";

    /**
     * String passed as message of IllegalStateException when is asked to set rankings but the state is not the correct one
     */
    private static final String ASKED_TO_SET_RANKINGS_IN_BAD_STATE = "Can't set rankings if game is not ended.";

    /**
     * String passed as message of BadBehaviourRuntimeException when is asked to add a player but max number of players is reached
     */
    private static final String ADD_PLAYER_WHEN_MAX_NUMBER_OF_PLAYERS_REACHED = "Can't add a player if max number of players is reached.";

    /**
     * String passed as message of IllegalStateException when is asked to add a player but the state is not the correct one
     */
    private static final String ASKED_TO_ADD_PLAYER_IN_BAD_STATE = "Can't add player if game is not waiting for players.";

    /**
     * String passed as message of BadBehaviourRuntimeException when is asked to perform an action that requires
     * the game to be started but it is not.
     */
    private static final String GAME_NOT_RUNNING = "Game is not running yet";

    /**
     * String passed as message of BadBehaviourRuntimeException when is asked to use a toolCard that is not in the drawn set
     */
    private static final String TOOLCARD_NOT_IN_DRAWN_SET = "Asked to use a toolCard that is not in the drawn set.";

    /**
     * String passed as message of EmptyListException when is asked to perform an action that requires as parameter
     * a list of dices but the one actually passed as argument is an empty list.
     */
    private static final String NO_DICES = "No dices given.";
    /**
     * String passed as message of IllegalStateException when is asked to start the game but the state is not the correct one
     */
    private static final String ASKED_TO_START_GAME_IN_BAD_STATE = "Can't start game if not waiting for patterns choice.";

    /**
     * String passed as message of BadBehaviourRuntimeException when is asked to start the game but it is impossible to create new rounds.
     */
    private static final String NO_ROUNDS = "Can't start a game with no rounds.";

    /**
     * String passed as message of BadBehaviourRuntimeException when is asked to create a new round but it is impossible to create new turns.
     */
    private static final String NO_TURNS = "Can't start a round with no turns.";

    /**
     * String passed as message of IllegalArgumentException when referenced dice is null
     */
    private static final String NULL_DICE = "Can't use or reference a null dice.";

    /**
     * The number of rounds the game is composed of
     */
    private final int numberOfRounds;

    /**
     * The maximum number of players that the game can have
     */
    private final int maxNumberOfPlayers;

    /**
     * The number of turns that has each round. Round with
     * different number of turns are not allowed.
     */
    private int numberOfTurnsPerRound;

    /**
     * Reference to the instance of the current {@link Round}
     */
    private Round currentRound;

    /**
     * Reference to the instance of the game's {@link Track}
     */
    private Track track;

    /**
     * Group of players playing the game
     */
    private Set<Player> players;

    /**
     * List of ToolCards that were assigned to this game at the beginning of it
     */
    private List<ToolCard> drawnToolCards;

    /**
     * List of Public Objective Cards that were assigned to this game at the beginning of it
     */
    private List<PublicObjectiveCard> drawnPublicObjectiveCards;

    /**
     * Represents the status of the game
     */
    private GameStatus status;


    /**
     * Constructor for a new Game instance. Basic configuration is passed as argument.
     * Cards will be assigned later.
     *
     * @param numberOfRounds the number of rounds the game is composed of
     * @param maxNumberOfPlayers the maximum number of players that the game can have
     */
    public Game(int numberOfRounds, int maxNumberOfPlayers) {
        if(numberOfRounds < 0){ throw new ValueOutOfBoundsException(GAME_WITH_NEGATIVE_NUMBER_OF_ROUNDS); }
        if(maxNumberOfPlayers < 0 ){ throw new ValueOutOfBoundsException(GAME_WITH_NEGATIVE_NUMBER_OF_PLAYERS); }

        this.currentRound = null;
        this.track = new Track();
        track.register(this);
        this.players = new HashSet<>();
        this.status = GameStatus.WAITING_FOR_CARDS;

        this.numberOfRounds = numberOfRounds;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    /**
     * Returns the current round.
     *
     * @return the current round
     */
    public Round getCurrentRound() {
        return currentRound;
    }

    /**
     * Returns the current track.
     *
     * @return the current track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }


    /**
     * Returns the list of Public Objective Cards that were assigned to this game at the beginning of it.
     *
     * @return the list of Public Objective Cards that were assigned to this game at the beginning of it
     */
    public List<PublicObjectiveCard> getDrawnPublicObjectiveCards() {
        return drawnPublicObjectiveCards;
    }

    /**
     * Returns the game's status.
     *
     * @return the game's status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Assigns to the game the selected toolCards and publicobjectivecards (given randomly by the controller)
     *
     * @param drawnToolCards list of ToolCards that are assigned
     * @param drawnPublicObjectiveCards list of PublicObjectiveCards that are assigne
     */
    public void setCards(List<ToolCard> drawnToolCards, List<PublicObjectiveCard> drawnPublicObjectiveCards){
        if( this.status != GameStatus.WAITING_FOR_CARDS ){
            throw new IllegalStateException(ASKED_TO_ASSIGN_CARDS_IN_BAD_STATE);
        }

        this.drawnToolCards = drawnToolCards;
        this.drawnPublicObjectiveCards = drawnPublicObjectiveCards;

        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }

    /**
     * Sets the final rankings.
     *
     * @param rankings list of ordered players: first is winner
     */
    public void setRankings(Map<Player, Integer> rankings) {
        if(this.status != GameStatus.ENDED){ throw  new IllegalStateException(ASKED_TO_SET_RANKINGS_IN_BAD_STATE);}
        if(rankings == null){ throw new IllegalArgumentException(NULL_RANKINGS);}

    }

    /**
     * If possible, adds the given player to the game.
     *
     * @param player player to add to the game
     */
    public void addPlayer(Player player){
        if(this.status != GameStatus.WAITING_FOR_PLAYERS){ throw new IllegalStateException(ASKED_TO_ADD_PLAYER_IN_BAD_STATE);}
        if(players.size() >= maxNumberOfPlayers){ throw new BadBehaviourRuntimeException(ADD_PLAYER_WHEN_MAX_NUMBER_OF_PLAYERS_REACHED); }

        players.add(player);
    }


    /**
     * Try to assign the given window pattern to the given player
     * @param windowPattern the window pattern to assign to the player
     * @param playerID the player id of the player that choose the given windowPattern
     * @return true if windowPattern was succesfully assigned to the given player. false if not.
     */
    public boolean assignWindowPatternToPlayer(WindowPattern windowPattern, String playerID){
        for(Player p: players){
            if(p.getID().equals(playerID) && p.getWindowPattern()==null){
                windowPattern.register(this);
                p.setWindowPattern(windowPattern);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the given player is the current playing one.
     *
     * @param playerID the playerID to be checked
     * @return if the given player is the current playing one
     */
    public boolean isCurrentPlayer(String playerID) {
        if(this.status != GameStatus.PLAYING){ throw new BadBehaviourRuntimeException(GAME_NOT_RUNNING);}
        return getCurrentRound().getCurrentTurn().isCurrentPlayer(playerID);
    }

    /**
     * Set the given ToolCard as used in the current Turn
     *
     * @param toolCard toolCard used in the current Turn
     */
    public void useToolCard(ToolCard toolCard){
        if(this.status != GameStatus.PLAYING){ throw new BadBehaviourRuntimeException(GAME_NOT_RUNNING);}

        if( !this.drawnToolCards.contains(toolCard) ) {
            throw new BadBehaviourRuntimeException(TOOLCARD_NOT_IN_DRAWN_SET);
        }
        this.drawnToolCards.get( this.drawnToolCards.indexOf(toolCard) ).use();
        this.getCurrentRound().getCurrentTurn().setUsedToolCard(toolCard);

        //NOTIFYING
        Map <String, Object> messageAttributes = new HashMap<>();

        messageAttributes.put("toolCard", toolCard.copy());
        //updates the toolCards as their tokens were updated
        messageAttributes.put("toolCards", drawnToolCards.stream().map(ToolCard::copy).collect(Collectors.toList()));
        //updates the player as their tokens were updated
        messageAttributes.put("player", currentRound.getCurrentTurn().getPlayer().getID());
        //updates the players favour tokens
        messageAttributes.put("favourTokens", players.stream().map(Player::getFavorTokens).collect(Collectors.toList()));


        notify(new Message(ViewBoundMessageType.USED_TOOLCARD, messageAttributes));

    }

    /**
     * Gets the toolCard in the model that corresponds to the passed shallow copy.
     *
     * @param toolCardCopy the toolCard shallow copy.
     * @return the actual toolCard from the drawnToolCards, or null if no such card is found.
     *
     * @author Lorenzo Minto
     */
    public ToolCard getToolCard(ToolCard toolCardCopy) {

        for (ToolCard card: drawnToolCards) {
            if (card.equals(toolCardCopy)) { return card;}
        }
        throw new BadBehaviourRuntimeException(TOOLCARD_NOT_IN_DRAWN_SET);
    }

    /**
     * Sets the game status to waiting for patterns choice
     */
    public void setStatusAsWaitingForPatternsChoice(){
        if(status==GameStatus.WAITING_FOR_PLAYERS){
            this.status = GameStatus.WAITING_FOR_PATTERNS_CHOICE;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Sets the game status to ended. It is called by controller when active players
     * are less than minimum number of players
     */
    public void forceEndGameDueToInactivity(){
        if(status==GameStatus.PLAYING){
            this.status = GameStatus.ENDED;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Starts the game and creates the first round with the given dices
     * @param dices list of dices to be used for the first round
     * @param permissions the set of permissions at the beginning of the game
     */
    public void startGame(List<Dice> dices, Set<Move> permissions){
        if(dices == null){ throw new IllegalArgumentException(NULL_DICE);}
        if(dices.isEmpty()){ throw new EmptyListException(NO_DICES);}
        if(this.status != GameStatus.WAITING_FOR_PATTERNS_CHOICE){ throw new IllegalStateException(ASKED_TO_START_GAME_IN_BAD_STATE);}

        this.status = GameStatus.PLAYING;
        this.numberOfTurnsPerRound = players.size() * 2;

        //Send to all players all the needed data about game
        Map <String, Object> messageAttributes = new HashMap<>();
        String[] playersIDs = players.stream().map(Player::getID).toArray(String[]::new);
        WindowPattern[] windowPatterns = players.stream().map(Player::getWindowPattern).toArray(WindowPattern[]::new);

        messageAttributes.put("drawnToolCards", drawnToolCards.stream().map(ToolCard::copy).collect(Collectors.toList()));
        messageAttributes.put("drawnPublicObjectiveCards", drawnPublicObjectiveCards.stream().map(PublicObjectiveCard::copy).collect(Collectors.toList()));
        messageAttributes.put("players", Arrays.asList(playersIDs));
        messageAttributes.put("windowPatterns", Arrays.stream(windowPatterns).map(WindowPattern::copy).collect(Collectors.toList()));
        messageAttributes.put("track", track.copy());
        messageAttributes.put("draftPoolDices", dices.stream().map(Dice::copy).collect(Collectors.toList()));
        messageAttributes.put("favourTokens", players.stream().map(Player::getFavorTokens).collect(Collectors.toList()));

        for (Player player: players) {
            messageAttributes.put("privateObjectiveCard", player.getPrivateObjectiveCard().copy()); //put overrides previous values
            messageAttributes.put("yourWindowPattern", player.getWindowPattern().copy()); //put overrides previous values
            Message message = new Message(ViewBoundMessageType.SETUP, messageAttributes, player.getID());

            notify(message);
        }

        try {
            nextRound(dices,permissions);
        } catch (NoMoreRoundsAvailableException e) {
            //May never happen
            throw new BadBehaviourRuntimeException(NO_ROUNDS);
        }
    }

    /**
     * Proceed the game going to the next round (if available).
     *
     * @param dices the dices that are drafted from the dicebag for the new round
     * @param permissions the set of permissions for the next round
     * @throws NoMoreRoundsAvailableException if the method is called but all the rounds
     * that could have been played in this game were actually already played
     */
    public void nextRound(List<Dice> dices, Set<Move> permissions) throws NoMoreRoundsAvailableException{
        if(dices == null){ throw new IllegalArgumentException(NULL_DICE);}
        if(dices.isEmpty()){ throw new EmptyListException(NO_DICES);}
        if(this.status != GameStatus.PLAYING){ throw new BadBehaviourRuntimeException(GAME_NOT_RUNNING); }

        int nextRoundNumber;
        if( this.currentRound == null ){
            nextRoundNumber = 0;
        } else {
            nextRoundNumber = this.currentRound.getNumber() + 1;
        }

        //Get the remaining dices in draftpool and put them in the track
        if(currentRound != null) {
            this.track.processDicesAndNotify(currentRound.getDraftPool().getDices());
        }

        if(nextRoundNumber > numberOfRounds - 1){
            this.status = GameStatus.ENDED;
            throw new NoMoreRoundsAvailableException();
        }



        DraftPool draftPool = new DraftPool(dices);
        draftPool.register(this);
        this.currentRound = new Round(nextRoundNumber, numberOfTurnsPerRound, getPlayers(), draftPool);

        //NOTIFYING
        Map <String, Object> messageAttributes = new HashMap<>();
        messageAttributes.put("number", nextRoundNumber);
        messageAttributes.put("draftPoolDices", dices.stream().map(Dice::copy).collect(Collectors.toList()));

        notify(new Message(ViewBoundMessageType.NEW_ROUND, messageAttributes));

        try {
            nextTurn(permissions);
        } catch (NoMoreTurnsAvailableException e) {
            //May never happen
            throw new BadBehaviourRuntimeException(NO_TURNS);
        }
    }

    /**
     * Advances the game to the following turn
     * Notifies the observers (View) with the list of the players that will play the following turns
     *
     * @param permissions the set of permissions for the next turn
     * @throws NoMoreTurnsAvailableException if no more turns are available in this round
     * @author Jacopo Pio Gargano
     */
    public void nextTurn(Set<Move> permissions) throws NoMoreTurnsAvailableException {
        if(this.status != GameStatus.PLAYING){ throw new BadBehaviourRuntimeException(GAME_NOT_RUNNING); }
        try {
            getCurrentRound().nextTurn();

            String nextPlayerID = getCurrentRound().getCurrentTurn().getPlayer().getID();

            getCurrentRound().getCurrentTurn().register(this);

            //NOTIFYING
            Map <String, Object> messageAttributes = new HashMap<>();
            messageAttributes.put("whoIsPlaying", nextPlayerID);
            notify(new Message(ViewBoundMessageType.NEW_TURN, messageAttributes));

            notify(new Message(ViewBoundMessageType.IT_IS_YOUR_TURN, null, nextPlayerID, permissions));

        } catch (NoMoreTurnsAvailableException e) {
            throw new NoMoreTurnsAvailableException();
        }
    }

    @Override
    public boolean update(Message m) {
        notify(m);
        return true;
    }
}
