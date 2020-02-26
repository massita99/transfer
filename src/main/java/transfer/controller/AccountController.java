package transfer.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import transfer.dto.AccountData;
import transfer.model.Account;
import transfer.service.AccountService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller("/api/accounts")
public class AccountController {

    @Inject
    private AccountService accountService;

    @Inject
    private ModelMapper modelMapper;

    @Get
    @Produces
    @Operation(summary = "Get all accounts",
            description = "Receive all accounts stored in repository"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Array of accounts",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountData.class))
                    )
            )
    })
    @Tag(name = "account")
    public Collection<AccountData> getAllAccounts() {
        return mapToDto(accountService.getAll());
    }

    @Get(uri = "/{accountId}")
    @Produces
    @Operation(summary = "Get account by id",
            description = "Retrieve account by id stored in DataBase"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Requested accounts",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            )
    })
    @Tag(name = "account")
    public HttpResponse<AccountData> getAccountById(@Parameter(description = "ID of account") String accountId) {
        var account = mapToDto(accountService.getById(accountId));
        return HttpResponse.ok(account);
    }

    @Put
    @Consumes
    @Produces
    @Operation(summary = "Create new account",
            description = "Create new account with empty balance"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountData.class)
                    )
            )
    })
    @Tag(name = "account")
    public HttpResponse<AccountData> createAccount() {
        var newAccount = mapToDto(accountService.create());
        return HttpResponse.ok(newAccount);
    }

    private AccountData mapToDto(Account account) {
        return modelMapper.map(account, AccountData.class);
    }

    private Collection<AccountData> mapToDto(Collection<Account> accounts) {
        return accounts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
