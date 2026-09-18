package com.solutis.helpdesk.service.ticket.repository;

import com.solutis.helpdesk.service.ticket.domain.model.Priority;
import com.solutis.helpdesk.service.ticket.domain.model.TicketPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketPriorityRepository extends JpaRepository<TicketPriority, Long> {
    Optional<TicketPriority> findByPriority(Priority priority);
}
