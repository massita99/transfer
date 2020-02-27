package integration;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import transfer.TestHelper;
import transfer.dto.AccountData;
import transfer.dto.TransactionData;
import transfer.dto.TransferData;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class IntegrationTest {

    @Inject
    @Client("/")
    private RxHttpClient client;

    @Test
    public void createAccountTransferMoneyCheckIt() {
        //Create account
        var createRequest = HttpRequest.PUT("/api/accounts", "");
        var createResponse = client.toBlocking().exchange(createRequest, AccountData.class);
        AccountData accountData = createResponse.getBody().get();
        assertThat(accountData.getBalance()).isEqualTo(BigDecimal.ZERO);

        //Perform Transaction
        var transferData = new TransferData();
        transferData.setAccountFromId(TestHelper.TEST_UUID);
        transferData.setAccountToId(accountData.getId());
        transferData.setAmount(BigDecimal.TEN);
        var transferRequest = HttpRequest.POST("/api/transactions", transferData);
        var transferResponse = client.toBlocking().exchange(transferRequest, TransactionData.class);
        TransactionData transactionData = transferResponse.getBody().get();
        assertThat(transactionData.getAmount()).isEqualTo(BigDecimal.TEN);

        //Check account
        var checkAccountRequest = HttpRequest.GET("/api/accounts/" + accountData.getId());
        var checkAccountResponse = client.toBlocking().exchange(checkAccountRequest, AccountData.class);
        assertThat(checkAccountResponse.getBody().get().getBalance()).isCloseTo(BigDecimal.TEN, Percentage.withPercentage(0));

    }
}

    
    