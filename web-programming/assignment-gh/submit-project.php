<?php
require_once 'config.php';

if (!isLoggedIn()) {
    redirect('index.php');
}

$error = '';
$success = '';

// Get categories for form
$categories = readJSON('categories.json');
usort($categories, function($a, $b) {
    return strcmp($a['name'], $b['name']);
});

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
        $userId = getCurrentUserId();
        $projects = readJSON('projects.json');
        
        $newProject = [
            'id' => getNextId($projects),
            'title' => $title,
            'description' => $description,
            'category_id' => $categoryId,
            'postal_code' => $postalCode,
            'image' => $imageUrl,
            'user_id' => $userId,
            'status' => 'pending',
            'admin_comment' => null,
            'submitted_at' => date('c'),
            'published_at' => null
        ];
        
        $projects[] = $newProject;
        
        if (writeJSON('projects.json', $projects)) {
            $success = 'Project submitted successfully! It will be reviewed by an admin.';
            // Clear form
            $_POST = [];
        } else {
            $error = 'Failed to submit project. Please try again.';
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Project - Budapest Community Budget</title>
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
        <h2>Submit a New Project</h2>

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
                       value="<?php echo isset($_POST['title']) ? e($_POST['title']) : ''; ?>">
            </div>

            <div class="form-group">
                <label for="category_id">Category *</label>
                <select id="category_id" name="category_id" required>
                    <option value="">Select a category</option>
                    <?php foreach ($categories as $cat): ?>
                        <option value="<?php echo $cat['id']; ?>"
                                <?php echo (isset($_POST['category_id']) && $_POST['category_id'] == $cat['id']) ? 'selected' : ''; ?>>
                            <?php echo e($cat['name']); ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </div>

            <div class="form-group">
                <label for="postal_code">Postal Code *</label>
                <input type="text" id="postal_code" name="postal_code" required 
                       value="<?php echo isset($_POST['postal_code']) ? e($_POST['postal_code']) : ''; ?>"
                       placeholder="e.g., 1051">
            </div>

            <div class="form-group">
                <label for="description">Project Description *</label>
                <textarea id="description" name="description" required rows="8"
                          placeholder="Describe your project idea in detail..."><?php echo isset($_POST['description']) ? e($_POST['description']) : ''; ?></textarea>
            </div>

            <div class="form-group">
                <label for="image">Image URL (optional)</label>
                <input type="url" id="image" name="image" 
                       value="<?php echo isset($_POST['image']) ? e($_POST['image']) : ''; ?>"
                       placeholder="https://example.com/image.jpg">
                <small>Enter a URL to an image that represents your project</small>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Submit Project</button>
                <a href="projects-own.php" class="btn btn-secondary">Cancel</a>
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
