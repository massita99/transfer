package transfer.dao;

import transfer.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountDao {
    /**
     * Return {@link Account} by id
     * @param id - id of account
     * @return stored {@link Account} with specified id
     */
    Optional<Account> find(UUID id);

    /**
     * Return List of {@link Account}
     * @return All {@link Account}s
     */
    List<Account> findAll();
}
