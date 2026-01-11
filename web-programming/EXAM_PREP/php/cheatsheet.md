# PHP Cheatsheet for Exam

## 1. Server-side Web Programming, HTTP, PHP, Output Generation

### HTTP Basics
- **HTTP Methods**: GET, POST, PUT, DELETE, PATCH
- **Status Codes**:
  - 200 OK
  - 301 Moved Permanently
  - 302 Found (redirect)
  - 400 Bad Request
  - 401 Unauthorized
  - 403 Forbidden
  - 404 Not Found
  - 500 Internal Server Error

### PHP Basics

#### Output Generation
```php
<?php
// Echo - outputs one or more strings
echo "Hello World";
echo "Hello", " ", "World";

// Print - outputs a string (returns 1)
print "Hello World";

// Print_r - prints human-readable information about a variable
print_r($array);

// Var_dump - dumps information about a variable
var_dump($variable);

// Debug (turn on error reporting)
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
?>
```

#### Variables and Data Types
```php
<?php
// Variables start with $
$name = "John";
$age = 25;
$price = 19.99;
$isActive = true;
$items = array(1, 2, 3);

// Constants
define("SITE_NAME", "MyWebsite");
const DB_HOST = "localhost";

// Data Types: string, integer, float, boolean, array, object, NULL
?>
```

#### Control Structures
```php
<?php
// If-else
if ($age >= 18) {
    echo "Adult";
} elseif ($age >= 13) {
    echo "Teenager";
} else {
    echo "Child";
}

// Switch
switch ($day) {
    case "Monday":
        echo "Start of week";
        break;
    case "Friday":
        echo "End of week";
        break;
    default:
        echo "Mid week";
}

// Loops
for ($i = 0; $i < 10; $i++) {
    echo $i;
}

foreach ($array as $key => $value) {
    echo "$key: $value";
}

while ($x < 10) {
    $x++;
}

do {
    $x++;
} while ($x < 10);
?>
```

#### Functions
```php
<?php
// Function definition
function greet($name, $greeting = "Hello") {
    return "$greeting, $name!";
}

// Function call
echo greet("John"); // Hello, John!
echo greet("Jane", "Hi"); // Hi, Jane!

// Anonymous functions (closures)
$multiply = function($a, $b) {
    return $a * $b;
};
echo $multiply(5, 3); // 15
?>
```

#### Arrays
```php
<?php
// Indexed arrays
$colors = array("red", "green", "blue");
$colors = ["red", "green", "blue"]; // Short syntax

// Associative arrays
$person = array(
    "name" => "John",
    "age" => 30,
    "city" => "New York"
);
$person = ["name" => "John", "age" => 30];

// Multidimensional arrays
$users = [
    ["name" => "John", "age" => 30],
    ["name" => "Jane", "age" => 25]
];

// Array functions
count($array);
array_push($array, "new");
array_pop($array);
in_array("value", $array);
array_key_exists("key", $array);
array_merge($arr1, $arr2);
array_filter($array, $callback);
array_map($callback, $array);
sort($array); // Ascending
rsort($array); // Descending
usort($array, $callback); // Custom sort
?>
```

---

## 2. Client-side Data as Input, Form Processing

### Superglobal Arrays
```php
<?php
// $_GET - data from URL parameters
$name = $_GET['name']; // ?name=John

// $_POST - data from POST requests
$email = $_POST['email'];

// $_REQUEST - contains $_GET, $_POST, and $_COOKIE
$value = $_REQUEST['key'];

// $_SERVER - server and execution environment information
$method = $_SERVER['REQUEST_METHOD'];
$host = $_SERVER['HTTP_HOST'];
$uri = $_SERVER['REQUEST_URI'];
$userAgent = $_SERVER['HTTP_USER_AGENT'];
$remoteAddr = $_SERVER['REMOTE_ADDR'];
?>
```

### Form Processing

