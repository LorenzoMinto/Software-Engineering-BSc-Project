package it.polimi.se2018.connection.rmi.serverRmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerRMI {

    private static int PORT = 1099; // porta di default
	
	public static void execute() {

		try {
			// Avvio il registry mettendolo in ascolto alla porta specificata (1099).
            // Il registry espone i servizi offerti dal serverRmi ai clientRmi.
			LocateRegistry.createRegistry(PORT);

		} catch (RemoteException e) {
		    // Se questa eccezione è stata catturata, probabilmente è perchè il Registry è già stato
            // avviato da linea di comando o da un'altra esecuzione del ServerRMI non terminata
            // ( da un altro processo in generale )
			System.out.println("Registry già presente!");
		}		
		
		try {

            // Creo una istanza di ServerRMIImplementation, che rappresenta il "servizio" che voglio offrire ai clientRmi
			ServerRMIImplementation serverImplementation = new ServerRMIImplementation();

			// A differenza di quanto fatto in ClientSocket, non devo creare manualmente un riferimento remoto a serverImplementation
            // in quanto ServerRMIImplementation estende UnicastRemoteObject che effettua l'esportazione all'interno del suo costruttore.
            // (quindi serverImplementation è già un riferimento remoto)
            // nota: la porta utilizza dall'oggetto remoto viene scelta in automatico

            // A questo punto comunico al Registry che ho un nuovo servizio da offrire ai clientRmi
            // passando a  Naming.rebind(..) :
            //  - una stringa di tipo "//host:port/name"
            //    >  host:port identifica l'indirizzo del Registry, se port è omesso viene utilizzata la porta di default: 1099
            //    >  name, a nostra  scelta, identifica il servizio ed è univoco all'interno del Registry avviato su host:port
            //  - l'oggetto serverImplementation
            //
            // nota: il clientRmi istanzierà lo stub corrispondente a serverImplementation
            //       tramite Naming.lookup("//localhost/ServerRMI");

			Naming.rebind("//localhost/MyServer", serverImplementation);


		} catch (MalformedURLException e) {
			System.err.println("Impossibile registrare l'oggetto indicato!");
		} catch (RemoteException e) {
			System.err.println("Errore di connessione: " + e.getMessage() + "!");
		}		

	}

}
