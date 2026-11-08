package exception;

public class DBConnectionException extends Exception {

    public DBConnectionException() {
        super();
    }

    public DBConnectionException(String message) {
        super(message);
    }
}