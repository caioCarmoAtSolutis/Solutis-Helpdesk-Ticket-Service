package com.solutis.helpdesk.service.ticket.infrastructure.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketServiceExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleEntityNotFoundException(EntityNotFoundException e) {
        var message = new ExceptionMessage(e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionExceptionMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var message = new MethodArgumentNotValidExceptionExceptionMessage(e.getMessage(), e.getAllErrors());
        return ResponseEntity.badRequest().body(message);
    }
    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<ExceptionMessage> handleUserNotValidException(UserNotValidException e) {
        var message = new ExceptionMessage(e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }
}