#### HTML Form Example
```html
<form action="process.php" method="POST" enctype="multipart/form-data">
    <input type="text" name="username" required>
    <input type="email" name="email" required>
    <input type="password" name="password">
    <textarea name="message"></textarea>
    <select name="country">
        <option value="us">USA</option>
        <option value="uk">UK</option>
    </select>
    <input type="checkbox" name="agree" value="yes">
    <input type="radio" name="gender" value="male">
    <input type="radio" name="gender" value="female">
    <input type="file" name="avatar">
    <button type="submit">Submit</button>
</form>
```

#### Processing Form Data
```php
<?php
// Check if form was submitted
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get form data
    $username = $_POST['username'];
    $email = $_POST['email'];
    $message = $_POST['message'];
    
    // Checkbox (check if set)
    $agree = isset($_POST['agree']) ? true : false;
    
    // Radio button
    $gender = $_POST['gender'];
    
    // Select dropdown
    $country = $_POST['country'];
}
?>
```

### Input Validation and Sanitization
```php
<?php
// Sanitization
$clean_string = filter_var($input, FILTER_SANITIZE_STRING);
$clean_email = filter_var($email, FILTER_SANITIZE_EMAIL);
$clean_url = filter_var($url, FILTER_SANITIZE_URL);
$clean_int = filter_var($number, FILTER_SANITIZE_NUMBER_INT);

// Validation
$is_valid_email = filter_var($email, FILTER_VALIDATE_EMAIL);
$is_valid_url = filter_var($url, FILTER_VALIDATE_URL);
$is_valid_int = filter_var($number, FILTER_VALIDATE_INT);

// Manual validation
if (empty($username)) {
    $errors[] = "Username is required";
}

if (strlen($password) < 8) {
    $errors[] = "Password must be at least 8 characters";
}

if (!preg_match("/^[a-zA-Z0-9_]+$/", $username)) {
    $errors[] = "Username can only contain letters, numbers, and underscores";
}

// Escape output to prevent XSS
echo htmlspecialchars($user_input, ENT_QUOTES, 'UTF-8');

// Helper for sticky forms
function old($key) {
    return isset($_POST[$key]) ? htmlspecialchars($_POST[$key]) : '';
}
?>
```

### File Upload Handling
```php
<?php
if (isset($_FILES['avatar'])) {
    $file = $_FILES['avatar'];
    
    // File properties
    $fileName = $file['name'];
    $fileTmpName = $file['tmp_name'];
    $fileSize = $file['size'];
    $fileError = $file['error'];
    $fileType = $file['type'];
    
    // Check for errors
    if ($fileError === UPLOAD_ERR_OK) {
        // Get file extension
        $fileExt = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        $allowed = ['jpg', 'jpeg', 'png', 'gif'];
        
        if (in_array($fileExt, $allowed)) {
            // Check file size (5MB max)
            if ($fileSize < 5000000) {
                // Generate unique filename
                $newFileName = uniqid('', true) . "." . $fileExt;
                $uploadPath = "uploads/" . $newFileName;
                
                // Move uploaded file
                if (move_uploaded_file($fileTmpName, $uploadPath)) {
                    echo "File uploaded successfully";
                }
            } else {
                echo "File too large";
            }
        } else {
            echo "Invalid file type";
        }
    }
}
?>
```

### Redirects and Headers
```php
<?php
// Redirect
header("Location: success.php");
exit();

// Set content type
header("Content-Type: application/json");

// Download file
header("Content-Type: application/pdf");
header("Content-Disposition: attachment; filename=\"document.pdf\"");

// Cache control
header("Cache-Control: no-cache, must-revalidate");
?>
```

---

## 3. Data Storage

### Working with Files

