package transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import transfer.TestHelper;
import transfer.model.Account;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MicronautTest
class TransactionServiceTest {

    @Inject
    TransactionService transactionService;

    @Inject
    AccountService accountService;

    @Inject
    TestHelper testHelper;

    @Test
    void testCreateNewTransaction() {
        //Given
        Account accountFrom = testHelper.createAccountWithMoney(BigDecimal.TEN);
        Account accountTo = accountService.create();

        //When
        transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN);

        //Then
        Account accountFromAfterTransaction = accountService.getById(accountFrom.getId()).get();
        assertThat(accountFromAfterTransaction.getBalance()).isZero();


    }

    @Test
    void testDoNotCreateNewTransactionForNotExistedAccount() {
        //Given
        Account accountFrom = new Account();
        Account accountTo = new Account();

        //Then
        assertThatThrownBy(() -> transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN));
    }

    @Test
    void testDoNotCreateNewTransactionIfNotEnoughMoney() {
        //Given
        Account accountFrom = accountService.create();
        Account accountTo = accountService.create();

        //Then
        assertThatThrownBy(() -> transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN));
    }

}