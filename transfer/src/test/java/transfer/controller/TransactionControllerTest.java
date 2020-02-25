package transfer.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import transfer.dto.TransferData;
import transfer.model.Transaction;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;
import transfer.model.exception.AccountNotExistException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

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
        assertThatThrownBy(() -> client.toBlocking().exchange(request, Collection.class));
    }

    @Test
    public void testGoodTransfer() {
        //Given
        var goodTransferData = new TransferData();
        goodTransferData.setAccountFromId(TEST_UUID);
        goodTransferData.setAccountToId(TEST_UUID);
        goodTransferData.setAmount(BigDecimal.TEN);
        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", goodTransferData);

        //When
        var response = client.toBlocking().exchange(request, String.class);

        //Then
        verify(transactionService, times(1)).performTransaction(goodTransferData.getAccountFromId(),
                goodTransferData.getAccountToId(), goodTransferData.getAmount());
    }

    @Test
    public void testFailTransferOnAccountNotExist() {
        //Given
        doThrow(new AccountNotExistException(TEST_UUID)).when(transactionService).performTransaction(TEST_TRANSFER.getAccountFromId(),
                TEST_TRANSFER.getAccountToId(), TEST_TRANSFER.getAmount());

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class));

    }

    @Test
    public void testFailTransferOnNotEnoughMoney() {
        //Given
        doThrow(new AccountDoNotHaveEnoughMoneyException(TEST_UUID)).when(transactionService).performTransaction(TEST_TRANSFER.getAccountFromId(),
                TEST_TRANSFER.getAccountToId(), TEST_TRANSFER.getAmount());

        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class));
    }

    @Test
    public void testEmptyAccountIdInRequest() {
        //Given
        var badTransferData = new TransferData();
        HttpRequest<TransferData> request = HttpRequest.POST("/api/transactions", TEST_TRANSFER);

        //When
        assertThatThrownBy(() -> client.toBlocking().exchange(request, String.class));
    }
}
