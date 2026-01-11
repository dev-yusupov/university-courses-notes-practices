<?php
require_once 'includes/Auth.php';
require_once 'includes/ProjectManager.php';

$auth = new Auth();
if (!$auth->isLoggedIn()) {
    header('Location: login.php');
    exit;
}

$pm = new ProjectManager();
$errors = [];
$title = '';
$description = '';
$category = '';
$postalCode = '';
$imageUrl = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = trim($_POST['title'] ?? '');
    $description = trim($_POST['description'] ?? '');
    $category = $_POST['category'] ?? '';
    $postalCode = trim($_POST['postal_code'] ?? '');
    $imageUrl = trim($_POST['image_url'] ?? '');

    // Validation for Category ID (including 0)
    if ($category === '' || !array_key_exists((int) $category, ProjectManager::CATEGORIES)) {
        $errors['category'] = 'Invalid category';
    }

    if (empty($errors)) {
        $result = $pm->createProject($title, $description, $category, $postalCode, $imageUrl, $auth->getCurrentUserId());
        if (isset($result['errors'])) {
            $errors = array_merge($errors, $result['errors']);
        } else {
            $_SESSION['flash_success'] = 'Project submitted! It will be reviewed by an admin.';
            header('Location: index.php');
            exit;
        }
    }
}
?>
<?php include 'includes/header.php'; ?>

<div class="auth-container" style="max-width: 600px;">
    <h2>Submit a Project</h2>
    <form action="submit.php" method="post">

        <div class="form-group">
            <label for="title">Project Title (Min 10 chars)</label>
            <input type="text" id="title" name="title" value="<?= htmlspecialchars($title) ?>" required>
            <?php if (isset($errors['title'])): ?>
                <span style="color:red"><?= $errors['title'] ?></span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="category">Category</label>
            <select id="category" name="category" required>
                <option value="">Select Category</option>
                <?php foreach (ProjectManager::CATEGORIES as $id => $label): ?>
                    <option value="<?= $id ?>" <?= $category == $id ? 'selected' : '' ?>>
                        <?= htmlspecialchars($label) ?>
                    </option>
                <?php endforeach; ?>
            </select>
            <?php if (isset($errors['category'])): ?>
                <span style="color:red"><?= $errors['category'] ?></span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="postal_code">Postal Code (4 digits)</label>
            <input type="text" id="postal_code" name="postal_code" value="<?= htmlspecialchars($postalCode) ?>"
                required>
            <?php if (isset($errors['postal_code'])): ?>
                <span style="color:red"><?= $errors['postal_code'] ?></span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="image_url">Image URL (Optional)</label>
            <input type="url" id="image_url" name="image_url" value="<?= htmlspecialchars($imageUrl) ?>">
            <?php if (isset($errors['image_url'])): ?>
                <span style="color:red"><?= $errors['image_url'] ?></span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="description">Description (Min 150 chars)</label>
            <textarea id="description" name="description" rows="6"
                required><?= htmlspecialchars($description) ?></textarea>
            <?php if (isset($errors['description'])): ?>
                <span style="color:red"><?= $errors['description'] ?></span>
            <?php endif; ?>
        </div>

        <button type="submit">Submit Proposal</button>
    </form>
</div>

<?php include 'includes/footer.php'; ?>