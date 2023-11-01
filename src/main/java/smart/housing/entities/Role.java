package smart.housing.entities;

import smart.housing.enums.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@NamedQueries({
        @NamedQuery(
                name = Role.FIND_ALL,
                query = "SELECT role FROM Role role"
        )
})
public class Role {

    /**
     * Name of named query to return all users
     */
    public static final String FIND_ALL = "Role.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "name")
    private String roleName;

    public void setRole(UserRole role) {
        this.roleName = role.getRoleName();
    }

    public UserRole getRole() {
        return UserRole.parseString(roleName);
    }

}
