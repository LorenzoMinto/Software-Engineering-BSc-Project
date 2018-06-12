package it.polimi.se2018.model;

/**
 * Enum representing the various states the game can be in.
 * @author Federico Haag
 */
public enum GameStatus {
    WAITING_FOR_CARDS, //the game is created and is waiting for toolCards and public objective cards
    WAITING_FOR_PLAYERS, //game is ready and waits for players
    WAITING_FOR_PATTERNS_CHOICE, //game is waiting players choose window pattern
    PLAYING, //the game is going on
    ENDED //the game is ended and players are viewing rankings a scores in their Views
}
