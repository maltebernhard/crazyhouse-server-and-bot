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

---

## Getting Started

### Prerequisites
- **Java 11+** (JDK required)  
- **Eclipse (J2EE Edition)** or any Java IDE  
- **Apache Tomcat 9** (for running the server)  
- **Haskell Platform** (for compiling and running the bot)  

### Running the Server
1. Import the project into Eclipse.  
2. Deploy the project to Tomcat.  
3. Start the server and open the GUI in a browser: 
``http://localhost:8080/GameServer/Crazyhouse_Board.html``

### Running the Bot
1. Compile the bot in Haskell:  
```bash
ghc -o CrazyhouseBot CrazyhouseBot.hs
```