#### Reading Files
```php
<?php
// Read entire file into string
$content = file_get_contents("data.txt");

// Read file into array (line by line)
$lines = file("data.txt");

// Read file with fopen
$handle = fopen("data.txt", "r");
if ($handle) {
    while (($line = fgets($handle)) !== false) {
        echo $line;
    }
    fclose($handle);
}

// Check if file exists
if (file_exists("data.txt")) {
    // File exists
}
?>
```

#### Writing Files
```php
<?php
// Write to file (overwrites)
file_put_contents("data.txt", "Hello World");

// Append to file
file_put_contents("data.txt", "New line\n", FILE_APPEND);

// Write with fopen
$handle = fopen("data.txt", "w");
fwrite($handle, "Content here");
fclose($handle);

// File modes:
// r - read only
// r+ - read and write
// w - write only (truncate)
// w+ - read and write (truncate)
// a - append only
// a+ - read and append
?>
```

#### Working with JSON
```php
<?php
// Encode to JSON
$data = ["name" => "John", "age" => 30];
$json = json_encode($data);
file_put_contents("data.json", $json);

// Decode from JSON
$json = file_get_contents("data.json");
$data = json_decode($json, true); // true for associative array

// Pretty print JSON
$json = json_encode($data, JSON_PRETTY_PRINT);

// Example: Reading and adding data
$filename = 'data.json';

// Check if file exists
if (!file_exists($filename)) {
    file_put_contents($filename, json_encode([]));
}

$json = file_get_contents($filename);
$data = json_decode($json, true);

// Add new item
$newItem = ['id' => uniqid(), 'text' => 'Learn PHP'];
$data[] = $newItem;

// Save back
file_put_contents($filename, json_encode($data, JSON_PRETTY_PRINT));
?>
```

### MySQL Database with PDO

#### Connection
```php
<?php
try {
    $host = 'localhost';
    $dbname = 'mydb';
    $username = 'root';
    $password = '';
    
    $pdo = new PDO(
        "mysql:host=$host;dbname=$dbname;charset=utf8mb4",
        $username,
        $password,
        [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES => false
        ]
    );
} catch (PDOException $e) {
    die("Connection failed: " . $e->getMessage());
}
?>
```

#### SELECT Queries
```php
<?php
// Simple query
$stmt = $pdo->query("SELECT * FROM users");
$users = $stmt->fetchAll();

// Fetch single row
$stmt = $pdo->query("SELECT * FROM users WHERE id = 1");
$user = $stmt->fetch();

// Prepared statement with named placeholders
$stmt = $pdo->prepare("SELECT * FROM users WHERE email = :email");
$stmt->execute(['email' => $email]);
$user = $stmt->fetch();

// Prepared statement with positional placeholders
$stmt = $pdo->prepare("SELECT * FROM users WHERE age > ?");
$stmt->execute([18]);
$users = $stmt->fetchAll();

// Fetch specific column
$stmt = $pdo->query("SELECT name FROM users");
$names = $stmt->fetchAll(PDO::FETCH_COLUMN);
?>
```

#### INSERT Queries
```php
<?php
// Simple insert
$stmt = $pdo->prepare("INSERT INTO users (name, email, age) VALUES (:name, :email, :age)");
$stmt->execute([
    'name' => $name,
    'email' => $email,
    'age' => $age
]);

// Get last inserted ID
$lastId = $pdo->lastInsertId();

// Insert multiple rows
$stmt = $pdo->prepare("INSERT INTO users (name, email) VALUES (?, ?)");
$users = [
    ['John', 'john@example.com'],
    ['Jane', 'jane@example.com']
];
foreach ($users as $user) {
    $stmt->execute($user);
}
?>
```

#### UPDATE Queries
```php
<?php
$stmt = $pdo->prepare("UPDATE users SET email = :email WHERE id = :id");
$stmt->execute([
    'email' => $newEmail,
    'id' => $userId
]);

// Get number of affected rows
$rowCount = $stmt->rowCount();
?>
```

