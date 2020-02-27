package transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import transfer.TestHelper;
import transfer.model.Account;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;
import transfer.model.exception.AccountNotExistException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static transfer.TestHelper.TEST_FAKE_ACCOUNT;

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
        Account accountTo = testHelper.createAccountWithMoney(BigDecimal.ZERO);

        //When
        transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN);

        //Then
        Account accountFromAfterTransaction = accountService.getById(accountFrom.getId());
        assertThat(accountFromAfterTransaction.getBalance()).isZero();


    }

    @Test
    void testDoNotCreateNewTransactionForNotExistedAccount() {
        //Given
        Account accountFrom = TEST_FAKE_ACCOUNT;
        Account accountTo = TEST_FAKE_ACCOUNT;

        //Then
        assertThatThrownBy(() -> transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN))
                .isInstanceOf(AccountNotExistException.class);
    }

    @Test
    void testDoNotCreateNewTransactionIfNotEnoughMoney() {
        //Given
        Account accountFrom = testHelper.createAccountWithMoney(BigDecimal.ZERO);
        Account accountTo = testHelper.createAccountWithMoney(BigDecimal.ZERO);

        //Then
        assertThatThrownBy(() -> transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN))
                .isInstanceOf(AccountDoNotHaveEnoughMoneyException.class);
    }

    @Test
    void testGetAllExistedTransactions() {
        //Given
        var createdTransaction = createTransaction();

        //When
        var transactions = transactionService.getAll();
        //Then
        assertThat(transactions).isNotEmpty();
    }

    private Transaction createTransaction() {
        Account accountFrom = testHelper.createAccountWithMoney(BigDecimal.TEN);
        Account accountTo = testHelper.createAccountWithMoney(BigDecimal.ZERO);

        return transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN);
    }

    @Test
    void testGetAllExistedTransactionsByAccount() {
        //Given
        Account accountFrom = testHelper.createAccountWithMoney(BigDecimal.TEN);
        Account accountTo = testHelper.createAccountWithMoney(BigDecimal.ZERO);

        //Make two transactions with same accounts
        transactionService.performTransaction(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN);
        transactionService.performTransaction(accountTo.getId(), accountFrom.getId(), BigDecimal.TEN);

        //When
        var transactions = transactionService.getAllByAccountId(accountFrom.getId());
        //Then
        assertThat(transactions.size()).isEqualTo(2);
    }

    @Test
    void testGetExistedTransactionById() {
        //Given
        var createdTransaction = createTransaction();

        //When
        var transactions = transactionService.getById(createdTransaction.getId());
        //Then
        assertThat(transactions).isEqualTo(createdTransaction);
    }

    @Test
    void testGetNoAccountForNotExistedId() {
        //When Then
        assertThatThrownBy(() -> transactionService.getById(BigInteger.TEN));
    }

}