const gameState = {
    playerName: '',
    startTime: null,
    timerInterval: null,
    stations: [],
    lines: [],
    roundOrder: [],
    currentRoundIndex: 0,
    deck: [],
    currentCard: null,
    cardsDrawnThisRound: 0,
    segments: [],
    railwayStationsConnected: 0,
    roundScores: [],
    selectedStation: null,
    platformCardsDrawn: { side: 0, center: 0 }, // Track platform types for extra task
    switchActive: false, // Track if switch card was drawn
    roundEnded: false, // Track if round should end
    abilities: {}, // Store abilities for each line {lineId: abilityName}
    abilityUsed: {}, // Track if ability was used {lineId: boolean}
    heavyTrafficStation: null // Station marked with Heavy Traffic ability
};

const CARD_TYPES = [
    { type: 'A', platform: 'side' },
    { type: 'B', platform: 'side' },
    { type: 'C', platform: 'side' },
    { type: 'D', platform: 'side' },
    { type: 'joker', platform: 'side' },
    { type: 'A', platform: 'center' },
    { type: 'B', platform: 'center' },
    { type: 'C', platform: 'center' },
    { type: 'D', platform: 'center' },
    { type: 'joker', platform: 'center' },
    { type: 'switch', platform: 'center' }
];

// Utility Functions
function showScreen(screenId) {
    document.querySelectorAll('.screen').forEach(screen => {
        screen.classList.remove('active');
    });
    document.querySelector(`#${screenId}`).classList.add('active');
}

