# JavaScript Exam Cheatsheet

## 📑 Table of Contents

### **1. Core JavaScript**
- [1.1 Array Methods](#11-array-methods)
- [1.2 String Methods](#12-string-methods)
- [1.3 Math & Random](#13-math--random)
- [1.4 Date & Time](#14-date--time)

### **2. DOM Manipulation**
- [2.1 DOM Selection](#21-dom-selection)
- [2.2 Modifying Elements](#22-modifying-elements)
- [2.3 Creating Elements](#23-creating-elements)

### **3. Events**
- [3.1 Event Handling](#31-event-handling)
- [3.2 Event Delegation](#32-event-delegation)
- [3.3 Common Events](#33-common-events)
- [3.4 Event Propagation](#34-event-propagation)

### **4. Forms**
- [4.1 Input Types](#41-input-types)
- [4.2 Form Events](#42-form-events)
- [4.3 Form Validation](#43-form-validation)
- [4.4 Getting Form Data](#44-getting-form-data)

### **5. Canvas API**
- [5.1 Initializing Canvas](#51-initializing-canvas)
- [5.2 Drawing Shapes](#52-drawing-shapes)
- [5.3 Images](#53-images)
- [5.4 Angles Formula](#54-angles-formula)

### **6. Asynchronous JavaScript**
- [6.1 Fetch API - GET](#61-fetch-api---get)
- [6.2 Fetch API - POST](#62-fetch-api---post)
- [6.3 Async/Await](#63-asyncawait)
- [6.4 Error Handling](#64-error-handling)
- [6.5 Parallel Requests](#65-parallel-requests)

### **7. Timers**
- [7.1 setTimeout](#71-settimeout)
- [7.2 setInterval](#72-setinterval)
- [7.3 setTimeout vs setInterval](#73-settimeout-vs-setinterval)
- [7.4 Sleep/Delay](#74-sleepdelay)

### **8. Storage**
- [8.1 Local Storage API](#81-local-storage-api)

---

## 1. Core JavaScript

### 1.1 Array Methods

```javascript
const numbers = [1, 5, 10, 15];
const users = [
    { id: 1, name: 'Anna', age: 22 },
    { id: 2, name: 'Bob', age: 17 }
];

// 1. FILTER: Returns specific items (Array -> Array)
const adults = users.filter(u => u.age >= 18); 

// 2. MAP: Transforms every item (Array -> Array)
const names = users.map(u => u.name); // ['Anna', 'Bob']
const html = users.map(u => `<li>${u.name}</li>`).join(''); // Great for generating lists!

// 3. FIND: Returns the FIRST match (Array -> Item)
const anna = users.find(u => u.id === 1);

// 4. SOME / EVERY: Returns Boolean (Array -> Boolean)
const hasMinors = users.some(u => u.age < 18); // true
const allAdults = users.every(u => u.age >= 18); // false

// 5. SORT: Mutates the array! (Returns Array)
// a - b = Ascending (1, 2, 3)
// b - a = Descending (3, 2, 1)
users.sort((a, b) => a.age - b.age); 

// 6. REDUCE: Accumulate values (Array -> Single Value)
// Total age: start at 0
const totalAge = users.reduce((sum, u) => sum + u.age, 0);
```

### 1.2 String Methods

```js
const str = "Hello World";
str.includes("World");  // true
str.startsWith("He");   // true
str.split(" ");         // ["Hello", "World"] (String to Array)
str.slice(0, 5);        // "Hello"
str.toLowerCase();      // "hello world"
parseInt("42px");       // 42 (Extracts number)
```

### 1.3 Math & Random

```js
// Get random integer between min and max (inclusive)
function randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// Get random color
function randomColor() {
    return `rgb(${randomInt(0, 255)}, ${randomInt(0, 255)}, ${randomInt(0, 255)})`;
}

// Distance between two points (Pythagoras) - collision detection
function getDistance(x1, y1, x2, y2) {
    return Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2);
}
```

### 1.4 Date & Time

```js
const now = new Date();

// Getters
now.getFullYear();  // 2023
now.getMonth();     // 0-11 (0 is Jan, 11 is Dec) - TRAP!
now.getDate();      // 1-31 (Day of month)
now.getDay();       // 0-6 (0 is Sunday) - TRAP!
now.getHours();     // 0-23
now.getMinutes();   // 0-59
now.getTime();      // Timestamp (milliseconds since 1970)

// Creating a specific date
const deadline = new Date('2023-12-31');
```

---

## 2. DOM Manipulation

### 2.1 DOM Selection

```js
const btn  = document.querySelector('#submit-btn');   // Single element (CSS syntax)
const rows = document.querySelectorAll('table tr');   // NodeList (Array-like)

// Converting NodeList to Array (to use .map or .filter)
const rowsArray = Array.from(rows);
```

### 2.2 Modifying Elements

```js
// CONTENT
el.textContent = 'New Text';       // Safe (escapes HTML)
el.innerHTML   = '<span>Hi</span>';// Renders HTML (careful of XSS)

// CLASSES
el.classList.add('active');
el.classList.remove('hidden');
el.classList.toggle('selected');
el.classList.contains('active'); // boolean

// STYLES
el.style.backgroundColor = 'red'; // camelCase
el.style.display = 'none';

// ATTRIBUTES
el.setAttribute('src', 'img.jpg');
el.getAttribute('data-id');
el.dataset.id = 5; // Shortcut for data-id
```

### 2.3 Creating Elements

```js
// The Long Way
const div = document.createElement('div');
div.classList.add('card');
div.textContent = 'Hello';
document.body.appendChild(div);

// The Fast Way (Modern)
document.body.insertAdjacentHTML('beforeend', `
    <div class="card">Hello</div>
`);
```

## Event Handling

```js
button.addEventListener('click', (event) => {
    event.preventDefault(); // Stop form submission
    console.log('Clicked!');
});
```

### 3.2 Event Delegation

```js
// Attach listener to the PARENT (e.g., <ul> or <table>), not the children
listParent.addEventListener('click', (e) => {
    // Check if the clicked thing (or its parent) matches your selector
    const btn = e.target.closest('.delete-btn');
    
    // Ensure we actually clicked inside the parent (sanity check)
    if (btn && listParent.contains(btn)) {
        // Handle the click
        const id = btn.dataset.id;
        console.log('Deleting item:', id);
    }
});
```

### 3.3 Common Events

- `click` - Mouse click
- `submit` - Form submission
- `input` - Input value changed
- `change` - Input lost focus after change
- `keydown` / `keyup` - Keyboard key pressed/released

---

## 5. Canvas API

### 5.1 Initializing Canvas

```js
const canvas = document.querySelector('canvas');
const ctx = canvas.getContext('2d');
```

### 5.2 Drawing Shapes

```js
// Rectangle
// Rectangles
ctx.fillStyle = 'red';              // Set color
ctx.fillRect(x, y, w, h);           // Filled square
ctx.strokeStyle = 'blue';           // Border color
ctx.strokeRect(x, y, w, h);         // Border only
ctx.clearRect(0, 0, width, height); // Erase everything

// Paths (Lines, Custom Shapes)
ctx.beginPath();
ctx.moveTo(50, 50);   // Start point
ctx.lineTo(100, 100); // Draw line
ctx.lineTo(100, 50);
ctx.closePath();      // Auto-connect back to start
ctx.fill();           // Fill the shape

// Circles (Arcs)
ctx.beginPath();
// x, y, radius, startAngle, endAngle
ctx.arc(100, 100, 50, 0, Math.PI * 2); 
ctx.fill();
```

### 5.3 Images

```js
const img = new Image();
img.src = 'player.png';
img.onload = () => {
    ctx.drawImage(img, x, y, width, height);
};
```

### 5.4 Angles Formula

If you get stuck on the angles in the exam: **Full Circle = Math.PI * 2 (Radians).** Canvas starts 0 degrees at "3 o'clock". To move start to "12 o'clock", subtract 90 degrees (Math.PI / 2). 

**Formula:** $$Angle = \left(\frac{CurrentValue}{MaxValue}\right) \times 2\pi - \frac{\pi}{2}$$

**Example (Seconds):**
```js
const secondAngle = (seconds / 60) * (Math.PI * 2) - (Math.PI / 2);
```

---

## 7. Timers

### 7.1 setTimeout

```js
// Separate function
function tick() {
  console.log('tick')
}
setTimeout(tick, 1000)

// Function literal
setTimeout(function () {
  console.log('tick')
}, 1000)

// Clear timer
const timer = setTimeout(() => {}, 1000)
// do something, or even in an event:
clearTimeout(timer)
```

---

## 6. Asynchronous JavaScript

### 6.1 Fetch API - GET

```js
fetch('api.php?action=list')
    .then(response => response.json())
    .then(data => {
        console.log(data); // Process array here
    })
    .catch(err => console.error(err));
```

### 6.2 Fetch API - POST

```js
const data = { username: 'john', score: 10 };

fetch('save_score.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
})
.then(resp => resp.json())
.then(result => console.log('Saved!', result));
```

---

## 7. Timers (continued)

### 7.2 setInterval

```js
// Separate function
function tick() {
  console.log('tick')
}
setInterval(tick, 1000)

// In-place function
setInterval(function () {
  console.log('tick')
}, 1000)

// Clear timer
const timer = setInterval(() => {}, 1000)
// do something, or even in an event:
clearInterval(timer)
```

### 7.3 setTimeout vs setInterval

- `setInterval` runs repeatedly at fixed intervals.
- `setTimeout` runs once after a delay. To repeat, you must call it again within the function.
- `setTimeout` is often preferred for animations to avoid overlapping calls if the function takes longer than the interval time.

---

## 8. Storage

### 8.1 Local Storage API

```js
// Storing values
localStorage.setItem("foo") = 42;
// or
localStorage.bar = 42;

// Reading values
console.log(localStorage.getItem("foo"));
// or
console.log(localStorage.bar);

// Storing complex data
const data = [1, 3, 5, 7];
localStorage.setItem('data', JSON.stringify(data));
const loadedData = JSON.parse(localStorage.getItem('data'));
```

---

## 4. Forms

### 4.1 Input Types

| HTML Input Type | JS Property | What it returns | Exam Use Case |
|-----------------|-------------|-----------------|---------------|
| `<input type="number">` | `el.valueAsNumber` | Number (or NaN) | Calculators. Saves you from writing parseFloat(). |
| `<input type="date">` | `el.valueAsDate` | Date Object | Timers/Calendars. Returns a real JS Date object. |
| `<input type="checkbox">` | `el.checked` | Boolean (true/false) | Filters. Never use .value for checkboxes. |
| `<input type="file">` | `el.files` | FileList (Array-like) | Image Previews. Contains the file binary data. |

```js
// HTML: 
// <input type="radio" name="gender" value="male">
// <input type="radio" name="gender" value="female">

const radios = form.elements.gender; // Returns a RadioNodeList (collection)
const selectedValue = radios.value;  // Returns the value of the CHECKED one directly!
```

```js
const select = form.elements.country;

// Get selected value (e.g., "US")
console.log(select.value);

// Get the text inside the option (e.g., "United States")
const text = select.options[select.selectedIndex].text;

// Get multiple values (if <select multiple>)
// You must loop manually, .value only returns the first one.
const selected = Array.from(select.selectedOptions).map(opt => opt.value);
```

```js
const emailInput = form.elements.email;

emailInput.addEventListener('input', () => {
    if (emailInput.value.endsWith('@elte.hu')) {
        emailInput.setCustomValidity(''); // Valid
    } else {
        emailInput.setCustomValidity('You must use an ELTE address!'); // Invalid
    }
});

// To trigger the bubble:
form.addEventListener('submit', (e) => {
    if (!form.checkValidity()) {
        e.preventDefault(); // Stop submit
        // The browser automatically shows the error bubbles here
    }
});
```

### 4.2 Form Events

| Event | Fired When | Use Case |
|-------|------------|----------|
| `submit` | Form is submitted (Enter key or Button) | Validation & Data Sending. Always preventDefault(). |
| `input` | Value changes (every keystroke) | Real-time Search or Character Count. |
| `change` | Value changes + focus lost | Dropdowns (Select) or Checkboxes. |
| `focus` / `blur` | Cursor enters/leaves field | Highlighting active fields or "Touched" validation. |

```js
// Get all form data as an Object
function getFormData(formElement) {
    const formData = new FormData(formElement);
    // Convert to a plain JavaScript Object
    return Object.fromEntries(formData.entries());
}

// Usage
form.addEventListener('submit', (e) => {
    e.preventDefault();
    const data = getFormData(form);
    console.log(data); // { username: "John", age: "22", gender: "male" }
});
```

### 4.3 Form Validation

```js
const emailInput = form.elements.email;

emailInput.addEventListener('input', () => {
    if (emailInput.value.endsWith('@elte.hu')) {
        emailInput.setCustomValidity(''); // Valid
    } else {
        emailInput.setCustomValidity('You must use an ELTE address!'); // Invalid
    }
});

// To trigger the bubble:
form.addEventListener('submit', (e) => {
    if (!form.checkValidity()) {
        e.preventDefault(); // Stop submit
        // The browser automatically shows the error bubbles here
    }
});
```

### 4.4 Getting Form Data

```js
// Get all form data as an Object
function getFormData(formElement) {
    const formData = new FormData(formElement);
    // Convert to a plain JavaScript Object
    return Object.fromEntries(formData.entries());
}

// Usage
form.addEventListener('submit', (e) => {
    e.preventDefault();
    const data = getFormData(form);
    console.log(data); // { username: "John", age: "22", gender: "male" }
});
```

### 3.4 Event Propagation

```js
button.addEventListener('click', (e) => {
    e.stopPropagation(); // Stops the event from going to the parent
    // e.preventDefault(); // Prevents default browser action (like submitting form)
});
```

### 6.3 Async/Await

```js
async function loadData() {
    // 1. Await the fetch call
    const response = await fetch('api.php?action=list');
    
    // 2. Await the JSON parsing
    const data = await response.json();
    
    // 3. Use data
    console.log(data);
}

loadData();
```

### 6.4 Error Handling

```js
async function safeLoad() {
    try {
        const response = await fetch('api.php');
        
        if (!response.ok) {
            throw new Error(`HTTP Error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Success:', data);

    } catch (error) {
        console.error('Failed to load:', error.message);
        // Update UI to show error
        document.querySelector('#error-msg').textContent = 'Connection failed!';
    }
}
```

### 6.5 Parallel Requests

```js
async function loadAll() {
    try {
        // Start both requests instantly
        const [usersResp, postsResp] = await Promise.all([
            fetch('api.php?data=users'),
            fetch('api.php?data=posts')
        ]);

        const users = await usersResp.json();
        const posts = await postsResp.json();

        console.log(users, posts);

    } catch (err) {
        console.error('One or both failed', err);
    }
}
```

### 7.4 Sleep/Delay

```js
// 1. Create the helper
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// 2. Use inside an async function
async function demo() {
    console.log('Start');
    await sleep(2000); // Pauses execution for 2 seconds
    console.log('End');
}
```
