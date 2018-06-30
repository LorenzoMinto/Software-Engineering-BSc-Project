package it.polimi.se2018.networking;

public interface ServerInterface {

    /**
     * Method called when the connection is lost
     * @param sender socket that lost connection
     */
    void lostSocketConnection(ClientProxyInterface sender);
}
