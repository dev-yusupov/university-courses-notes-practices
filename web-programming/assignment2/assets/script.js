document.addEventListener('DOMContentLoaded', () => {
    const grid = document.getElementById('projects-grid');
    const filter = document.getElementById('category-filter');

    if (grid) {
        loadProjects();
    }

    if (filter) {
        filter.addEventListener('change', () => {
            loadProjects(filter.value);
        });
    }

    async function loadProjects(category = '') {
        try {
            const url = category !== '' ? `api/get_projects.php?category=${encodeURIComponent(category)}` : 'api/get_projects.php';
            const res = await fetch(url);
            const json = await res.json();

            // Handle both legacy array and new wrapper
            const projects = Array.isArray(json) ? json : (json.data || []);
            const meta = json.meta || {};

            renderProjectsGrouped(projects, meta, category);
        } catch (e) {
            console.error(e);
            grid.innerHTML = '<p style="color:red">Failed to load projects.</p>';
        }
    }

    function renderProjectsGrouped(projects, meta, activeFilter) {
        grid.innerHTML = '';
        if (projects.length === 0) {
            grid.innerHTML = '<p>No projects found.</p>';
            return;
        }

        // Grouping
        const groups = {};
        // Categories 0 to 3
        const cats = [0, 1, 2, 3];
        cats.forEach(c => groups[c] = []);

        projects.forEach(p => {
            if (!groups[p.category]) groups[p.category] = [];
            groups[p.category].push(p);
        });

        const container = document.createElement('div');
        container.style.width = '100%';

        // Iterate categories
        cats.forEach(catId => {
            const catProjects = groups[catId];
            if (!catProjects) return;

            // Filter logic
            // Check if activeFilter is SET (string '0' is truthy, '' is falsy)
            if (activeFilter !== '' && parseInt(activeFilter) !== catId) return;
            if (activeFilter === '' && catProjects.length === 0) return;
            if (activeFilter !== '' && catProjects.length === 0) return;

            // Category Header
            const header = document.createElement('h2');
            header.style.marginTop = '40px';
            header.style.marginBottom = '20px';
            header.style.borderBottom = `3px solid ${getCategoryColor(catId)}`;
            header.style.paddingBottom = '10px';
            header.textContent = getCategoryLabel(catId);

            // Votes Left Info
            if (meta.loggedIn && meta.votesLeft && meta.votesLeft[catId] !== undefined) {
                const span = document.createElement('span');
                span.style.fontSize = '0.9rem';
                span.style.fontWeight = 'normal';
                span.style.marginLeft = '15px';
                span.style.color = '#555';
                span.textContent = `(You have ${meta.votesLeft[catId]} votes remaining)`;
                header.appendChild(span);
            }

            container.appendChild(header);

            // Grid string for this category
            const catGrid = document.createElement('div');
            catGrid.className = 'grid';

            // ... (rest same, loop projects)
            catProjects.forEach(p => {
                const card = createProjectCard(p, meta);
                catGrid.appendChild(card);
            });

            container.appendChild(catGrid);
        });

        grid.appendChild(container);

        document.querySelectorAll('.vote-btn').forEach(btn => {
            btn.addEventListener('click', handleVote);
        });
    }

    function createProjectCard(p, meta) {
        const card = document.createElement('div');
        card.className = 'card';
        card.style.borderTop = `5px solid ${getCategoryColor(p.category)}`;

        const desc = p.description.replace(/\n/g, '<br>');
        const imageHtml = p.image ? `<img src="${escapeHtml(p.image)}" alt="Project Image" style="max-width:100%; height:auto; border-radius:4px; margin-bottom:10px;">` : '';
        const postalBadge = p.postal_code ? `<span class="badge" style="background:#555">Zip: ${p.postal_code}</span>` : '';

        // Voting Window Check (2 weeks from approved)
        let votingClosed = false;
        if (p.approved) {
            const approvedDate = new Date(p.approved);
            const now = new Date();
            const diffDays = (now - approvedDate) / (1000 * 60 * 60 * 24);
            if (diffDays > 14) votingClosed = true;
        }

        let voteBtnState = '';
        let voteBtnText = 'Vote';
        let voteBtnStyle = '';

        if (!meta.loggedIn) {
            voteBtnState = 'disabled';
            voteBtnText = 'Login to Vote';
        } else if (votingClosed) {
            voteBtnState = 'disabled';
            voteBtnText = 'Voting Closed';
            voteBtnStyle = 'background-color: #95a5a6; cursor: not-allowed;';
            card.style.opacity = '0.8'; // Visual cue
            card.style.backgroundColor = '#f4f4f4';
        } else if (p.userVoted) {
            voteBtnText = 'Retract Vote'; // Withdrawal allowed
            voteBtnStyle = 'background-color: #e74c3c;';
        } else {
            // Check if limit reached for category
            if (meta.votesLeft && meta.votesLeft[p.category] <= 0) {
                voteBtnState = 'disabled';
                voteBtnText = 'No votes left';
                voteBtnStyle = 'background-color: #95a5a6;';
            }
        }

        card.innerHTML = `
            ${imageHtml}
            <h3><a href="details.php?id=${p.id}" style="text-decoration:none; color:inherit;">${escapeHtml(p.title)}</a></h3>
            <div style="margin-bottom:10px;">
                <span class="badge" style="background:${getCategoryColor(p.category)}">${getCategoryLabel(p.category)}</span>
                ${postalBadge}
            </div>
            <p>Votes: <strong>${p.voteCount}</strong></p>
            <p>${desc}</p>
            <div class="actions">
                <button class="vote-btn" data-id="${p.id}" ${voteBtnState} style="${voteBtnStyle}">
                    ${voteBtnText}
                </button>
            </div>
        `;
        return card;
    }

    async function handleVote(e) {
        const btn = e.target;
        const id = btn.dataset.id;

        // Optimistic UI update or wait? Let's wait for result.
        btn.textContent = '...';
        btn.disabled = true;

        try {
            const res = await fetch('api/vote.php', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ projectId: id })
            });
            const data = await res.json();

            if (data.success) {
                btn.textContent = 'Voted';
                // Reload to update counts properly or increment locally
                // Ideally reload specific card or increment locally.
                loadProjects(filter ? filter.value : '');
            } else {
                alert(data.error || 'Failed to vote');
                btn.textContent = 'Vote';
                btn.disabled = false;
            }
        } catch (e) {
            console.error(e);
            alert('Network error');
            btn.textContent = 'Vote';
            btn.disabled = false;
        }
    }

    function getCategoryColor(cat) {
        cat = parseInt(cat);
        switch (cat) {
            case 0: return '#f1c40f'; // small
            case 1: return '#e67e22'; // large
            case 2: return '#9b59b6'; // equal
            case 3: return '#2ecc71'; // green
            default: return '#95a5a6';
        }
    }

    function getCategoryLabel(cat) {
        cat = parseInt(cat);
        const map = {
            0: 'Local small project',
            1: 'Local large project',
            2: 'Equal opportunity Budapest',
            3: 'Green Budapest'
        };
        return map[cat] || 'Unknown';
    }

    function escapeHtml(text) {
        if (!text) return '';
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
    }
});
