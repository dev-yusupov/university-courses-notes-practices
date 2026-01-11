<?php
require_once 'config.php';

if (!isLoggedIn()) {
    redirect('index.php');
}

$projectId = isset($_GET['id']) && is_numeric($_GET['id']) ? (int)$_GET['id'] : 0;

if (!$projectId) {
    redirect('projects-own.php');
}

$userId = getCurrentUserId();

// Get project
$projects = readJSON('projects.json');
$project = null;
foreach ($projects as $p) {
    if ($p['id'] == $projectId && $p['user_id'] == $userId) {
        $project = $p;
        break;
    }
}

if (!$project || $project['status'] !== 'rework') {
    redirect('projects-own.php');
}

// Get categories
$categories = readJSON('categories.json');
usort($categories, function($a, $b) {
    return strcmp($a['name'], $b['name']);
});

$error = '';
$success = '';

// Handle form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = trim($_POST['title'] ?? '');
    $description = trim($_POST['description'] ?? '');
    $categoryId = isset($_POST['category_id']) && is_numeric($_POST['category_id']) ? (int)$_POST['category_id'] : 0;
    $postalCode = trim($_POST['postal_code'] ?? '');
    $imageUrl = trim($_POST['image'] ?? '');
    
    if (empty($title) || empty($description) || !$categoryId || empty($postalCode)) {
        $error = 'Please fill in all required fields';
    } else {
        $projects = readJSON('projects.json');
        
        foreach ($projects as &$p) {
            if ($p['id'] == $projectId && $p['user_id'] == $userId) {
                $p['title'] = $title;
                $p['description'] = $description;
                $p['category_id'] = $categoryId;
                $p['postal_code'] = $postalCode;
                $p['image'] = $imageUrl;
                $project = $p;
                break;
            }
        }
        
        if (writeJSON('projects.json', $projects)) {
            $success = 'Project updated successfully! You can now resubmit it from the project page.';
        } else {
            $error = 'Failed to update project. Please try again.';
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Project - Budapest Community Budget</title>
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
        <h2>Edit Project</h2>

        <?php if ($project['admin_comment']): ?>
            <div class="admin-feedback">
                <h3>Admin Feedback:</h3>
                <p><?php echo nl2br(e($project['admin_comment'])); ?></p>
            </div>
        <?php endif; ?>

        <?php if ($error): ?>
            <div class="error"><?php echo e($error); ?></div>
        <?php endif; ?>
        <?php if ($success): ?>
            <div class="success"><?php echo e($success); ?></div>
        <?php endif; ?>

        <form method="POST" class="project-form">
            <div class="form-group">
                <label for="title">Project Title *</label>
                <input type="text" id="title" name="title" required 
                       value="<?php echo e($project['title']); ?>">
            </div>

            <div class="form-group">
                <label for="category_id">Category *</label>
                <select id="category_id" name="category_id" required>
                    <option value="">Select a category</option>
                    <?php foreach ($categories as $cat): ?>
                        <option value="<?php echo $cat['id']; ?>"
                                <?php echo $project['category_id'] == $cat['id'] ? 'selected' : ''; ?>>
                            <?php echo e($cat['name']); ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </div>

            <div class="form-group">
                <label for="postal_code">Postal Code *</label>
                <input type="text" id="postal_code" name="postal_code" required 
                       value="<?php echo e($project['postal_code']); ?>">
            </div>

            <div class="form-group">
                <label for="description">Project Description *</label>
                <textarea id="description" name="description" required rows="8"><?php echo e($project['description']); ?></textarea>
            </div>

            <div class="form-group">
                <label for="image">Image URL (optional)</label>
                <input type="url" id="image" name="image" 
                       value="<?php echo e($project['image']); ?>">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Changes</button>
                <a href="project.php?id=<?php echo $projectId; ?>" class="btn btn-secondary">Back to Project</a>
            </div>
        </form>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Budapest Community Budget. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>