function shuffleArray(array) {
    const shuffled = [...array];
    for (let i = shuffled.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled;
}

function formatTime(seconds) {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
}

// Embedded data as fallback
const EMBEDDED_STATIONS = [{"id":0,"x":0,"y":0,"type":"A","train":false,"side":"Buda","district":0},{"id":1,"x":2,"y":0,"type":"B","train":false,"side":"Buda","district":1},{"id":2,"x":4,"y":0,"type":"D","train":false,"side":"Buda","district":2},{"id":3,"x":6,"y":0,"type":"D","train":false,"side":"Pest","district":2},{"id":4,"x":7,"y":0,"type":"B","train":false,"side":"Pest","district":3},{"id":5,"x":9,"y":0,"type":"C","train":false,"side":"Pest","district":4},{"id":6,"x":0,"y":1,"type":"D","train":false,"side":"Buda","district":1},{"id":7,"x":1,"y":1,"type":"B","train":false,"side":"Buda","district":1},{"id":8,"x":5,"y":1,"type":"A","train":false,"side":"Buda","district":2},{"id":9,"x":8,"y":1,"type":"C","train":false,"side":"Pest","district":3},{"id":10,"x":9,"y":1,"type":"A","train":false,"side":"Pest","district":3},{"id":11,"x":2,"y":2,"type":"D","train":false,"side":"Buda","district":1},{"id":12,"x":4,"y":2,"type":"D","train":false,"side":"Buda","district":2},{"id":13,"x":5,"y":2,"type":"D","train":false,"side":"Buda","district":3},{"id":14,"x":6,"y":2,"type":"C","train":false,"side":"Pest","district":3},{"id":15,"x":9,"y":2,"type":"D","train":true,"side":"Pest","district":4},{"id":16,"x":0,"y":3,"type":"C","train":false,"side":"Buda","district":5},{"id":17,"x":2,"y":3,"type":"B","train":false,"side":"Buda","district":5},{"id":18,"x":3,"y":3,"type":"C","train":false,"side":"Buda","district":6},{"id":19,"x":7,"y":3,"type":"A","train":false,"side":"Pest","district":7},{"id":20,"x":8,"y":3,"type":"D","train":false,"side":"Pest","district":7},{"id":21,"x":0,"y":4,"type":"B","train":false,"side":"Buda","district":5},{"id":22,"x":3,"y":4,"type":"A","train":false,"side":"Buda","district":6},{"id":23,"x":4,"y":4,"type":"B","train":false,"side":"Buda","district":6},{"id":24,"x":5,"y":4,"type":"C","train":false,"side":"Pest","district":6},{"id":25,"x":6,"y":4,"type":"A","train":true,"side":"Pest","district":6},{"id":26,"x":9,"y":4,"type":"A","train":false,"side":"Pest","district":7},{"id":27,"x":0,"y":5,"type":"A","train":false,"side":"Buda","district":5},{"id":28,"x":2,"y":5,"type":"C","train":false,"side":"Buda","district":5},{"id":29,"x":5,"y":5,"type":"D","train":false,"side":"Pest","district":6},{"id":30,"x":6,"y":5,"type":"?","train":false,"side":"Pest","district":6},{"id":31,"x":9,"y":5,"type":"B","train":false,"side":"Pest","district":7},{"id":32,"x":1,"y":6,"type":"C","train":false,"side":"Buda","district":5},{"id":33,"x":3,"y":6,"type":"D","train":true,"side":"Buda","district":6},{"id":34,"x":6,"y":6,"type":"B","train":false,"side":"Pest","district":6},{"id":35,"x":7,"y":6,"type":"D","train":false,"side":"Pest","district":7},{"id":36,"x":8,"y":6,"type":"C","train":true,"side":"Pest","district":7},{"id":37,"x":0,"y":7,"type":"B","train":false,"side":"Buda","district":9},{"id":38,"x":3,"y":7,"type":"A","train":false,"side":"Buda","district":10},{"id":39,"x":4,"y":7,"type":"B","train":false,"side":"Buda","district":10},{"id":40,"x":6,"y":7,"type":"B","train":false,"side":"Pest","district":10},{"id":41,"x":9,"y":7,"type":"A","train":false,"side":"Pest","district":11},{"id":42,"x":1,"y":8,"type":"A","train":false,"side":"Buda","district":9},{"id":43,"x":2,"y":8,"type":"B","train":false,"side":"Buda","district":9},{"id":44,"x":5,"y":8,"type":"C","train":false,"side":"Buda","district":10},{"id":45,"x":8,"y":8,"type":"D","train":false,"side":"Pest","district":11},{"id":46,"x":0,"y":9,"type":"D","train":false,"side":"Buda","district":8},{"id":47,"x":2,"y":9,"type":"C","train":false,"side":"Buda","district":9},{"id":48,"x":3,"y":9,"type":"A","train":true,"side":"Buda","district":10},{"id":49,"x":6,"y":9,"type":"D","train":false,"side":"Buda","district":10},{"id":50,"x":7,"y":9,"type":"A","train":false,"side":"Pest","district":11},{"id":51,"x":8,"y":9,"type":"C","train":false,"side":"Pest","district":11},{"id":52,"x":9,"y":9,"type":"B","train":false,"side":"Pest","district":12}];

const EMBEDDED_LINES = [{"id":0,"name":"M1","color":"#FFD800","start":19},{"id":1,"name":"M2","color":"#E41F18","start":28},{"id":2,"name":"M3","color":"#005CA5","start":3},{"id":3,"name":"M4","color":"#4CA22F","start":39}];

// Load game data
async function loadGameData() {
    try {
        // Fallback to XMLHttpRequest (works with file://)
        const loadJSON = (url) => {
            return new Promise((resolve, reject) => {
                const xhr = new XMLHttpRequest();
                xhr.overrideMimeType("application/json");
                xhr.open('GET', url, true);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === 4) {
                        if (xhr.status === 200 || xhr.status === 0) { // 0 for file://
                            if (!xhr.responseText || xhr.responseText.trim() === '') {
                                reject(new Error('Empty response'));
                                return;
                            }
                            try {
                                const data = JSON.parse(xhr.responseText);
                                resolve(data);
                            } catch (e) {
                                reject(new Error('JSON parse error: ' + e.message));
                            }
                        } else {
                            reject(new Error(`HTTP ${xhr.status}: ${xhr.statusText}`));
                        }
                    }
                };
                xhr.onerror = () => reject(new Error(`Failed to load ${url}`));
                xhr.send();
            });
        };
        
        // Try multiple path variations
        const paths = [
            { stations: 'data/stations.json', lines: 'data/lines.json' },
            { stations: './data/stations.json', lines: './data/lines.json' }
        ];
        
        let lastError = null;
        
        for (const pathSet of paths) {
            try {
                console.log('Trying to load from:', pathSet);
                const [stations, lines] = await Promise.all([
                    loadJSON(pathSet.stations),
                    loadJSON(pathSet.lines)
                ]);
                
                gameState.stations = stations;
                gameState.lines = lines;
                
                console.log('Successfully loaded from files - stations:', gameState.stations.length, 'lines:', gameState.lines.length);
                return true;
            } catch (e) {
                lastError = e;
                console.log('Failed with path:', pathSet, 'Error:', e.message);
            }
        }
        
        // If all attempts failed, use embedded data
        console.log('All file loading attempts failed, using embedded data');
        gameState.stations = EMBEDDED_STATIONS;
        gameState.lines = EMBEDDED_LINES;
        console.log('Loaded embedded data - stations:', gameState.stations.length, 'lines:', gameState.lines.length);
        return true;
        
    } catch (error) {
        console.error('Error loading game data:', error);
        alert('Error loading game data: ' + error.message);
        return false;
    }
}

// Initialize deck
function initializeDeck() {
    gameState.deck = shuffleArray([...CARD_TYPES]);
}

