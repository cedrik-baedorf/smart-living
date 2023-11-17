package smart.housing.exceptions;

public class BudgetManagementServiceException extends RuntimeException {

    public BudgetManagementServiceException(String msg) {
        this(msg, null);
    }
    public BudgetManagementServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
