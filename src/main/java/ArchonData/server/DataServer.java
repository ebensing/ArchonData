package ArchonData.server;

import ArchonData.data.Artist;
import ArchonData.data.MongoResource;
import ArchonData.data.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * User: EJ
 * Date: 11/16/13
 * Time: 11:21 AM
 */
public class DataServer extends UnicastRemoteObject implements DataService {

    private Datastore db = MongoResource.INSTANCE.getDatastore("archon");


    public DataServer() throws RemoteException {
        super();
    }

    @Override
    public Artist addArtist(Artist artist) throws RemoteException {
        Query<Artist> q = null;
        UpdateOperations<Artist> op;

        try {
            q = artist.createQuery(new String[] {"name"}, db);
        } catch (Exception e) {
            e.printStackTrace();
        }

        op = artist.getUpdate(db);

        return db.findAndModify(q, op, true, true);
    }

    @Override
    public Artist forceAddArtist(Artist artist) throws RemoteException {
        db.save(artist);
        return artist;
    }

    @Override
    public Artist updateArtist(Artist artist) throws RemoteException {
        Query<Artist> q = artist.getIdQuery(db);
        UpdateOperations<Artist> op;

        op = artist.getUpdate(db);

        db.update(q, op);

        return artist;
    }

    @Override
    public void deleteArtist(Artist artist) throws RemoteException {
        db.delete(artist.getIdQuery(db));
    }

    @Override
    public Artist getArtist(ObjectId id) throws RemoteException {
        return db.find(Artist.class).field(Mapper.ID_KEY).equal(id).get();
    }

    @Override
    public List<Artist> getArtists(String sort) throws RemoteException {
        return db.find(Artist.class).order(sort).asList();
    }

    @Override
    public Artist getArtist(String name) throws RemoteException {
        return db.find(Artist.class).field("name").equal(name).get();
    }

    @Override
    public User getUser(String name, String password) throws RemoteException {
        User user = db.find(User.class).field("username").equal(name).get();

        if (user != null && user.checkPassword(password)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User addUser(String username, String password) throws RemoteException {
        User user = new User(username, password);

        db.save(user);

        return user;
    }

    @Override
    public void deleteUser(String username) throws RemoteException {
        db.delete(db.createQuery(User.class).field("username").equal(username));
    }

    @Override
    public void deleteAllUsers() throws RemoteException {
        db.delete(db.createQuery(User.class));
    }

    @Override
    public List<Artist> getArtists() throws RemoteException {
        return db.find(Artist.class).asList();
    }

    @Override
    public void deleteAllArtists() throws RemoteException {
        db.delete(db.createQuery(Artist.class));
    }
}
