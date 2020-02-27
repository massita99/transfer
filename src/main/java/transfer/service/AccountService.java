package transfer.service;

import transfer.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * Service provide interface to work with accounts
 */
public interface AccountService {

    /**
     * Create new account with zero balance
     *
     * @return created {@link Account}
     */
    Account create();

    /**
     * Return {@link Account} by id
     *
     * @param id - id of account
     * @return stored {@link Account} with specified id
     * @Throws {@link transfer.model.exception.AccountNotExistException} if accounts not exist
     */
    Account getById(String id);

    /**
     * Return all stored {@link Account}s
     * Otherwise return empty {@link List}
     *
     * @return all stored {@link Account}'s'
     */
    List<Account> getAll();
}
