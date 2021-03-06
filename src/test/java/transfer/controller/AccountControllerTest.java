package transfer.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import transfer.TestHelper;
import transfer.dto.AccountData;
import transfer.model.exception.AccountNotExistException;
import transfer.service.AccountService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@MicronautTest
class AccountControllerTest {

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
                .thenReturn(List.of(TestHelper.TEST_FAKE_ACCOUNT, TestHelper.TEST_FAKE_ACCOUNT));
        HttpRequest<String> request = HttpRequest.GET("/api/accounts");

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
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
        assertThat(response.body()).isEmpty();
    }

    @Test
    public void testGetAccountById() {
        //Given
        when(accountService.getById(TestHelper.TEST_UUID))
                .thenReturn(TestHelper.TEST_FAKE_ACCOUNT);
        HttpRequest<String> request = HttpRequest.GET("/api/accounts/" + TestHelper.TEST_UUID);

        //When
        var response = client.toBlocking().exchange(request, AccountData.class);

        //Then
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertThat(response.body()).extracting(AccountData::getId).isEqualTo(TestHelper.TEST_FAKE_ACCOUNT.getId());
    }

    @Test
    public void testGetAccountNotFoundById() {
        //Given
        when(accountService.getById(TestHelper.TEST_UUID))
                .thenThrow(new AccountNotExistException(TestHelper.TEST_UUID));
        HttpRequest<String> request = HttpRequest.GET("/api/accounts/" + TestHelper.TEST_UUID);

        //When Then
        assertThatThrownBy(() -> client.toBlocking().exchange(request, AccountData.class))
                .hasMessage(NOT_FOUND.getReason());
    }

    @Test
    public void testCreate() {
        //Given
        when(accountService.create())
                .thenReturn(TestHelper.TEST_FAKE_ACCOUNT);
        HttpRequest<String> request = HttpRequest.PUT("/api/accounts", "");

        //When
        var response = client.toBlocking().exchange(request, AccountData.class);

        //Then
        assertEquals(response.getStatus(), HttpStatus.CREATED);
        assertThat(response.body()).extracting(AccountData::getBalance).isEqualTo(BigDecimal.ZERO);
        assertThat(response.getHeaders().get("Location")).isNotEmpty();
    }

}