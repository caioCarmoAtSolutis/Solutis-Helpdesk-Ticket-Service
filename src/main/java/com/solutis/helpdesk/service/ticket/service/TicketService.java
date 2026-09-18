package com.solutis.helpdesk.service.ticket.service;

import com.solutis.helpdesk.service.ticket.repository.TicketCategoryRepository;
import com.solutis.helpdesk.service.ticket.repository.TicketPriorityRepository;
import com.solutis.helpdesk.service.ticket.repository.TicketRepository;
import com.solutis.helpdesk.service.ticket.repository.TicketStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
