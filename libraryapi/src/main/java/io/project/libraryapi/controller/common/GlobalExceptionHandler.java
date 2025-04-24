package io.project.libraryapi.controller.common;

import io.project.libraryapi.controller.dto.FieldError;
import io.project.libraryapi.controller.dto.ResponseError;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
}