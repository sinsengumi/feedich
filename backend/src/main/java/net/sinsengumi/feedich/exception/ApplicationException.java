package net.sinsengumi.feedich.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends UserViewException {

    private static final long serialVersionUID = 1L;

    public ApplicationException(String message, String userMessage) {
        super(message, userMessage);
    }

    public ApplicationException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }

    public ApplicationException(String message, String userMessage, Throwable cause) {
        super(message, userMessage, cause);
    }
}