// Draw a card
function drawCard() {
    if (gameState.roundEnded) return; // Don't draw if round already ended
    
    if (gameState.deck.length === 0) {
        initializeDeck();
    }
    
    const card = gameState.deck.pop();
    gameState.currentCard = card;
    gameState.cardsDrawnThisRound++;
    
    // Track platform types for alternative round-ending
    if (card.platform === 'side') {
        gameState.platformCardsDrawn.side++;
    } else if (card.platform === 'center') {
        gameState.platformCardsDrawn.center++;
    }
    
    // Check if switch card was drawn
    if (card.type === 'switch') {
        gameState.switchActive = true;
        // Draw another card immediately for the actual station type
        const nextCard = gameState.deck.pop();
        if (nextCard) {
            gameState.currentCard = nextCard;
            if (nextCard.platform === 'side') {
                gameState.platformCardsDrawn.side++;
            } else if (nextCard.platform === 'center') {
                gameState.platformCardsDrawn.center++;
            }
        }
    } else {
        gameState.switchActive = false;
    }
    
    displayCurrentCard();
    updateCardCount();
    
    // Check for alternative round end (5 cards of same platform type)
    if (gameState.platformCardsDrawn.side >= 5 || gameState.platformCardsDrawn.center >= 5) {
        gameState.roundEnded = true;
        document.querySelector('#draw-card-button').textContent = 'End Round';
        document.querySelector('#draw-card-button').style.background = '#ff9800';
        return;
    }
    
    // Check for round end (8 cards drawn) - fallback
    if (gameState.cardsDrawnThisRound >= 8) {
        setTimeout(() => endRound(), 500);
    }
    
    // Clear any selected station
    gameState.selectedStation = null;
    updateClickableStations();
}

// Display current card
function displayCurrentCard() {
    const cardDisplay = document.querySelector('#current-card-display');
    
    if (!gameState.currentCard) {
        cardDisplay.innerHTML = '<div class="card-placeholder">Draw a card to start</div>';
        return;
    }
    
    const card = gameState.currentCard;
    let cardText = card.type.toUpperCase();
    if (card.type === 'joker') cardText = '🃏';
    if (card.type === 'switch') cardText = '⇄';
    
    cardDisplay.innerHTML = `
        <div class="card type-${card.type}">
            <div>${cardText}</div>
            <div style="font-size: 0.4em; margin-top: 5px;">${card.platform}</div>
        </div>
    `;
}

// Update card count display
function updateCardCount() {
    document.querySelector('#cards-drawn-count').textContent = gameState.cardsDrawnThisRound;
    document.querySelector('#side-count').textContent = gameState.platformCardsDrawn.side;
    document.querySelector('#center-count').textContent = gameState.platformCardsDrawn.center;
}

// Render game board
function renderBoard() {
    const board = document.querySelector('#game-board');
    board.innerHTML = '';
    
    // Create 10x10 grid
    for (let y = 0; y < 10; y++) {
        for (let x = 0; x < 10; x++) {
            const cell = document.createElement('div');
            cell.className = 'cell';
            cell.dataset.x = x;
            cell.dataset.y = y;
            
            // Find station at this position
            const station = gameState.stations.find(s => s.x === x && s.y === y);
            
            if (station) {
                // Add background color based on side
                if (station.side) {
                    cell.classList.add(station.side.toLowerCase());
                }
                
                // Create station element
                const stationEl = document.createElement('div');
                stationEl.className = `station type-${station.type}`;
                stationEl.dataset.stationId = station.id;
                
                // Check if it's a starting station
                const isStart = gameState.lines.some(line => line.start === station.id);
                if (isStart) {
                    stationEl.classList.add('start');
                }
                
                // Add train icon if applicable
                if (station.train) {
                    stationEl.classList.add('train');
                }
                
                // Display station type
                let displayText = station.type === '?' ? '🃏' : station.type;
                stationEl.textContent = displayText;
                
                // Add click handler
                stationEl.addEventListener('click', () => handleStationClick(station));
                
                cell.appendChild(stationEl);
            }
            
            board.appendChild(cell);
        }
    }
    
    // Render all segments
    renderSegments();
}

