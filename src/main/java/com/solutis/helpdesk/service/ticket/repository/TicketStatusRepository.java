package com.solutis.helpdesk.service.ticket.repository;

import com.solutis.helpdesk.service.ticket.domain.model.Status;
import com.solutis.helpdesk.service.ticket.domain.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {
    Optional<TicketStatus> findByStatus(Status status);
}
