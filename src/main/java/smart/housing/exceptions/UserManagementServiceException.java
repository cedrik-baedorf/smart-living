package smart.housing.exceptions;

public class UserManagementServiceException extends RuntimeException {

    public UserManagementServiceException(String msg) {
        this(msg, null);
    }
    public UserManagementServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
