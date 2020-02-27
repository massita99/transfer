package transfer.controller.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import transfer.model.exception.TransactionNotExistException;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {TransactionNotExistException.class, ExceptionHandler.class})
public class TransactionNotExistExceptionHandler implements ExceptionHandler<TransactionNotExistException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, TransactionNotExistException exception) {
        return HttpResponse.notFound(exception.getMessage());
    }
}