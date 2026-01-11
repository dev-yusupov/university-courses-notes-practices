// === SHORTCUTS ===
const $ = (s) => document.querySelector(s);
const $$ = (s) => document.querySelectorAll(s);

// === ELEMENT CREATION ===
// Usage: const btn = create('button', { class: 'btn-red', 'data-id': 5 }, 'Delete');
function create(tag, attrs = {}, ...children) {
    const el = document.createElement(tag);
    for (const [key, val] of Object.entries(attrs)) {
        if (key.startsWith('data-')) el.dataset[key.slice(5)] = val;
        else if (key === 'class') el.className = val; // safer for class
        else el[key] = val;
    }
    children.forEach(child => {
        if (typeof child === 'string' || typeof child === 'number') {
            el.appendChild(document.createTextNode(child));
        } else if (child instanceof Node) el.appendChild(child);
    });
    return el;
}

// === EVENT DELEGATION (CRITICAL) ===
// Usage: delegate(table, 'click', '.delete-btn', (e) => removeRow(e));
function delegate(parent, type, selector, handler) {
    parent.addEventListener(type, e => {
        const target = e.target.closest(selector);
        if (target && parent.contains(target)) handler(e, target);
    });
}
