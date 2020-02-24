package transfer.dao.impl;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.modelmapper.ModelMapper;
import transfer.dao.TransactionDao;
import transfer.model.Transaction;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.jooq.impl.DSL.table;
import static transfer.model.Transaction.TRANSACTION;
import static transfer.model.Transaction.TRANSACTION_SEQ;

public class JooqTransactionDaoImpl implements TransactionDao {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private DSLContext dsl;


    @Override
    public Transaction createInTransaction(String fromId, String toId, BigDecimal amount, Configuration configuration) {
        var transaction = new Transaction(fromId, toId, amount);
        BigInteger nextID = dsl.nextval(TRANSACTION_SEQ);
        transaction.setId(nextID);

        var recordFromTransaction = modelMapper.map(transaction, Map.class);

        DSL.using(configuration).insertInto(table(TRANSACTION))
                .set(recordFromTransaction)
                .execute();

        return transaction;
    }
}
