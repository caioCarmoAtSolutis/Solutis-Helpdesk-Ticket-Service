package com.solutis.helpdesk.service.ticket.domain.validation;

import com.solutis.helpdesk.service.ticket.feign.client.user.Role;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserData;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserServiceClient;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.UserHasInvalidRoleException;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.UserIsInactiveException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserValidator {
    @Autowired
    private UserServiceClient userServiceClient;

    public void validateUser(UUID customerId, Role expectedRole) {
        UserData user = getUser(customerId);

        if (!user.active())
            throw new UserIsInactiveException("User with id " +  customerId + " is not active!");

        validateUserHasRole(user, expectedRole);
    }

    private UserData getUser(UUID userId) {
        ResponseEntity<UserData> response = userServiceClient.getUserById(userId);

        if (HttpStatusCode.valueOf(400).equals(response.getStatusCode()))
            throw new EntityNotFoundException("User with id " + userId + " not found!");

        return response.getBody();
    }

    private void validateUserHasRole(UserData user,  Role expectedRole) {
        if (!Role.CLIENT.equals(user.role().value()))
            throw new UserHasInvalidRoleException("User with id " +  user.id() + " has role " + user.role().value() + " instead of " + expectedRole + "!");
    }
}
