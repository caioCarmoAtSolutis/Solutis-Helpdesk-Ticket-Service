package com.solutis.helpdesk.service.ticket.repository;

import com.solutis.helpdesk.service.ticket.domain.model.Category;
import com.solutis.helpdesk.service.ticket.domain.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory,Long> {
    Optional<TicketCategory> findByCategory(Category category);
}
