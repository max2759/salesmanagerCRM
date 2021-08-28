package be.atc.salesmanagercrm.exceptions;

import lombok.Getter;

public class AccessDeniedException extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(String message, Throwable cause, ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }

    public AccessDeniedException(String message, ErrorCodes errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }
}
