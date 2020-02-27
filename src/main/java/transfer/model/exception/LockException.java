package transfer.model.exception;

/**
 * Class <class>LockException</class> describe exception entity that should be changed already locked by another process
 */
public class LockException extends RuntimeException {

    public LockException(Exception ex) {
        super(ex);
    }
}
