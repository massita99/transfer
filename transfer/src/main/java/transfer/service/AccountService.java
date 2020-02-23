package transfer.service;

import transfer.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service provide interface to work with accounts
 */
public interface AccountService {

    /**
     * Create new account with zero balance
     * @return created {@link Account}
     */
    Account create();

    /**
     * Return {@link Account} by id
     * Otherwise return empty {@link Optional}
      * @param id - id of account
     * @return stored {@link Account} with specified id
     */
    Optional<Account> getById(UUID id);

    /**
     * Return all stored {@link Account}s
     * Otherwise return empty {@link List}
     * @return all stored {@link Account}'s'
     */
    List<Account> getAll();
}
