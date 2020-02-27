package transfer.dao.transaction;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import transfer.model.exception.LockException;

import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Function;

public class JooqTransactionProvider<E> {

    @Inject
    private DSLContext dsl;

    /**
     * Perform {@param handler} code in transaction
     * @param handler - user code that should perform in transaction
     * @throws LockException - when {@param handler} try update locked objects
     */
    public void doInTransaction(Consumer<Configuration> handler) {
        try {
            dsl.transaction(handler::accept);
        } catch (DataAccessException ex) {
            throw new LockException(ex);
        }
    }

    /**
     * Apply {@param function} in transaction and return result from repository
     * @param function - user code that should perform in transaction
     * @return function result
     * @throws LockException - when {@param handler} try update locked objects
     */
    public E returnInTransaction(Function<Configuration, E> function) {
        try {
            return dsl.transactionResult(function::apply);
        } catch (DataAccessException ex) {
            throw new LockException(ex);
        }
    }

}
