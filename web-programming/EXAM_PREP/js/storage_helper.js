// Save data (must be a string, so we use JSON.stringify)
function save(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

// Load data (parse it back to object/array)
function load(key, defaultVal = []) {
    const data = localStorage.getItem(key);
    return data ? JSON.parse(data) : defaultVal;
}

// Usage
// save('todos', [{text: 'Buy milk'}]);
// const todos = load('todos');
