package smart.housing.entities;

import smart.housing.enums.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

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

    @Override
    public String toString() {
        return getRole().getRoleName();
    }

}
