package com.solutis.helpdesk.service.ticket.message;

import com.solutis.helpdesk.service.ticket.domain.dto.DetailedTicketData;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TicketMessageSender {
    @Value("${rabbitmq.ticket.created.routing-key}")
    private String TICKET_CREATED_ROUTING_KEY;

    @Value("${rabbitmq.ticket.assigned.routing-key}")
    private String TICKET_ASSIGNED_ROUTING_KEY;

    @Value("${rabbitmq.ticket.status-changed.routing-key}")
    private String TICKET_STATUS_CHANGED_ROUTING_KEY;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTicketCreatedMessage(DetailedTicketData ticket) {
        rabbitTemplate.convertAndSend(TICKET_CREATED_ROUTING_KEY, ticket);
    }

    public void sendTicketAssignedMessage(DetailedTicketData ticket) {
        rabbitTemplate.convertAndSend(TICKET_ASSIGNED_ROUTING_KEY, ticket);
    }

    public void sendTicketStatusChangedMessage(DetailedTicketData ticket) {
        rabbitTemplate.convertAndSend(TICKET_STATUS_CHANGED_ROUTING_KEY, ticket);
    }
}
