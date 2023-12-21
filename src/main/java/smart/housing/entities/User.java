package smart.housing.entities;

import smart.housing.enums.UserRole;
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

    /**
     * Role of the user
     */
    @Column(name = "role")
    private String role;

    /**
     * Email of the user
     */
    @Column(name = "email")
    private String email;

    /**
     * Default constructor for an object of this class setting the username to 'standard'.
     */
    public User() {
        this("standard");
    }

    /**
     * Overloaded constructor accepting a value for the <code>username</code> property
     * @param username identifier of the user
     */
    public User(String username) {
        if(username == null)
            throw new RuntimeException("Username cannot be equal to null");
        this.USERNAME = username;
    }

    /**
     * Overloaded constructor accepting a value for the username and a password to be hashed
     * @param username identifier of the user
     * @param password password of the user. This value will not be saved without being hashed
     * @param algorithm hash algorithm to be used to hash the password
     */
    public User(String username, String password, HashAlgorithm algorithm) {
        this(username);
        this.setPassword(password, algorithm);
    }

    /**
     * Setter for the <code>password</code> attribute
     * @param password unhashed password of the user
     * @param algorithm hash algorithm create the password hash
     */
    public void setPassword(String password, HashAlgorithm algorithm) {
        this.password = algorithm.hash(password);
    }

    /**
     * Setter for the <code>password</code> attribute
     * @param passwordHash hashed password of the user
     */
    public void setPasswordHash(String passwordHash) { this.password = passwordHash; }

    /**
     * Setter for the <code>lastName</code> attribute
     * @param lastName last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Setter for the <code>firstName</code> attribute
     * @param firstName first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Setter for the <code>role</code> attribute
     * @param role user role of the user
     */
    public void setRole(UserRole role) {
        this.role = role.getRoleName();
    }

    /**
     * Setter for the <code>email</code> attribute
     * @param email email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the <code>username</code> attribute
     * @return username of the user
     */
    public String getUsername() {
        return USERNAME;
    }

    /**
     * Getter for the <code>password</code> attribute
     * @return hashed password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the <code>lastName</code> attribute
     * @return last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for the <code>firstName</code> attribute
     * @return first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for the <code>role</code> attribute
     * @return user role of the user
     */
    public UserRole getRole() {
        if(this.role == null || this.role.isEmpty())
            return null;
        UserRole role = UserRole.parseString(this.role);
        if(role != null)
            return role;
        this.role = UserRole.DEFAULT_ROLE.getRoleName();
        return getRole();
    }

    /**
     * Getter for the <code>email</code> attribute
     * @return email of the user
     */
    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return
            firstName + " "
            + lastName + " ("
            + USERNAME + ")";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass().equals(getClass()))
            return this.toString().equals(((User) obj).toString());
        return false;
    }

}