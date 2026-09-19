package com.solutis.helpdesk.service.ticket.controller;

import com.solutis.helpdesk.service.ticket.service.LoadDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load-data")
public class LoadDatabaseController {
    @Autowired
    private LoadDatabaseService loadService;

    @GetMapping
    public ResponseEntity<Void> LoadDatabase() {
        loadService.load();
        return ResponseEntity.ok().build();
    }
}
