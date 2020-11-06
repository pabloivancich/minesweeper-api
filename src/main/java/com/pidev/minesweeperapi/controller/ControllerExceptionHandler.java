package com.pidev.minesweeperapi.controller;

import com.pidev.minesweeperapi.controller.dto.ErrorResponse;
import com.pidev.minesweeperapi.exception.InvalidCellException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.ResponseEntity.status;

/**
 * Controller advise that provides global exception handling and returns a standard error response.
 */
@ControllerAdvice
public class ControllerExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler({InvalidCellException.class})
    public ResponseEntity<ErrorResponse> handleException(InvalidCellException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Creates an Error Response Entity with the status and a message related to the error.
     * @param status the HTTP status.
     * @param message a message.
     * @return a Response Entity with a ErrorResponse body.
     */
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message) {
        return status(status).body(new ErrorResponse(status.value(), status.getReasonPhrase(), message));
    }
}
