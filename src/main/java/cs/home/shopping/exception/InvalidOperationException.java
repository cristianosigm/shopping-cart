package cs.home.shopping.exception;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException() {
        super("Invalid operation requested. Aborting...");
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
