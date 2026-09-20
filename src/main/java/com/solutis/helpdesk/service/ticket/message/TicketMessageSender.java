package com.solutis.helpdesk.service.ticket.message;

import com.solutis.helpdesk.service.ticket.domain.dto.DetailedTicketData;
import com.solutis.helpdesk.service.ticket.domain.model.Ticket;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TicketMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.ticket.created.routing-key}")
    private String ticketCreatedRoutingkey;

    @Value("${rabbitmq.ticket.assigned.routing-key}")
    private String ticketAssignedRoutingkey;

    @Value("${rabbitmq.ticket.status-changed.routing-key}")
    private String ticketStatusChangedRoutingkey;

    private Message transformTicketToMessage(DetailedTicketData ticket) {
        return new Message(ticket.toString().getBytes());
    }

    public void sendTicketCreatedMessage(DetailedTicketData ticket) {
        rabbitTemplate.send(ticketCreatedRoutingkey, transformTicketToMessage(ticket));
    }

    public void sendTicketAssignedMessage(DetailedTicketData ticket) {
        rabbitTemplate.send(ticketAssignedRoutingkey, transformTicketToMessage(ticket));
    }

    public void sendTicketStatusChangedMessage(DetailedTicketData ticket) {
        rabbitTemplate.send(ticketStatusChangedRoutingkey, transformTicketToMessage(ticket));
    }
}
