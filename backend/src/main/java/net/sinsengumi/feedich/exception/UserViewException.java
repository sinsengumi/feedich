package net.sinsengumi.feedich.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserViewException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String userMessage;

    public UserViewException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public UserViewException(String userMessage, Throwable cause) {
        super(cause);
        this.userMessage = userMessage;
    }

    public UserViewException(String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}