// Render segments
function renderSegments() {
    // Remove existing segments
    document.querySelectorAll('.segment').forEach(seg => seg.remove());
    
    const board = document.querySelector('#game-board');
    const cellSize = board.offsetWidth / 10;
    
    gameState.segments.forEach(segment => {
        const fromStation = gameState.stations.find(s => s.id === segment.from);
        const toStation = gameState.stations.find(s => s.id === segment.to);
        const line = gameState.lines.find(l => l.id === segment.lineId);
        
        if (!fromStation || !toStation || !line) return;
        
        const segmentEl = document.createElement('div');
        segmentEl.className = `segment line-${line.name}`;
        
        const dx = toStation.x - fromStation.x;
        const dy = toStation.y - fromStation.y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        
        if (dx !== 0 && dy === 0) {
            // Horizontal
            segmentEl.classList.add('horizontal');
            const minX = Math.min(fromStation.x, toStation.x);
            segmentEl.style.left = `${(minX * cellSize) + cellSize / 2}px`;
            segmentEl.style.top = `${(fromStation.y * cellSize) + cellSize / 2}px`;
            segmentEl.style.width = `${Math.abs(dx) * cellSize}px`;
        } else if (dx === 0 && dy !== 0) {
            // Vertical
            segmentEl.classList.add('vertical');
            const minY = Math.min(fromStation.y, toStation.y);
            segmentEl.style.left = `${(fromStation.x * cellSize) + cellSize / 2}px`;
            segmentEl.style.top = `${(minY * cellSize) + cellSize / 2}px`;
            segmentEl.style.height = `${Math.abs(dy) * cellSize}px`;
        } else if (Math.abs(dx) === Math.abs(dy)) {
            // Diagonal
            const length = distance * cellSize;
            const angle = Math.atan2(dy, dx) * 180 / Math.PI;
            
            segmentEl.style.width = `${length}px`;
            segmentEl.style.height = '4px';
            segmentEl.style.left = `${(fromStation.x * cellSize) + cellSize / 2}px`;
            segmentEl.style.top = `${(fromStation.y * cellSize) + cellSize / 2}px`;
            segmentEl.style.transform = `rotate(${angle}deg)`;
            segmentEl.style.transformOrigin = '0 50%';
        }
        
        board.appendChild(segmentEl);
    });
}

// Get current line
function getCurrentLine() {
    return gameState.lines[gameState.roundOrder[gameState.currentRoundIndex]];
}

// Get line endpoints
function getLineEndpoints(lineId) {
    const lineSegments = gameState.segments.filter(s => s.lineId === lineId);
    
    if (lineSegments.length === 0) {
        const line = gameState.lines.find(l => l.id === lineId);
        return [line.start];
    }
    
    // Build adjacency map
    const adjacency = new Map();
    lineSegments.forEach(seg => {
        if (!adjacency.has(seg.from)) adjacency.set(seg.from, []);
        if (!adjacency.has(seg.to)) adjacency.set(seg.to, []);
        adjacency.get(seg.from).push(seg.to);
        adjacency.get(seg.to).push(seg.from);
    });
    
    // Find endpoints (nodes with only one connection)
    const endpoints = [];
    adjacency.forEach((neighbors, stationId) => {
        if (neighbors.length === 1) {
            endpoints.push(stationId);
        }
    });
    
    return endpoints;
}

// Get all stations on a line
function getLineStations(lineId) {
    const lineSegments = gameState.segments.filter(s => s.lineId === lineId);
    const stations = new Set();
    
    lineSegments.forEach(seg => {
        stations.add(seg.from);
        stations.add(seg.to);
    });
    
    // Add starting station if no segments yet
    if (stations.size === 0) {
        const line = gameState.lines.find(l => l.id === lineId);
        stations.add(line.start);
    }
    
    return Array.from(stations);
}

// Check if segment is valid
function isValidSegment(fromStationId, toStationId, lineId) {
    const fromStation = gameState.stations.find(s => s.id === fromStationId);
    const toStation = gameState.stations.find(s => s.id === toStationId);
    
    if (!fromStation || !toStation) return false;
    
    const dx = toStation.x - fromStation.x;
    const dy = toStation.y - fromStation.y;
    
    // Must be straight line (horizontal, vertical, or 45° diagonal)
    if (dx !== 0 && dy !== 0 && Math.abs(dx) !== Math.abs(dy)) {
        return false;
    }
    
    // Check if segment already exists
    const segmentExists = gameState.segments.some(s => 
        (s.from === fromStationId && s.to === toStationId) ||
        (s.from === toStationId && s.to === fromStationId)
    );
    if (segmentExists) return false;
    
    // Check if toStation is already on this line
    const lineStations = getLineStations(lineId);
    if (lineStations.includes(toStationId)) return false;
    
    // Check if segment passes through other stations
    if (passesThrough(fromStation, toStation)) {
        return false;
    }
    
    // Check for intersections with other segments
    if (intersectsOtherSegments(fromStation, toStation)) {
        return false;
    }
    
    return true;
}

// Check if segment passes through any station
function passesThrough(from, to) {
    const dx = to.x - from.x;
    const dy = to.y - from.y;
    const steps = Math.max(Math.abs(dx), Math.abs(dy));
    
    if (steps <= 1) return false;
    
    const stepX = dx / steps;
    const stepY = dy / steps;
    
    for (let i = 1; i < steps; i++) {
        const checkX = Math.round(from.x + stepX * i);
        const checkY = Math.round(from.y + stepY * i);
        
        const stationAtPoint = gameState.stations.find(s => s.x === checkX && s.y === checkY);
        if (stationAtPoint) {
            return true;
        }
    }
    
    return false;
}

// Check if segment intersects other segments
function intersectsOtherSegments(from, to) {
    for (const segment of gameState.segments) {
        const segFrom = gameState.stations.find(s => s.id === segment.from);
        const segTo = gameState.stations.find(s => s.id === segment.to);
        
        if (segmentsIntersect(from, to, segFrom, segTo)) {
            return true;
        }
    }
    return false;
}

