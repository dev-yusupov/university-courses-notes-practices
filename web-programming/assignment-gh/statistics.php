<?php
require_once 'config.php';

if (!isAdmin()) {
    redirect('index.php');
}

// Get all data
$projects = readJSON('projects.json');
$votes = readJSON('votes.json');
$users = readJSON('users.json');
$categories = readJSON('categories.json');

// Total counts
$totalProjects = count($projects);
$approvedProjects = 0;
$pendingProjects = 0;
$rejectedProjects = 0;
$reworkProjects = 0;

foreach ($projects as $project) {
    switch ($project['status']) {
        case 'approved':
            $approvedProjects++;
            break;
        case 'pending':
            $pendingProjects++;
            break;
        case 'rejected':
            $rejectedProjects++;
            break;
        case 'rework':
            $reworkProjects++;
            break;
    }
}

$totalVotes = count($votes);
$totalUsers = count($users);

// Create lookups
$categoryLookup = [];
foreach ($categories as $cat) {
    $categoryLookup[$cat['id']] = $cat['name'];
}

$userLookup = [];
foreach ($users as $user) {
    $userLookup[$user['id']] = $user['username'];
}

// Top projects by category (with most votes)
$topProjects = [];
foreach ($projects as $project) {
    if ($project['status'] === 'approved') {
        $voteCount = 0;
        foreach ($votes as $vote) {
            if ($vote['project_id'] == $project['id']) {
                $voteCount++;
            }
        }
        
        $categoryName = $categoryLookup[$project['category_id']] ?? 'Unknown';
        if (!isset($topProjects[$categoryName])) {
            $topProjects[$categoryName] = [];
        }
        
        $topProjects[$categoryName][] = [
            'id' => $project['id'],
            'title' => $project['title'],
            'category_name' => $categoryName,
            'username' => $userLookup[$project['user_id']] ?? 'Unknown',
            'vote_count' => $voteCount,
            'published_at' => $project['published_at']
        ];
    }
}

// Sort each category by vote count
foreach ($topProjects as &$categoryProjects) {
    usort($categoryProjects, function($a, $b) {
        return $b['vote_count'] - $a['vote_count'];
    });
}

// Category statistics
$categoryStats = [];
foreach ($categories as $cat) {
    $projectCount = 0;
    $voteCount = 0;
    $approvedProjectIds = [];
    
    foreach ($projects as $project) {
        if ($project['category_id'] == $cat['id'] && $project['status'] === 'approved') {
            $projectCount++;
            $approvedProjectIds[] = $project['id'];
        }
    }
    
    foreach ($votes as $vote) {
        if (in_array($vote['project_id'], $approvedProjectIds)) {
            $voteCount++;
        }
    }
    
    $categoryStats[] = [
        'name' => $cat['name'],
        'project_count' => $projectCount,
        'vote_count' => $voteCount
    ];
}

// Sort by vote count DESC
usort($categoryStats, function($a, $b) {
    return $b['vote_count'] - $a['vote_count'];
});

// Most active voters
$voterCounts = [];
foreach ($votes as $vote) {
    $userId = $vote['user_id'];
    if (!isset($voterCounts[$userId])) {
        $voterCounts[$userId] = 0;
    }
    $voterCounts[$userId]++;
}

$activeVoters = [];
foreach ($voterCounts as $userId => $count) {
    $activeVoters[] = [
        'username' => $userLookup[$userId] ?? 'Unknown',
        'vote_count' => $count
    ];
}

usort($activeVoters, function($a, $b) {
    return $b['vote_count'] - $a['vote_count'];
});
$activeVoters = array_slice($activeVoters, 0, 10);

// Most active project submitters
$submitterCounts = [];
foreach ($projects as $project) {
    $userId = $project['user_id'];
    if (!isset($submitterCounts[$userId])) {
        $submitterCounts[$userId] = ['total' => 0, 'approved' => 0];
    }
    $submitterCounts[$userId]['total']++;
    if ($project['status'] === 'approved') {
        $submitterCounts[$userId]['approved']++;
    }
}

$activeSubmitters = [];
foreach ($submitterCounts as $userId => $counts) {
    $activeSubmitters[] = [
        'username' => $userLookup[$userId] ?? 'Unknown',
        'project_count' => $counts['total'],
        'approved_count' => $counts['approved']
    ];
}

