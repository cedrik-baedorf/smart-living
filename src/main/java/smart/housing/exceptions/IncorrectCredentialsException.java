package smart.housing.exceptions;

public class IncorrectCredentialsException extends LoginServiceException {

    public IncorrectCredentialsException(String msg) {
        this(msg, null);
    }
    public IncorrectCredentialsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
