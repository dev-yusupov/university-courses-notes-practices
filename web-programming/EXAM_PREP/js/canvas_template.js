const canvas = document.querySelector('canvas');
const ctx = canvas.getContext('2d');

// Game State
const state = {
    running: false,
    entities: [],
    lastTime: performance.now()
};

function update(dt) {
    // Logic: Move objects here
    // state.entities.forEach(e => e.x += e.vx * dt);
}

function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // Draw logic here
    // ctx.fillStyle = 'red';
    // ctx.fillRect(10, 10, 50, 50);
}

function loop(now) {
    const dt = (now - state.lastTime) / 1000;
    state.lastTime = now;

    update(dt);
    draw();

    if (state.running) requestAnimationFrame(loop);
}

// Start
// state.running = true;
// loop(performance.now());