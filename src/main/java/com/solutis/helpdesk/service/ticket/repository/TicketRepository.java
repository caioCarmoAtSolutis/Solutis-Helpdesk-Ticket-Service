package com.solutis.helpdesk.service.ticket.repository;

import com.solutis.helpdesk.service.ticket.domain.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findAllByCustomerId(Pageable pageable, UUID customerId);
}
