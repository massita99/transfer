package transfer.controller.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import transfer.model.exception.LockException;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {LockException.class, ExceptionHandler.class})
public class LockExceptionHandler implements ExceptionHandler<LockException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, LockException exception) {
        return HttpResponse.serverError(exception.getMessage());
    }
}