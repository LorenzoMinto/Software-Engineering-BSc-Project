package it.polimi.se2018.model;

/**
 * Enum representing the various states the game can be in
 */
public enum GameStatus {
    WAITING_FOR_CARDS, //the game is created and is waiting for toolcards and public objective cards
    WAITING_FOR_PLAYERS, //game is ready and waits for players
    PLAYING, //the game is going on
    ENDED //the game is ended and players are viewing rankings a scores in their Views
}
