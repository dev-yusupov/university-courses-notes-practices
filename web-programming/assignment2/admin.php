<?php
require_once 'includes/Auth.php';
require_once 'includes/ProjectManager.php';

$auth = new Auth();
$pm = new ProjectManager();

if (!$auth->isAdmin()) {
    header('Location: index.php');
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $projectId = $_POST['project_id'] ?? '';
    $action = $_POST['action'] ?? '';

    if ($projectId) {
        if ($action === 'approved') {
            $pm->updateProjectStatus($projectId, ProjectManager::STATUS_APPROVED);
            $_SESSION['flash_success'] = "Project Approved!";
        } elseif ($action === 'rejected') {
            $pm->updateProjectStatus($projectId, ProjectManager::STATUS_REJECTED);
            $_SESSION['flash_success'] = "Project Rejected!";
        } elseif ($action === 'rework') {
            $comment = trim($_POST['comment'] ?? '');
            if ($comment !== '') {
                $pm->requestRework($projectId, $comment);
                $_SESSION['flash_success'] = "Rework requested.";
            } else {
                $_SESSION['flash_error'] = "Comment required for rework.";
            }
        }
    }
    header('Location: admin.php');
    exit;
}

$pendingProjects = $pm->getAllProjects(ProjectManager::STATUS_PENDING);
?>
<?php include 'includes/header.php'; ?>

<div class="container">
    <h2>Admin Dashboard</h2>
    <p>Manage project proposals.</p>

    <h3>Pending Submissions</h3>
    <?php if (count($pendingProjects) === 0): ?>
        <p>No pending projects.</p>
    <?php else: ?>
        <div class="grid">
            <?php foreach ($pendingProjects as $p): ?>
                <div class="card">
                    <h3><a href="details.php?id=<?= $p['id'] ?>"
                            style="text-decoration:none; color:inherit;"><?= htmlspecialchars($p['title']) ?></a></h3>

                    <div style="margin: 10px 0;">
                        <span
                            class="badge"><?= htmlspecialchars(ProjectManager::CATEGORIES[$p['category']] ?? $p['category']) ?></span>
                        <?php if (!empty($p['postal_code'])): ?>
                            <span class="badge" style="background:#555">Zip: <?= htmlspecialchars($p['postal_code']) ?></span>
                        <?php endif; ?>
                    </div>

                    <?php if (!empty($p['image'])): ?>
                        <div style="margin:10px 0;">
                            <img src="<?= htmlspecialchars($p['image']) ?>"
                                style="max-width:100px; height:auto; border:1px solid #ccc;">
                        </div>
                    <?php endif; ?>

                    <p><?= nl2br(htmlspecialchars($p['description'])) ?></p>
                    <small>Submitted by user ID: <?= $p['owner'] ?></small>

                    <div class="actions" style="margin-top: 10px; border-top: 1px solid #eee; padding-top: 10px;">
                        <form action="admin.php" method="post" style="display:inline;">
                            <input type="hidden" name="project_id" value="<?= $p['id'] ?>">
                            <input type="hidden" name="action" value="approved">
                            <button type="submit" style="background-color: var(--success); margin-right:5px;">Approve</button>
                        </form>
                        <form action="admin.php" method="post" style="display:inline;">
                            <input type="hidden" name="project_id" value="<?= $p['id'] ?>">
                            <input type="hidden" name="action" value="rejected">
                            <button type="submit" style="background-color: var(--danger);">Reject</button>
                        </form>

                        <form action="admin.php" method="post"
                            style="margin-top:10px; border-top:1px dashed #ccc; padding-top:10px;">
                            <input type="hidden" name="project_id" value="<?= $p['id'] ?>">
                            <input type="hidden" name="action" value="rework">
                            <textarea name="comment" rows="2" placeholder="Reason for rework..."
                                style="width:100%; margin-bottom:5px;" required></textarea>
                            <button type="submit" style="background-color: #e67e22; width:100%;">Request Rework</button>
                        </form>
                    </div>
                </div>
            <?php endforeach; ?>
        </div>
    <?php endif; ?>
</div>

<?php include 'includes/footer.php'; ?>