<?php
require_once 'config.php';

if (!isLoggedIn()) {
    redirect('index.php');
}

$userId = getCurrentUserId();

// Get user's projects
$allProjects = readJSON('projects.json');
$categories = readJSON('categories.json');

$categoryLookup = [];
foreach ($categories as $cat) {
    $categoryLookup[$cat['id']] = $cat['name'];
}

$projects = [];
foreach ($allProjects as $project) {
    if ($project['user_id'] == $userId) {
        $project['category_name'] = $categoryLookup[$project['category_id']] ?? 'Unknown';
        $projects[] = $project;
    }
}

// Sort by submitted_at DESC
usort($projects, function($a, $b) {
    return strtotime($b['submitted_at']) - strtotime($a['submitted_at']);
});
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Projects - Budapest Community Budget</title>
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
                <?php if (isAdmin()): ?>
                    <a href="projects-admin.php">Admin Panel</a>
                    <a href="statistics.php">Statistics</a>
                <?php endif; ?>
                <span class="user-info">Welcome, <?php echo e($_SESSION['username']); ?>!</span>
                <a href="logout.php" class="btn btn-secondary">Logout</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <h2>My Projects</h2>

        <?php if (empty($projects)): ?>
            <p class="no-projects">You haven't submitted any projects yet.</p>
            <a href="submit-project.php" class="btn btn-primary">Submit Your First Project</a>
        <?php else: ?>
            <div class="projects-table">
                <table>
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Category</th>
                            <th>Status</th>
                            <th>Submitted</th>
                            <th>Published</th>
                            <th>Votes</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($projects as $project): ?>
                            <tr class="status-<?php echo $project['status']; ?>">
                                <td>
                                    <a href="project.php?id=<?php echo $project['id']; ?>">
                                        <?php echo e($project['title']); ?>
                                    </a>
                                </td>
                                <td><?php echo e($project['category_name']); ?></td>
                                <td>
                                    <span class="status-badge status-<?php echo $project['status']; ?>">
                                        <?php echo ucfirst($project['status']); ?>
                                    </span>
                                    <?php if ($project['status'] === 'rework'): ?>
                                        <span class="action-needed">⚠ Action Needed</span>
                                    <?php endif; ?>
                                </td>
                                <td><?php echo date('Y-m-d', strtotime($project['submitted_at'])); ?></td>
                                <td>
                                    <?php echo $project['published_at'] ? date('Y-m-d', strtotime($project['published_at'])) : '-'; ?>
                                </td>
                                <td>
                                    <?php 
                                        echo $project['status'] === 'approved' ? getVoteCount($project['id']) : '-';
                                    ?>
                                </td>
                                <td>
                                    <a href="project.php?id=<?php echo $project['id']; ?>" class="btn btn-small">View</a>
                                    <?php if ($project['status'] === 'rework'): ?>
                                        <a href="edit-project.php?id=<?php echo $project['id']; ?>" class="btn btn-small btn-warning">Edit</a>
                                    <?php endif; ?>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            </div>
        <?php endif; ?>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Budapest Community Budget. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>
