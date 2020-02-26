package transfer.model.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String request) {
        super(String.format("Request data:'%s' is invalid", request));
    }
}
