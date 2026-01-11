<?php
require_once 'includes/Auth.php';
require_once 'includes/ProjectManager.php';

$auth = new Auth();
if (!$auth->isLoggedIn()) {
    header('Location: login.php');
    exit;
}

$pm = new ProjectManager();
$id = $_GET['id'] ?? null;
$project = $id ? $pm->getProject($id) : null;

// Access Control
if (!$project || $project['owner'] !== $auth->getCurrentUserId() || $project['status'] !== ProjectManager::STATUS_NEEDS_REWORK) {
    echo "Unauthorized or Project not editable.";
    exit;
}

$errors = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = trim($_POST['title'] ?? '');
    $description = trim($_POST['description'] ?? '');
    $category = $_POST['category'] ?? '';
    $postalCode = trim($_POST['postal_code'] ?? '');
    $imageUrl = trim($_POST['image_url'] ?? '');

    // Validation (reuse create validation logic via editProject or manual?)
    // ProjectManager::editProject doesn't duplicate validation logic internally in my implementation plan?
    // I should validate here similar to submit.php.
    if (strlen($title) < 10)
        $errors['title'] = 'Title must be at least 10 characters';
    if (strlen($description) < 150)
        $errors['description'] = 'Description must be at least 150 characters';
    if ($category === '' || !array_key_exists((int) $category, ProjectManager::CATEGORIES))
        $errors['category'] = 'Invalid category';
    if (!preg_match('/^(1(0[1-9]|1[0-9]|2[0-3])[1-9]|1007)$/', $postalCode))
        $errors['postal_code'] = 'Invalid Postal Code (Budapest format required)';
    if (!empty($imageUrl) && !filter_var($imageUrl, FILTER_VALIDATE_URL))
        $errors['image_url'] = 'Invalid URL';

    if (empty($errors)) {
        $pm->editProject($id, $title, $description, $category, $postalCode, $imageUrl);
        $_SESSION['flash_success'] = 'Project updated and resubmitted for review.';
        header('Location: details.php?id=' . $id);
        exit;
    }
}

// Pre-fill
$title = $project['title'];
$description = $project['description'];
$category = $project['category'];
$postalCode = $project['postal_code'];
$imageUrl = $project['image'];

?>
<?php include 'includes/header.php'; ?>

<div class="auth-container" style="max-width: 800px;">
    <h2>Edit Project for Rework</h2>

    <?php
    // Show Rework Request Comment if available in history
    if (!empty($project['history'])) {
        $lastHistory = end($project['history']);
        if ($lastHistory['action'] === 'rework_requested') {
            echo '<div class="alert" style="background:#fce5cd; border-left:5px solid #e69138; padding:15px; margin-bottom:20px;">';
            echo '<strong>Admin Feedback:</strong> ' . htmlspecialchars($lastHistory['comment']);
            echo '</div>';
        }
    }
    ?>

    <form action="edit.php?id=<?= $id ?>" method="post">

        <div class="form-group">
            <label for="title">Project Title (Min 10 chars)</label>
            <input type="text" id="title" name="title" value="<?= htmlspecialchars($title) ?>" required>
            <?php if (isset($errors['title'])): ?><span style="color:red">
                    <?= $errors['title'] ?>
                </span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="category">Category</label>
            <select id="category" name="category" required>
                <?php foreach (ProjectManager::CATEGORIES as $cid => $label): ?>
                    <option value="<?= $cid ?>" <?= (int) $category === $cid ? 'selected' : '' ?>>
                        <?= htmlspecialchars($label) ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </div>

        <div class="form-group">
            <label for="postal_code">Postal Code (4 digits)</label>
            <input type="text" id="postal_code" name="postal_code" value="<?= htmlspecialchars($postalCode) ?>"
                required>
            <?php if (isset($errors['postal_code'])): ?><span style="color:red">
                    <?= $errors['postal_code'] ?>
                </span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="image_url">Image URL</label>
            <input type="url" id="image_url" name="image_url" value="<?= htmlspecialchars($imageUrl) ?>">
            <?php if (isset($errors['image_url'])): ?><span style="color:red">
                    <?= $errors['image_url'] ?>
                </span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="description">Description (Min 150 chars)</label>
            <textarea id="description" name="description" rows="10"
                required><?= htmlspecialchars($description) ?></textarea>
            <?php if (isset($errors['description'])): ?><span style="color:red">
                    <?= $errors['description'] ?>
                </span>
            <?php endif; ?>
        </div>

        <button type="submit">Resubmit Project</button>
        <a href="my_projects.php" style="margin-left: 20px;">Cancel</a>
    </form>
</div>

<?php include 'includes/footer.php'; ?>