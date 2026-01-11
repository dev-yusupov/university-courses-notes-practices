<?php
require_once 'config.php';

// Handle AJAX vote requests
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_SERVER['HTTP_X_REQUESTED_WITH'])) {
    if (!isLoggedIn()) {
        jsonResponse(['success' => false, 'error' => 'Not logged in']);
    }
    
    $action = $_POST['action'] ?? '';
    $projectId = isset($_POST['project_id']) && is_numeric($_POST['project_id']) ? (int)$_POST['project_id'] : 0;
    
    if (!$projectId) {
        jsonResponse(['success' => false, 'error' => 'Invalid project']);
    }
    
    $userId = getCurrentUserId();
    
    // Get project info
    $projects = readJSON('projects.json');
    $project = null;
    foreach ($projects as $p) {
        if ($p['id'] == $projectId) {
            $project = $p;
            break;
        }
    }
    
    if (!$project || $project['status'] !== 'approved') {
        jsonResponse(['success' => false, 'error' => 'Project not found or not approved']);
    }
    
    if (!isVotingOpen($project['published_at'])) {
        jsonResponse(['success' => false, 'error' => 'Voting period has ended']);
    }
    
    $votes = readJSON('votes.json');
    
    if ($action === 'vote') {
        // Check if user already voted
        if (hasUserVoted($userId, $projectId)) {
            jsonResponse(['success' => false, 'error' => 'Already voted']);
        }
        
        // Check category vote limit
        $votesInCategory = getUserVotesInCategory($userId, $project['category_id']);
        if ($votesInCategory >= 3) {
            jsonResponse(['success' => false, 'error' => 'Maximum 3 votes per category']);
        }
        
        // Cast vote
        $newVote = [
            'id' => getNextId($votes),
            'user_id' => $userId,
            'project_id' => $projectId,
            'voted_at' => date('c')
        ];
        $votes[] = $newVote;
        
        if (writeJSON('votes.json', $votes)) {
            $voteCount = getVoteCount($projectId);
            jsonResponse([
                'success' => true, 
                'voteCount' => $voteCount,
                'votesInCategory' => $votesInCategory + 1
            ]);
        } else {
            jsonResponse(['success' => false, 'error' => 'Failed to cast vote']);
        }
        
    } elseif ($action === 'withdraw') {
        // Withdraw vote
        $newVotes = [];
        $found = false;
        foreach ($votes as $vote) {
            if ($vote['user_id'] == $userId && $vote['project_id'] == $projectId) {
                $found = true;
                continue; // Skip this vote (delete it)
            }
            $newVotes[] = $vote;
        }
        
        if ($found && writeJSON('votes.json', $newVotes)) {
            $voteCount = getVoteCount($projectId);
            $votesInCategory = getUserVotesInCategory($userId, $project['category_id']);
            jsonResponse([
                'success' => true, 
                'voteCount' => $voteCount,
                'votesInCategory' => $votesInCategory
            ]);
        } else {
            jsonResponse(['success' => false, 'error' => 'Failed to withdraw vote']);
        }
    }
    
    jsonResponse(['success' => false, 'error' => 'Invalid action']);
}
