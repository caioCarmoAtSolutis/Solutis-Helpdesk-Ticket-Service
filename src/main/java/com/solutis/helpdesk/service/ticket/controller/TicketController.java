package com.solutis.helpdesk.service.ticket.controller;


import com.solutis.helpdesk.service.ticket.infrastructure.exception.ExceptionMessage;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.MethodArgumentNotValidExceptionExceptionMessage;
import com.solutis.helpdesk.service.ticket.service.TicketService;
import com.solutis.helpdesk.service.ticket.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "Create Ticket")
    @Operation(summary = "Create new Ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New ticket created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DetailedTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Customer id is not valid",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @PostMapping
    public ResponseEntity<DetailedTicketData> createTicket(@Valid @RequestBody TicketData data, UriComponentsBuilder uriComponentsBuilder) {
        DetailedTicketData createdTicket = ticketService.createTicket(data);
        URI location = uriComponentsBuilder.path("/tickets/{id}").buildAndExpand(createdTicket.id()).toUri();
        return ResponseEntity.created(location).body(createdTicket);
    }

    @Tag(name = "List Ticket")
    @Operation(summary = "Get ticket by ticketId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket for specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ListTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @GetMapping("/{id}")
    public ResponseEntity<ListTicketData> getTicketById(@PathVariable UUID id) {
        ListTicketData ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @Tag(name = "List Ticket")
    @Operation(summary = "List all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List with all tickets",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ListTicketData.class)) }) })
    @GetMapping
    public ResponseEntity<Page<ListTicketData>> getAllTickets(@PageableDefault(size = 10) Pageable pageable) {
        Page<ListTicketData> page = ticketService.getAllTickets(pageable);
        return ResponseEntity.ok(page);
    }

    @Tag(name = "List Ticket")
    @Operation(summary = "Get tickets by customer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tickets for specified customer id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ListTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Customer id is not valid",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ListTicketData>> getTicketsWithSpecificCustomerId(@PageableDefault(size = 10) Pageable pageable, @PathVariable UUID customerId) {
        Page<ListTicketData> page = ticketService.getTicketsWithSpecificCustomerId(pageable, customerId);
        return ResponseEntity.ok(page);
    }

    @Tag(name = "Update Ticket")
    @Operation(summary = "Change priority for specific ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Priority changed successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DetailedTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @PatchMapping("/{id}/priority")
    public ResponseEntity<DetailedTicketData> changeTicketPriority(@PathVariable UUID id, @Valid @RequestBody TicketPriorityData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @Tag(name = "Update Ticket")
    @Operation(summary = "Change status for specific ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DetailedTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @PatchMapping("/{id}/status")
    public ResponseEntity<DetailedTicketData> changeTicketStatus(@PathVariable UUID id, @Valid @RequestBody TicketStatusData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @Tag(name = "Update Ticket")
    @Operation(summary = "Change category for specific ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category changed successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DetailedTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @PatchMapping("/{id}/category")
    public ResponseEntity<DetailedTicketData> changeTicketCategory(@PathVariable UUID id, @Valid @RequestBody TicketCategoryData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @Tag(name = "Update Ticket")
    @Operation(summary = "Assign technician for specific ticket by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technician assigned",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DetailedTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidExceptionExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Technician id is not valid",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @PatchMapping("/{id}/technician")
    public ResponseEntity<DetailedTicketData> assignTechnician(@PathVariable UUID id, @Valid @RequestBody AssignTechnicianData data) {
        DetailedTicketData updatedTicket = ticketService.updatedTicket(id, data);
        return ResponseEntity.ok(updatedTicket);
    }

    @Tag(name = "Update Ticket")
    @Operation(summary = "Close ticket by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New user created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DetailedTicketData.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @PostMapping("/{id}/close")
    public ResponseEntity<DetailedTicketData> closeTicket(@PathVariable UUID id) {
        DetailedTicketData updatedTicket = ticketService.closeTicket(id);
        return ResponseEntity.ok(updatedTicket);
    }

    @Tag(name = "Delete Ticket")
    @Operation(summary = "Hard delete ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Can't find ticket with specified id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessage.class)) }) })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