#### DELETE Queries
```php
<?php
$stmt = $pdo->prepare("DELETE FROM users WHERE id = :id");
$stmt->execute(['id' => $userId]);

$rowCount = $stmt->rowCount();
?>
```

#### Transactions
```php
<?php
try {
    $pdo->beginTransaction();
    
    $pdo->exec("INSERT INTO accounts (name, balance) VALUES ('John', 1000)");
    $pdo->exec("UPDATE accounts SET balance = balance - 100 WHERE name = 'Jane'");
    
    $pdo->commit();
} catch (Exception $e) {
    $pdo->rollBack();
    echo "Failed: " . $e->getMessage();
}
?>
```

---

## 4. Session-handling, Authentication

### Sessions

#### Starting and Using Sessions
```php
<?php
// Start session (must be at the beginning, before any output)
session_start();

// Set session variables
$_SESSION['user_id'] = 123;
$_SESSION['username'] = 'john_doe';
$_SESSION['role'] = 'admin';

// Get session variables
$userId = $_SESSION['user_id'];

// Check if session variable exists
if (isset($_SESSION['user_id'])) {
    echo "User is logged in";
}

// Remove specific session variable
unset($_SESSION['username']);

// Destroy entire session
session_destroy();

// Regenerate session ID (security)
session_regenerate_id(true);
?>
```

#### Session Configuration
```php
<?php
// Set session lifetime (in php.ini or at runtime)
ini_set('session.gc_maxlifetime', 3600); // 1 hour

// Set session cookie parameters
session_set_cookie_params([
    'lifetime' => 3600,
    'path' => '/',
    'domain' => 'example.com',
    'secure' => true,      // Only send over HTTPS
    'httponly' => true,    // Not accessible via JavaScript
    'samesite' => 'Strict' // CSRF protection
]);

session_start();
?>
```

### Cookies
```php
<?php
// Set cookie
setcookie("username", "john", time() + 3600, "/"); // Expires in 1 hour
setcookie("theme", "dark", time() + (86400 * 30), "/"); // Expires in 30 days

// Set secure cookie
setcookie("token", $value, [
    'expires' => time() + 3600,
    'path' => '/',
    'domain' => 'example.com',
    'secure' => true,
    'httponly' => true,
    'samesite' => 'Lax'
]);

// Get cookie
if (isset($_COOKIE['username'])) {
    $username = $_COOKIE['username'];
}

// Delete cookie (set expiration to past)
setcookie("username", "", time() - 3600, "/");
?>
```

### Authentication System

#### Registration
```php
<?php
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = trim($_POST['username']);
    $email = trim($_POST['email']);
    $password = $_POST['password'];
    
    // Validate input
    $errors = [];
    
    if (empty($username)) {
        $errors[] = "Username is required";
    }
    
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $errors[] = "Invalid email";
    }
    
    if (strlen($password) < 8) {
        $errors[] = "Password must be at least 8 characters";
    }
    
    if (empty($errors)) {
        // Hash password
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        
        // Insert into database
        $stmt = $pdo->prepare("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
        try {
            $stmt->execute([$username, $email, $hashedPassword]);
            $_SESSION['success'] = "Registration successful";
            header("Location: login.php");
            exit();
        } catch (PDOException $e) {
            $errors[] = "Username or email already exists";
        }
    }
}
?>
```

#### Login
```php
<?php
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = trim($_POST['username']);
    $password = $_POST['password'];
    
    // Get user from database
    $stmt = $pdo->prepare("SELECT * FROM users WHERE username = ? OR email = ?");
    $stmt->execute([$username, $username]);
    $user = $stmt->fetch();
    
    if ($user && password_verify($password, $user['password'])) {
        // Login successful
        session_regenerate_id(true); // Security: prevent session fixation
        
        $_SESSION['user_id'] = $user['id'];
        $_SESSION['username'] = $user['username'];
        $_SESSION['role'] = $user['role'];
        $_SESSION['logged_in'] = true;
        
        // Remember me functionality (optional)
        if (isset($_POST['remember'])) {
            $token = bin2hex(random_bytes(32));
            setcookie('remember_token', $token, time() + (86400 * 30), '/', '', true, true);
            
            // Store token in database
            $stmt = $pdo->prepare("UPDATE users SET remember_token = ? WHERE id = ?");
            $stmt->execute([$token, $user['id']]);
        }
        
        header("Location: dashboard.php");
        exit();
    } else {
        $error = "Invalid credentials";
    }
}
?>
```

