package com.solutis.helpdesk.service.ticket.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateTicketData(
        UUID technicianId,

        @NotNull
        UUID customerId,

        @NotBlank
        @Size(max = 50)
        String title,

        @NotBlank
        @Size(max = 250)
        String description,

        @NotNull
        TicketPriorityData priority,

        @NotNull
        TicketStatusData status,

        @NotNull
        TicketCategoryData category
) {
}
