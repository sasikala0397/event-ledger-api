package com.eventledger.api.service;

import com.eventledger.api.entity.Event;
import com.eventledger.api.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event saveEvent(Event event) {
        return repository.findById(event.getEventId())
                .orElseGet(() -> repository.save(event));
    }

    public Event getEvent(String id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Event> getEventsByAccount(String accountId) {
        return repository.findByAccountIdOrderByEventTimestampAsc(accountId);
    }

public BigDecimal getBalance(String accountId) {
    return getEventsByAccount(accountId).stream()
            .map(e -> {
                if ("CREDIT".equals(e.getType())) {
                    return e.getAmount(); // add credits
                } else if ("DEBIT".equals(e.getType())) {
                    return e.getAmount().negate(); // subtract debits
                } else {
                    return BigDecimal.ZERO;
                }
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}


}
