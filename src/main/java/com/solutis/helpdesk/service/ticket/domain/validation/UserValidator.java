package com.solutis.helpdesk.service.ticket.domain.validation;

import com.solutis.helpdesk.service.ticket.feign.client.user.Role;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserData;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserServiceClient;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.ExceptionMessageBuilder;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.UserHasInvalidRoleException;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.UserIsInactiveException;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new UserIsInactiveException(ExceptionMessageBuilder.getInstance().userIsInactiveMessage(customerId.toString()));

        validateUserHasRole(user, expectedRole);
    }

    private UserData getUser(UUID userId) {
        try {
            ResponseEntity<UserData> response = userServiceClient.getUserById(userId);
            return response.getBody();
        } catch (FeignException ex) {
            throw new EntityNotFoundException(ExceptionMessageBuilder.getInstance().userIdNotFountMessage(userId.toString()));
        }
    }

    private void validateUserHasRole(UserData user,  Role expectedRole) {
        if (!expectedRole.equals(user.role().role()))
            throw new UserHasInvalidRoleException(ExceptionMessageBuilder.getInstance().userHasInvalidRoleMessage(user, expectedRole));
    }
}
