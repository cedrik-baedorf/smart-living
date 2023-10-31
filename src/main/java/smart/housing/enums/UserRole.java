package smart.housing.enums;

public enum UserRole {

    ADMIN("ADMIN"), USER("USER");

    private final String ROLE_NAME;

    UserRole(String roleName) {
        this.ROLE_NAME = roleName.toUpperCase();
    }

    public String getRoleName() {
        return this.ROLE_NAME;
    }

    public static UserRole parseString(String roleName) {
        roleName = roleName.toUpperCase();
        for(UserRole role : UserRole.values()) {
            if(role.getRoleName().equals(roleName))
                return role;
        }
        return null;
    }

}