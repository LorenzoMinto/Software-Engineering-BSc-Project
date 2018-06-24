package it.polimi.se2018.networking;

public interface ConnectionHandler {

    /**
     * Method called when the connection is lost
     * @param sender socket that lost connection
     * @return true if the connection was relative to a playing player. false if user in wr or neither.
     */
    void lostConnection(ClientProxyInterface sender);

    /**
     * Method called when the connection is restored
     * @param previous the previous ClientProxyInterface used to communicate with the view
     * @param next the new ClientProxyInterface used to communicate with the view
     */
    void restoredConnection(ClientProxyInterface previous, ClientProxyInterface next);
}
