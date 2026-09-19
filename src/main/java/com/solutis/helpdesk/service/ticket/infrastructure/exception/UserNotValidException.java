package com.solutis.helpdesk.service.ticket.infrastructure.exception;

import java.util.UUID;

public class UserNotValidException extends RuntimeException {
    public UserNotValidException(UUID userId) {
        super("User with id: " + userId + " is not valid!");
    }
}
