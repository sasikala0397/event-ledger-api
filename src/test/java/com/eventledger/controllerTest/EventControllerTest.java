package com.eventledger.controllerTest;

import com.eventledger.entity.Event;
import com.eventledger.repository.EventRepository;
import com.eventledger.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventService service;

    @Autowired
    private EventRepository repository;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void testCreateEventAndGetById() throws Exception {
        String json = """
            {
              "eventId":"evt-001",
              "accountId":"acct-123",
              "type":"CREDIT",
              "amount":150.00,
              "currency":"USD",
              "eventTimestamp":"2026-05-15T14:02:11Z"
            }
            """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value("evt-001"));

        mockMvc.perform(get("/events/evt-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acct-123"));
    }

    @Test
    void testGetEventsByAccount() throws Exception {
        Event credit = new Event();
        credit.setEventId("evt-001");
        credit.setAccountId("acct-123");
        credit.setType("CREDIT");
        credit.setAmount(BigDecimal.valueOf(150));
        credit.setCurrency("USD");
        credit.setEventTimestamp(Instant.parse("2026-05-15T14:02:11Z"));
        service.saveEvent(credit);

        Event debit = new Event();
        debit.setEventId("evt-002");
        debit.setAccountId("acct-123");
        debit.setType("DEBIT");
        debit.setAmount(BigDecimal.valueOf(50));
        debit.setCurrency("USD");
        debit.setEventTimestamp(Instant.parse("2026-05-10T14:02:11Z"));
        service.saveEvent(debit);

        mockMvc.perform(get("/events?accountId=acct-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value("evt-002"))
                .andExpect(jsonPath("$[1].eventId").value("evt-001"));
    }

    @Test
    void testGetBalance() throws Exception {
        Event credit = new Event();
        credit.setEventId("evt-001");
        credit.setAccountId("acct-123");
        credit.setType("CREDIT");
        credit.setAmount(BigDecimal.valueOf(150));
        credit.setCurrency("USD");
        credit.setEventTimestamp(Instant.parse("2026-05-15T14:02:11Z"));
        service.saveEvent(credit);

        Event debit = new Event();
        debit.setEventId("evt-002");
        debit.setAccountId("acct-123");
        debit.setType("DEBIT");
        debit.setAmount(BigDecimal.valueOf(50));
        debit.setCurrency("USD");
        debit.setEventTimestamp(Instant.parse("2026-05-10T14:02:11Z"));
        service.saveEvent(debit);

        mockMvc.perform(get("/accounts/acct-123/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    void testDuplicateEventReturnsOk() throws Exception {
        String json = """
            {
              "eventId":"evt-001",
              "accountId":"acct-123",
              "type":"CREDIT",
              "amount":150.00,
              "currency":"USD",
              "eventTimestamp":"2026-05-15T14:02:11Z"
            }
            """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("evt-001"));
    }

    @Test
    void testGetMissingEventReturnsNotFound() throws Exception {
        mockMvc.perform(get("/events/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationErrorReturnsBadRequest() throws Exception {
        String invalidJson = """
            {
              "eventId":"evt-001",
              "accountId":"acct-123",
              "type":"INVALID",
              "amount":0,
              "currency":"USD",
              "eventTimestamp":"2026-05-15T14:02:11Z"
            }
            """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
