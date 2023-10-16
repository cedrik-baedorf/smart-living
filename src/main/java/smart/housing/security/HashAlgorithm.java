package smart.housing.security;

public interface HashAlgorithm {

    int HASH_LENGTH = 64;

    String hash(String str);

}
