package transfer.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import transfer.dto.AccountData;
import transfer.model.Account;
import transfer.model.exception.AccountNotExistException;
import transfer.service.AccountService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@MicronautTest
class AccountControllerTest {

    public static final String TEST_UUID = "A-A-A-A-A";
    public static final Account TEST_ACCOUNT = new Account();


    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    AccountService accountService;

    @MockBean(AccountService.class)
    public AccountService accountService() {
        return mock(AccountService.class);
    }

    @Test
    public void testGetAllAccounts() {
        //Given
        when(accountService.getAll())
                .thenReturn(List.of(TEST_ACCOUNT, TEST_ACCOUNT));
        HttpRequest<String> request = HttpRequest.GET("/api/accounts");

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.body().size()).isEqualTo(2);

    }

    @Test
    public void testGetNoAccounts() {
        //Given
        when(accountService.getAll())
                .thenReturn(List.of());
        HttpRequest<String> request = HttpRequest.GET("/api/accounts");

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.body()).isEmpty();
    }

    @Test
    public void testGetAccountById() {
        //Given
        when(accountService.getById(TEST_UUID))
                .thenReturn(TEST_ACCOUNT);
        HttpRequest<String> request = HttpRequest.GET("/api/accounts/" + TEST_UUID);

        //When
        var response = client.toBlocking().exchange(request, AccountData.class);

        //Then
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertThat(response.body()).extracting(AccountData::getId).isEqualTo(TEST_ACCOUNT.getId());
    }

    @Test
    public void testGetAccountNotFoundById() {
        //Given
        when(accountService.getById(TEST_UUID))
                .thenThrow(new AccountNotExistException(TEST_UUID));
        HttpRequest<String> request = HttpRequest.GET("/api/accounts/" + TEST_UUID);

        //When Then
        assertThatThrownBy(() -> client.toBlocking().exchange(request, AccountData.class));
    }

    @Test
    public void testCreate() {
        //Given
        when(accountService.create())
                .thenReturn(TEST_ACCOUNT);
        HttpRequest<String> request = HttpRequest.PUT("/api/accounts", "");

        //When
        var response = client.toBlocking().exchange(request, AccountData.class);

        //Then
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertThat(response.body()).extracting(AccountData::getBalance).isEqualTo(BigDecimal.ZERO);
    }

}