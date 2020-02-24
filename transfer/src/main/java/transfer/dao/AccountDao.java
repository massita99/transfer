package transfer.dao;

import transfer.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends LockableDao<Account> {

    /**
     * Create new {@link Account} with uniq id, zero balance and createdDate equals now()
     *
     * @return stored {@link Account}
     */
    Account create();
    /**
     * Return {@link Account} by id
     *
     * @param id - id of account
     * @return stored {@link Account} with specified id
     */
    Optional<Account> find(String id);

    /**
     * Return List of {@link Account}
     *
     * @return All {@link Account}s
     */
    List<Account> findAll();

}
