package transfer.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import transfer.TestHelper;
import transfer.dto.TransactionData;
import transfer.dto.TransferData;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;
import transfer.model.exception.AccountNotExistException;
import transfer.model.exception.TransactionNotExistException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static transfer.TestHelper.TEST_FAKE_TRANSACTION;
import static transfer.TestHelper.TEST_ID;

@MicronautTest
public class TransactionControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    TransactionService transactionService;

    @MockBean(TransactionService.class)
    public TransactionService transactionService() {
        return mock(TransactionService.class);
    }

    @Test
    public void testGetAccountNotFoundById() {
        //Given
        when(transactionService.getById(TEST_ID))
                .thenThrow(new TransactionNotExistException(TEST_ID.toString()));
        HttpRequest<String> request = HttpRequest.GET("/api/transactions/" + TEST_ID);

        //When Then
        assertThatThrownBy(() -> client.toBlocking().exchange(request, TransactionData.class))
                .hasMessage(NOT_FOUND.getReason());
    }

    @Test
    public void testGetAllTransactions() {
        //Given
        when(transactionService.getAll())
                .thenReturn(List.of(TEST_FAKE_TRANSACTION));
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions");

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
        assertThat(response.body().size()).isEqualTo(1);

    }

    @Test
    public void testTransactionById() {
        //Given
        when(transactionService.getById(TEST_ID))
                .thenReturn(TEST_FAKE_TRANSACTION);
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions/" + TEST_ID);

        //When
        var response = client.toBlocking().exchange(request, TransactionData.class);

        //Then
        assertThat(response.body().getId()).isEqualTo(TEST_FAKE_TRANSACTION.getId());
    }

    @Test
    public void testGetAllTransactionsByAccountId() {
        //Given
        when(transactionService.getAllByAccountId(TestHelper.TEST_UUID))
                .thenReturn(List.of(TEST_FAKE_TRANSACTION));
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions?accountId=" + TestHelper.TEST_UUID);

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
        assertThat(response.body().size()).isEqualTo(1);
    }

    @Test
    public void testFailOnGetAllTransactionsByNotExistedAccountId() {
        //Given
        when(transactionService.getAllByAccountId(TestHelper.TEST_UUID))
                .thenThrow(new AccountNotExistException(TestHelper.TEST_UUID));
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions?accountId=" + TestHelper.TEST_UUID);

        //When Then
        assertThatThrownBy(() -> client.toBlocking().exchange(request, Collection.class))
                .hasMessage(NOT_FOUND.getReason());
    }

    @Test
    public void testGoodTransfer() {
        //Given
        var goodTransferData = new TransferData();
        goodTransferData.setAccountFromId(TestHelper.TEST_UUID);
        goodTransferData.setAccountToId(TestHelper.TEST_UUID);
        goodTransferData.setAmount(BigDecimal.TEN);

        var goodTransactionData = new Transaction(TestHelper.TEST_UUID, TestHelper.TEST_UUID, BigDecimal.TEN);
        goodTransactionData.setId(BigInteger.ONE);

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", goodTransferData);
        when(transactionService.performTransaction(TestHelper.TEST_UUID, TestHelper.TEST_UUID, BigDecimal.TEN))
                .thenReturn(goodTransactionData);

        //When
        var transaction = client.toBlocking().exchange(request, TransactionData.class);

        //Then
        assertThat(transaction.getBody().get().getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transaction.getHeaders().get("Location")).isNotEmpty();
    }

    @Test
    public void testFailTransferOnAccountNotExist() {
        //Given
        doThrow(new AccountNotExistException(TestHelper.TEST_UUID)).when(transactionService).performTransaction(
                TestHelper.TEST_TRANSFER.getAccountFromId(),
                TestHelper.TEST_TRANSFER.getAccountToId(),
                TestHelper.TEST_TRANSFER.getAmount());

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TestHelper.TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, Void.class))
                .hasMessage(BAD_REQUEST.getReason());

    }

    @Test
    public void testFailTransferOnNotEnoughMoney() {
        //Given
        doThrow(new AccountDoNotHaveEnoughMoneyException(TestHelper.TEST_UUID)).when(transactionService)
                .performTransaction(TestHelper.TEST_TRANSFER.getAccountFromId(),
                        TestHelper.TEST_TRANSFER.getAccountToId(),
                        TestHelper.TEST_TRANSFER.getAmount());

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TestHelper.TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class))
                .hasMessage(BAD_REQUEST.getReason());
    }

    @Test
    public void testEmptyAccountIdInRequest() {
        //Given
        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TestHelper.TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class))
                .hasMessage(BAD_REQUEST.getReason());
    }
}
