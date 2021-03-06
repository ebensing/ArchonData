package ArchonData.data;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * User: EJ
 * Date: 11/5/13
 * Time: 10:48 PM
 */

@Entity
public class Artist extends BaseData {

    private String name;
    private int seen;
    private String description;
    private String genre;

    @Transient
    private HashMap<String, Boolean> changed = new HashMap<>();

    @SuppressWarnings("UnusedDeclaration")
    public Artist() {
       this(false);
    }

    public Artist(ObjectId id) {
        this(false);
        this.id = id;
    }

    public Artist(boolean markDirty) {
        this.name = "";
        this.seen = 0;
        this.description = "";
        this.genre = "";

        if (markDirty) {
            this.changed.put("name", true);
            this.changed.put("seen", true);
            this.changed.put("genre", true);
            this.changed.put("description", true);
        }
    }

    private void markClean() {
        this.changed.put("name", false);
        this.changed.put("seen", false);
        this.changed.put("genre", false);
        this.changed.put("description", false);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getName() {
        return name;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setName(String name) {
        this.changed.put("name", true);
        this.name = name;
    }

    @SuppressWarnings("UnusedDeclaration")
    public int getSeen() {
        return seen;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSeen(int seen) {
        this.changed.put("seen", true);
        this.seen = seen;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getDescription() {
        return description;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDescription(String description) {
        this.changed.put("description", true);
        this.description = description;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getGenre() {
        return genre;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setGenre(String genre) {
        this.changed.put("genre", true);
        this.genre = genre;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
        json = mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public Query<Artist> createQuery(String[] fields, Datastore db) throws NoSuchFieldException, IllegalAccessException {
        Query<Artist> q = db.find(Artist.class);

        Class<?> c = this.getClass();


        for(String f : fields) {
           Field field = c.getDeclaredField(f);
           field.setAccessible(true);

           q = q.field(f).equal(field.get(this));
        }

        return q;
    }

    public UpdateOperations<Artist> getUpdate(Datastore db) {
        UpdateOperations<Artist> op = db.createUpdateOperations(Artist.class);

        Class<?> c = this.getClass();


        try {
            for (Map.Entry<String, Boolean> item : this.changed.entrySet()) {
                if (item.getValue()) {
                    Field f = c.getDeclaredField(item.getKey());
                    f.setAccessible(true);

                    op = op.set(item.getKey(), f.get(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.markClean();

        return op;
    }

    public Query<Artist> getIdQuery(Datastore db) {
        return db.createQuery(Artist.class).field(Mapper.ID_KEY).equal(getId());
    }


}
