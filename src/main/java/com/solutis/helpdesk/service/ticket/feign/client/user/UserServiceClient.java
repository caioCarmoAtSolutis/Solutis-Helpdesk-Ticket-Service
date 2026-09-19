package com.solutis.helpdesk.service.ticket.feign.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user-service.url-base}")
public interface UserServiceClient {
    @GetMapping("/users/{userId}")
    ResponseEntity<UserData> getUserById(@PathVariable UUID userId);
}
