<?php
session_start();
// === INIT ===
$errors = [];
$input = []; // Stores cleaned input

// === HELPER: STICKY INPUT ===
function old($key) {
    return isset($_POST[$key]) ? htmlspecialchars($_POST[$key]) : '';
}

// === POST HANDLER ===
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // 1. Sanitize
    $input['email'] = trim($_POST['email'] ?? '');
    $input['age'] = (int)($_POST['age'] ?? 0);

    // 2. Validate
    if (empty($input['email'])) {
        $errors['email'] = 'Email is required!';
    } else if (!filter_var($input['email'], FILTER_VALIDATE_EMAIL)) {
        $errors['email'] = 'Invalid email format!';
    }

    if ($input['age'] < 18) {
        $errors['age'] = 'You must be 18+';
    }

    // 3. Process
    if (empty($errors)) {
        // SAVE TO DB HERE
        // ...
        
        // REDIRECT (Post-Redirect-Get pattern)
        header('Location: success.php');
        exit();
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head><title>Exam</title></head>
<body>
    
    <?php if ($errors): ?>
        <ul style="color:red">
            <?php foreach($errors as $e): ?><li><?= $e ?></li><?php endforeach; ?>
        </ul>
    <?php endif; ?>

    <form method="POST" novalidate>
        <label>Email:
            <input type="text" name="email" value="<?= old('email') ?>">
        </label>
        <?php if(isset($errors['email'])): ?> 
            <span style="color:red"><?= $errors['email'] ?></span>
        <?php endif; ?>
        <br>

        <button type="submit">Submit</button>
    </form>
</body>
</html>
