<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Budapest Community Budget</title>
    <link rel="stylesheet" href="assets/style.css">
</head>

<body>
    <header>
        <nav>
            <div class="logo">
                <a href="index.php">Budapest Budget</a>
            </div>
            <ul>
                <li><a href="index.php">Projects</a></li>
                <?php if ($auth->isLoggedIn()): ?>
                    <li><a href="submit.php">Submit Project</a></li>
                    <?php if ($auth->isAdmin()): ?>
                        <li><a href="admin.php">Admin</a></li>
                        <li><a href="stats.php">Stats</a></li>
                    <?php endif; ?>
                    <li><span>Hello, <?= htmlspecialchars($_SESSION['username']) ?></span></li>
                    <li><a href="my_projects.php">My Projects</a></li>
                    <li><a href="logout.php">Logout</a></li>
                <?php else: ?>
                    <li><a href="login.php">Login</a></li>
                    <li><a href="register.php">Register</a></li>
                <?php endif; ?>
            </ul>
        </nav>
    </header>
    <main>
        <?php if (isset($_SESSION['flash_error'])): ?>
            <div class="alert error"><?= css_safe_val($_SESSION['flash_error']) ?></div>
            <?php unset($_SESSION['flash_error']); ?>
        <?php endif; ?>
        <?php if (isset($_SESSION['flash_success'])): ?>
            <div class="alert success"><?= css_safe_val($_SESSION['flash_success']) ?></div>
            <?php unset($_SESSION['flash_success']); ?>
        <?php endif; ?>

        <?php
        function css_safe_val($str)
        {
            return htmlspecialchars($str);
        }
        ?>