package com.solutis.helpdesk.service.ticket.repository;

import com.solutis.helpdesk.service.ticket.domain.model.Priority;
import com.solutis.helpdesk.service.ticket.domain.model.TicketPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketPriorityRepository extends JpaRepository<TicketPriority, Long> {
    TicketPriority findByPriority(Priority priority);
}
