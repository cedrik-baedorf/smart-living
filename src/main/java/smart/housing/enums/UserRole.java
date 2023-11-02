package smart.housing.enums;

public enum UserRole {

    ADMIN("ADMIN", 1), SUPREME("SUPREME", 2), USER("USER", 3);

    public static final UserRole DEFAULT_ROLE = UserRole.USER;

    private final String ROLE_NAME;
    private final int RANK;

    UserRole(String roleName, int rank) {
        this.ROLE_NAME = roleName.toUpperCase();
        this.RANK = rank;
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

    public int getRank() {
        return RANK;
    }

    /**
     * This method can be used to compare ranks between two UserRoles.
     * The lower the <code>int</code> value of a rank, the higher that the role's rank is.
     * @param role <code>{@link UserRole}</code> object who's rank will be compared.
     * @return <code>true</code> if this roles rank is greater or equal than the role in comparison
     * (<code>this.RANK <= role.getRANK</code>)
     */
    public boolean outranks(UserRole role) {
        return RANK <= role.getRank();
    }

    @Override
    public String toString() {
        return this.getRoleName();
    }

}