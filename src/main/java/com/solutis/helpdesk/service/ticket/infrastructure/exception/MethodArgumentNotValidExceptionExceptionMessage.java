package com.solutis.helpdesk.service.ticket.infrastructure.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.ObjectError;

import java.util.List;

public record MethodArgumentNotValidExceptionExceptionMessage(
        @NotBlank
        String message,
        @NotNull
        List<ObjectError> allErrors
) {
}
