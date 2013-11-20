package ArchonData;

import ArchonData.data.Artist;
import ArchonData.server.DataServer;
import ArchonData.server.DataService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: EJ
 * Date: 11/16/13
 * Time: 11:55 AM
 */
public class ServiceTest {

    private DataService client;
    private Registry reg = null;

    @Before
    public void setUp() throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        reg = LocateRegistry.createRegistry(main.port);
        DataServer server = new DataServer();
        reg.bind("DataService", server);

        client = (DataService) reg.lookup("DataService");

    }

    @After
    public void cleanUp() throws Exception {
        client.deleteAllArtists();
        UnicastRemoteObject.unexportObject(reg, true);
    }

    @Test
    public void testAddArtist() throws RemoteException {
        Artist artist = new Artist(true);
        artist.setName("epica");

        Artist created = client.addArtist(artist);

        assertEquals(created, null);

        artist.setName("epica");
        Artist created2 = client.addArtist(artist);

        assertEquals(created2.getName(), artist.getName());

    }

    @Test
    public void testForceAddArtist() throws RemoteException {
        Artist artist = new Artist(true);
        artist.setName("nightwish");

        Artist artist2 = new Artist(true);
        artist2.setName("nightwish");

        client.forceAddArtist(artist);
        client.forceAddArtist(artist2);

        List<Artist> artists = client.getArtists();

        assertEquals(2, artists.size());
        assertEquals("nightwish", artists.get(0).getName());
        assertEquals("nightwish", artists.get(1).getName());

    }

    @Test
    public void testUpdateArtist() throws RemoteException {
        Artist artist = new Artist(true);
        artist.setName("amon amarth");

        Artist created = client.forceAddArtist(artist);

        created.setSeen(3);
        created.setDescription("awesome metal band");
        created.setGenre("death metal");

        client.updateArtist(created);

        Artist gArtist = client.getArtist(created.getId());

        assertEquals(gArtist.getName(), "amon amarth");
        assertEquals(gArtist.getDescription(), "awesome metal band");
        assertEquals(gArtist.getSeen(), 3);
        assertEquals(gArtist.getGenre(), "death metal");
    }

    @Test
    public void testGetArtist() throws RemoteException {
        Artist artist = new Artist(true);
        artist.setName("Dark Tranquility");

        client.forceAddArtist(artist);

        Artist second = new Artist(true);
        second.setName("Scar Symmetry");

        client.forceAddArtist(second);

        Artist test1 = client.getArtist(artist.getId());

        assertEquals(test1.getName(), artist.getName());

        Artist test2 = client.getArtist(second.getId());

        assertEquals(test2.getName(), second.getName());
    }

    @Test
    public void testGetArtistByName() throws RemoteException {
        Artist artist = new Artist(true);
        artist.setName("Dark Tranquility");

        client.forceAddArtist(artist);

        Artist second = new Artist(true);
        second.setName("Scar Symmetry");

        client.forceAddArtist(second);

        Artist test1 = client.getArtist(artist.getName());

        assertEquals(test1.getName(), artist.getName());

        Artist test2 = client.getArtist(second.getName());

        assertEquals(test2.getName(), second.getName());
    }

    @Test
    public void testGetArtists() throws RemoteException {

        Artist artist = new Artist(true);
        artist.setName("Dark Tranquility");
        Artist second = new Artist(true);
        second.setName("Scar Symmetry");
        Artist third = new Artist(true);
        third.setName("Wintersun");

        client.addArtist(artist);
        client.addArtist(second);
        client.addArtist(third);

        List<Artist> all = client.getArtists("name");

        assertEquals(all.size(), 3);
        assertEquals(all.get(0).getName(), artist.getName());
        assertEquals(all.get(1).getName(), second.getName());
        assertEquals(all.get(2).getName(), third.getName());

    }

    @Test
    public void testDeleteArtist() throws RemoteException {

        Artist artist = new Artist(true);
        artist.setName("Dark Tranquility");
        Artist second = new Artist(true);
        second.setName("Scar Symmetry");
        Artist third = new Artist(true);
        third.setName("Wintersun");

        client.forceAddArtist(artist);
        client.forceAddArtist(second);
        client.forceAddArtist(third);

        client.deleteArtist(artist);

        List<Artist> all = client.getArtists("name");

        assertEquals(all.size(), 2);
        assertEquals(all.get(0).getName(), second.getName());
        assertEquals(all.get(1).getName(), third.getName());

    }

    @Test
    public void testDeleteAllArtists() throws RemoteException {

        Artist artist = new Artist(true);
        artist.setName("Dark Tranquility");
        Artist second = new Artist(true);
        second.setName("Scar Symmetry");
        Artist third = new Artist(true);
        third.setName("Wintersun");

        client.forceAddArtist(artist);
        client.forceAddArtist(second);
        client.forceAddArtist(third);

        client.deleteAllArtists();

        List<Artist> all = client.getArtists();

        assertEquals(all.size(), 0);
    }

}