// Check if two segments intersect
function segmentsIntersect(a1, a2, b1, b2) {
    // Allow intersection only at endpoints (stations)
    const sharesEndpoint = 
        (a1.x === b1.x && a1.y === b1.y) ||
        (a1.x === b2.x && a1.y === b2.y) ||
        (a2.x === b1.x && a2.y === b1.y) ||
        (a2.x === b2.x && a2.y === b2.y);
    
    if (sharesEndpoint) return false;
    
    // Check for actual intersection
    const det = (a2.x - a1.x) * (b2.y - b1.y) - (a2.y - a1.y) * (b2.x - b1.x);
    
    if (det === 0) {
        // Parallel or collinear - check for overlap
        if (a1.x === a2.x && b1.x === b2.x && a1.x === b1.x) {
            // Both vertical on same x
            const aMin = Math.min(a1.y, a2.y);
            const aMax = Math.max(a1.y, a2.y);
            const bMin = Math.min(b1.y, b2.y);
            const bMax = Math.max(b1.y, b2.y);
            return !(aMax < bMin || bMax < aMin);
        }
        if (a1.y === a2.y && b1.y === b2.y && a1.y === b1.y) {
            // Both horizontal on same y
            const aMin = Math.min(a1.x, a2.x);
            const aMax = Math.max(a1.x, a2.x);
            const bMin = Math.min(b1.x, b2.x);
            const bMax = Math.max(b1.x, b2.x);
            return !(aMax < bMin || bMax < aMin);
        }
        return false;
    }
    
    const t = ((b1.x - a1.x) * (b2.y - b1.y) - (b1.y - a1.y) * (b2.x - b1.x)) / det;
    const u = ((b1.x - a1.x) * (a2.y - a1.y) - (b1.y - a1.y) * (a2.x - a1.x)) / det;
    
    return t > 0 && t < 1 && u > 0 && u < 1;
}

// Check if station matches current card
function stationMatchesCard(station, card) {
    if (!card) return false;
    
    if (card.type === 'joker') return true;
    if (station.type === '?') return true; // Joker station (Deák tér)
    
    return station.type === card.type;
}

// Update clickable stations
function updateClickableStations() {
    document.querySelectorAll('.station').forEach(el => {
        el.classList.remove('clickable');
    });
    
    if (!gameState.currentCard) return;
    
    const currentLine = getCurrentLine();
    
    // If switch is active, can draw from any station on the line
    const validStartPoints = gameState.switchActive ? 
        getLineStations(currentLine.id) : 
        getLineEndpoints(currentLine.id);
    
    // Find all valid target stations
    gameState.stations.forEach(station => {
        // Skip if station doesn't match card (unless joker ability is active)
        const cardToMatch = gameState.abilityUsed[currentLine.id] === 'joker-active' ? 
            { type: 'joker' } : gameState.currentCard;
        
        if (!stationMatchesCard(station, cardToMatch)) return;
        
        // Check from each valid start point
        for (const startId of validStartPoints) {
            if (isValidSegment(startId, station.id, currentLine.id)) {
                const stationEl = document.querySelector(`[data-station-id="${station.id}"]`);
                if (stationEl) {
                    stationEl.classList.add('clickable');
                }
                break;
            }
        }
    });
}

// Handle station click
function handleStationClick(station) {
    if (!gameState.currentCard) return;
    
    const currentLine = getCurrentLine();
    
    // Get valid start points (all stations if switch active, otherwise endpoints)
    const validStartPoints = gameState.switchActive ? 
        getLineStations(currentLine.id) : 
        getLineEndpoints(currentLine.id);
    
    // Check card matching (with joker ability)
    const cardToMatch = gameState.abilityUsed[currentLine.id] === 'joker-active' ? 
        { type: 'joker' } : gameState.currentCard;
    
    // Find which start point can connect to this station
    for (const startId of validStartPoints) {
        if (isValidSegment(startId, station.id, currentLine.id) && 
            stationMatchesCard(station, cardToMatch)) {
            
            // Add segment
            gameState.segments.push({
                from: startId,
                to: station.id,
                lineId: currentLine.id
            });
            
            // Check if this is a railway station (count double if heavy traffic)
            if (station.train) {
                const increment = (gameState.heavyTrafficStation === station.id) ? 2 : 1;
                gameState.railwayStationsConnected = Math.min(gameState.railwayStationsConnected + increment, 10);
                updateRailwayDisplay();
            }
            
            // If double draw ability is active, allow one more segment
            if (gameState.abilityUsed[currentLine.id] === 'double-draw-active') {
                gameState.abilityUsed[currentLine.id] = 'double-draw-second';
                updateAbilityDisplay();
            } else {
                // Clear abilities if they were active
                if (gameState.abilityUsed[currentLine.id] === 'joker-active') {
                    gameState.abilityUsed[currentLine.id] = true;
                }
                if (gameState.abilityUsed[currentLine.id] === 'switch-active') {
                    gameState.abilityUsed[currentLine.id] = true;
                }
                if (gameState.abilityUsed[currentLine.id] === 'double-draw-second') {
                    gameState.abilityUsed[currentLine.id] = true;
                }
                
                // Clear current card
                gameState.currentCard = null;
                gameState.switchActive = false;
            }
            
            // Update display
            renderSegments();
            updateScoring();
            displayCurrentCard();
            updateClickableStations();
            updateAbilityDisplay();
            
            return;
        }
    }
}

