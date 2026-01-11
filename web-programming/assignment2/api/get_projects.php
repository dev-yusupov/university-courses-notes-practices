<?php
require_once __DIR__ . '/../includes/Auth.php';
require_once __DIR__ . '/../includes/ProjectManager.php';

header('Content-Type: application/json');

$auth = new Auth();
$pm = new ProjectManager();

$category = isset($_GET['category']) && $_GET['category'] !== '' ? (int) $_GET['category'] : null;

// Public API => Approved projects only
$projects = $pm->getAllProjects(ProjectManager::STATUS_APPROVED);

// Filter by category if set
if ($category !== null) {
    $projects = array_filter($projects, function ($p) use ($category) {
        return (int) $p['category'] === $category;
    });
}
$projects = array_values($projects);

// Enhance with "did I vote?" info if logged in
// Calculate votes left per category
$votesLeft = [];
if ($auth->isLoggedIn()) {
    $userId = $auth->getCurrentUserId();
    $userVotes = $pm->getUserVotes($userId);

    // Count votes per category
    $counts = [];
    foreach ($userVotes as $v) {
        // Vote object has 'project' key (ID). Need to look up project to get category.
        if (isset($v['project'])) {
            $pDetails = $pm->getProject($v['project']);
            if ($pDetails && isset($pDetails['category'])) {
                $c = $pDetails['category'];
                $counts[$c] = ($counts[$c] ?? 0) + 1;
            }
        }
    }

    $allCats = array_keys(ProjectManager::CATEGORIES);
    foreach ($allCats as $cat) {
        $votesLeft[$cat] = 3 - ($counts[$cat] ?? 0);
    }

    // Map of projectId -> true
    $votedProjectIds = array_column($userVotes, 'project');

    foreach ($projects as &$p) {
        $p['userVoted'] = in_array($p['id'], $votedProjectIds);
    }
} else {
    foreach ($projects as &$p) {
        $p['userVoted'] = false;
    }
}

// Return Wrapper
echo json_encode([
    'data' => $projects,
    'meta' => [
        'loggedIn' => $auth->isLoggedIn(),
        'votesLeft' => $votesLeft
    ]
]);
