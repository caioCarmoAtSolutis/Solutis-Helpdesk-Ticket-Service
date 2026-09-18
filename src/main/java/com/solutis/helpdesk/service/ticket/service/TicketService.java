package com.solutis.helpdesk.service.ticket.service;

import com.solutis.helpdesk.service.ticket.domain.dto.*;
import com.solutis.helpdesk.service.ticket.domain.model.*;
import com.solutis.helpdesk.service.ticket.repository.TicketCategoryRepository;
import com.solutis.helpdesk.service.ticket.repository.TicketPriorityRepository;
import com.solutis.helpdesk.service.ticket.repository.TicketRepository;
import com.solutis.helpdesk.service.ticket.repository.TicketStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;
    @Autowired
    private TicketPriorityRepository ticketPriorityRepository;
    @Autowired
    private TicketStatusRepository ticketStatusRepository;


    public DetailedTicketData createTicket(TicketData data) {
        validateCustomer(data.customerId());
        TicketPriority priority = getPriority(data.priority().priority());
        TicketCategory category = getCategory(data.category().category());
        Ticket ticket = new Ticket(data, priority, category);
        ticket = ticketRepository.save(ticket);
        return new DetailedTicketData(ticket);
    }

    public ListTicketData getTicketById(UUID id) {
        Ticket ticket = getTicket(id);
        return new ListTicketData(ticket);
    }

    public Page<ListTicketData> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).map(ListTicketData::new);
    }

    public Page<ListTicketData> getTicketsWithSpecificCustomerId(Pageable pageable, UUID customerId) {
        validateCustomer(customerId);
        return ticketRepository.findAllByCustomerId(pageable, customerId).map(ListTicketData::new);
    }

    public DetailedTicketData updatedTicket(UUID id, TicketPriorityData data) {
        Ticket ticket = getTicket(id);
        TicketPriority priority = getPriority(data.priority());
        ticket.setPriority(priority);
        ticket = ticketRepository.save(ticket);
        return new DetailedTicketData(ticket);
    }

    public DetailedTicketData updatedTicket(UUID id, TicketStatusData data) {
        Ticket ticket = getTicket(id);
        TicketStatus status = getStatus(data.status());
        ticket.setStatus(status);
        ticket = ticketRepository.save(ticket);
        return new DetailedTicketData(ticket);
    }

    public DetailedTicketData updatedTicket(UUID id, TicketCategoryData data) {
        Ticket ticket = getTicket(id);
        TicketCategory category = getCategory(data.category());
        ticket.setCategory(category);
        ticket = ticketRepository.save(ticket);
        return new DetailedTicketData(ticket);
    }

    public DetailedTicketData updatedTicket(UUID id, AssignTechnicianData data) {
        Ticket ticket = getTicket(id);
        validateTechnician(data.technicianId());
        ticket.setTechnicianId(data.technicianId());
        ticket = ticketRepository.save(ticket);
        return new DetailedTicketData(ticket);
    }

    public DetailedTicketData closeTicket(UUID id) {
        Ticket ticket = getTicket(id);
        ticket.setStatus(getStatus(Status.CLOSED));
        ticket = ticketRepository.save(ticket);
        return new DetailedTicketData(ticket);
    }

    public void deleteTicket(UUID id) {
        getTicket(id);
        ticketRepository.deleteById(id);
    }

    private Ticket getTicket(UUID id) {
        Optional<Ticket> optional = ticketRepository.findById(id);
        if (optional.isEmpty())
            throw new EntityNotFoundException("Ticket with id " + id.toString() + " doesn't exist!");
        return optional.get();
    }

    private TicketPriority getPriority(Priority priority) {
        Optional<TicketPriority> optional = ticketPriorityRepository.findByPriority(priority);
        if (optional.isEmpty())
            throw new EntityNotFoundException("Priority with value " + priority.toString() + " doesn't exist!");
        return optional.get();
    }

    private TicketStatus getStatus(Status status) {
        Optional<TicketStatus> optional = ticketStatusRepository.findByStatus(status);
        if (optional.isEmpty())
            throw new EntityNotFoundException("Status with value " + status.toString() + " doesn't exist!");
        return optional.get();
    }

    private TicketCategory getCategory(Category category) {
        Optional<TicketCategory> optional = ticketCategoryRepository.findByCategory(category);
        if (optional.isEmpty())
            throw new EntityNotFoundException("Category with value " + category.toString() + " doesn't exist!");
        return optional.get();
    }

    private void validateCustomer(UUID customerId) {
    }

    private void validateTechnician(UUID technicianId) {
    }
}
