package com.solutis.helpdesk.service.ticket.domain.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignTechnicianData(
        @NotNull
        UUID technicianId
) {
}
