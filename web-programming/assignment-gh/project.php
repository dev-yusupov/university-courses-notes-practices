<?php
require_once 'config.php';

// Get project ID
$projectId = isset($_GET['id']) && is_numeric($_GET['id']) ? (int)$_GET['id'] : 0;

if (!$projectId) {
    redirect('index.php');
}

// Get project details
$projects = readJSON('projects.json');
$categories = readJSON('categories.json');
$users = readJSON('users.json');

$project = null;
foreach ($projects as $p) {
    if ($p['id'] == $projectId) {
        $project = $p;
        break;
    }
}

if (!$project) {
    redirect('index.php');
}

// Add category name and username
foreach ($categories as $cat) {
    if ($cat['id'] == $project['category_id']) {
        $project['category_name'] = $cat['name'];
        break;
    }
}

foreach ($users as $user) {
    if ($user['id'] == $project['user_id']) {
        $project['username'] = $user['username'];
        break;
    }
}

// Check access permissions
$canView = false;
if ($project['status'] === 'approved') {
    $canView = true;
} elseif (isLoggedIn()) {
    if (isAdmin() || $project['user_id'] == getCurrentUserId()) {
        $canView = true;
    }
}

if (!$canView) {
    redirect('index.php');
}

// Handle admin actions
$message = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isAdmin()) {
    $action = $_POST['action'] ?? '';
    $projects = readJSON('projects.json');
    
    if ($action === 'approve') {
        foreach ($projects as &$p) {
            if ($p['id'] == $projectId) {
                $p['status'] = 'approved';
                $p['published_at'] = date('c');
                $project = $p;
                break;
            }
        }
        if (writeJSON('projects.json', $projects)) {
            $message = 'Project approved and published!';
        }
    } elseif ($action === 'reject') {
        foreach ($projects as &$p) {
            if ($p['id'] == $projectId) {
                $p['status'] = 'rejected';
                $project = $p;
                break;
            }
        }
        if (writeJSON('projects.json', $projects)) {
            $message = 'Project rejected.';
        }
    } elseif ($action === 'rework') {
        $comment = trim($_POST['comment'] ?? '');
        if (!empty($comment)) {
            foreach ($projects as &$p) {
                if ($p['id'] == $projectId) {
                    $p['status'] = 'rework';
                    $p['admin_comment'] = $comment;
                    $project = $p;
                    break;
                }
            }
            if (writeJSON('projects.json', $projects)) {
                $message = 'Project sent back for rework.';
            }
        }
    }
}

// Handle user resubmission after rework
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isLoggedIn() && $project['user_id'] == getCurrentUserId() && $project['status'] === 'rework') {
    $action = $_POST['action'] ?? '';
    if ($action === 'resubmit') {
        $projects = readJSON('projects.json');
        foreach ($projects as &$p) {
            if ($p['id'] == $projectId) {
                $p['status'] = 'pending';
                $p['admin_comment'] = null;
                $project = $p;
                break;
            }
        }
        if (writeJSON('projects.json', $projects)) {
            $message = 'Project resubmitted for approval!';
        }
    }
}

