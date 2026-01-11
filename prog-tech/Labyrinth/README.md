# Labyrinth Game - Documentation

## Project Overview

The Labyrinth Game is a Java Swing-based maze escape game where the player must navigate through procedurally generated labyrinths while avoiding a dangerous dragon. The objective is to reach the top-right corner from the bottom-left starting position, solving as many labyrinths as possible.

## Game Features

### Core Gameplay
- **Procedurally Generated Mazes**: Each labyrinth is unique, generated using a depth-first search algorithm
- **Player Movement**: WASD or arrow key controls for intuitive navigation
- **Dragon AI**: Enemy that moves randomly through the maze, changing direction when hitting walls
- **Fog of War**: Limited visibility (3 units) creates tension and strategic gameplay
- **Progressive Difficulty**: Continue through multiple labyrinths to increase your score
- **Timer**: Tracks elapsed time for each level

### Game Mechanics
- Player starts at the bottom-left corner
- Goal area is in the top-right corner (marked in green)
- Dragon spawns at a random location far from the player
- If dragon gets adjacent to player, game over
- Score is the number of labyrinths successfully completed

### User Interface
- **Menu System**:
  - New Game: Restart from scratch
  - High Scores: View top 10 players
  - Instructions: In-game help
  - About: Game information
- **HUD Display**:
  - Current score (labyrinths solved)
  - Timer
  - Control instructions
  - Goal indicator

### High Score System
- Automatic save when player loses
- **Persistent storage using PostgreSQL database**
- Top 10 leaderboard with SQL queries
- Player name entry on game over
- Timestamp tracking for each score
- Professional enterprise-grade database

## Technical Architecture

### Class Structure

#### Model Classes

**Position.java**
- Represents x,y coordinates in the maze
- Provides distance calculation methods
- Used for collision detection and proximity checks

**Player.java**
- Manages player state (position, alive status, score)
- Handles movement with collision detection
- Tracks number of labyrinths solved

**Dragon.java**
- Implements enemy AI behavior
- Moves in straight lines until hitting walls
- Randomly changes direction
- Checks proximity to player

**GameState.java**
- Central game state management
- Handles level progression
- Timer management
- Win/loss condition checking
- Updates game entities each frame

**HighScore.java**
- High score data model
- Database integration methods
- Sorting and top-10 filtering
- Load/save functionality using DatabaseManager

**DatabaseManager.java**
- PostgreSQL database connection management
- SQL table creation and schema management
- CRUD operations for high scores
- Singleton pattern for database access
- Automatic database initialization
- Connection pooling ready

#### Generation

**LabyrinthGenerator.java**
- Procedural maze generation using recursive backtracking
- Ensures solvable mazes with paths from start to goal
- Random valid position finder for entity placement
- Configurable maze dimensions

#### View/Controller

**GamePanel.java**
- Main game rendering panel
- Keyboard input handling (WASD controls)
- Graphics rendering with fog of war
- Game loop with 200ms update interval
- HUD rendering
- Dialog management for game over/level complete

**GameFrame.java**
- Main application window
- Menu bar creation
- High score display dialog
- Instructions and about dialogs
- Game restart functionality

**Labyrinth.java**
- Application entry point
- Launches game on Swing EDT

## Database Schema

### PostgreSQL Database Structure

**Table: high_scores**
```sql
CREATE TABLE IF NOT EXISTS high_scores (
    id SERIAL PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    score INTEGER NOT NULL,
    date_achieved TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**Fields:**
- `id` - Auto-incrementing serial primary key
- `player_name` - Player's name (VARCHAR 100)
- `score` - Number of labyrinths solved (INTEGER)
- `date_achieved` - Timestamp when score was achieved (TIMESTAMP)

**Key Operations:**
- `INSERT` - Save new high score with player name and score
- `SELECT ... ORDER BY score DESC LIMIT 10` - Get top 10 high scores
- `SELECT COUNT(*)` - Get total number of scores
- All queries use PreparedStatement to prevent SQL injection

**Database Connection:**
- Host: localhost (WSL Docker)
- Port: 5432
- Database: labyrinthdb
- User: labyrinth
- Password: labyrinth123
- Driver: postgresql-42.7.1.jar

**Docker Commands:**
```bash
# View high scores directly
docker exec -it labyrinth-db psql -U labyrinth -d labyrinthdb -c "SELECT * FROM high_scores ORDER BY score DESC LIMIT 10;"

