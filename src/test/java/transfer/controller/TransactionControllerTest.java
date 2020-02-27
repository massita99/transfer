package transfer.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import transfer.dto.TransactionData;
import transfer.dto.TransferData;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;
import transfer.model.exception.AccountNotExistException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static io.micronaut.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@MicronautTest
public class TransactionControllerTest {

    public static final Transaction TEST_TRANSACTION = new Transaction();
    public static final TransferData TEST_TRANSFER = new TransferData();
    public static final String TEST_UUID = "A-A-A-A-A";

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
    public void testGetAllTransactions() {
        //Given
        when(transactionService.getAll())
                .thenReturn(List.of(TEST_TRANSACTION));
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions");

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.body().size()).isEqualTo(1);

    }

    @Test
    public void testGetAllTransactionsByAccountId() {
        //Given
        when(transactionService.getAllByAccountId(TEST_UUID))
                .thenReturn(List.of(TEST_TRANSACTION));
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions?accountId=" + TEST_UUID);

        //When
        var response = client.toBlocking().exchange(request, Collection.class);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.body().size()).isEqualTo(1);
    }

    @Test
    public void testFailOnGetAllTransactionsByNotExistedAccountId() {
        //Given
        when(transactionService.getAllByAccountId(TEST_UUID))
                .thenThrow(new AccountNotExistException(TEST_UUID));
        HttpRequest<Transaction> request = HttpRequest.GET("/api/transactions?accountId=" + TEST_UUID);

        //When Then
        assertThatThrownBy(() -> client.toBlocking().exchange(request, Collection.class))
                .hasMessage(NOT_FOUND.getReason());
    }

    @Test
    public void testGoodTransfer() {
        //Given
        var goodTransferData = new TransferData();
        goodTransferData.setAccountFromId(TEST_UUID);
        goodTransferData.setAccountToId(TEST_UUID);
        goodTransferData.setAmount(BigDecimal.TEN);
        var goodTransactionData = new Transaction(TEST_UUID, TEST_UUID, BigDecimal.TEN);
        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", goodTransferData);
        when(transactionService.performTransaction(TEST_UUID, TEST_UUID, BigDecimal.TEN))
                .thenReturn(goodTransactionData);

        //When
        var transaction = client.toBlocking().exchange(request, TransactionData.class);

        //Then
        assertThat(transaction.getBody().get().getAmount()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void testFailTransferOnAccountNotExist() {
        //Given
        doThrow(new AccountNotExistException(TEST_UUID)).when(transactionService).performTransaction(
                TEST_TRANSFER.getAccountFromId(),
                TEST_TRANSFER.getAccountToId(),
                TEST_TRANSFER.getAmount());

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, Void.class))
                .hasMessage(BAD_REQUEST.getReason());

    }

    @Test
    public void testFailTransferOnNotEnoughMoney() {
        //Given
        doThrow(new AccountDoNotHaveEnoughMoneyException(TEST_UUID)).when(transactionService).performTransaction(TEST_TRANSFER.getAccountFromId(),
                TEST_TRANSFER.getAccountToId(), TEST_TRANSFER.getAmount());

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class))
                .hasMessage(BAD_REQUEST.getReason());
    }

    @Test
    public void testEmptyAccountIdInRequest() {
        //Given
        var badTransferData = new TransferData();
        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class))
                .hasMessage(BAD_REQUEST.getReason());
    }
}
