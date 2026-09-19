package com.solutis.helpdesk.service.ticket.domain.model;

import com.solutis.helpdesk.service.ticket.domain.dto.TicketData;
import com.solutis.helpdesk.service.ticket.service.TicketService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TICKETS")
public class Ticket {
    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "TECHNICIAN_ID")
    private UUID technicianId;

    @Column(name = "CUSTOMER_ID")
    private UUID customerId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    private TicketPriority priority;

    @ManyToOne
    private TicketStatus status;

    @ManyToOne
    private TicketCategory category;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public Ticket(TicketData data, TicketPriority priority, TicketCategory category, TicketStatus status) {
        this.id = UUID.randomUUID();
        this.customerId = data.customerId();
        this.title = data.title();
        this.description = data.description();
        this.priority = priority;
        this.category = category;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
}
