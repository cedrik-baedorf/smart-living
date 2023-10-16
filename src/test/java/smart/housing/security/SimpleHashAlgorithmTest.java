package smart.housing.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SimpleHashAlgorithmTest {

    private static final String
        PASSWORD = "password",
        COPY_PASSWORD = "password",
        DIFFERENT_PASSWORD = "different password";

    private static SimpleHashAlgorithm algorithm;

    @BeforeAll
    public static void initialize() {
        algorithm = new SimpleHashAlgorithm();
    }

    @Test
    public void testEqualHash() {
        String firstHash = algorithm.hash(PASSWORD);
        String secondHash = algorithm.hash(COPY_PASSWORD);
        assertEquals(firstHash, secondHash);
    }

    @Test
    public void testHashLength() {
        int expectedLength = HashAlgorithm.HASH_LENGTH;
        int actualLength = algorithm.hash(PASSWORD).length();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testDifferentStrings() {
        String firstHash = algorithm.hash(PASSWORD);
        String secondHash = algorithm.hash(DIFFERENT_PASSWORD);
        assertNotEquals(firstHash, secondHash);
    }

}