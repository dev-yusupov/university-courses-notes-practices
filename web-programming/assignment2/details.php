<?php
require_once 'includes/Auth.php';
require_once 'includes/ProjectManager.php';

$auth = new Auth();
$pm = new ProjectManager();

$id = $_GET['id'] ?? null;
if (!$id) {
    header('Location: index.php');
    exit;
}

$project = $pm->getProject($id);

if (!$project) {
    echo "Project not found.";
    exit;
}

// Access Control Logic
$isOwner = $auth->isLoggedIn() && $auth->getCurrentUserId() === $project['owner'];
$isAdmin = $auth->isAdmin();
$isPublic = $project['status'] === ProjectManager::STATUS_APPROVED;

if (!$isPublic && !$isOwner && !$isAdmin) {
    // Redirect unauthorized users
    header('Location: index.php');
    exit;
}
?>
<?php include 'includes/header.php'; ?>

<div class="container" style="max-width: 800px;">
    <div style="margin-bottom: 20px;">
        <a href="index.php">&larr; Back to Projects</a>
    </div>

    <div class="card">
        <h1><?= htmlspecialchars($project['title']) ?></h1>

        <div style="margin-bottom: 20px;">
            <?php
            $statusLabel = 'Unknown';
            $statusColor = '#7f8c8d';
            switch ($project['status']) {
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
            }
            ?>
            <span class="badge" style="background-color: <?= $statusColor ?>">
                <?= $statusLabel ?>
            </span>
            <span class="badge" style="background-color: #555;">
                <?= ProjectManager::CATEGORIES[$project['category']] ?? 'Unknown' ?>
            </span>
            <?php if (!empty($project['postal_code'])): ?>
                <span class="badge" style="background-color: #7f8c8d;">Zip:
                    <?= htmlspecialchars($project['postal_code']) ?></span>
            <?php endif; ?>
        </div>

        <?php if (!empty($project['image'])): ?>
            <div style="margin-bottom: 20px;">
                <img src="<?= htmlspecialchars($project['image']) ?>" alt="Project Image"
                    style="max-width: 100%; border-radius: 8px;">
            </div>
        <?php endif; ?>

        <div style="font-size: 1.1rem; line-height: 1.8;">
            <?= nl2br(htmlspecialchars($project['description'])) ?>
        </div>

        <div style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;">
            <p><strong>Votes:</strong> <?= $project['voteCount'] ?></p>
            <p>submitted on: <?= date('Y-m-d H:i', strtotime($project['submitted'])) ?></p>
            <?php if (isset($project['approved'])): ?>
                <p>Approved on: <?= date('Y-m-d H:i', strtotime($project['approved'])) ?></p>
            <?php endif; ?>
        </div>

        <!-- History Log (Admin/Owner) -->
        <?php if (($isAdmin || $isOwner) && !empty($project['history'])): ?>
            <div style="margin-top: 40px; border-top: 1px solid #eee; padding-top: 20px;">
                <h3>Project History</h3>
                <?php foreach (array_reverse($project['history']) as $log): ?>
                    <div
                        style="background: #f9f9f9; padding: 15px; border-radius: 4px; margin-bottom: 15px; border-left: 3px solid #ccc;">
                        <div style="font-size: 0.9rem; color: #777; margin-bottom: 5px;">
                            <strong><?= htmlspecialchars($log['action']) ?></strong> &bull;
                            <?= htmlspecialchars($log['date']) ?>
                        </div>

                        <?php if (isset($log['comment'])): ?>
                            <div style="margin-top: 5px;">
                                <strong>Comment:</strong> <?= htmlspecialchars($log['comment']) ?>
                            </div>
                        <?php endif; ?>

                        <?php if (isset($log['changes']) && !empty($log['changes'])): ?>
                            <table
                                style="width: 100%; margin-top: 10px; border-collapse: collapse; font-size: 0.9rem; background:white;">
                                <thead>
                                    <tr style="background: #eee;">
                                        <th style="text-align: left; padding: 5px; border: 1px solid #ddd;">Field</th>
                                        <th style="text-align: left; padding: 5px; border: 1px solid #ddd;">Old Value</th>
                                        <th style="text-align: left; padding: 5px; border: 1px solid #ddd;">New Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php foreach ($log['changes'] as $field => $vals): ?>
                                        <tr>
                                            <td style="padding: 5px; border: 1px solid #ddd; font-weight:bold;">
                                                <?= htmlspecialchars($field) ?></td>
                                            <td style="padding: 5px; border: 1px solid #ddd; color: #e74c3c;">
                                                <?= htmlspecialchars($vals['old']) ?></td>
                                            <td style="padding: 5px; border: 1px solid #ddd; color: #2ecc71;">
                                                <?= htmlspecialchars($vals['new']) ?></td>
                                        </tr>
                                    <?php endforeach; ?>
                                </tbody>
                            </table>
                        <?php endif; ?>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>
    </div>
</div>

<?php include 'includes/footer.php'; ?>