// Get vote count and user vote status
$voteCount = getVoteCount($projectId);
$hasVoted = isLoggedIn() ? hasUserVoted(getCurrentUserId(), $projectId) : false;
$votingOpen = isVotingOpen($project['published_at']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo e($project['title']); ?> - Budapest Community Budget</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>Budapest Community Budget</h1>
            <nav>
                <a href="index.php">Home</a>
                <?php if (isLoggedIn()): ?>
                    <a href="submit-project.php">Submit Project</a>
                    <a href="projects-own.php">My Projects</a>
                    <?php if (isAdmin()): ?>
                        <a href="projects-admin.php">Admin Panel</a>
                        <a href="statistics.php">Statistics</a>
                    <?php endif; ?>
                    <span class="user-info">Welcome, <?php echo e($_SESSION['username']); ?>!</span>
                    <a href="logout.php" class="btn btn-secondary">Logout</a>
                <?php else: ?>
                    <a href="login.php" class="btn btn-primary">Login / Register</a>
                <?php endif; ?>
            </nav>
        </div>
    </header>

    <main class="container">
        <?php if ($message): ?>
            <div class="success"><?php echo e($message); ?></div>
        <?php endif; ?>

        <div class="project-detail">
            <div class="project-header">
                <h2><?php echo e($project['title']); ?></h2>
                <span class="project-status status-<?php echo $project['status']; ?>">
                    <?php echo ucfirst($project['status']); ?>
                </span>
            </div>

            <?php if ($project['image']): ?>
                <div class="project-image">
                    <img src="<?php echo e($project['image']); ?>" alt="<?php echo e($project['title']); ?>">
                </div>
            <?php endif; ?>

            <div class="project-info-grid">
                <div class="info-item">
                    <strong>Category:</strong> <?php echo e($project['category_name']); ?>
                </div>
                <div class="info-item">
                    <strong>Postal Code:</strong> <?php echo e($project['postal_code']); ?>
                </div>
                <div class="info-item">
                    <strong>Submitted by:</strong> <?php echo e($project['username']); ?>
                </div>
                <div class="info-item">
                    <strong>Submitted on:</strong> <?php echo date('Y-m-d H:i', strtotime($project['submitted_at'])); ?>
                </div>
                <?php if ($project['published_at']): ?>
                    <div class="info-item">
                        <strong>Published on:</strong> <?php echo date('Y-m-d H:i', strtotime($project['published_at'])); ?>
                    </div>
                <?php endif; ?>
                <?php if ($project['status'] === 'approved'): ?>
                    <div class="info-item">
                        <strong>Votes:</strong> <?php echo $voteCount; ?>
                    </div>
                <?php endif; ?>
            </div>

            <div class="project-description">
                <h3>Description</h3>
                <p><?php echo nl2br(e($project['description'])); ?></p>
            </div>

            <?php if ($project['status'] === 'rework' && isLoggedIn() && $project['user_id'] == getCurrentUserId()): ?>
                <div class="rework-section">
                    <h3>Admin Feedback</h3>
                    <div class="admin-comment"><?php echo nl2br(e($project['admin_comment'])); ?></div>
                    <p>Please review the admin's feedback and <a href="edit-project.php?id=<?php echo $projectId; ?>">edit your project</a>, then resubmit it.</p>
                    <form method="POST" onsubmit="return confirm('Are you sure you want to resubmit this project?');">
                        <input type="hidden" name="action" value="resubmit">
                        <button type="submit" class="btn btn-primary">Resubmit for Approval</button>
                    </form>
                </div>
            <?php endif; ?>

            <?php if ($project['status'] === 'approved' && isLoggedIn() && $votingOpen): ?>
                <div class="voting-section" data-project-id="<?php echo $projectId; ?>">
                    <?php 
                        $userVotesInCategory = getUserVotesInCategory(getCurrentUserId(), $project['category_id']);
                        $canVote = !$hasVoted && $userVotesInCategory < 3;
                    ?>
                    <?php if ($hasVoted): ?>
                        <button class="btn btn-vote voted" onclick="withdrawVote(<?php echo $projectId; ?>)">
                            Withdraw Vote
                        </button>
                    <?php elseif ($canVote): ?>
                        <button class="btn btn-vote" onclick="castVote(<?php echo $projectId; ?>)">
                            Vote for this Project
                        </button>
                    <?php else: ?>
                        <p class="vote-limit">You have used all 3 votes in this category.</p>
                    <?php endif; ?>
                </div>
            <?php elseif ($project['status'] === 'approved' && !$votingOpen): ?>
                <div class="voting-closed">
                    <p>Voting period for this project has ended.</p>
                </div>
            <?php endif; ?>

            <?php if (isAdmin() && $project['status'] === 'pending'): ?>
                <div class="admin-actions">
                    <h3>Admin Actions</h3>
                    <form method="POST" style="display: inline;">
                        <input type="hidden" name="action" value="approve">
                        <button type="submit" class="btn btn-success" onclick="return confirm('Approve this project?');">
                            Approve & Publish
                        </button>
                    </form>
                    <form method="POST" style="display: inline;">
                        <input type="hidden" name="action" value="reject">
                        <button type="submit" class="btn btn-danger" onclick="return confirm('Reject this project?');">
                            Reject
                        </button>
                    </form>
                    <div class="rework-form">
                        <h4>Send Back for Rework</h4>
                        <form method="POST">
                            <input type="hidden" name="action" value="rework">
                            <textarea name="comment" placeholder="Enter feedback for the user..." required rows="4"></textarea>
                            <button type="submit" class="btn btn-warning">Send Back for Rework</button>
                        </form>
                    </div>
                </div>
            <?php endif; ?>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Budapest Community Budget. All rights reserved.</p>
        </div>
    </footer>

    <script src="script.js"></script>
</body>
</html>
