package transfer.dao.impl;

import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import transfer.dao.TransactionDao;
import transfer.model.Account;
import transfer.model.Transaction;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.jooq.impl.DSL.table;
import static transfer.model.Account.ACCOUNT;
import static transfer.model.Transaction.TRANSACTION;
import static transfer.model.Transaction.TRANSACTION_SEQ;

public class JooqTransactionDaoImpl implements TransactionDao {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private DSLContext dsl;


    @Override
    public Transaction create(String fromId, String toId, BigDecimal amount) {
        var transaction = new Transaction(fromId, toId, amount);
        BigInteger nextID = dsl.nextval(TRANSACTION_SEQ);
        transaction.setId(nextID);

        var recordFromTransaction = modelMapper.map(transaction, Map.class);

        dsl.insertInto(table(TRANSACTION))
                .set(recordFromTransaction)
                .execute();

        return transaction;
    }
}
