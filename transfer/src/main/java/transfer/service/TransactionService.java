package transfer.service;


import transfer.model.Account;

import java.math.BigDecimal;

/**
 * Service provide interface to work with transactions
 */
public interface TransactionService {

    /**
     * Create new transaction with requester amount between two accounts
     *
     * @param fromAccountId id of {@link Account} from where money where charged
     * @param toAccountId   id of {@link Account} to where money come
     * @Throws {@link transfer.model.exception.AccountNotExistException} if one of accounts not exist
     * @Throws {@link transfer.model.exception.AccountDoNotHaveEhoughMoneyException} if no enough money exception
     */
    void performTransaction(String fromAccountId, String toAccountId, BigDecimal amount);
}