usort($activeSubmitters, function($a, $b) {
    return $b['project_count'] - $a['project_count'];
});
$activeSubmitters = array_slice($activeSubmitters, 0, 10);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Statistics - Budapest Community Budget</title>
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
        <h2>Statistics Dashboard</h2>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number"><?php echo $totalProjects; ?></div>
                <div class="stat-label">Total Projects</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $approvedProjects; ?></div>
                <div class="stat-label">Approved Projects</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $pendingProjects; ?></div>
                <div class="stat-label">Pending Projects</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $totalVotes; ?></div>
                <div class="stat-label">Total Votes</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $totalUsers; ?></div>
                <div class="stat-label">Total Users</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $rejectedProjects; ?></div>
                <div class="stat-label">Rejected Projects</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $reworkProjects; ?></div>
                <div class="stat-label">Projects in Rework</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><?php echo $totalVotes > 0 ? round($totalVotes / max($approvedProjects, 1), 1) : 0; ?></div>
                <div class="stat-label">Avg Votes per Project</div>
            </div>
        </div>

        <div class="stats-section">
            <h3>Leading Projects by Category</h3>
            <?php if (empty($topProjects)): ?>
                <p class="no-data">No approved projects yet.</p>
            <?php else: ?>
                <?php foreach ($topProjects as $categoryName => $projects): ?>
                    <div class="category-stats">
                        <h4><?php echo e($categoryName); ?></h4>
                        <table class="stats-table">
                            <thead>
                                <tr>
                                    <th>Rank</th>
                                    <th>Project</th>
                                    <th>Author</th>
                                    <th>Votes</th>
                                    <th>Published</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php 
                                $rank = 1;
                                foreach (array_slice($projects, 0, 5) as $project): 
                                ?>
                                    <tr>
                                        <td><?php echo $rank++; ?></td>
                                        <td>
                                            <a href="project.php?id=<?php echo $project['id']; ?>">
                                                <?php echo e($project['title']); ?>
                                            </a>
                                        </td>
                                        <td><?php echo e($project['username']); ?></td>
                                        <td><strong><?php echo $project['vote_count']; ?></strong></td>
                                        <td><?php echo date('Y-m-d', strtotime($project['published_at'])); ?></td>
                                    </tr>
                                <?php endforeach; ?>
                            </tbody>
                        </table>
                    </div>
                <?php endforeach; ?>
            <?php endif; ?>
        </div>

        <div class="stats-section">
            <h3>Category Statistics</h3>
            <table class="stats-table">
                <thead>
                    <tr>
                        <th>Category</th>
                        <th>Projects</th>
                        <th>Total Votes</th>
                        <th>Avg Votes per Project</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($categoryStats as $stat): ?>
                        <tr>
                            <td><?php echo e($stat['name']); ?></td>
                            <td><?php echo $stat['project_count']; ?></td>
                            <td><?php echo $stat['vote_count']; ?></td>
                            <td><?php echo $stat['project_count'] > 0 ? round($stat['vote_count'] / $stat['project_count'], 1) : 0; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        </div>

        <div class="stats-row">
            <div class="stats-section half">
                <h3>Most Active Voters</h3>
                <?php if (empty($activeVoters)): ?>
                    <p class="no-data">No votes cast yet.</p>
                <?php else: ?>
                    <table class="stats-table">
                        <thead>
                            <tr>
                                <th>Rank</th>
                                <th>Username</th>
                                <th>Votes Cast</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php 
                            $rank = 1;
                            foreach ($activeVoters as $voter): 
                            ?>
                                <tr>
                                    <td><?php echo $rank++; ?></td>
                                    <td><?php echo e($voter['username']); ?></td>
                                    <td><?php echo $voter['vote_count']; ?></td>
                                </tr>
                            <?php endforeach; ?>
                        </tbody>
                    </table>
                <?php endif; ?>
            </div>

            <div class="stats-section half">
                <h3>Most Active Project Submitters</h3>
                <?php if (empty($activeSubmitters)): ?>
                    <p class="no-data">No projects submitted yet.</p>
                <?php else: ?>
                    <table class="stats-table">
                        <thead>
                            <tr>
                                <th>Rank</th>
                                <th>Username</th>
                                <th>Projects</th>
                                <th>Approved</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php 
                            $rank = 1;
                            foreach ($activeSubmitters as $submitter): 
                            ?>
                                <tr>
                                    <td><?php echo $rank++; ?></td>
                                    <td><?php echo e($submitter['username']); ?></td>
                                    <td><?php echo $submitter['project_count']; ?></td>
                                    <td><?php echo $submitter['approved_count']; ?></td>
                                </tr>
                            <?php endforeach; ?>
                        </tbody>
                    </table>
                <?php endif; ?>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Budapest Community Budget. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>
