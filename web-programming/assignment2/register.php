<?php
require_once 'includes/Auth.php';
$auth = new Auth();

if ($auth->isLoggedIn()) {
    header('Location: index.php');
    exit;
}

$errors = [];
$data = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = $_POST;
    $errors = $auth->register($data);

    if (empty($errors)) {
        $_SESSION['flash_success'] = 'Registration successful! You are now logged in.';
        header('Location: index.php');
        exit;
    }
}
?>
<?php include 'includes/header.php'; ?>

<div class="auth-container">
    <h2>Register</h2>
    <form action="register.php" method="post">
        <div class="form-group">
            <label for="username">Username (No spaces)</label>
            <input type="text" id="username" name="username" value="<?= htmlspecialchars($data['username'] ?? '') ?>"
                required>
            <?php if (isset($errors['username'])): ?>
                <span style="color:red">
                    <?= $errors['username'] ?>
                </span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" value="<?= htmlspecialchars($data['email'] ?? '') ?>" required>
            <?php if (isset($errors['email'])): ?>
                <span style="color:red"><?= $errors['email'] ?></span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="password">Password (8+ chars, Upper, Lower, Number)</label>
            <input type="password" id="password" name="password" required>
            <?php if (isset($errors['password'])): ?>
                <span style="color:red">
                    <?= $errors['password'] ?>
                </span>
            <?php endif; ?>
        </div>

        <div class="form-group">
            <label for="password_confirm">Confirm Password</label>
            <input type="password" id="password_confirm" name="password_confirm" required>
            <?php if (isset($errors['password_confirm'])): ?>
                <span style="color:red">
                    <?= $errors['password_confirm'] ?>
                </span>
            <?php endif; ?>
        </div>

        <button type="submit">Register</button>
        <p>Already have an account? <a href="login.php">Login here</a>.</p>
    </form>
</div>

<?php include 'includes/footer.php'; ?>