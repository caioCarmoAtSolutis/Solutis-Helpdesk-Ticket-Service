package com.solutis.helpdesk.service.ticket.infrastructure.exception;

import com.solutis.helpdesk.service.ticket.domain.model.Category;
import com.solutis.helpdesk.service.ticket.domain.model.Priority;
import com.solutis.helpdesk.service.ticket.domain.model.Status;
import com.solutis.helpdesk.service.ticket.feign.client.user.Role;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserData;

public class ExceptionMessageBuilder {

    private static ExceptionMessageBuilder instance;

    public static ExceptionMessageBuilder getInstance() {
        if (instance == null)
            instance = new ExceptionMessageBuilder();
        return instance;
    }

    public String ticketIdNotFountMessage(String ticketId) {
        return "Ticket with id " + ticketId + " doesn't exist!";
    }

    public String priorityWithValueNotFountMessage(Priority priority) {
        return "Priority with value " + priority.toString() + " doesn't exist!";
    }

    public String statusWithValueNotFountMessage(Status status) {
        return "Status with value " + status.toString() + " doesn't exist!";
    }

    public String CategoryWithValueNotFountMessage(Category category) {
        return "Category with value " + category.toString() + " doesn't exist!";
    }

    public String userIdNotFountMessage(String userId) {
        return "User with id " + userId + " not found!";
    }

    public String userIsInactiveMessage(String customerId) {
        return "User with id " +  customerId + " is not active!";
    }

    public String userHasInvalidRoleMessage(UserData user, Role expectedRole) {
        return "User with id " +  user.id() + " has role " + user.role().role() + " instead of " + expectedRole + "!";
    }
}
