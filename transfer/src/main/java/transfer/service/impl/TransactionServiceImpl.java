package transfer.service.impl;

import transfer.dao.AccountDao;
import transfer.dao.TransactionDao;
import transfer.dao.transaction.JooqTransactionProvider;
import transfer.model.exception.AccountDoNotHaveEhoughMoneyException;
import transfer.model.exception.AccountNotExistException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private AccountDao accountDao;

    @Inject
    private JooqTransactionProvider transactionProvider;


    @Override
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
                throw new AccountDoNotHaveEhoughMoneyException(fromAccountId);
            }

            accountDao.updateLocked(fromAccount.get().minus(amount), configuration);
            accountDao.updateLocked(toAccount.get().plus(amount), configuration);

            transactionDao.createInTransaction(fromAccountId, toAccountId, amount, configuration);
        });
    }
}
