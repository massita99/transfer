package transfer.model.exception;

public class AccountNotExistException extends RuntimeException {

    public AccountNotExistException(String accountId) {
        super(String.format("Account with id %s not found", accountId));
    }
}
