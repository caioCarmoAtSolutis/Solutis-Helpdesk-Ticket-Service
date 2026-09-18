package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Priority;
import com.solutis.helpdesk.service.ticket.domain.model.TicketPriority;
import jakarta.validation.constraints.NotNull;

public record TicketPriorityData(
        @NotNull
        Priority priority
) {
    public TicketPriorityData(TicketPriority ticketPriority) {
        this(ticketPriority.getPriority());
    }
}

