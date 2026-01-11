<?php
require_once 'config.php';

// Get all categories
$categories = readJSON('categories.json');

// Get filter
$filterCategory = isset($_GET['category']) && is_numeric($_GET['category']) ? (int)$_GET['category'] : null;

// Get published projects
$allProjects = readJSON('projects.json');
$users = readJSON('users.json');

// Create user lookup
$userLookup = [];
foreach ($users as $user) {
    $userLookup[$user['id']] = $user['username'];
}

// Create category lookup
$categoryLookup = [];
foreach ($categories as $cat) {
    $categoryLookup[$cat['id']] = $cat['name'];
}

// Filter and group projects
$projectsByCategory = [];
foreach ($allProjects as $project) {
    if ($project['status'] === 'approved') {
        if ($filterCategory && $project['category_id'] != $filterCategory) {
            continue;
        }
        
        $categoryName = $categoryLookup[$project['category_id']] ?? 'Unknown';
        if (!isset($projectsByCategory[$categoryName])) {
            $projectsByCategory[$categoryName] = [];
        }
        
        $project['username'] = $userLookup[$project['user_id']] ?? 'Unknown';
        $project['category_name'] = $categoryName;
        $projectsByCategory[$categoryName][] = $project;
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Budapest Community Budget</title>
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
        <h2>Community Projects</h2>
        
        <div class="filter-section">
            <label for="category-filter">Filter by category:</label>
            <select id="category-filter" onchange="window.location.href='index.php' + (this.value ? '?category=' + this.value : '')">
                <option value="">All Categories</option>
                <?php foreach ($categories as $cat): ?>
                    <option value="<?php echo $cat['id']; ?>" <?php echo $filterCategory == $cat['id'] ? 'selected' : ''; ?>>
                        <?php echo e($cat['name']); ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </div>

        <?php if (empty($projectsByCategory)): ?>
            <p class="no-projects">No projects available yet.</p>
        <?php else: ?>
            <?php foreach ($projectsByCategory as $categoryName => $projects): ?>
                <div class="category-section">
                    <h3><?php echo e($categoryName); ?></h3>
                    <div class="projects-list">
                        <?php foreach ($projects as $project): ?>
                            <?php 
                                $voteCount = getVoteCount($project['id']);
                                $hasVoted = isLoggedIn() ? hasUserVoted(getCurrentUserId(), $project['id']) : false;
                                $votingOpen = isVotingOpen($project['published_at']);
                                $userVotesInCategory = isLoggedIn() ? getUserVotesInCategory(getCurrentUserId(), $project['category_id']) : 0;
                                $canVote = isLoggedIn() && $votingOpen && !$hasVoted && $userVotesInCategory < 3;
                            ?>
                            <div class="project-item" data-project-id="<?php echo $project['id']; ?>">
                                <div class="project-info">
                                    <a href="project.php?id=<?php echo $project['id']; ?>" class="project-title">
                                        <?php echo e($project['title']); ?>
                                    </a>
                                    <span class="project-meta">by <?php echo e($project['username']); ?></span>
                                </div>
                                <div class="project-voting">
                                    <span class="vote-count"><?php echo $voteCount; ?> vote<?php echo $voteCount != 1 ? 's' : ''; ?></span>
                                    <?php if (isLoggedIn()): ?>
                                        <?php if ($votingOpen): ?>
                                            <?php if ($hasVoted): ?>
                                                <button class="btn btn-vote voted" onclick="withdrawVote(<?php echo $project['id']; ?>)">
                                                    Withdraw Vote
                                                </button>
                                            <?php elseif ($canVote): ?>
                                                <button class="btn btn-vote" onclick="castVote(<?php echo $project['id']; ?>)">
                                                    Vote
                                                </button>
                                            <?php elseif ($userVotesInCategory >= 3): ?>
                                                <button class="btn btn-vote" disabled title="You have used all 3 votes in this category">
                                                    Max Votes Reached
                                                </button>
                                            <?php endif; ?>
                                        <?php else: ?>
                                            <span class="voting-closed">Voting closed</span>
                                        <?php endif; ?>
                                    <?php else: ?>
                                        <a href="login.php" class="btn btn-vote">Login to Vote</a>
                                    <?php endif; ?>
                                </div>
                            </div>
                        <?php endforeach; ?>
                    </div>
                </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Budapest Community Budget. All rights reserved.</p>
        </div>
    </footer>

    <script src="script.js"></script>
</body>
</html>
