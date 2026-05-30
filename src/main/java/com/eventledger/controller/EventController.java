package com.eventledger.controller;

import com.eventledger.entity.Event;
import com.eventledger.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
 
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event saved = service.saveEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/events/{id}")
    public Event getEvent(@PathVariable String id) {
        return service.getEvent(id);
    }

    @GetMapping("/events")
    public List<Event> getEvents(@RequestParam String accountId) {
        return service.getEventsByAccount(accountId);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable String accountId) {
        return service.getBalance(accountId);
    }
}
