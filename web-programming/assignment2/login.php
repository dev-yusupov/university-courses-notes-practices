<?php
require_once 'includes/Auth.php';
$auth = new Auth();

if ($auth->isLoggedIn()) {
    header('Location: index.php');
    exit;
}

$error = '';
$username = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'] ?? '';
    $password = $_POST['password'] ?? '';

    if ($auth->authenticate($username, $password)) {
        $_SESSION['flash_success'] = 'Welcome back!';
        header('Location: index.php');
        exit;
    } else {
        $error = 'Invalid username or password';
    }
}
?>
<?php include 'includes/header.php'; ?>

<div class="auth-container">
    <h2>Login</h2>
    <?php if ($error): ?>
        <div class="alert error">
            <?= $error ?>
        </div>
    <?php endif; ?>

    <form action="login.php" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" value="<?= htmlspecialchars($username) ?>" required>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Login</button>
        <p>Don't have an account? <a href="register.php">Register here</a>.</p>
    </form>
</div>

<?php include 'includes/footer.php'; ?>