# Access database shell
docker exec -it labyrinth-db psql -U labyrinth -d labyrinthdb
```

## Algorithm Descriptions

### Maze Generation Algorithm

The game uses a **Recursive Backtracking (Depth-First Search)** algorithm to generate playable mazes:

1. Start with a grid filled entirely with walls
2. Choose a starting cell and mark it as a path
3. Randomly select an unvisited neighboring cell (2 units away)
4. Carve a path between current and chosen cell
5. Recursively repeat from the chosen cell
6. Backtrack when no unvisited neighbors exist
7. Ensure start (bottom-left) and goal (top-right) areas are paths

This algorithm guarantees:
- A unique solution path exists
- The maze is fully connected
- No isolated sections or impossible areas
- Natural-looking maze patterns

### Dragon AI Algorithm

The dragon follows a simple but effective movement pattern:

1. Move in current direction
2. If hit a wall or boundary:
   - Randomly choose a new valid direction
   - Continue moving
3. Check distance to player after each move
4. If adjacent to player (distance ≤ 1.5), trigger game over

This creates unpredictable but believable behavior that keeps players alert.

### Fog of War System

Visibility is calculated using Manhattan distance:
- Only cells within 3 units of the player are visible
- Distance = |playerX - cellX| + |playerY - cellY|
- Creates tension and strategic decision-making
- Player must explore carefully to avoid the dragon

## UML Class Diagram

```
┌─────────────────┐
│   Labyrinth     │
│   (Main)        │
└────────┬────────┘
         │
         │ creates
         ▼
    ┌─────────────────┐
    │   GameFrame     │
    │  (JFrame)       │
    └────────┬────────┘
             │
             │ contains
             ▼
       ┌─────────────────┐
       │   GamePanel     │
       │   (JPanel)      │
       └────────┬────────┘
                │
                │ uses
                ▼
          ┌─────────────────┐
          │   GameState     │
          └────────┬────────┘
                   │
         ┌─────────┼─────────┬──────────┐
         │         │         │          │
         ▼         ▼         ▼          ▼
    ┌────────┐ ┌────────┐ ┌──────────┐ ┌──────────────────┐
    │ Player │ │ Dragon │ │ Position │ │ LabyrinthGenerator│
    └────────┘ └────────┘ └──────────┘ └──────────────────┘
         │         │
         └─────────┴──────────┐
                              ▼
                         ┌──────────┐
                         │HighScore │
                         └─────┬────┘
                               │ uses
                               ▼
                      ┌─────────────────┐
                      │ DatabaseManager │
                      │  (PostgreSQL)   │
                      └────────┬────────┘
                               │ connects to
                               ▼
                      ┌─────────────────┐
                      │   PostgreSQL    │
                      │   (Docker)      │
                      │  labyrinthdb    │
                      └─────────────────┘
```

### Event Flow Diagram

```
User Input (Keyboard)
    │
    ▼
GamePanel.keyPressed()
    │
    ▼
GameState.movePlayer()
    │
    ▼
Player.move() → Check collision
    │
    ▼
GamePanel.repaint()


Timer Event (200ms)
    │
    ▼
GameState.update()
    │
    ├──→ Dragon.move()
    │
    ├──→ Check dragon proximity
    │
    └──→ Check win condition
         │
         ▼
    GamePanel.checkGameOver()
         │
         ├──→ If won: Show dialog → Next level
         │
         └──→ If lost: Save score → Show high scores
```

## Implementation Highlights

### Key Features Implemented

1. **Object-Oriented Design**
   - Separation of concerns (Model-View-Controller inspired)
   - Encapsulation of game logic in separate classes
   - Reusable Position class for spatial calculations

2. **Graphics Rendering**
   - Custom paintComponent for maze rendering
   - Smooth camera following player
   - Color-coded game elements
   - Fog of war effect using alpha blending

3. **Game Loop**
   - Timer-based updates (200ms interval)
   - Separation of rendering and logic updates
   - Smooth player movement with immediate visual feedback

4. **Persistence**
   - High score serialization to disk
   - Automatic save on game over
   - Robust error handling for file I/O
## How to Run
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Java Swing libraries (included in JDK)
- PostgreSQL JDBC Driver (included in `lib/` folder)
- Docker with PostgreSQL container running
- SQLite JDBC Driver (included in `lib/` folder)
### Setting Up PostgreSQL

**1. Start PostgreSQL Container (WSL Docker):**
```bash
docker run -d --name labyrinth-db \
  -e POSTGRES_USER=labyrinth \
  -e POSTGRES_PASSWORD=labyrinth123 \
  -e POSTGRES_DB=labyrinthdb \
  -p 5432:5432 \
### Compilation
```bash
javac -cp "lib/postgresql-42.7.1.jar" -d bin src/labyrinth/*.java
```

### Execution
```bash
java -cp "bin;lib/postgresql-42.7.1.jar" labyrinth.Labyrinth
```Click OK/sqlite-jdbc-3.44.1.0.jar`
5. Click OK

### Using NetBeans
1. Open the project in NetBeans
2. Start PostgreSQL container (see above)
3. Add the PostgreSQL JDBC library (see above)
4. Right-click on the project → Run
5. Or press F6 to run the main class

### Database
- PostgreSQL database running in Docker container
- High scores table created automatically on first run
- Connection: localhost:5432/labyrinthdb
- User: labyrinth / Password: labyrinth123
- Professional relational database with ACID compliance
### Using NetBeans
1. Open the project in NetBeans
2. Add the SQLite JDBC library (see above)
3. Right-click on the project → Run
4. Or press F6 to run the main class

### Database
- The SQLite database `labyrinth.db` is created automatically on first run
- High scores are stored persistently in the database
- No external database server required
java -cp bin labyrinth.Labyrinth
```

### Using NetBeans
1. Open the project in NetBeans
2. Right-click on the project → Run
3. Or press F6 to run the main class

## Controls

- **W** or **↑**: Move Up
- **A** or **←**: Move Left
- **S** or **↓**: Move Down
- **D** or **→**: Move Right

## Game Tips

1. **Plan Your Route**: Use the fog of war to carefully plan your path
2. **Listen for the Dragon**: Watch for the red circle in your visibility range
3. **Corner Strategy**: Sometimes waiting in corners is safer
4. **Speed vs Safety**: Balance fast completion with careful exploration
5. **Learn the Patterns**: Dragon movement is predictable once you understand it

## Future Enhancements

Possible additions to improve the game:
- Multiple difficulty levels (maze size, dragon speed, visibility range)
- Power-ups (temporary invisibility, speed boost, dragon freeze)
- Multiple dragons on higher levels
- Sound effects and background music
- Minimap showing explored areas
- Checkpoints for longer gameplay sessions
- Multiplayer mode
- Custom level editor

## Credits

**Project**: Programming Technology Course Assignment  
**Year**: 2025  
**Game Design**: Classic maze chase concept with procedural generation  
**Implementation**: Java Swing with custom graphics rendering

---

*Have fun escaping the labyrinth! Can you reach the top of the high score board?*
