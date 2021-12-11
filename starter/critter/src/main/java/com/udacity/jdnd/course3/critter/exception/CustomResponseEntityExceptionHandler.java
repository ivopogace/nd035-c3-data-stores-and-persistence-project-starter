package com.udacity.jdnd.course3.critter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public void handleEntityNotFoundException(EntityNotFoundException e, HttpServletResponse response) throws IOException {
        log.error(e.getMessage());
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void handleConstraintValidationException(ConstraintViolationException e, HttpServletResponse response) throws IOException {
        log.error(e.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @ExceptionHandler
    public void handleEntityExistsException(EntityExistsException e, HttpServletResponse response) throws IOException {
        log.error(e.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @ExceptionHandler
    public void handleValidationException(ValidationException e, HttpServletResponse response) throws IOException {
        log.error(e.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
