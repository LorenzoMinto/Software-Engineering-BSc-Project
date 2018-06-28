package it.polimi.se2018.networking;

public interface ServerInterface {

    /**
     * Method called when the connection is lost
     * @param sender socket that lost connection
     */
    void lostSocketConnection(ClientProxyInterface sender);

    /**
     * Method called when the connection is restored
     * @param previous the previous ClientProxyInterface used to communicate with the view
     * @param next the new ClientProxyInterface used to communicate with the view
     */
    void restoredSocketConnection(ClientProxyInterface previous, ClientProxyInterface next);
}
