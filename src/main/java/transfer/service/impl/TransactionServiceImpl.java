package transfer.service.impl;

import transfer.aop.annotation.DebugLog;
import transfer.dao.AccountDao;
import transfer.dao.TransactionDao;
import transfer.dao.transaction.JooqTransactionProvider;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;
import transfer.model.exception.AccountNotExistException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private AccountDao accountDao;

    @Inject
    private JooqTransactionProvider transactionProvider;


    @Override
    @DebugLog
    public void performTransaction(String fromAccountId, String toAccountId, BigDecimal amount) {
        transactionProvider.doInTransaction(configuration -> {

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

            transactionDao.createInTransaction(fromAccountId, toAccountId, amount, configuration);
        });
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
