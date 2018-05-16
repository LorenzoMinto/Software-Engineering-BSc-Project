package it.polimi.se2018.connection.Socket.serverSocket;

import it.polimi.se2018.connection.Socket.clientSocket.ClientInterface;

import java.net.Socket;
import java.util.ArrayList;

public class ServerSocket {

    private static int PORT = 1111;
    private static ServerInterface server;

    private ArrayList<ClientInterface> clients = new ArrayList<ClientInterface>();

    public ServerSocket() {

       // servizio offerto ai clientRmi
       this.server = new ServerImplementation(this);

       // Avvio il ClientGatherer, un nuovo thread che si occupa di gestire la connessione di nuovi clientRmi
       (new ClientGatherer(this, PORT)).start();

    }

    /**
     * Aggiungi un nuovo clientRmi alla lista
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
    protected synchronized ArrayList<ClientInterface> getClients() {
        return this.clients;
    }

    /**
     * Rimuovi un clientRmi disconnesso
     * @param client
     */
    protected synchronized void removeClient(ClientInterface client) {
        this.clients.remove(client);
    }

    protected synchronized ServerInterface getImplementation() {
        return this.server;
    }

    public static void execute() {
            new ServerSocket();
    }
}
