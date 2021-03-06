package transfer.service.impl;

import org.jooq.Configuration;
import transfer.aop.annotation.DebugLog;
import transfer.dao.AccountDao;
import transfer.dao.TransactionDao;
import transfer.dao.transaction.JooqTransactionProvider;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;
import transfer.model.exception.AccountNotExistException;
import transfer.model.exception.TransactionNotExistException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private AccountDao accountDao;

    @Inject
    private JooqTransactionProvider<Transaction> transactionProvider;


    @Override
    @DebugLog
    public Transaction performTransaction(String fromAccountId, String toAccountId, BigDecimal amount) {
        return transactionProvider.returnInTransaction(configuration -> {

            var fromAccount = accountDao.lockAndGet(fromAccountId, configuration);
            var toAccount = accountDao.lockAndGet(toAccountId, configuration);
            if (fromAccount.isEmpty()) {
                throw new AccountNotExistException(fromAccountId);
            }
            if (toAccount.isEmpty()) {
                throw new AccountNotExistException(toAccountId);
            }
            if (fromAccount.get().getBalance().compareTo(amount) < 0) {
                throw new AccountDoNotHaveEnoughMoneyException(fromAccountId);
            }

            accountDao.updateLocked(fromAccount.get().minus(amount), configuration);
            accountDao.updateLocked(toAccount.get().plus(amount), configuration);

            return transactionDao.createInTransaction(fromAccountId, toAccountId, amount, configuration);
        });
    }

    @Override
    public Transaction getById(BigInteger id) {
        return transactionDao.find(id).
                orElseThrow(() -> new TransactionNotExistException(id.toString()));
    }

    @Override
    public List<Transaction> getAll() {
        return transactionDao.findAll();
    }

    @Override
    public List<Transaction> getAllByAccountId(String accountId) {
        var account = accountDao.find(accountId);
        if (account.isEmpty()) {
            throw new AccountNotExistException(accountId);
        }
        return transactionDao.findAllByAccountId(accountId);
    }
}
