package transfer.service.impl;

import transfer.dao.AccountDao;
import transfer.model.Account;
import transfer.service.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    private AccountDao accountDao;

    @Override
    public Account create() {
        return new Account();
    }

    @Override
    public Optional<Account> getById(UUID id) {
        return accountDao.find(id);
    }

    @Override
    public List<Account> getAll() {
        return accountDao.findAll();
    }
}
