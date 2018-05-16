package it.polimi.se2018.connection.rmi.clientRmi;


import it.polimi.se2018.connection.rmi.Message;
import it.polimi.se2018.connection.rmi.serverRmi.ServerRMIInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class ClientRMI {

    public static void execute() {
        ServerRMIInterface server;
        try {

            // Vedi prima la descrizione del ServerRMI

            // Chiedo al Registry ( in esecuzione su localhost alla porta di default ) di localizzare 'ServerRMI'
            // e restituirmi il suo Stub

            server = (ServerRMIInterface)Naming.lookup("//localhost/MyServer");

            // Creo l'oggetto clientRmi normalmente.
            ClientRMIImplementation client = new ClientRMIImplementation();

            // Dato che NetworkHandler non estende la classe UnicastRemoteObject, devo creare lo Skeleton
            // usando il metodo UnicastRemoteObject.exportObject che prende come parametri l'oggetto da esportare e la porta da
            // utilizzare per la connessione. Con 0 la porta viene scelta automaticamente.
            //
            // nota_1: se al serverRmi passassimo "clientRmi", passeremmo una sua copia ( la deserializzazione della serializzazione di clientRmi )
            //         per questo creiamo un riferimento remoto.
            // nota_1: passando direttamente il riferimento remoto al serverRmi, non serve esportarlo sul Registry come servizio.

            ClientRMIInterface remoteRef = (ClientRMIInterface) UnicastRemoteObject.exportObject(client, 0);

            // Passo al serverRmi ( oggetto per me remoto ) il riferimento a clientRmi ( oggetto per lui remoto )
            server.addClient(remoteRef);

            Scanner scanner = new Scanner(System.in);
            boolean active = true;
            while(active){
                System.out.println("Inserire un messaggio:");
                String text = scanner.nextLine();

                Message message = new Message(text);

                // Invio l'evento
                server.send(message);
            }
            scanner.close();

        } catch (MalformedURLException e) {
            System.err.println("URL non trovato!");
        } catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("Il riferimento passato non è associato a nulla!");
        }


    }

}
