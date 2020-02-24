package transfer.dao.transaction;

public class LockException extends RuntimeException {

    public LockException(Exception ex) {
        super(ex);
    }
}