// Update railway display
function updateRailwayDisplay() {
    document.querySelectorAll('.railway-point').forEach((el, index) => {
        if (index <= gameState.railwayStationsConnected) {
            el.classList.add('active');
        } else {
            el.classList.remove('active');
        }
    });
    
    document.querySelector('#railway-count').textContent = gameState.railwayStationsConnected;
}

// Calculate scoring
function updateScoring() {
    const currentLine = getCurrentLine();
    const lineStations = getLineStations(currentLine.id);
    const lineSegments = gameState.segments.filter(s => s.lineId === currentLine.id);
    
    // PK: Number of districts (count heavy traffic station double)
    const districts = new Set();
    lineStations.forEach(stationId => {
        const station = gameState.stations.find(s => s.id === stationId);
        if (station && station.district) {
            districts.add(station.district);
        }
    });
    const pk = districts.size;
    
    // PM: Maximum stations in a single district (count heavy traffic station as 2)
    const districtCounts = {};
    lineStations.forEach(stationId => {
        const station = gameState.stations.find(s => s.id === stationId);
        if (station && station.district) {
            const count = (gameState.heavyTrafficStation === stationId) ? 2 : 1;
            districtCounts[station.district] = (districtCounts[station.district] || 0) + count;
        }
    });
    const pm = Math.max(0, ...Object.values(districtCounts));
    
    // PD: Number of Danube crossings
    let pd = 0;
    lineSegments.forEach(segment => {
        const from = gameState.stations.find(s => s.id === segment.from);
        const to = gameState.stations.find(s => s.id === segment.to);
        if (from && to && from.side && to.side && from.side !== to.side) {
            pd++;
        }
    });
    
    // FP: Round score
    const fp = (pk * pm) + pd;
    
    // Update display
    document.querySelector('#score-pk').textContent = pk;
    document.querySelector('#score-pm').textContent = pm;
    document.querySelector('#score-pd').textContent = pd;
    document.querySelector('#score-fp').textContent = fp;
    
    updateTotalScore();
}

// Update total score
function updateTotalScore() {
    // Sum of all round scores
    const roundsTotal = gameState.roundScores.reduce((sum, score) => sum + score, 0);
    
    // Railway points
    const railwayPoints = [0, 1, 2, 4, 6, 8, 11, 14, 17, 21, 25];
    const pp = railwayPoints[gameState.railwayStationsConnected] || 0;
    
    // Junction points (count heavy traffic station as 2 for junctions)
    const junctionCounts = {2: 0, 3: 0, 4: 0};
    gameState.stations.forEach(station => {
        const linesAtStation = new Set();
        gameState.segments.forEach(segment => {
            if (segment.from === station.id || segment.to === station.id) {
                linesAtStation.add(segment.lineId);
            }
        });
        
        const count = linesAtStation.size;
        if (count >= 2 && count <= 4) {
            // If heavy traffic, count as 2 junctions
            const increment = (gameState.heavyTrafficStation === station.id) ? 2 : 1;
            junctionCounts[count] += increment;
        }
    });
    
    const junctionPoints = (junctionCounts[2] * 2) + (junctionCounts[3] * 5) + (junctionCounts[4] * 9);
    
    // Final score
    const finalScore = roundsTotal + pp + junctionPoints;
    
    document.querySelector('#score-rounds-total').textContent = roundsTotal;
    document.querySelector('#score-railway').textContent = pp;
    document.querySelector('#score-junctions').textContent = junctionPoints;
    document.querySelector('#score-final').textContent = finalScore;
}

// End round
function endRound() {
    // Save round score
    const fp = parseInt(document.querySelector('#score-fp').textContent);
    gameState.roundScores.push(fp);
    
    // Mark round as completed
    const roundItems = document.querySelectorAll('.round-item');
    if (roundItems[gameState.currentRoundIndex]) {
        roundItems[gameState.currentRoundIndex].classList.remove('active');
        roundItems[gameState.currentRoundIndex].classList.add('completed');
    }
    
    // Move to next round
    gameState.currentRoundIndex++;
    
    if (gameState.currentRoundIndex >= gameState.roundOrder.length) {
        // Game over
        endGame();
        return;
    }
    
    // Start next round
    startRound();
}

