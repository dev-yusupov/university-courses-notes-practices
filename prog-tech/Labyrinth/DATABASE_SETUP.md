# Quick Setup Guide - Database Integration

## What's Been Added

✅ **SQLite Database Integration**
- Full CRUD operations for high scores
- Automatic database initialization
- SQL injection protection with PreparedStatements
- Singleton pattern for database management
- Graceful error handling

## Files Created/Modified

### New Files:
1. `DatabaseManager.java` - Complete database management system
2. `lib/sqlite-jdbc-3.44.1.0.jar` - SQLite JDBC driver (downloaded)
3. `lib/README.md` - Library setup instructions

### Modified Files:
1. `HighScore.java` - Now uses database instead of file serialization
2. `GameFrame.java` - Added database cleanup on exit
3. `README.md` - Updated with database documentation

## Setup Steps for NetBeans

1. **Open the project in NetBeans**

2. **Add the SQLite JDBC library:**
   - Right-click on your project → **Properties**
   - Select **Libraries** category
   - Click **Add JAR/Folder**
   - Navigate to `lib/sqlite-jdbc-3.44.1.0.jar`
   - Click **Open**, then **OK**

3. **Clean and Build:**
   - Right-click on project → **Clean and Build**
   - Or press **Shift+F11**

4. **Run the game:**
   - Press **F6** or right-click → **Run**

## How It Works

### Database Initialization
When the game starts:
```java
DatabaseManager.getInstance() // Creates singleton
→ initializeDatabase()
→ Creates "labyrinth.db" file
→ Creates "high_scores" table if not exists
→ Console: "Database initialized successfully"
```

### Saving High Scores
When player loses:
```java
User enters name
→ HighScore.saveHighScore(name, score)
→ DatabaseManager.saveHighScore(name, score)
→ SQL: INSERT INTO high_scores (player_name, score) VALUES (?, ?)
→ Console: "High score saved to database: [name] - [score]"
```

### Loading High Scores
When viewing high score menu:
```java
HighScore.getTop10()
→ DatabaseManager.getTopScores(10)
→ SQL: SELECT player_name, score FROM high_scores 
       ORDER BY score DESC, date_achieved ASC LIMIT 10
→ Returns List<HighScore>
```

## Testing the Database

1. **Run the game**
2. **Play until the dragon catches you**
3. **Enter your name** (e.g., "TestPlayer")
4. **Check console output:**
   ```
   Database initialized successfully
   High score saved to database: TestPlayer - 2
   ```
5. **Open High Scores menu** - Your score should appear!
6. **Check database file:**
   - File `labyrinth.db` created in project root
   - Can open with SQLite browser tools

## Database Schema

```sql
CREATE TABLE high_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_name TEXT NOT NULL,
    score INTEGER NOT NULL,
    date_achieved TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

## Troubleshooting

### "SQLite JDBC driver not found!"
- **Solution:** Add `lib/sqlite-jdbc-3.44.1.0.jar` to Libraries in project properties
- Clean and rebuild project

### Database file not created
- **Check:** Write permissions in project directory
- **Location:** Should be in project root (same folder as src/)

### Scores not saving
- **Check console** for error messages
- **Verify:** Database file exists and is not locked
- **Test:** Run DatabaseManager methods directly

## Database Operations Available

```java
// Get database instance
DatabaseManager db = DatabaseManager.getInstance();

// Save a score
db.saveHighScore("PlayerName", 5);

// Get top 10
List<HighScore> top10 = db.getTopScores(10);

// Get all scores
List<HighScore> all = db.getAllScores();

// Get count
int count = db.getScoreCount();

// Clear all (for testing)
db.clearAllScores();

// Close connection (automatic on app exit)
db.close();
```

## Benefits of Database Approach

✅ **Scalability:** Can store unlimited scores  
✅ **Query Power:** SQL sorting, filtering, aggregation  
✅ **Data Integrity:** ACID compliance  
✅ **Concurrent Access:** Multiple instances can access  
✅ **Timestamps:** Automatic date tracking  
✅ **Professional:** Industry-standard data persistence  
✅ **No External Server:** Embedded SQLite database  

---

**You're all set! The game now has professional database integration.** 🎮🗄️
