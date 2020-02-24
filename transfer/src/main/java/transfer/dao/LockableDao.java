package transfer.dao;

import org.jooq.Configuration;
import transfer.model.Account;

import java.util.Optional;

public interface LockableDao<E> {

    /**
     * Lock {@link Account} by id, lock provided by transactionKeeper
     * @param id - account id
     * @param transactionKeeper - configuration that lock record for transaction
     */
    Optional<Account> lockAndGet(String id, Configuration transactionKeeper);

    /**
     * Update rows that was locked by same {@param transactionKeeper}
     * @param entity - new State of entity of locked entity or another nonlocked Entity
     * @param transactionKeeper - configuration that lock record for transaction
     */
    void updateLocked(E entity, Configuration transactionKeeper);
}
