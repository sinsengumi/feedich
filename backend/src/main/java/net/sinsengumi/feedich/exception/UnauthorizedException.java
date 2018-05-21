package net.sinsengumi.feedich.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends UserViewException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message, String userMessage) {
        super(message, userMessage);
    }

    public UnauthorizedException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }

    public UnauthorizedException(String message, String userMessage, Throwable cause) {
        super(message, userMessage, cause);
    }
}