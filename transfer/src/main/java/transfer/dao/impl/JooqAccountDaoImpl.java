package transfer.dao.impl;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.modelmapper.ModelMapper;
import transfer.dao.AccountDao;
import transfer.model.Account;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static transfer.model.Account.ACCOUNT;

public class JooqAccountDaoImpl implements AccountDao {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private DSLContext dsl;

    @Override
    public Account create() {
        var account = new Account();
        var recordFromAccount = modelMapper.map(account, Map.class);

        dsl.insertInto(table(ACCOUNT))
                .set(recordFromAccount)
                .execute();

        return account;
    }

    @Override
    public Optional<Account> find(String id) {
        var account = dsl.selectFrom(ACCOUNT)
                .where(field(Account.Fields.id).eq(id))
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Account.class))
                .findFirst();
        if (account.isEmpty()) {
            return Optional.empty();
        }
        return account;
    }

    @Override
    public List<Account> findAll() {
        return dsl.selectFrom(ACCOUNT)
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Account.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> lockAndGet(String id, Configuration transactionKeeper) {
        var account = DSL.using(transactionKeeper).selectFrom(ACCOUNT)
                .where(field(Account.Fields.id).eq(id))
                .forUpdate()
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Account.class))
                .findFirst();

        if (account.isEmpty()) {
            return Optional.empty();
        }
        return account;

    }

    @Override
    public void updateLocked(Account entity, Configuration transactionKeeper) {
        var recordFromAccount = modelMapper.map(entity, Map.class);
        DSL.using(transactionKeeper)
                .update(table(ACCOUNT))
                .set(recordFromAccount)
                .where(field(Account.Fields.id).eq(entity.getId()))
                .execute();
    }


}
