<?php
require_once __DIR__ . '/../includes/Auth.php';
require_once __DIR__ . '/../includes/ProjectManager.php';

header('Content-Type: application/json');

$auth = new Auth();
$pm = new ProjectManager();

if (!$auth->isLoggedIn()) {
    http_response_code(401);
    echo json_encode(['error' => 'You must be logged in to vote.']);
    exit;
}

$input = json_decode(file_get_contents('php://input'), true);
$projectId = $input['projectId'] ?? null;

if (!$projectId) {
    http_response_code(400);
    echo json_encode(['error' => 'Project ID required']);
    exit;
}

$result = $pm->vote($auth->getCurrentUserId(), $projectId);

if (isset($result['error'])) {
    // Determine strictness? 400 or 200 with error? 
    // Usually 400 for business logic error is fine but let's keep it simple.
    echo json_encode(['success' => false, 'error' => $result['error']]);
} else {
    echo json_encode(['success' => true, 'message' => $result['success']]);
}