#### Logout
```php
<?php
session_start();

// Unset all session variables
$_SESSION = [];

// Delete session cookie
if (isset($_COOKIE[session_name()])) {
    setcookie(session_name(), '', time() - 3600, '/');
}

// Delete remember me cookie
if (isset($_COOKIE['remember_token'])) {
    setcookie('remember_token', '', time() - 3600, '/');
}

// Destroy session
session_destroy();

header("Location: login.php");
exit();
?>
```

#### Protected Pages (Authentication Check)
```php
<?php
session_start();

// Check if user is logged in
if (!isset($_SESSION['logged_in']) || $_SESSION['logged_in'] !== true) {
    header("Location: login.php");
    exit();
}

// Check user role
if ($_SESSION['role'] !== 'admin') {
    header("Location: access-denied.php");
    exit();
}

// User is authenticated, continue with page content
?>
```

### Security Best Practices
```php
<?php
// 1. CSRF Protection
session_start();

// Generate CSRF token
if (!isset($_SESSION['csrf_token'])) {
    $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
}

// In form:
// <input type="hidden" name="csrf_token" value="<?php echo $_SESSION['csrf_token']; ?>">

// Verify CSRF token
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!isset($_POST['csrf_token']) || $_POST['csrf_token'] !== $_SESSION['csrf_token']) {
        die("CSRF token validation failed");
    }
}

// 2. XSS Prevention
echo htmlspecialchars($user_input, ENT_QUOTES, 'UTF-8');

// 3. SQL Injection Prevention
// Always use prepared statements!
$stmt = $pdo->prepare("SELECT * FROM users WHERE id = ?");
$stmt->execute([$userId]);

// 4. Password Hashing
$hash = password_hash($password, PASSWORD_DEFAULT);
$isValid = password_verify($password, $hash);

// 5. Secure Session Configuration
ini_set('session.cookie_httponly', 1);
ini_set('session.cookie_secure', 1);
ini_set('session.use_strict_mode', 1);
?>
```

---

## 5. AJAX

### AJAX Basics

#### JavaScript (Client-side)
```javascript
// Using Fetch API (modern approach)
fetch('api.php', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name: 'John', age: 30 })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));

// GET request
fetch('api.php?id=123')
    .then(response => response.json())
    .then(data => console.log(data));

// Using async/await
async function fetchData() {
    try {
        const response = await fetch('api.php');
        const data = await response.json();
        console.log(data);
    } catch (error) {
        console.error('Error:', error);
    }
}

// Using XMLHttpRequest
function sendAjaxRequest(url, data, callback) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            callback(xhr.responseText);
        }
    };
    
    xhr.send(data);
}
```

#### PHP (Server-side)
```php
<?php
// api.php - Handle AJAX requests

// Set header for JSON response
header('Content-Type: application/json');

// Handle POST request
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get JSON data
    $json = file_get_contents('php://input');
    $data = json_decode($json, true);
    
    // Or get form data
    $name = $_POST['name'] ?? '';
    $age = $_POST['age'] ?? '';
    
    // Process data
    $result = [
        'success' => true,
        'message' => 'Data received',
        'data' => $data
    ];
    
    echo json_encode($result);
    exit();
}

// Handle GET request
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $id = $_GET['id'] ?? 0;
    
    // Fetch from database
    $stmt = $pdo->prepare("SELECT * FROM users WHERE id = ?");
    $stmt->execute([$id]);
    $user = $stmt->fetch();
    
    if ($user) {
        echo json_encode(['success' => true, 'user' => $user]);
    } else {
        echo json_encode(['success' => false, 'message' => 'User not found']);
    }
    exit();
}
?>
```

