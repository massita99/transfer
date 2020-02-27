package transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import transfer.TestHelper;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MicronautTest
public class AccountServiceTest {

    @Inject
    AccountService accountService;

    @Test
    void testCreateAccount() {
        //When
        var account = accountService.create();
        //Then
        assertThat(account).isNotNull();
    }

    @Test
    void testCreatedAccountAlwaysEmpty() {
        //When
        var account = accountService.create();
        var balance = account.getBalance();
        //Then
        assertThat(balance).isZero();
    }

    @Test
    void testCreatedAccountHaveUniqueIdentifier() {
        //When
        var firstAccount = accountService.create();
        var secondAccount = accountService.create();

        var firstAccountIdentifier = firstAccount.getId();
        var secondAccountIdentifier = secondAccount.getId();
        //Then
        assertThat(firstAccountIdentifier).isNotEqualTo(secondAccountIdentifier);
    }

    @Test
    void testGetExistedAccountById() {
        //Given
        var createdAccount = accountService.create();
        //When
        var account = accountService.getById(createdAccount.getId());
        //Then
        assertThat(account).isEqualTo(createdAccount);
    }

    @Test
    void testGetNoAccountForNotExistedId() {
        //When Then
        assertThatThrownBy(() -> accountService.getById(TestHelper.TEST_UUID));
    }

    @Test
    void testGetAllExistedAccounts() {
        //Given
        var createdAccount1 = accountService.create();
        var createdAccount2 = accountService.create();

        //When
        var accounts = accountService.getAll();
        //Then
        assertThat(accounts).contains(createdAccount1, createdAccount2);
    }
}
