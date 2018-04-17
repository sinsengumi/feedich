package net.sinsengumi.feedich.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.http.ErrorResponse;
import net.sinsengumi.feedich.util.AppUtil;

@Slf4j
@ControllerAdvice(basePackageClasses = CommonControllerAdvice.class)
public class CommonControllerAdvice {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleError(Throwable e, HttpServletResponse response) {
        String errorCode = AppUtil.createErrorCode();
        log.error("[{}] {}", errorCode, e.getMessage(), e);

        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            response.setStatus(responseStatus.value().value());
        }

        return ErrorResponse.build(errorCode, e);
    }
}
