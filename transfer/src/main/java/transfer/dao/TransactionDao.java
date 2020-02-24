package transfer.dao;

import transfer.model.Account;
import transfer.model.Transaction;

import org.jooq.Configuration;
import java.math.BigDecimal;

public interface TransactionDao {

    /**
     * Create new {@link Transaction} with uniq id, requested balance and createdDate equals now()
     * @param fromId - id of {@link Account} from where money acquired
     * @param toId - id of {@link Account} to where money transferred
     * @return stored {@link Transaction}
     */
    Transaction createInTransaction(String fromId, String toId, BigDecimal amount, Configuration configuration);
}
