package com.solutis.helpdesk.service.ticket.infrastructure.exception;

import jakarta.validation.constraints.NotBlank;

public record ExceptionMessage(
        @NotBlank
        String message
) {
}
