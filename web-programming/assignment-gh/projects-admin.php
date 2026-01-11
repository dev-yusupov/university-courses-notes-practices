<?php
require_once 'config.php';

if (!isAdmin()) {
    redirect('index.php');
}

// Get all pending projects grouped by category
$allProjects = readJSON('projects.json');
$categories = readJSON('categories.json');
$users = readJSON('users.json');

$categoryLookup = [];
foreach ($categories as $cat) {
    $categoryLookup[$cat['id']] = $cat['name'];
}

$userLookup = [];
foreach ($users as $user) {
    $userLookup[$user['id']] = $user['username'];
}

$projectsByCategory = [];
$pendingCount = 0;

foreach ($allProjects as $project) {
    if ($project['status'] === 'pending') {
        $pendingCount++;
        $categoryName = $categoryLookup[$project['category_id']] ?? 'Unknown';
        if (!isset($projectsByCategory[$categoryName])) {
            $projectsByCategory[$categoryName] = [];
        }
        $project['category_name'] = $categoryName;
        $project['username'] = $userLookup[$project['user_id']] ?? 'Unknown';
        $projectsByCategory[$categoryName][] = $project;
    }
}

// Sort projects within each category by submitted_at
foreach ($projectsByCategory as &$projects) {
    usort($projects, function($a, $b) {
        return strtotime($a['submitted_at']) - strtotime($b['submitted_at']);
    });
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Budapest Community Budget</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>Budapest Community Budget</h1>
            <nav>
                <a href="index.php">Home</a>
                <a href="submit-project.php">Submit Project</a>
                <a href="projects-own.php">My Projects</a>
                <a href="projects-admin.php">Admin Panel</a>
                <a href="statistics.php">Statistics</a>
                <span class="user-info">Welcome, <?php echo e($_SESSION['username']); ?>!</span>
                <a href="logout.php" class="btn btn-secondary">Logout</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <h2>Admin Panel - Pending Projects</h2>
        
        <div class="admin-stats">
            <div class="stat-card">
                <div class="stat-number"><?php echo $pendingCount; ?></div>
                <div class="stat-label">Pending Projects</div>
            </div>
        </div>

        <?php if (empty($projectsByCategory)): ?>
            <p class="no-projects">No pending projects at this time.</p>
        <?php else: ?>
            <?php foreach ($projectsByCategory as $categoryName => $projects): ?>
                <div class="category-section">
                    <h3><?php echo e($categoryName); ?> (<?php echo count($projects); ?>)</h3>
                    <div class="admin-projects-list">
                        <?php foreach ($projects as $project): ?>
                            <div class="admin-project-item">
                                <div class="project-info">
                                    <a href="project.php?id=<?php echo $project['id']; ?>" class="project-title">
                                        <?php echo e($project['title']); ?>
                                    </a>
                                    <div class="project-meta">
                                        <span>by <?php echo e($project['username']); ?></span>
                                        <span>•</span>
                                        <span>Submitted: <?php echo date('Y-m-d H:i', strtotime($project['submitted_at'])); ?></span>
                                        <span>•</span>
                                        <span>Postal Code: <?php echo e($project['postal_code']); ?></span>
                                    </div>
                                    <div class="project-description-preview">
                                        <?php 
                                            $preview = strlen($project['description']) > 200 
                                                ? substr($project['description'], 0, 200) . '...' 
                                                : $project['description'];
                                            echo e($preview);
                                        ?>
                                    </div>
                                </div>
                                <div class="project-actions">
                                    <a href="project.php?id=<?php echo $project['id']; ?>" class="btn btn-primary">
                                        Review
                                    </a>
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
</body>
</html>