### Complete AJAX Examples

#### Example 1: Load Data
```html
<!-- HTML -->
<button onclick="loadUsers()">Load Users</button>
<div id="users"></div>

<script>
function loadUsers() {
    fetch('get-users.php')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                let html = '<ul>';
                data.users.forEach(user => {
                    html += `<li>${user.name} (${user.email})</li>`;
                });
                html += '</ul>';
                document.getElementById('users').innerHTML = html;
            }
        })
        .catch(error => console.error('Error:', error));
}
</script>
```

```php
<?php
// get-users.php
header('Content-Type: application/json');

$stmt = $pdo->query("SELECT id, name, email FROM users");
$users = $stmt->fetchAll();

echo json_encode([
    'success' => true,
    'users' => $users
]);
?>
```

#### Example 2: Submit Form
```html
<!-- HTML -->
<form id="userForm">
    <input type="text" name="name" required>
    <input type="email" name="email" required>
    <button type="submit">Submit</button>
</form>
<div id="message"></div>

<script>
document.getElementById('userForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const formData = new FormData(this);
    
    fetch('save-user.php', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        const messageDiv = document.getElementById('message');
        if (data.success) {
            messageDiv.innerHTML = '<p style="color: green;">' + data.message + '</p>';
            this.reset();
        } else {
            messageDiv.innerHTML = '<p style="color: red;">' + data.message + '</p>';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
});
</script>
```

```php
<?php
// save-user.php
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = trim($_POST['name']);
    $email = trim($_POST['email']);
    
    // Validate
    if (empty($name) || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
        echo json_encode([
            'success' => false,
            'message' => 'Invalid input'
        ]);
        exit();
    }
    
    // Save to database
    $stmt = $pdo->prepare("INSERT INTO users (name, email) VALUES (?, ?)");
    if ($stmt->execute([$name, $email])) {
        echo json_encode([
            'success' => true,
            'message' => 'User saved successfully',
            'id' => $pdo->lastInsertId()
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Failed to save user'
        ]);
    }
}
?>
```

#### Example 3: Live Search
```html
<!-- HTML -->
<input type="text" id="searchInput" placeholder="Search users...">
<div id="results"></div>

<script>
let searchTimeout;

document.getElementById('searchInput').addEventListener('input', function() {
    clearTimeout(searchTimeout);
    
    const query = this.value.trim();
    
    if (query.length < 2) {
        document.getElementById('results').innerHTML = '';
        return;
    }
    
    searchTimeout = setTimeout(() => {
        fetch('search.php?q=' + encodeURIComponent(query))
            .then(response => response.json())
            .then(data => {
                let html = '';
                if (data.results.length > 0) {
                    html = '<ul>';
                    data.results.forEach(user => {
                        html += `<li>${user.name} - ${user.email}</li>`;
                    });
                    html += '</ul>';
                } else {
                    html = '<p>No results found</p>';
                }
                document.getElementById('results').innerHTML = html;
            })
            .catch(error => console.error('Error:', error));
    }, 300); // Debounce 300ms
});
</script>
```

```php
<?php
// search.php
header('Content-Type: application/json');

$query = $_GET['q'] ?? '';

if (strlen($query) >= 2) {
    $searchTerm = "%$query%";
    $stmt = $pdo->prepare("SELECT name, email FROM users WHERE name LIKE ? OR email LIKE ? LIMIT 10");
    $stmt->execute([$searchTerm, $searchTerm]);
    $results = $stmt->fetchAll();
    
    echo json_encode(['results' => $results]);
} else {
    echo json_encode(['results' => []]);
}
?>
```

---

