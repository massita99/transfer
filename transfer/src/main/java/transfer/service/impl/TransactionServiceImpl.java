package transfer.service.impl;

import transfer.dao.AccountDao;
import transfer.dao.TransactionDao;
import transfer.model.exception.AccountDoNotHaveEhoughMoneyException;
import transfer.model.Transaction;
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


    @Override
    public Transaction create(String fromAccountId, String toAccountId, BigDecimal amount) {
        var fromAccount = accountDao.find(fromAccountId);
        var toAccount = accountDao.find(toAccountId);
        if (fromAccount.isEmpty()) {
            throw new AccountNotExistException(fromAccountId);
        }
        if (toAccount.isEmpty()) {
            throw new AccountNotExistException(toAccountId);
        }
        if (fromAccount.get().getBalance().compareTo(amount) < 0)
        {
            throw new AccountDoNotHaveEhoughMoneyException(fromAccountId);
        }

        return transactionDao.create(fromAccountId, toAccountId, amount);
    }
}
