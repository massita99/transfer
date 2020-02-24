package transfer;

import org.jooq.DSLContext;
import transfer.dao.AccountDao;
import transfer.model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
public class TestHelper {

    @Inject
    private AccountDao accountDao;

    @Inject
    private DSLContext dsl;

    public Account createAccountWithMoney(BigDecimal balance) {
        Account account = accountDao.create();
        account.setBalance(balance);
        accountDao.updateLocked(account, dsl.configuration());
        return account;
    }
}
