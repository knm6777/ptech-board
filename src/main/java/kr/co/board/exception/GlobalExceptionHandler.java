package kr.co.board.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected String handleCustomException(final CustomException e, Model model) {
        log.error("handleCustomException: {}", e.getErrorCode());
        model.addAttribute("errorCode", e.getErrorCode());

        return "app/error/error";
    }
}