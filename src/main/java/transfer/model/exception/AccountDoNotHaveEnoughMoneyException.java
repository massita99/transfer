package transfer.model.exception;

/**
 * Class <class>AccountDoNotHaveEnoughMoneyException</class> describe exception when account have not enough money to
 * perform transaction
 */
public class AccountDoNotHaveEnoughMoneyException extends RuntimeException {

    public AccountDoNotHaveEnoughMoneyException(String accountId) {
        super(String.format("Account with id %s do not have enough money", accountId));
    }
}
