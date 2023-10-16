package smart.housing.security;

public class SimpleHashAlgorithm implements HashAlgorithm {

    public String hash(String str) {
        String hashCode = String.valueOf(str.hashCode());
        return "0".repeat(HASH_LENGTH - hashCode.length()) + hashCode;
    }

}
