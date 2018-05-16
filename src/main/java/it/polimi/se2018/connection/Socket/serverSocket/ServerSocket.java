package it.polimi.se2018.connection.Socket.serverSocket;

import it.polimi.se2018.connection.RemoteClientInterface;
import it.polimi.se2018.connection.ServerInterface;

import java.net.Socket;
import java.util.ArrayList;

public class ServerSocket {

    private static int PORT = 1111;
    private ServerInterface server;

    private ArrayList<RemoteClientInterface> clients = new ArrayList<>();

    public ServerSocket() {

       // servizio offerto ai clientRmi
       this.server = new ServerImplementation(this);

       // Avvio il SocketClientGatherer, un nuovo thread che si occupa di gestire la connessione di nuovi clientRmi
       (new SocketClientGatherer(this, PORT)).start();

    }

    /**
     * Aggiungi un nuovo clientSocket alla lista
     * @param clientConnection
     */
    protected synchronized void addClient( Socket clientConnection ) {

        // Creo un VirtualClient per ogni nuovo clientRmi,
        // per ogni clientRmi un thread che ascolta i messaggi provenienti da quel clientRmi

        VirtualClient cm = new VirtualClient(this, clientConnection);
        clients.add(cm);
        cm.start();

    }

    /**
     * Ritorna tutti i clientRmi
     * @return
     */
    protected synchronized ArrayList<RemoteClientInterface> getClients() {
        return this.clients;
    }

    /**
     * Rimuovi un clientRmi disconnesso
     * @param client
     */
    protected synchronized void removeClient(RemoteClientInterface client) {
        this.clients.remove(client);
    }

    protected synchronized ServerInterface getImplementation() {
        return this.server;
    }

    public static void execute() {
            new ServerSocket();
    }
}
