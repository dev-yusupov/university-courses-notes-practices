<?php
require_once 'includes/Auth.php';
require_once 'includes/ProjectManager.php';

$auth = new Auth();
$pm = new ProjectManager();

if (!$auth->isLoggedIn()) {
    header('Location: login.php');
    exit;
}

$myProjects = $pm->getProjectsByUser($auth->getCurrentUserId());
?>
<?php include 'includes/header.php'; ?>

<div class="container">
    <h2>My Projects</h2>
    <?php if (empty($myProjects)): ?>
        <p>You haven't submitted any projects yet.</p>
    <?php else: ?>
        <div class="grid">
            <?php foreach ($myProjects as $p): ?>
                <div class="card" style="border-left: 5px solid #ccc;">
                    <h3><a href="details.php?id=<?= $p['id'] ?>"
                            style="text-decoration:none; color:inherit;"><?= htmlspecialchars($p['title']) ?></a></h3>
                    <div style="margin-bottom:10px;">
                        <?php
                        $statusLabel = 'Unknown';
                        $statusColor = '#7f8c8d';
                        switch ($p['status']) {
                            case ProjectManager::STATUS_APPROVED:
                                $statusLabel = 'Approved';
                                $statusColor = 'var(--success)';
                                break;
                            case ProjectManager::STATUS_REJECTED:
                                $statusLabel = 'Rejected';
                                $statusColor = 'var(--danger)';
                                break;
                            case ProjectManager::STATUS_PENDING:
                                $statusLabel = 'Pending';
                                $statusColor = '#f39c12';
                                break;
                            case ProjectManager::STATUS_NEEDS_REWORK:
                                $statusLabel = 'Needs Rework';
                                $statusColor = '#e67e22';
                                break;
                        }
                        ?>
                        <span class="badge" style="background-color: <?= $statusColor ?>">
                            <?= $statusLabel ?>
                        </span>
                        <span class="badge"><?= ProjectManager::CATEGORIES[$p['category']] ?? 'Unknown' ?></span>
                    </div>
                    <p>
                        <?= nl2br(htmlspecialchars($p['description'])) ?>
                    </p>

                    <?php if ((int) $p['status'] === ProjectManager::STATUS_NEEDS_REWORK): ?>
                        <div style="margin-top:15px;">
                            <a href="edit.php?id=<?= $p['id'] ?>"
                                style="background-color:#e67e22; color:white; padding:8px 15px; text-decoration:none; border-radius:4px; display:inline-block;">Edit
                                Project</a>
                        </div>
                    <?php endif; ?>
                </div>
            <?php endforeach; ?>
        </div>
    <?php endif; ?>
</div>

<?php include 'includes/footer.php'; ?>