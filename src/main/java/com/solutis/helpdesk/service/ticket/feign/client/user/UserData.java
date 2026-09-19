package com.solutis.helpdesk.service.ticket.feign.client.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserData(
        @NotNull
        UUID id,

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotNull
        RoleData role,

        @NotNull
        Boolean active,

        @NotNull
        LocalDateTime createdAt
) {
}
