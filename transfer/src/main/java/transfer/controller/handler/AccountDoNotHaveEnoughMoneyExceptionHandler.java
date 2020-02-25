package transfer.controller.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import transfer.model.exception.AccountDoNotHaveEnoughMoneyException;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {AccountDoNotHaveEnoughMoneyException.class, ExceptionHandler.class})
public class AccountDoNotHaveEnoughMoneyExceptionHandler implements ExceptionHandler<AccountDoNotHaveEnoughMoneyException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, AccountDoNotHaveEnoughMoneyException exception) {
        return HttpResponse.badRequest(exception.getMessage());
    }
}