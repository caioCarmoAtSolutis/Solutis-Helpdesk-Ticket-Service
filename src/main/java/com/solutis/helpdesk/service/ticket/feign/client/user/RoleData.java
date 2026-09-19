package com.solutis.helpdesk.service.ticket.feign.client.user;

import jakarta.validation.constraints.NotNull;

public record RoleData(
        @NotNull
        Role value
) {
}
