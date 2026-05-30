package com.eventledger.service;

import com.eventledger.entity.Event;
import com.eventledger.exception.EventNotFoundException;
import com.eventledger.repository.EventRepository;
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

    public boolean eventExists(String eventId) {
        return repository.existsById(eventId);
    }

    public Event getEvent(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + id));
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
