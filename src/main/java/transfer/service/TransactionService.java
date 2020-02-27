package transfer.service;


import transfer.model.Account;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

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
     * @Throws {@link AccountDoNotHaveEnoughMoneyException} if no enough money exception
     */
    Transaction performTransaction(String fromAccountId, String toAccountId, BigDecimal amount);

    /**
     * Return {@link Transaction} by id
     *
     * @param id - id of account
     * @return stored {@link Transaction} with specified id
     * @Throws {@link transfer.model.exception.TransactionNotExistException} if transaction not exist
     */
    Transaction getById(BigInteger id);

    /**
     * Return all stored {@link Transaction}s
     * Otherwise return empty {@link List}
     * @return all stored {@link Transaction}'s'
     */
    List<Transaction> getAll();

    /**
     * Return all stored {@link Transaction}s by account Id
     * Otherwise return empty {@link List}
     * @return all stored {@link Transaction}'s'
     * @throws {@link transfer.model.exception.AccountNotExistException} if one of accounts not exist
     */
    List<Transaction> getAllByAccountId(String accountId);
}
