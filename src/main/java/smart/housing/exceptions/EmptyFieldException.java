package smart.housing.exceptions;

public class EmptyFieldException extends RuntimeException {

    private final String EMPTY_FIELD;

    public EmptyFieldException(String msg, String emptyField) {
        this(msg, emptyField, null);
    }
    public EmptyFieldException(String msg, String EMPTY_FIELD, Throwable cause) {
        super(msg, cause);
        this.EMPTY_FIELD = EMPTY_FIELD;
    }

    public String getEmptyField() {
        return EMPTY_FIELD;
    }

}
