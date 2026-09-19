package com.solutis.helpdesk.service.ticket.infrastructure.exception;

public class UserIsInactiveException extends RuntimeException {
    public UserIsInactiveException(String message) {
        super(message);
    }
}
