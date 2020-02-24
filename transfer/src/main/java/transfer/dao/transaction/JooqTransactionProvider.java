package transfer.dao.transaction;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import transfer.model.exception.LockException;

import javax.inject.Inject;
import java.util.function.Consumer;

public class JooqTransactionProvider {

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

}
