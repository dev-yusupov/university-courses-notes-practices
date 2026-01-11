<?php
require_once 'config.php';

$error = '';
$success = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['action'])) {
        if ($_POST['action'] === 'login') {
            // Login
            $username = trim($_POST['username'] ?? '');
            $password = $_POST['password'] ?? '';
            
            if (empty($username) || empty($password)) {
                $error = 'Please fill in all fields';
            } else {
                $users = readJSON('users.json');
                $userFound = null;
                
                foreach ($users as $user) {
                    if ($user['username'] === $username) {
                        $userFound = $user;
                        break;
                    }
                }
                
                if ($userFound && password_verify($password, $userFound['password'])) {
                    $_SESSION['user_id'] = $userFound['id'];
                    $_SESSION['username'] = $userFound['username'];
                    $_SESSION['is_admin'] = (bool)$userFound['is_admin'];
                    redirect('index.php');
                } else {
                    $error = 'Invalid username or password';
                }
            }
        } elseif ($_POST['action'] === 'register') {
            // Registration
            $username = trim($_POST['username'] ?? '');
            $email = trim($_POST['email'] ?? '');
            $password = $_POST['password'] ?? '';
            $password2 = $_POST['password2'] ?? '';
            
            if (empty($username) || empty($email) || empty($password) || empty($password2)) {
                $error = 'Please fill in all fields';
            } elseif ($password !== $password2) {
                $error = 'Passwords do not match';
            } elseif (strlen($password) < 6) {
                $error = 'Password must be at least 6 characters';
            } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                $error = 'Invalid email address';
            } else {
                $users = readJSON('users.json');
                
                // Check if username exists
                $usernameExists = false;
                $emailExists = false;
                foreach ($users as $user) {
                    if ($user['username'] === $username) {
                        $usernameExists = true;
                        break;
                    }
                    if ($user['email'] === $email) {
                        $emailExists = true;
                        break;
                    }
                }
                
                if ($usernameExists) {
                    $error = 'Username already exists';
                } elseif ($emailExists) {
                    $error = 'Email already exists';
                } else {
                    // Create user
                    $newUser = [
                        'id' => getNextId($users),
                        'username' => $username,
                        'email' => $email,
                        'password' => password_hash($password, PASSWORD_DEFAULT),
                        'is_admin' => false,
                        'created_at' => date('c')
                    ];
                    $users[] = $newUser;
                    
                    if (writeJSON('users.json', $users)) {
                        $success = 'Registration successful! You can now log in.';
                    } else {
                        $error = 'Registration failed. Please try again.';
                    }
                }
            }
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login / Register - Budapest Community Budget</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>Budapest Community Budget</h1>
            <nav>
                <a href="index.php">Home</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <div class="auth-container">
            <div class="auth-form">
                <h2>Login</h2>
                <?php if ($error && isset($_POST['action']) && $_POST['action'] === 'login'): ?>
                    <div class="error"><?php echo e($error); ?></div>
                <?php endif; ?>
                <?php if ($success): ?>
                    <div class="success"><?php echo e($success); ?></div>
                <?php endif; ?>
                <form method="POST" action="login.php">
                    <input type="hidden" name="action" value="login">
                    <div class="form-group">
                        <label for="login-username">Username</label>
                        <input type="text" id="login-username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="login-password">Password</label>
                        <input type="password" id="login-password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Login</button>
                </form>
            </div>

            <div class="auth-form">
                <h2>Register</h2>
                <?php if ($error && isset($_POST['action']) && $_POST['action'] === 'register'): ?>
                    <div class="error"><?php echo e($error); ?></div>
                <?php endif; ?>
                <form method="POST" action="login.php">
                    <input type="hidden" name="action" value="register">
                    <div class="form-group">
                        <label for="reg-username">Username</label>
                        <input type="text" id="reg-username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="reg-email">Email</label>
                        <input type="email" id="reg-email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="reg-password">Password</label>
                        <input type="password" id="reg-password" name="password" required minlength="6">
                    </div>
                    <div class="form-group">
                        <label for="reg-password2">Confirm Password</label>
                        <input type="password" id="reg-password2" name="password2" required minlength="6">
                    </div>
                    <button type="submit" class="btn btn-primary">Register</button>
                </form>
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
