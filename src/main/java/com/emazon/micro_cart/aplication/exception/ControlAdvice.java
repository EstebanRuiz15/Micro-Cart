package com.emazon.micro_cart.aplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.emazon.micro_cart.domain.exception.ErrorExceptionCategoriesRepit;
import com.emazon.micro_cart.domain.exception.ErrorExceptionConflict;
import com.emazon.micro_cart.domain.exception.ErrorExceptionQuantity;
import com.emazon.micro_cart.domain.exception.ErrorFeignException;
import com.emazon.micro_cart.domain.exception.ErrorNotFoudArticle;
import com.emazon.micro_cart.domain.exception.ErrorNotFoundArticleToDelete;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControlAdvice {

    @ExceptionHandler(ErrorNotFoudArticle.class)
    public ResponseEntity<ExceptionResponse> handleErrorNotFoundArticle(ErrorNotFoudArticle ex, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "";
        if (ex.getMessage() != null && ex.getMessage().contains(":")) {
            String[] parts = ex.getMessage().split(":");
            if (parts.length > 1) {
                errorMessage = parts[1].trim();
            }
        }

        ExceptionResponse response = new ExceptionResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Validation failed");
        response.setDetails(errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorExceptionQuantity.class)
    public ResponseEntity<?> resourceNotFoundException(ErrorExceptionQuantity ex, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ErrorExceptionCategoriesRepit.class)
    public ResponseEntity<?> categoriesRepeatError(ErrorExceptionCategoriesRepit ex, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ErrorFeignException.class)
    public ResponseEntity<?> handleErrorFeignException(ErrorFeignException ex, WebRequest request) {

        ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = "";
        if (ex.getMessage() != null && ex.getMessage().contains(":")) {
            String[] parts = ex.getMessage().split(":");
            if (parts.length > 1) {
                errorMessage = parts[1].trim();
            }
        }

        ExceptionResponse response = new ExceptionResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Validation failed");
        response.setDetails(errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorNotFoundArticleToDelete.class)
    public ResponseEntity<?> ErrorNotArticleToDelete(ErrorNotFoundArticleToDelete ex, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ErrorExceptionConflict.class)
    public ResponseEntity<?> ErrorExceptionConflic(ErrorExceptionConflict ex, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
