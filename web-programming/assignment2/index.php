<?php
require_once 'includes/Auth.php';
$auth = new Auth();
?>
<?php include 'includes/header.php'; ?>

<section class="hero">
    <h1>Help shape the future of Budapest!</h1>
    <p>Vote for the best community projects or submit your own idea.</p>
</section>

<section class="project-list-container">
    <h2>Community Projects</h2>

    <div class="filters">
        <label for="category-filter">Filter by Category:</label>
        <select id="category-filter">
            <option value="">All Categories</option>
            <option value="local_small">Local small project</option>
            <option value="local_large">Local large project</option>
            <option value="equal_opp">Equal opportunity Budapest</option>
            <option value="green">Green Budapest</option>
        </select>
    </div>

    <div id="projects-grid" class="grid">
        <!-- Projects will be loaded here via JS or PHP -->
        <p>Loading projects...</p>
    </div>
</section>

<?php include 'includes/footer.php'; ?>