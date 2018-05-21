package net.sinsengumi.feedich.model.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;

    public static ErrorResponse build(String code, String message) {
        return new ErrorResponse(code, message);
    }
}