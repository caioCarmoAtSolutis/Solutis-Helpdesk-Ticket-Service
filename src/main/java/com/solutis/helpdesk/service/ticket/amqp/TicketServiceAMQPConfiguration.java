package com.solutis.helpdesk.service.ticket.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketServiceAMQPConfiguration {
    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeRabbitAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Queue createTicketCreatedQueue() {
        return QueueBuilder.nonDurable("ticket.created").build();
    }

    @Bean
    public Queue createTicketAssignedQueue() {
        return QueueBuilder.nonDurable("ticket.assigned").build();
    }

    @Bean
    public Queue createTicketStatusChangedQueue() {
        return QueueBuilder.nonDurable("ticket.status.changed").build();
    }
}
