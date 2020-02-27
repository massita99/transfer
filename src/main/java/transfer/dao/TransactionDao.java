package transfer.dao;

import transfer.model.Account;
import transfer.model.Transaction;

import org.jooq.Configuration;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface TransactionDao {

    /**
     * Create new {@link Transaction} with uniq id, requested balance and createdDate equals now()
     * @param fromId - id of {@link Account} from where money acquired
     * @param toId - id of {@link Account} to where money transferred
     * @return successfully performed {@link Transaction}
     */
    Transaction createInTransaction(String fromId, String toId, BigDecimal amount, Configuration configuration);

    /**
     * Return {@link Transaction} by id
     *
     * @param id - id of account
     * @return stored {@link Transaction} with specified id
     */
    Optional<Transaction> find(BigInteger id);

    /**
     * Return List of {@link Transaction}
     *
     * @return All {@link Transaction}s
     */
    List<Transaction> findAll();

    /**
     * Return List of {@link Transaction} by account ID
     * @param accountId id of account
     * @return All {@link Transaction}s
     */
    List<Transaction> findAllByAccountId(String accountId);
}
