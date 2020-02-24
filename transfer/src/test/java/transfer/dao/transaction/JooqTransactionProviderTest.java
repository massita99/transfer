package transfer.dao.transaction;

import io.micronaut.test.annotation.MicronautTest;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import transfer.dao.AccountDao;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;


@MicronautTest
class JooqTransactionProviderTest {

    @Inject
    JooqTransactionProvider transactionProvider;

    @Inject
    AccountDao accountDao;

    @Test
    void testCanUpdateLockedBySameTransaction() {
        //Given
        var account = accountDao.create();

        //When
        transactionProvider.doInTransaction(configuration -> {
                    var lockedAccount = accountDao.lockAndGet(account.getId(), configuration).get();
                    lockedAccount.setBalance(BigDecimal.TEN);
                    accountDao.updateLocked(lockedAccount, configuration);
                }
        );

        //Then
        var storedAccount = accountDao.find(account.getId()).get();
        assertThat(storedAccount.getBalance()).isCloseTo(BigDecimal.TEN, Offset.offset(BigDecimal.ONE));
    }

    @Test
    void doNotUpdateLockedByAnotherTransaction() {
        //Given
        var account = accountDao.create();

        //When
        Executors.newSingleThreadExecutor().submit(() -> transactionProvider.doInTransaction(configuration -> {
            accountDao.lockAndGet(account.getId(), configuration);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        //Then
        await().untilAsserted(() -> assertThatThrownBy(
                        () ->
                                transactionProvider.doInTransaction(configuration -> {
                                    account.setBalance(BigDecimal.TEN);
                                    accountDao.updateLocked(account, configuration);
                                })
                        )
                );
    }

}