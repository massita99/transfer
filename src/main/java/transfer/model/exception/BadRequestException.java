package transfer.model.exception;

/**
 * Class <class>BadRequestException</class> describe exception requested data is not fit for operation
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String request) {
        super(String.format("Request data:'%s' is invalid", request));
    }
}
