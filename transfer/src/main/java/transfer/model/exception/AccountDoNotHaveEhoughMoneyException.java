package transfer.model.exception;

public class AccountDoNotHaveEhoughMoneyException extends RuntimeException {

    public AccountDoNotHaveEhoughMoneyException(String accountId) {
        super(String.format("Account with id %s do not have enough money", accountId));
    }
}
