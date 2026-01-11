// Voting functionality using Fetch API

function castVote(projectId) {
    fetch('vote.php', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: `action=vote&project_id=${projectId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update UI
            updateVoteUI(projectId, true, data.voteCount);
            showMessage('Vote cast successfully!', 'success');
        } else {
            showMessage(data.error || 'Failed to cast vote', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('An error occurred. Please try again.', 'error');
    });
}

function withdrawVote(projectId) {
    if (!confirm('Are you sure you want to withdraw your vote?')) {
        return;
    }
    
    fetch('vote.php', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: `action=withdraw&project_id=${projectId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update UI
            updateVoteUI(projectId, false, data.voteCount);
            showMessage('Vote withdrawn successfully!', 'success');
        } else {
            showMessage(data.error || 'Failed to withdraw vote', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('An error occurred. Please try again.', 'error');
    });
}

function updateVoteUI(projectId, hasVoted, voteCount) {
    // Find all instances of this project (could be on index page or project page)
    const projectElements = document.querySelectorAll(`[data-project-id="${projectId}"]`);
    
    projectElements.forEach(element => {
        // Update vote count
        const voteCountElement = element.querySelector('.vote-count');
        if (voteCountElement) {
            voteCountElement.textContent = `${voteCount} vote${voteCount !== 1 ? 's' : ''}`;
        }
        
        // Update button
        const voteButton = element.querySelector('.btn-vote');
        if (voteButton && !voteButton.disabled) {
            if (hasVoted) {
                voteButton.textContent = 'Withdraw Vote';
                voteButton.classList.add('voted');
                voteButton.onclick = function() { withdrawVote(projectId); };
            } else {
                voteButton.textContent = voteButton.closest('.project-item') ? 'Vote' : 'Vote for this Project';
                voteButton.classList.remove('voted');
                voteButton.onclick = function() { castVote(projectId); };
            }
        }
    });
}

function showMessage(message, type) {
    // Create message element
    const messageDiv = document.createElement('div');
    messageDiv.className = type === 'success' ? 'success' : 'error';
    messageDiv.textContent = message;
    messageDiv.style.position = 'fixed';
    messageDiv.style.top = '20px';
    messageDiv.style.right = '20px';
    messageDiv.style.zIndex = '9999';
    messageDiv.style.minWidth = '300px';
    messageDiv.style.animation = 'slideIn 0.3s ease-out';
    
    document.body.appendChild(messageDiv);
    
    // Remove after 3 seconds
    setTimeout(() => {
        messageDiv.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => {
            document.body.removeChild(messageDiv);
        }, 300);
    }, 3000);
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
