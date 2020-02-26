package transfer.controller.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import transfer.model.exception.AccountNotExistException;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {AccountNotExistException.class, ExceptionHandler.class})
public class AccountNotExistExceptionHandler implements ExceptionHandler<AccountNotExistException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, AccountNotExistException exception) {
        return HttpResponse.notFound(exception.getMessage());
    }
}