package net.sinsengumi.feedich.infrastructure;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String code;
    private String message;

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse build(String code, String message) {
        return new ErrorResponse(code, message);
    }
}