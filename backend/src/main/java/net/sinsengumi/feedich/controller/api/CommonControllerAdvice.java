package net.sinsengumi.feedich.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.exception.UserViewException;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.http.ErrorResponse;
import net.sinsengumi.feedich.service.ErrorService;
import net.sinsengumi.feedich.util.AppUtil;

@Slf4j
@ControllerAdvice(basePackageClasses = CommonControllerAdvice.class)
@AllArgsConstructor
public class CommonControllerAdvice {

    private final ErrorService errorService;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleError(Throwable e, HttpServletRequest request,
            HttpServletResponse response, @AuthenticationPrincipal FeedichOAuth2User user) {
        String errorCode = AppUtil.createErrorCode();

        errorService.error(errorCode, e, request, user);

        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus httpStatus = responseStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : responseStatus.value();
        String userMessage = null;
        if (e instanceof UserViewException) {
            userMessage = ((UserViewException) e).getUserMessage();
        } else {
            userMessage = "サーバーエラーが発生しました";
        }

        return new ResponseEntity<ErrorResponse>(ErrorResponse.build(errorCode, userMessage), httpStatus);
    }
}
