package kr.co.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleCustomException(final CustomException e, Model model) {
        log.error("handleCustomException: {}", e.getErrorCode());

        ErrorResponse res = new ErrorResponse(e.getErrorCode());
        model.addAttribute("errorCode", res);

        return "app/error/error";
    }

}