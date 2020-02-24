package transfer.dao;

import org.jooq.Configuration;
import transfer.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

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

    /**
     * Lock {@link Account} by id, lock provided by transactionKeeper
     * @param id - account id
     * @param transactionKeeper - configuration that lock record for transaction
     */
    Optional<Account> lockAndGet(String id, Configuration transactionKeeper);

    /**
     * Update rows that was locked by same {@param transactionKeeper}
     * @param account - new State of account of locked entity or another nonlocked account
     * @param transactionKeeper - configuration that lock record for transaction
     */
    void updateLocked(Account account, Configuration transactionKeeper);

}
