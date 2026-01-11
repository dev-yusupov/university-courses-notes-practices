const canvas = $('canvas');
const ctx = canvas.getContext('2d');

// === CONFIG ===
const GAME = {
    w: canvas.width,
    h: canvas.height,
    lastTime: performance.now(),
    objects: []
};

// === HELPER: DETECT COLLISION ===
function isColliding(rect1, rect2) {
    return (
        rect1.x < rect2.x + rect2.w &&
        rect1.x + rect1.w > rect2.x &&
        rect1.y < rect2.y + rect2.h &&
        rect1.y + rect1.h > rect2.y
    );
}

// === MAIN LOOP ===
function gameLoop(now) {
    const dt = (now - GAME.lastTime) / 1000; // Delta time in seconds
    GAME.lastTime = now;

    update(dt);
    draw();

    requestAnimationFrame(gameLoop);
}

// === EDIT THESE TWO ===
function update(dt) {
    // Example: Move all objects
    // GAME.objects.forEach(obj => {
    //     obj.x += obj.vx * dt;
    //     if(obj.x > GAME.w) obj.vx *= -1; // Bounce logic
    // });
}

function draw() {
    ctx.clearRect(0, 0, GAME.w, GAME.h);
    // GAME.objects.forEach(obj => {
    //     ctx.fillRect(obj.x, obj.y, obj.w, obj.h);
    // });
}

// Start
// requestAnimationFrame(gameLoop);
