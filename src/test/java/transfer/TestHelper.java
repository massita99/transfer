package transfer;

import org.jooq.DSLContext;
import transfer.dao.AccountDao;
import transfer.dto.TransferData;
import transfer.model.Account;
import transfer.model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.BigInteger;

@Singleton
public class TestHelper {

    public static final String TEST_UUID = "TEST";
    public static final BigInteger TEST_ID = BigInteger.valueOf(Long.MAX_VALUE);
    public static final Transaction TEST_FAKE_TRANSACTION = new Transaction();
    public static final TransferData TEST_TRANSFER = new TransferData();
    public static final Account TEST_FAKE_ACCOUNT = new Account();

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
