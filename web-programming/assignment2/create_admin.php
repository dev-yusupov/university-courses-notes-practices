<?php
require_once 'includes/Storage.php';

$storage = new Storage('users.json');
$users = $storage->findAll();

$exists = false;
foreach ($users as $u) {
    if ($u['username'] === 'admin') {
        $exists = true;
        break;
    }
}

if (!$exists) {
    $admin = [
        'id' => uniqid('admin_', true),
        'username' => 'admin',
        'email' => 'admin@example.com',
        'password' => password_hash('admin', PASSWORD_DEFAULT),
        'is_admin' => true
    ];
    $storage->add($admin);
    echo "Admin user created successfully.\n";
} else {
    echo "Admin user already exists.\n";
}
