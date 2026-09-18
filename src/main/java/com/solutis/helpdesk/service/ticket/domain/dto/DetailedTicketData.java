package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

public record DetailedTicketData(
        UUID id,
        UUID technicianId,
        UUID customerId,
        String title,
        String description,
        TicketPriorityData priority,
        TicketStatusData status,
        TicketCategoryData category,
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