## 6. Organizing Code, Design Patterns, Outlook

### Code Organization

#### File Structure
```
project/
├── config/
│   ├── database.php
│   └── config.php
├── includes/
│   ├── header.php
│   ├── footer.php
│   └── functions.php
├── public/
│   ├── index.php
│   ├── login.php
│   └── css/
│       └── style.css
├── src/
│   ├── classes/
│   │   ├── User.php
│   │   └── Database.php
│   └── controllers/
│       └── UserController.php
└── vendor/
```

#### Configuration File
```php
<?php
// config/config.php
define('DB_HOST', 'localhost');
define('DB_NAME', 'mydb');
define('DB_USER', 'root');
define('DB_PASS', '');

define('SITE_URL', 'http://localhost');
define('SITE_NAME', 'My Website');

// Error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Timezone
date_default_timezone_set('UTC');
?>
```

#### Database Connection (Singleton Pattern)
```php
<?php
// config/database.php
class Database {
    private static $instance = null;
    private $connection;
    
    private function __construct() {
        try {
            $this->connection = new PDO(
                "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME,
                DB_USER,
                DB_PASS,
                [
                    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
                ]
            );
        } catch (PDOException $e) {
            die("Connection failed: " . $e->getMessage());
        }
    }
    
    public static function getInstance() {
        if (self::$instance === null) {
            self::$instance = new Database();
        }
        return self::$instance;
    }
    
    public function getConnection() {
        return $this->connection;
    }
}
?>
```

#### Helper Functions
```php
<?php
// includes/functions.php

function redirect($url) {
    header("Location: $url");
    exit();
}

function sanitize($data) {
    return htmlspecialchars(trim($data), ENT_QUOTES, 'UTF-8');
}

function isLoggedIn() {
    return isset($_SESSION['logged_in']) && $_SESSION['logged_in'] === true;
}

function requireAuth() {
    if (!isLoggedIn()) {
        redirect('login.php');
    }
}

function setFlashMessage($message, $type = 'info') {
    $_SESSION['flash'] = ['message' => $message, 'type' => $type];
}

function getFlashMessage() {
    if (isset($_SESSION['flash'])) {
        $flash = $_SESSION['flash'];
        unset($_SESSION['flash']);
        return $flash;
    }
    return null;
}
?>
```

### Object-Oriented PHP

#### Classes and Objects
```php
<?php
// src/classes/User.php

class User {
    private $id;
    private $username;
    private $email;
    private $db;
    
    public function __construct($db) {
        $this->db = $db;
    }
    
    // Getters
    public function getId() {
        return $this->id;
    }
    
    public function getUsername() {
        return $this->username;
    }
    
    // Setters
    public function setUsername($username) {
        $this->username = $username;
    }
    
    // Methods
    public function create($username, $email, $password) {
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        
        $stmt = $this->db->prepare(
            "INSERT INTO users (username, email, password) VALUES (?, ?, ?)"
        );
        
        return $stmt->execute([$username, $email, $hashedPassword]);
    }
    
    public function findByUsername($username) {
        $stmt = $this->db->prepare("SELECT * FROM users WHERE username = ?");
        $stmt->execute([$username]);
        $data = $stmt->fetch();
        
        if ($data) {
            $this->id = $data['id'];
            $this->username = $data['username'];
            $this->email = $data['email'];
            return true;
        }
        
        return false;
    }
    
    public static function getAll($db) {
        $stmt = $db->query("SELECT * FROM users");
        return $stmt->fetchAll();
    }
}
?>
```

#### Inheritance
```php
<?php
class Person {
    protected $name;
    protected $age;
    
    public function __construct($name, $age) {
        $this->name = $name;
        $this->age = $age;
    }
    
    public function getName() {
        return $this->name;
    }
}

class Student extends Person {
    private $studentId;
    
    public function __construct($name, $age, $studentId) {
        parent::__construct($name, $age);
        $this->studentId = $studentId;
    }
    
    public function getStudentId() {
        return $this->studentId;
    }
}
?>
```

