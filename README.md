# event-ledger-api


## 📖 Overview
This project implements an **Event Ledger API** that processes financial transaction events.  
It demonstrates:
- Idempotency (duplicate events are ignored)
- Out‑of‑order event handling
- Balance computation
- Input validation

Built with **Spring Boot** and **H2 in‑memory database** for easy local setup.

## ⚙️ Prerequisites
- Java 17+
- Maven (wrapper included: `./mvnw`)
- Git

## 🚀 Setup & Run
Clone the repository:
```bash
git clone https://github.com/sasikala0397/event-ledger-api.git
cd event-ledger-api
