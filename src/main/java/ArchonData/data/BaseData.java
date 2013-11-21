package ArchonData.data;

import ArchonData.util.ObjectIdJsonDeserializer;
import ArchonData.util.ObjectIdJsonSerializer;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

/**
 * User: EJ
 * Date: 11/20/13
 * Time: 9:47 PM
 */
public abstract class BaseData implements Serializable {

    @SuppressWarnings("UnusedDeclaration")
    @JsonDeserialize(using=ObjectIdJsonDeserializer.class)
    @Id
    protected ObjectId id;

    @SuppressWarnings("UnusedDeclaration")
    @JsonSerialize(using=ObjectIdJsonSerializer.class)
    public ObjectId getId() {
        return id;
    }
}
