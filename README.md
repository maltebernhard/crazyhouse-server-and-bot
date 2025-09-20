# Crazyhouse Game Server & Bot
This repository implements the chess variant **Crazyhouse**, extending a generic game server with full game logic, testing, and a bot opponent. It consists of three main components:

## 1. Game Server (Java)
- Implements Crazyhouse rules within the server framework  
- Validates moves, updates the game state, and communicates with the web frontend  
- Includes integration tests with JUnit to ensure correctness and full branch coverage  

## 2. Quality Assurance
- Automated tests with JUnit for functional validation  
- Code quality measured against defined metrics (cyclomatic complexity, nesting depth, method size)  

## 3. Crazyhouse Bot (Haskell)
- Simple AI opponent implemented in Haskell  
- Generates all valid moves for a given state and selects one to play  
- Compatible with the Java server for human-vs-bot or bot-vs-bot matches  

---

This project serves as a **reference implementation**, showcasing the integration of object-oriented Java programming, functional Haskell programming, and software testing practices in the context of game server development.

# Usage
## TODO
