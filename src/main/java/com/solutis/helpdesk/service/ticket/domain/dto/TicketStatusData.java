package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Status;
import com.solutis.helpdesk.service.ticket.domain.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketStatusData(
        @NotNull
        Status status
) {
    public TicketStatusData(TicketStatus ticketStatus) {
        this(ticketStatus.getStatus());
    }
}
