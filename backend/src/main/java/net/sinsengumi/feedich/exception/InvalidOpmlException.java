package net.sinsengumi.feedich.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOpmlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidOpmlException(String message) {
        super(message);
    }

    public InvalidOpmlException(Throwable cause) {
        super(cause);
    }

    public InvalidOpmlException(String message, Throwable cause) {
        super(message, cause);
    }
}