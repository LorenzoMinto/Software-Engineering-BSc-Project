package it.polimi.se2018.connection.Socket.serverSocket;

import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {

    private static int PORT = 1111;

    private ArrayList<SocketServerMessageReceiver> messageReceivers = new ArrayList<>();

    private SocketServerImplementation serverImplementation;

    public SocketServer() {

       this.serverImplementation = new SocketServerImplementation(this);

       (new SocketClientGatherer(this, PORT)).start();

    }


    /**
     * Aggiungi un nuovo clientSocket alla lista
     * @param socket
     */
    protected synchronized void addClient(Socket socket) {

        // Creo un VirtualClient per ogni nuovo clientSocket
        // per ogni clientSocket un thread che ascolta i messaggi provenienti da quel clientSocket
        SocketServerMessageReceiver messageReceiver = new SocketServerMessageReceiver(socket, this.serverImplementation);
        messageReceivers.add(messageReceiver);
        messageReceiver.start();

    }

    /**
     * Ritorna tutti i clientRmi
     * @return
     */
    protected synchronized ArrayList<SocketServerMessageReceiver> getMessageReceivers() {
        return this.messageReceivers;
    }

    /**
     * Rimuovi un clientRmi disconnesso
     * @param messageReceiver
     */
    protected synchronized void removeClient(SocketServerMessageReceiver messageReceiver) {
        this.messageReceivers.remove(messageReceiver);
    }

}
