package ArchonData.data;

import org.mindrot.jbcrypt.BCrypt;
import org.mongodb.morphia.annotations.Entity;

/**
 * User: EJ
 * Date: 11/20/13
 * Time: 9:45 PM
 */

@Entity
public class User extends BaseData {

    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String pwd) {
        this.password = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
    }


    public boolean checkPassword(String pwd) {
        return BCrypt.checkpw(pwd, this.password);
    }


}
