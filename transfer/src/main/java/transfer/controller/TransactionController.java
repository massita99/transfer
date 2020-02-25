package transfer.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.netty.util.internal.StringUtil;
import transfer.dto.TransferData;
import transfer.model.Transaction;
import transfer.model.exception.BadRequestException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;

@Controller("api/transactions")
public class TransactionController {

    public static final String ACCOUNT_ID_PARAMETER = "accountId";
    @Inject
    TransactionService transactionService;

    @Get
    @Produces
    public Collection<Transaction> getAllTransactions(HttpRequest<?> request) {
        String accountId = request.getParameters()
                .getFirst(ACCOUNT_ID_PARAMETER)
                .orElse(null);
        if (accountId == null) {
            return transactionService.getAll();
        }
        return transactionService.getAllByAccountId(accountId);
    }

    @Post
    @Produces
    @Consumes
    public HttpStatus performTransfer(@Body TransferData data) {

        if (checkRequest(data)) {
            throw new BadRequestException(data.toString());
        }
        transactionService.performTransaction(data.getAccountFromId(), data.getAccountToId(), data.getAmount());
        return HttpStatus.OK;
    }

    private boolean checkRequest(@Body TransferData data) {
        return StringUtil.isNullOrEmpty(data.getAccountFromId())
                || StringUtil.isNullOrEmpty(data.getAccountToId())
                || data.getAmount().compareTo(BigDecimal.ZERO) < 0;
    }

}
