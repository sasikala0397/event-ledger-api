package com.eventledger.serviceTest;

import com.eventledger.entity.Event;
import com.eventledger.repository.EventRepository;
import com.eventledger.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

class EventServiceTest {
    @Autowired
    private EventService service;
@Autowired
private EventRepository repository;

@BeforeEach
void cleanUp() {
 
    repository.deleteAll();
}
    @Test
    void testIdempotency() {
        Event e1 = new Event();
        e1.setEventId("evt-001");
        e1.setAccountId("acct-123");
        e1.setType("CREDIT");
        e1.setAmount(BigDecimal.valueOf(100));
        e1.setCurrency("USD");
        e1.setEventTimestamp(Instant.now());

        service.saveEvent(e1);
        service.saveEvent(e1); // duplicate

        assertEquals(1, service.getEventsByAccount("acct-123").size());
    }

    @Test
    void testOutOfOrderEvents() {
        Event e1 = new Event();
        e1.setEventId("evt-001");
        e1.setAccountId("acct-123");
        e1.setType("CREDIT");
        e1.setAmount(BigDecimal.valueOf(100));
        e1.setCurrency("USD");
        e1.setEventTimestamp(Instant.parse("2026-05-15T14:02:11Z"));

        Event e2 = new Event();
        e2.setEventId("evt-002");
        e2.setAccountId("acct-123");
        e2.setType("DEBIT");
        e2.setAmount(BigDecimal.valueOf(50));
        e2.setCurrency("USD");
        e2.setEventTimestamp(Instant.parse("2026-05-10T14:02:11Z"));

        service.saveEvent(e1);
        service.saveEvent(e2);

        List<Event> events = service.getEventsByAccount("acct-123");
        assertEquals("evt-002", events.get(0).getEventId()); // ordered by timestamp
    }

    @Test
    void testBalanceComputation() {
        Event e1 = new Event();
        e1.setEventId("evt-001");
        e1.setAccountId("acct-123");
        e1.setType("CREDIT");
        e1.setAmount(BigDecimal.valueOf(100));
        e1.setCurrency("USD");
        e1.setEventTimestamp(Instant.now());

        Event e2 = new Event();
        e2.setEventId("evt-002");
        e2.setAccountId("acct-123");
        e2.setType("DEBIT");
        e2.setAmount(BigDecimal.valueOf(40));
        e2.setCurrency("USD");
        e2.setEventTimestamp(Instant.now());

        service.saveEvent(e1);
        service.saveEvent(e2);
           BigDecimal balance = service.getBalance("acct-123");
    assertEquals(0, BigDecimal.valueOf(60).compareTo(balance));

    }
    

}
