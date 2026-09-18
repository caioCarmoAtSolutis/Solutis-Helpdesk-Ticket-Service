package com.solutis.helpdesk.service.ticket.repository;

import com.solutis.helpdesk.service.ticket.domain.model.Status;
import com.solutis.helpdesk.service.ticket.domain.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {
    TicketStatus findByStatus(Status status);
}
