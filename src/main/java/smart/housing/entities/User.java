package smart.housing.entities;

import smart.housing.security.HashAlgorithm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity represents one row of table <i>Users</i>
 */
@Entity
@Table(name= "Users")
public class User {

    /**
     * Unique username
     */
    @Id
    @Column(name = "username")
    private final String username;

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

    public User(String username, String password, HashAlgorithm algorithm) {
        this(username);
        this.setPassword(password, algorithm);
    }

    public User(String username) {
        if(username == null)
            throw new NullPointerException("Username cannot be equal to null");
        this.username = username;
    }

    public void setPassword(String password, HashAlgorithm algorithm) {
        this.password = algorithm.hash(password);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String fistName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
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
        return "("
            + username + ";"
            + password + ";"
            + lastName + ";"
            + firstName + ")";
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