package com.solutis.helpdesk.service.ticket.domain.dto;

import com.solutis.helpdesk.service.ticket.domain.model.Category;
import com.solutis.helpdesk.service.ticket.domain.model.TicketCategory;
import jakarta.validation.constraints.NotNull;

public record TicketCategoryData(
        @NotNull
        Category category
) {
    public TicketCategoryData(TicketCategory ticketCategory) {
        this(ticketCategory.getCategory());
    }
}
