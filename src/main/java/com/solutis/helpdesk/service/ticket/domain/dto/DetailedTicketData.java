package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record DetailedTicketData(
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
        TicketCategoryData category,

        @NotNull
        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
    public DetailedTicketData(Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getTechnicianId(),
                ticket.getCustomerId(),
                ticket.getTitle(),
                ticket.getDescription(),
                new TicketPriorityData(ticket.getPriority()),
                new TicketStatusData(ticket.getStatus()),
                new TicketCategoryData(ticket.getCategory()),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}

