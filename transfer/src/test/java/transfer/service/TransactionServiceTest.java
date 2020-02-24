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

    @Test
    void testGetAllExistedTransactions() {
        //Given
        Account accountFrom = testHelper.createAccountWithMoney(BigDecimal.TEN);
        Account accountTo = accountService.create();
        transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN);

        //When
        var transactions = transactionService.getAll();
        //Then
        assertThat(transactions).isNotEmpty();
    }

    @Test
    void testGetAllExistedTransactionsByAccount() {
        //Given
        Account accountFrom = testHelper.createAccountWithMoney(BigDecimal.TEN);
        Account accountTo = accountService.create();

        //Make two transactions with same accounts
        transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN);
        transactionService.performTransaction(accountTo.getId(), accountFrom.getId(), BigDecimal.TEN);

        //When
        var transactions = transactionService.getAllByAccountId(accountFrom.getId());
        //Then
        assertThat(transactions.size()).isEqualTo(2);
    }

}