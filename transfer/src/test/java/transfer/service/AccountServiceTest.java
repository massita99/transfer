package transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import transfer.dao.AccountDao;
import transfer.model.Account;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MicronautTest
public class AccountServiceTest {

    public static final UUID TEST_UUID = UUID.fromString("A-A-A-A-A");
    public static final Account TEST_ACCOUNT = new Account();

    @Inject
    AccountService accountService;

    @Inject
    AccountDao accountDao;

    @MockBean(AccountDao.class)
    public AccountDao accountDao() {
        return mock(AccountDao.class);
    }

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
        when(accountDao.find(TEST_UUID))
                .thenReturn(Optional.of(TEST_ACCOUNT));
        //When
        var account = accountService.getById(TEST_UUID);
        //Then
        assertThat(account.isPresent()).isTrue();
        assertThat(account.get()).isEqualTo(TEST_ACCOUNT);
    }

    @Test
    void testGetNoAccountForNotExistedId() {
        //Given
        when(accountDao.find(TEST_UUID))
                .thenReturn(Optional.empty());
        //When
        var account = accountService.getById(TEST_UUID);
        //Then
        assertThat(account.isPresent()).isFalse();
    }

    @Test
    void testGetAllExistedAccounts() {
        //Given
        when(accountDao.findAll())
                .thenReturn(List.of(TEST_ACCOUNT));
        //When
        var accounts = accountService.getAll();
        //Then
        assertThat(accounts).containsExactly(TEST_ACCOUNT);
    }
}