### Design Patterns

#### MVC Pattern
```php
<?php
// Model
class UserModel {
    private $db;
    
    public function __construct($db) {
        $this->db = $db;
    }
    
    public function getAllUsers() {
        $stmt = $this->db->query("SELECT * FROM users");
        return $stmt->fetchAll();
    }
}

// View
class UserView {
    public function displayUsers($users) {
        echo "<h1>Users</h1><ul>";
        foreach ($users as $user) {
            echo "<li>{$user['name']} - {$user['email']}</li>";
        }
        echo "</ul>";
    }
}

// Controller
class UserController {
    private $model;
    private $view;
    
    public function __construct($model, $view) {
        $this->model = $model;
        $this->view = $view;
    }
    
    public function showAllUsers() {
        $users = $this->model->getAllUsers();
        $this->view->displayUsers($users);
    }
}
?>
```

---

## Quick Reference

### Common PHP Functions

**String Functions:**
- `strlen($str)` - Get string length
- `substr($str, $start, $length)` - Extract substring
- `str_replace($search, $replace, $str)` - Replace string
- `strpos($str, $needle)` - Find position of substring
- `strtolower($str)` - Convert to lowercase
- `strtoupper($str)` - Convert to uppercase
- `trim($str)` - Remove whitespace
- `explode($delimiter, $str)` - Split string to array
- `implode($glue, $array)` - Join array to string

**Array Functions:**
- `count($array)` - Count elements
- `in_array($value, $array)` - Check if value exists
- `array_key_exists($key, $array)` - Check if key exists
- `array_merge($arr1, $arr2)` - Merge arrays
- `array_filter($array, $callback)` - Filter array
- `array_map($callback, $array)` - Transform array
- `sort($array)` - Sort ascending
- `rsort($array)` - Sort descending
- `usort($array, $callback)` - Custom sort

**File Functions:**
- `file_exists($file)` - Check if file exists
- `file_get_contents($file)` - Read entire file
- `file_put_contents($file, $data)` - Write to file
- `unlink($file)` - Delete file

### Security Checklist
- ✅ Use prepared statements for database queries
- ✅ Validate and sanitize all user input
- ✅ Escape output with `htmlspecialchars()`
- ✅ Hash passwords with `password_hash()`
- ✅ Use HTTPS for sensitive data
- ✅ Implement CSRF protection
- ✅ Set secure session configuration
- ✅ Validate file uploads
- ✅ Always call `exit()` after `header()` redirects
- ✅ Keep PHP and libraries up to date

### JavaScript vs PHP Comparison

| Feature | JavaScript | PHP |
|---------|-----------|-----|
| Variables | `const x = 1` | `$x = 1;` |
| Concat | `"A" + "B"` | `"A" . "B"` |
| Object/Assoc | `{ key: "val" }` | `['key' => 'val']` |
| Property | `obj.key` | `$arr['key']` (Array) or `$obj->key` (Object) |
| Length | `arr.length` | `count($arr)` |
| Append | `arr.push(x)` | `$arr[] = $x;` |
| Logic | `&&, ||` | `&&, ||` |

---

## Exam Tips

1. **Understand HTTP methods** - Know when to use GET vs POST
2. **Form handling** - Remember to validate and sanitize input
3. **Database operations** - Always use prepared statements
4. **Sessions** - Call `session_start()` before any output
5. **AJAX** - Set proper headers (`Content-Type: application/json`) and return JSON
6. **File uploads** - Check file size, type, and errors
7. **Authentication** - Hash passwords, regenerate session IDs, verify credentials
8. **Error handling** - Use try-catch for exceptions
9. **Code organization** - Separate concerns (MVC pattern)
10. **Security** - CSRF tokens, XSS prevention, SQL injection prevention

Good luck with your exam! 🚀
