<?php
// JSON Data Storage Configuration
define('DATA_DIR', __DIR__ . '/data/');

// Start session if not already started
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// JSON file operations
function readJSON($filename) {
    $filepath = DATA_DIR . $filename;
    if (!file_exists($filepath)) {
        return [];
    }
    $content = file_get_contents($filepath);
    return json_decode($content, true) ?: [];
}

function writeJSON($filename, $data) {
    $filepath = DATA_DIR . $filename;
    $dir = dirname($filepath);
    if (!is_dir($dir)) {
        mkdir($dir, 0777, true);
    }
    return file_put_contents($filepath, json_encode($data, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
}

function getNextId($items) {
    if (empty($items)) {
        return 1;
    }
    $maxId = max(array_column($items, 'id'));
    return $maxId + 1;
}

// Check if user is logged in
function isLoggedIn() {
    return isset($_SESSION['user_id']);
}

// Check if user is admin
function isAdmin() {
    return isset($_SESSION['is_admin']) && $_SESSION['is_admin'] === true;
}

// Get current user ID
function getCurrentUserId() {
    return $_SESSION['user_id'] ?? null;
}

// Get current user info
function getCurrentUser() {
    if (!isLoggedIn()) {
        return null;
    }
    $userId = getCurrentUserId();
    $users = readJSON('users.json');
    foreach ($users as $user) {
        if ($user['id'] == $userId) {
            return $user;
        }
    }
    return null;
}

// Redirect helper
function redirect($url) {
    header("Location: $url");
    exit();
}

// Sanitize output
function e($string) {
    return htmlspecialchars($string, ENT_QUOTES, 'UTF-8');
}

// Check if voting is still open for a project (2 weeks after publication)
function isVotingOpen($publishedAt) {
    if (!$publishedAt) {
        return false;
    }
    $publishedTime = strtotime($publishedAt);
    $twoWeeksLater = strtotime('+2 weeks', $publishedTime);
    return time() <= $twoWeeksLater;
}

// Get vote count for a project
function getVoteCount($projectId) {
    $votes = readJSON('votes.json');
    $count = 0;
    foreach ($votes as $vote) {
        if ($vote['project_id'] == $projectId) {
            $count++;
        }
    }
    return $count;
}

// Check if user has voted for a project
function hasUserVoted($userId, $projectId) {
    $votes = readJSON('votes.json');
    foreach ($votes as $vote) {
        if ($vote['user_id'] == $userId && $vote['project_id'] == $projectId) {
            return true;
        }
    }
    return false;
}

// Get user's vote count in a category
function getUserVotesInCategory($userId, $categoryId) {
    $votes = readJSON('votes.json');
    $projects = readJSON('projects.json');
    $count = 0;
    
    foreach ($votes as $vote) {
        if ($vote['user_id'] == $userId) {
            // Find the project for this vote
            foreach ($projects as $project) {
                if ($project['id'] == $vote['project_id'] && $project['category_id'] == $categoryId) {
                    $count++;
                    break;
                }
            }
        }
    }
    return $count;
}

// JSON response helper
function jsonResponse($data) {
    header('Content-Type: application/json');
    echo json_encode($data);
    exit();
}
