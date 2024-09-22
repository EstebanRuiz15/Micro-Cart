package com.emazon.micro_cart.aplication.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.emazon.micro_cart.domain.exception.ErrorExceptionCategoriesRepit;
import com.emazon.micro_cart.domain.exception.ErrorExceptionQuantity;
import com.emazon.micro_cart.domain.exception.ErrorFeignException;
import com.emazon.micro_cart.domain.exception.ErrorNotFoudArticle;
import com.emazon.micro_cart.domain.util.ConstantsDomain;

@ControllerAdvice
public class ControlAdvice {

    @ExceptionHandler(ErrorNotFoudArticle.class)
    public ResponseEntity<ExceptionResponse> handleErrorNotFoundArticle(ErrorNotFoudArticle ex, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ConstantsDomain.ERROR_VALIDATION,
                errors.toString());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
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
}

