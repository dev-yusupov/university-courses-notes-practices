<?php
session_start();
$errors = [];
$data = [];

// Helper to keep input values ("Sticky Form")
function old($key) {
    return isset($_POST[$key]) ? htmlspecialchars($_POST[$key]) : '';
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // 1. Validate
    $username = trim($_POST['username'] ?? '');
    
    if (strlen($username) === 0) {
        $errors['username'] = 'Username is required';
    }

    // 2. Success or Fail
    if (count($errors) === 0) {
        // Save data...
        // Redirect to prevent form resubmission
        header('Location: success.php');
        exit();
    }
}
?>

<form method="post" action="">
    <input type="text" name="username" value="<?= old('username') ?>">
    <?php if (isset($errors['username'])): ?>
        <span style="color: red"><?= $errors['username'] ?></span>
    <?php endif; ?>
    <button type="submit">Send</button>
</form>
