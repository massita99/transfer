package transfer.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import transfer.model.Account;
import transfer.service.AccountService;

import javax.inject.Inject;
import java.util.Collection;

@Controller("api/accounts")
public class AccountController {

    @Inject
    private AccountService accountService;

    @Get
    @Produces
    public Collection<Account> getAllAccounts() {
        return accountService.getAll();
    }

    @Get(uri = "/{accountId}")
    @Produces
    public HttpResponse<Account> getAllAccounts(@Body String accountId) {
        var account = accountService.getById(accountId);
        return HttpResponse.ok(account);
    }

    @Put
    @Consumes
    @Produces
    public HttpResponse<Account> createAccount() {
        return HttpResponse.ok(accountService.create());
    }


}
