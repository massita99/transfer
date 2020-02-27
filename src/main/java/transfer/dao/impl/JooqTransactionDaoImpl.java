package transfer.dao.impl;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.modelmapper.ModelMapper;
import transfer.dao.TransactionDao;
import transfer.model.Account;
import transfer.model.Transaction;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static transfer.model.Account.ACCOUNT;
import static transfer.util.StringUtil.fromCamelCase;
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

    @Override
    public Optional<Transaction> find(BigInteger id) {
        var transaction = dsl.selectFrom(TRANSACTION)
                .where(field(Transaction.Fields.id).eq(id))
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Transaction.class))
                .findFirst();
        if (transaction.isEmpty()) {
            return Optional.empty();
        }
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return dsl.selectFrom(TRANSACTION)
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Transaction.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllByAccountId(String accountId) {
        return dsl.selectFrom(TRANSACTION)
                .where(field(fromCamelCase(Transaction.Fields.accountToId)).eq(accountId)
                        .or(field(fromCamelCase(Transaction.Fields.accountFromId)).eq(accountId)))
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Transaction.class))
                .collect(Collectors.toList());
    }
}
