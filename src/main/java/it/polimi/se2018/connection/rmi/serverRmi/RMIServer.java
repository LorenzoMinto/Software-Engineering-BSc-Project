package it.polimi.se2018.connection.rmi.serverRmi;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer{

    private static final int PORT = 1099; // porta di default

	public RMIServer(){
		try {
			LocateRegistry.createRegistry(PORT);

		} catch (RemoteException e) {
			System.out.println("Registry già presente!");
		}

		try {

			RMIServerImplementation serverImplementation = new RMIServerImplementation();

			Naming.rebind("//localhost/MyServer", serverImplementation);


		} catch (MalformedURLException e) {
			System.err.println("Impossibile registrare l'oggetto indicato!");
		} catch (RemoteException e) {
			System.err.println("Errore di connessione: " + e.getMessage() + "!");
		}
	}

}
