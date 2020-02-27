package transfer.model.exception;

/**
 * Class <class>AccountNotExistException</class> describe exception when account can not be found
 */
public class AccountNotExistException extends RuntimeException {

    public AccountNotExistException(String accountId) {
        super(String.format("Account with id %s not found", accountId));
    }
}
