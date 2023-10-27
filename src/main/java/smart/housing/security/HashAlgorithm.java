package smart.housing.security;

public interface HashAlgorithm {

    HashAlgorithm DEFAULT = new SimpleHashAlgorithm();

    int HASH_LENGTH = 64;

    String hash(String str);

}