// Start round
function startRound() {
    gameState.cardsDrawnThisRound = 0;
    gameState.currentCard = null;
    gameState.platformCardsDrawn = { side: 0, center: 0 };
    gameState.roundEnded = false;
    gameState.switchActive = false;
    initializeDeck();
    
    const currentLine = getCurrentLine();
    const lineDisplay = document.querySelector('#current-line-display');
    lineDisplay.textContent = currentLine.name;
    lineDisplay.className = `line-${currentLine.name}`;
    
    // Reset draw card button
    const drawButton = document.querySelector('#draw-card-button');
    drawButton.textContent = 'Draw Next Card';
    drawButton.style.background = '';
    
    // Update round order display
    const roundItems = document.querySelectorAll('.round-item');
    roundItems.forEach((item, index) => {
        item.classList.remove('active');
        if (index === gameState.currentRoundIndex) {
            item.classList.add('active');
        }
    });
    
    displayCurrentCard();
    updateCardCount();
    updateClickableStations();
    updateScoring();
    updateAbilityDisplay();
}

// End game
function endGame() {
    // Stop timer
    if (gameState.timerInterval) {
        clearInterval(gameState.timerInterval);
    }
    
    const elapsedSeconds = Math.floor((Date.now() - gameState.startTime) / 1000);
    const finalScore = parseInt(document.querySelector('#score-final').textContent);
    
    // Save to local storage
    saveGameResult(gameState.playerName, finalScore, elapsedSeconds);
    
    // Show game over screen
    document.querySelector('#final-player-name').textContent = gameState.playerName;
    document.querySelector('#final-score-display').textContent = finalScore;
    document.querySelector('#final-time-display').textContent = formatTime(elapsedSeconds);
    
    showScreen('game-over-screen');
}

// Save game result
function saveGameResult(name, score, time) {
    const results = JSON.parse(localStorage.getItem('metroGameResults') || '[]');
    results.push({
        name,
        score,
        time,
        date: new Date().toISOString()
    });
    
    // Sort by score descending
    results.sort((a, b) => b.score - a.score);
    
    // Keep top 10
    if (results.length > 10) {
        results.length = 10;
    }
    
    localStorage.setItem('metroGameResults', JSON.stringify(results));
}

// Load and display scores
function loadScores() {
    const results = JSON.parse(localStorage.getItem('metroGameResults') || '[]');
    const scoresList = document.querySelector('#scores-list');
    
    if (results.length === 0) {
        scoresList.innerHTML = '<p class="no-scores">No games played yet!</p>';
        return;
    }
    
    scoresList.innerHTML = results.map((result, index) => `
        <div class="score-entry">
            <span class="rank">#${index + 1}</span>
            <span class="name">${result.name}</span>
            <span class="score">${result.score} pts</span>
            <span class="time">${formatTime(result.time)}</span>
        </div>
    `).join('');
}

// Start game
async function startGame() {
    const playerNameInput = document.querySelector('#player-name');
    const playerName = playerNameInput.value.trim();
    
    if (!playerName) {
        alert('Please enter your name!');
        return;
    }
    
    gameState.playerName = playerName;
    gameState.startTime = Date.now();
    gameState.segments = [];
    gameState.railwayStationsConnected = 0;
    gameState.roundScores = [];
    gameState.currentRoundIndex = 0;
    
    // Randomize round order
    gameState.roundOrder = shuffleArray([0, 1, 2, 3]);
    
    // Assign random abilities to each line
    const abilityTypes = ['double-draw', 'joker', 'switch', 'heavy-traffic'];
    const shuffledAbilities = shuffleArray([...abilityTypes]);
    gameState.roundOrder.forEach((lineIndex, roundIndex) => {
        gameState.abilities[lineIndex] = shuffledAbilities[roundIndex];
        gameState.abilityUsed[lineIndex] = false;
    });
    
    // Display round order
    const roundOrderDisplay = document.querySelector('#round-order-display');
    roundOrderDisplay.innerHTML = gameState.roundOrder.map((lineIndex, roundIndex) => {
        const line = gameState.lines[lineIndex];
        return `<div class="round-item line-${line.name}" data-round="${roundIndex}">${line.name}</div>`;
    }).join('');
    
    // Start timer
    let elapsedSeconds = 0;
    gameState.timerInterval = setInterval(() => {
        elapsedSeconds++;
        document.querySelector('#timer-display').textContent = formatTime(elapsedSeconds);
    }, 1000);
    
    // Display player name
    document.querySelector('#player-name-display').textContent = playerName;
    
    // Render board
    renderBoard();
    
    // Start first round
    startRound();
    
    // Show game screen
    showScreen('game-screen');
}

