package net.sinsengumi.feedich.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOpmlException extends UserViewException {

    private static final long serialVersionUID = 1L;

    public InvalidOpmlException(String message, String userMessage) {
        super(message, userMessage);
    }

    public InvalidOpmlException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }

    public InvalidOpmlException(String message, String userMessage, Throwable cause) {
        super(message, userMessage, cause);
    }
}