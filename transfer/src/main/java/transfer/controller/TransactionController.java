package transfer.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import transfer.dto.TransactionData;
import transfer.dto.TransferData;
import transfer.model.Transaction;
import transfer.model.exception.BadRequestException;
import transfer.service.TransactionService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/api/transactions")
public class TransactionController {

    @Inject
    TransactionService transactionService;

    @Inject
    private ModelMapper modelMapper;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Get("/{?accountId}")
    @Produces
    @Operation(summary = "Get all transactions",
            description = "Receive all or all account transactions stored in repository"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Array of transactions",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionData.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            )
    })
    @Tag(name = "transaction")
    public Collection<TransactionData> getAllTransactions(@Parameter(description = "ID of account") Optional<String> accountId) {

        if (accountId.isEmpty()) {
            return mapToDto(transactionService.getAll());
        }
        return mapToDto(transactionService.getAllByAccountId(accountId.get()));
    }

    @Post
    @Produces
    @Consumes
    @Operation(summary = "Perform money transfer",
            description = "Perform money transfer between two accounts"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction successfully performed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request day is wrong: whether accountIds is empty, account have not enough money" +
                            " or amount is smaller than 0"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Another transaction performed on account, try later"
            )
    })
    @Tag(name = "transfer")
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

    private TransactionData mapToDto(Transaction transaction) {
        return modelMapper.map(transaction, TransactionData.class);
    }

    private Collection<TransactionData> mapToDto(Collection<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
