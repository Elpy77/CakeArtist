package exception;

public class OperationException extends Exception {
    private static final long serialVersionUID = 1L;

    public OperationException() {
        super();
    }

    public OperationException(String message) {
        super(message);
    }
}