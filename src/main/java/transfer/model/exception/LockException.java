package transfer.model.exception;

public class LockException extends RuntimeException {

    public LockException(Exception ex) {
        super(ex);
    }
}
