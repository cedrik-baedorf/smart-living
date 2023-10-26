package smart.housing.exceptions;

public class LoginServiceException extends RuntimeException {

    public LoginServiceException(String msg) {
        this(msg, null);
    }
    public LoginServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