// Ability Functions
function updateAbilityDisplay() {
    const currentLine = getCurrentLine();
    const abilitySection = document.querySelector('#abilities-section');
    const abilityInfo = document.querySelector('#ability-info');
    const useAbilityButton = document.querySelector('#use-ability-button');
    
    const ability = gameState.abilities[currentLine.id];
    const used = gameState.abilityUsed[currentLine.id];
    
    if (!ability) {
        abilitySection.style.display = 'none';
        return;
    }
    
    abilitySection.style.display = 'block';
    
    const abilityDescriptions = {
        'double-draw': '⚡ Double Draw: Draw two segments this turn',
        'joker': '🃏 Joker: Turn current card into a Joker',
        'switch': '⇄ Switch: Treat current card as Switch',
        'heavy-traffic': '🚦 Heavy Traffic: Mark one station to count as 2'
    };
    
    abilityInfo.innerHTML = `<strong>${abilityDescriptions[ability]}</strong>`;
    
    if (used === true) {
        abilityInfo.classList.add('used');
        useAbilityButton.style.display = 'none';
    } else if (typeof used === 'string' && used.includes('active')) {
        abilityInfo.innerHTML += '<br><em>Ability is active!</em>';
        useAbilityButton.style.display = 'none';
    } else {
        abilityInfo.classList.remove('used');
        useAbilityButton.style.display = 'block';
        useAbilityButton.textContent = 'Use Ability';
    }
}

function useAbility() {
    const currentLine = getCurrentLine();
    const ability = gameState.abilities[currentLine.id];
    
    if (gameState.abilityUsed[currentLine.id]) {
        alert('Ability already used for this line!');
        return;
    }
    
    switch (ability) {
        case 'double-draw':
            if (!gameState.currentCard) {
                alert('Draw a card first!');
                return;
            }
            gameState.abilityUsed[currentLine.id] = 'double-draw-active';
            alert('Double Draw activated! You can draw 2 segments this turn.');
            break;
            
        case 'joker':
            if (!gameState.currentCard) {
                alert('Draw a card first!');
                return;
            }
            gameState.abilityUsed[currentLine.id] = 'joker-active';
            alert('Joker activated! Current card is now a Joker.');
            break;
            
        case 'switch':
            if (!gameState.currentCard) {
                alert('Draw a card first!');
                return;
            }
            gameState.abilityUsed[currentLine.id] = 'switch-active';
            gameState.switchActive = true;
            alert('Switch activated! You can draw from any station on this line.');
            break;
            
        case 'heavy-traffic':
            alert('Click on a station to mark it for Heavy Traffic (counts as 2 for all scoring).');
            gameState.abilityUsed[currentLine.id] = 'selecting-station';
            // Enable clicking on any station on current line
            const lineStations = getLineStations(currentLine.id);
            document.querySelectorAll('.station').forEach(el => {
                const stationId = parseInt(el.dataset.stationId);
                if (lineStations.includes(stationId)) {
                    el.style.cursor = 'pointer';
                    el.style.border = '4px solid orange';
                    el.addEventListener('click', function selectHeavyTraffic() {
                        gameState.heavyTrafficStation = stationId;
                        gameState.abilityUsed[currentLine.id] = true;
                        el.style.border = '5px solid red';
                        el.innerHTML += '<div style="position:absolute;top:-10px;right:-10px;font-size:1.5em;">🚦</div>';
                        document.querySelectorAll('.station').forEach(s => s.style.cursor = '');
                        alert(`Heavy Traffic set at station ${stationId}! It now counts as 2 stations.`);
                        updateAbilityDisplay();
                        updateScoring();
                        el.removeEventListener('click', selectHeavyTraffic);
                    }, { once: true });
                }
            });
            break;
    }
    
    updateAbilityDisplay();
    updateClickableStations();
}

// Event Listeners
document.addEventListener('click', (e) => {
    const target = e.target;
    
    // Blue heart easter egg
    if (target.textContent === '💙') {
        const colors = ['💙', '❤️', '💚', '💛', '💜', '🧡'];
        const currentIndex = colors.indexOf(target.textContent);
        target.textContent = colors[(currentIndex + 1) % colors.length];
    }
});

document.querySelector('#start-button').addEventListener('click', startGame);

document.querySelector('#rules-button').addEventListener('click', () => {
    showScreen('rules-screen');
});

document.querySelector('#scores-button').addEventListener('click', () => {
    loadScores();
    showScreen('scores-screen');
});

document.querySelector('#back-to-menu').addEventListener('click', () => {
    showScreen('menu-screen');
});

document.querySelector('#back-to-menu-scores').addEventListener('click', () => {
    showScreen('menu-screen');
});

document.querySelector('#back-to-menu-final').addEventListener('click', () => {
    showScreen('menu-screen');
});

document.querySelector('#draw-card-button').addEventListener('click', () => {
    if (gameState.roundEnded) {
        endRound();
    } else {
        drawCard();
    }
});

document.querySelector('#use-ability-button').addEventListener('click', () => {
    useAbility();
});

// Initialize
loadGameData().then(success => {
    if (success) {
        showScreen('menu-screen');
    }
});
