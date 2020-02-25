package transfer.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.modelmapper.ModelMapper;
import transfer.dao.AccountDao;
import transfer.dto.AccountData;
import transfer.model.Account;
import transfer.service.AccountService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller("api/accounts")
public class AccountController {

    @Inject
    private AccountService accountService;

    @Inject
    private ModelMapper modelMapper;

    @Get
    @Produces
    public Collection<AccountData> getAllAccounts() {
        return mapToDto(accountService.getAll());
    }

    @Get(uri = "/{accountId}")
    @Produces
    public HttpResponse<AccountData> getAllAccounts(@Body String accountId) {
        var account = mapToDto(accountService.getById(accountId));
        return HttpResponse.ok(account);
    }

    @Put
    @Consumes
    @Produces
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
