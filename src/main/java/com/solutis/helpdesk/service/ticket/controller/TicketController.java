package com.solutis.helpdesk.service.ticket.controller;


import com.solutis.helpdesk.service.ticket.service.TicketService;
import com.solutis.helpdesk.service.ticket.domain.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<DetailedTicketData> createTicket(@Valid @RequestBody TicketData data, UriComponentsBuilder uriComponentsBuilder) {
        DetailedTicketData createdTicket = ticketService.createTicket();
        URI location = uriComponentsBuilder.path("/tickets/{id}").buildAndExpand(createdTicket.id()).toUri();
        return ResponseEntity.created(location).body(createdTicket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListTicketData> getTicketById(@PathVariable UUID id) {
        ListTicketData ticket = ticketService.getTicket(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<Page<ListTicketData>> getAllTickets(@PageableDefault(size = 10) Pageable pageable) {
        Page<ListTicketData> page = ticketService.getAllTickets(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ListTicketData>> getTicketsWithSpecificCustomerId(@PageableDefault(size = 10) Pageable pageable, @PathVariable UUID customerId) {
        Page<ListTicketData> page = ticketService.getTicketsWithSpecificCustomerId(pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<DetailedTicketData> changeTicketPriority(@PathVariable UUID id, @Valid @RequestBody TicketPriorityData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DetailedTicketData> changeTicketStatus(@PathVariable UUID id, @Valid @RequestBody TicketStatusData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<DetailedTicketData> changeTicketCategory(@PathVariable UUID id, @Valid @RequestBody TicketCategoryData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @PatchMapping("/{id}/technician")
    public ResponseEntity<DetailedTicketData> assignTechnician(@PathVariable UUID id, @Valid @RequestBody AssignTechnicianData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<DetailedTicketData> closeTicket(@PathVariable UUID id) {
        DetailedTicketData updatedTicket = ticketService.closeTicket(id);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
