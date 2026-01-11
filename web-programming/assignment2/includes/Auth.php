<?php

// Start session if not already started
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once __DIR__ . '/Storage.php';

class Auth
{
    private $userStorage;

    public function __construct()
    {
        $this->userStorage = new Storage('users.json');
    }

    public function register(array $data): array
    {
        $errors = [];

        $username = trim($data['username'] ?? '');
        $email = trim($data['email'] ?? '');
        $password = $data['password'] ?? '';
        $confirm = $data['password_confirm'] ?? '';

        // Username Validation
        if (empty($username)) {
            $errors['username'] = 'Username is required';
        } elseif (preg_match('/\s/', $username)) {
            $errors['username'] = 'Username cannot contain spaces';
        }

        // Email Validation
        if (empty($email)) {
            $errors['email'] = 'Email is required';
        } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $errors['email'] = 'Invalid email format';
        }

        // Password Validation
        if (empty($password)) {
            $errors['password'] = 'Password is required';
        } else {
            if (strlen($password) < 8)
                $errors['password'] = 'Password must be at least 8 characters';
            elseif (!preg_match('/[a-z]/', $password))
                $errors['password'] = 'Password must contain a lowercase letter';
            elseif (!preg_match('/[A-Z]/', $password))
                $errors['password'] = 'Password must contain an uppercase letter';
            elseif (!preg_match('/[0-9]/', $password))
                $errors['password'] = 'Password must contain a number';
        }

        if ($password !== $confirm) {
            $errors['password_confirm'] = 'Passwords do not match';
        }

        // Check if username exists
        $existing = $this->userStorage->findOne(function ($u) use ($username) {
            return $u['username'] === $username;
        });

        if ($existing) {
            $errors['username'] = 'Username already taken';
        }

        if (empty($errors)) {
            $newUser = [
                'id' => uniqid('user_', true),
                'username' => $username,
                'email' => $email, // Add email to storage
                'password' => password_hash($password, PASSWORD_DEFAULT),
                'is_admin' => false // Use is_admin instead of role
            ];
            $this->userStorage->add($newUser);
            $this->login($newUser);
        }

        return $errors;
    }

    public function login(array $user)
    {
        $_SESSION['user_id'] = $user['id'];
        $_SESSION['is_admin'] = $user['is_admin'] ?? false; // Store is_admin in session
        $_SESSION['username'] = $user['username'];
    }

    public function authenticate(string $username, string $password): bool
    {
        $user = $this->userStorage->findOne(function ($u) use ($username) {
            return $u['username'] === $username;
        });

        if ($user && password_verify($password, $user['password'])) {
            $this->login($user);
            return true;
        }
        return false;
    }

    public function logout()
    {
        session_unset();
        session_destroy();
    }

    public function isLoggedIn(): bool
    {
        return isset($_SESSION['user_id']);
    }

    public function getUser()
    {
        if (!$this->isLoggedIn())
            return null;
        return $this->userStorage->findById($_SESSION['user_id']);
    }

    public function isAdmin(): bool
    {
        return $this->isLoggedIn() && ($_SESSION['is_admin'] ?? false) === true; // Check is_admin from session
    }

    public function getCurrentUserId()
    {
        return $_SESSION['user_id'] ?? null;
    }
}
