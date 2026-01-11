// 1. SELECTOR HELPER
function $(selector) {
    return document.querySelector(selector);
}

// 2. ELEMENT CREATOR (Fast HTML generation)
// Usage: createElement('div', { class: 'card' }, 'Hello World')
function createElement(tag, attributes = {}, ...children) {
    const element = document.createElement(tag);
    for (const [key, value] of Object.entries(attributes)) {
        if (key.startsWith('data-')) {
            element.dataset[key.slice(5)] = value;
        } else {
            element[key] = value;
        }
    }
    for (const child of children) {
        if (typeof child === 'string' || typeof child === 'number') {
            element.appendChild(document.createTextNode(child));
        } else if (child instanceof Node) {
            element.appendChild(child);
        }
    }
    return element;
}

// 3. EVENT DELEGATION (Crucial for dynamically added items!)
// Usage: delegate(table, 'click', '.delete-btn', handleDelete)
function delegate(parent, type, selector, handler) {
    parent.addEventListener(type, event => {
        const targetElement = event.target.closest(selector);
        if (targetElement && parent.contains(targetElement)) {
            handler.call(targetElement, event);
        }
    });
}
