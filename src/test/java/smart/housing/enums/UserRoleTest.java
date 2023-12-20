package smart.housing.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {

    @Test
    public void testParseRole_validRoleName() {
        String roleName = "AdMiN";
        UserRole expectedRole = UserRole.ADMIN;
        UserRole actualRole = UserRole.parseString(roleName);
        assertEquals(expectedRole, actualRole);
    }

    @Test
    public void testParseRole_invalidRoleName() {
        String roleName = "not a role";
        UserRole actualRole = UserRole.parseString(roleName);
        assertNull(actualRole);
    }

}