package smart.housing.entities;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import smart.housing.security.HashAlgorithm;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testIfPasswordIsHashed() {
        String password = "password";
        String expectedHash = "XF258736FFAA";

        HashAlgorithm algorithm = Mockito.mock(HashAlgorithm.class);

        Mockito.when(algorithm.hash(password)).thenReturn(expectedHash);

        User user = new User("username", password, algorithm);

        String actualHash = user.getPassword();

        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testUser_usernameEqualNull() {
        assertThrows(RuntimeException.class, () -> new User(null));
    }

}