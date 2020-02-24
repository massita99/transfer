package transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
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

    @Test
    void testCreateNewTransaction() {
        //Given
        Account accountFrom = accountService.create();
        Account accountTo = accountService.create();

        //When
        var transaction = transactionService.create(accountFrom.getId(), accountTo.getId(), BigDecimal.ZERO);

        //Then
        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isNotZero();
    }

    @Test
    void testDoNotCreateNewTransactionForNotExistedAccount() {
        //Given
        Account accountFrom = new Account();
        Account accountTo = new Account();

        //Then
        assertThatThrownBy(() -> transactionService.create(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN));
    }

    @Test
    void testDoNotCreateNewTransactionIfNotEnoughMoney() {
        //Given
        Account accountFrom = accountService.create();
        Account accountTo = accountService.create();

        //Then
        assertThatThrownBy(() -> transactionService.create(accountFrom.getId(), accountTo.getId(), BigDecimal.TEN));
    }

}