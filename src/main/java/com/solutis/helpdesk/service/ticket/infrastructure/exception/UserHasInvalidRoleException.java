package com.solutis.helpdesk.service.ticket.infrastructure.exception;

public class UserHasInvalidRoleException extends RuntimeException {
    public UserHasInvalidRoleException(String message) {
        super(message);
    }
}
