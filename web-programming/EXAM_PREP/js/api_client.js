async function fetchApi(url, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(url, options);
    if (!response.ok) throw new Error('Network response was not ok');
    return await response.json();
}

// Usage examples:
// await fetchApi('api.php?task=list');
// await fetchApi('api.php', 'POST', { name: 'John' });
