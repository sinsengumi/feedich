package net.sinsengumi.feedich.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends UserViewException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message, String userMessage) {
        super(message, userMessage);
    }

    public NotFoundException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }

    public NotFoundException(String message, String userMessage, Throwable cause) {
        super(message, userMessage, cause);
    }
}