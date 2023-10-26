package smart.housing.entities;

import smart.housing.security.HashAlgorithm;

import javax.persistence.*;

/**
 * This entity represents one row of table <i>Users</i>
 */
@Entity
@Table(name= "Users")
@NamedQueries({
        @NamedQuery(
                name = User.FIND_ALL,
                query = "SELECT user FROM User user"
        ),
        @NamedQuery(
                name = User.FIND_WITH_FILTERS,
                query = """
                        SELECT user FROM User user
                        WHERE   user.USERNAME   = coalesce(:username, user.USERNAME)
                        AND     user.lastName   = coalesce(:lastName, user.lastName)
                        AND     user.firstName  = coalesce(:firstName, user.firstName)
                        """
        )
})
public class User {

    /**
     * Name of named query to return all users
     */
    public static final String FIND_ALL = "User.findAll";

    /**
     * Name of named query to return all filtered users
     */
    public static final String FIND_WITH_FILTERS = "User.findWithFilters";

    public static final int USERNAME_LENGTH = 8;

    /**
     * Unique username
     */
    @Id
    @Column(name = "username")
    private final String USERNAME;

    /**
     * Hashed password to the username
     */
    @Column(name = "password")
    private String password;

    /**
     * Last name of the user
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * First name of the user
     */
    @Column(name = "first_name")
    private String firstName;

    public User() {
        this("standard");
    }

    public User(String username) {
        if(username == null)
            throw new RuntimeException("Username cannot be equal to null");
        this.USERNAME = username;
    }

    public User(String username, String password, HashAlgorithm algorithm) {
        this(username);
        this.setPassword(password, algorithm);
    }

    public void setPassword(String password, HashAlgorithm algorithm) {
        this.password = algorithm.hash(password);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return
            firstName + " "
            + lastName + " ("
            + username + ")";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass().equals(getClass())) {
            return obj.toString().equals(this.toString());
        }
        return false;
    }

}