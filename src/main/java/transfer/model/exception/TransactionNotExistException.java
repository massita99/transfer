package transfer.model.exception;

/**
 * Class <class>TransactionNotExistException</class> describe exception when transaction can not be found
 */
public class TransactionNotExistException extends RuntimeException {

    public TransactionNotExistException(String transactionId) {
        super(String.format("Transaction with id %s not found", transactionId));
    }
}

    
    