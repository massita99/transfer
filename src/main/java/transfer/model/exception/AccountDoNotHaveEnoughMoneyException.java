package transfer.model.exception;

public class AccountDoNotHaveEnoughMoneyException extends RuntimeException {

    public AccountDoNotHaveEnoughMoneyException(String accountId) {
        super(String.format("Account with id %s do not have enough money", accountId));
    }
}
