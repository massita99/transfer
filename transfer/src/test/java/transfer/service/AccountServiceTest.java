package transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import transfer.dao.AccountDao;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class AccountServiceTest {

    public static final String TEST_UUID = "A-A-A-A-A";

    @Inject
    AccountService accountService;

    @Inject
    AccountDao accountDao;

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
        assertThat(account.isPresent()).isTrue();
        assertThat(account.get()).isEqualTo(createdAccount);
    }

    @Test
    void testGetNoAccountForNotExistedId() {
        //When
        var account = accountService.getById(TEST_UUID);
        //Then
        assertThat(account.isPresent()).isFalse();
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
