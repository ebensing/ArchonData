package ArchonData.server;

import ArchonData.data.Artist;
import ArchonData.data.User;
import org.bson.types.ObjectId;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * User: EJ
 * Date: 11/16/13
 * Time: 11:17 AM
 */
public interface DataService extends Remote {

    // Artist Methods
    public Artist addArtist(Artist artist) throws RemoteException;
    public Artist forceAddArtist(Artist artist) throws RemoteException;
    public Artist updateArtist(Artist artist) throws RemoteException;
    public void deleteArtist(Artist artist) throws RemoteException;
    public Artist getArtist(ObjectId id) throws RemoteException;
    public List<Artist> getArtists() throws RemoteException;
    public void deleteAllArtists() throws RemoteException;
    public List<Artist> getArtists(String sort) throws RemoteException;
    public Artist getArtist(String name) throws RemoteException;

    // User Methods
    public User getUser(String name, String password) throws RemoteException;
    public User addUser(String username, String password) throws RemoteException;
    public void deleteUser(String username) throws RemoteException;
    public void deleteAllUsers() throws RemoteException;
}
