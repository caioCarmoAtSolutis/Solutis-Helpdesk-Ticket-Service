package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Ticket;

import java.util.UUID;

public record ListTicketData(
        UUID id,
        UUID customerId,
        UUID technicianId,
        String title,
        TicketPriorityData priority,
        TicketStatusData status,
        TicketCategoryData category
) {
    public ListTicketData(Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getTechnicianId(),
                ticket.getTitle(),
                new TicketPriorityData(ticket.getPriority()),
                new TicketStatusData(ticket.getStatus()),
                new TicketCategoryData(ticket.getCategory())
        );
    }
}
