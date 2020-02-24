package transfer.dao;

import transfer.model.Account;
import transfer.model.Transaction;

import java.math.BigDecimal;

public interface TransactionDao {

    /**
     * Create new {@link Transaction} with uniq id, requested balance and createdDate equals now()
     *
     * @return stored {@link Transaction}
     */
    Transaction create(String fromId, String toId, BigDecimal amount);
}
