package com.solutis.helpdesk.service.ticket.message.rabbitmq.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketServiceAMQPConfiguration {
    @Value("${rabbitmq.ticket.created.routing-key}")
    private String TICKET_CREATED_ROUTING_KEY;

    @Value("${rabbitmq.ticket.assigned.routing-key}")
    private String TICKET_ASSIGNED_ROUTING_KEY;

    @Value("${rabbitmq.ticket.status-changed.routing-key}")
    private String TICKET_STATUS_CHANGED_ROUTING_KEY;

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeRabbitAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public JacksonJsonMessageConverter JacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter jsonMessageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue createTicketCreatedQueue() {
        return QueueBuilder.durable(TICKET_CREATED_ROUTING_KEY).build();
    }

    @Bean
    public Queue createTicketAssignedQueue() {
        return QueueBuilder.durable(TICKET_ASSIGNED_ROUTING_KEY).build();
    }

    @Bean
    public Queue createTicketStatusChangedQueue() {
        return QueueBuilder.durable(TICKET_STATUS_CHANGED_ROUTING_KEY).build();
    }
}
