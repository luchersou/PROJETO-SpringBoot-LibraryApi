package io.project.libraryapi.controller.common;

import io.project.libraryapi.controller.dto.FieldError;
import io.project.libraryapi.controller.dto.ResponseError;
import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.exceptions.InvalidFieldException;
import io.project.libraryapi.exceptions.NotAllowedOperationException;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Spring automatically validates DTO fields annotated with @Valid using Hibernate Validator and throws MethodArgumentNotValidException if constraints are violated.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<org.springframework.validation.FieldError> springErrors = e.getFieldErrors();

        List<FieldError> errorList = springErrors
                .stream()
                .map(fe -> new io.project.libraryapi.controller.dto.FieldError(
                        fe.getField(),
                        fe.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ResponseError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                errorList
        );
    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleDuplicateRecordException(DuplicateRecordException e){
        return ResponseError.conflict(e.getMessage());
    }

    @ExceptionHandler(NotAllowedOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNotAllowedOperationException(
            NotAllowedOperationException e){
        return ResponseError.defaultResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleInvalidFieldException(InvalidFieldException e){
        return new ResponseError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error.",
                List.of(new FieldError(e.getField(), e.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleAccessDeniedException(AccessDeniedException e){
        return new ResponseError(HttpStatus.FORBIDDEN.value(), "Access Denied.", List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleUntreatedErrors(RuntimeException e){
        System.out.println(e.getMessage());
        return new ResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error has occurred. Please contact management."
                , List.of());
    }
}