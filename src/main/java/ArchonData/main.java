package ArchonData;

import ArchonData.server.DataServer;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: EJ
 * Date: 11/16/13
 * Time: 11:39 AM
 */
public class main {

    public static final int PORT = 3809;
    public static final String NAME = "DataService";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        DataServer server = new DataServer();
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind(NAME, server);

        System.out.println("DataService bound...");
    }
}
