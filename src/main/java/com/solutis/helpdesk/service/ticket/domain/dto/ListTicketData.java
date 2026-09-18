package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ListTicketData(
        @NotNull
        UUID id,

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
    public ListTicketData(Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getTechnicianId(),
                ticket.getTitle(),
                ticket.getDescription(),
                new TicketPriorityData(ticket.getPriority()),
                new TicketStatusData(ticket.getStatus()),
                new TicketCategoryData(ticket.getCategory())
        );
    }
}

