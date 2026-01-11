<?php
require_once 'includes/Auth.php';
require_once 'includes/ProjectManager.php';

$auth = new Auth();
$pm = new ProjectManager();

if (!$auth->isAdmin()) {
    header('Location: index.php');
    exit;
}

$allProjects = $pm->getAllProjects(null); // Get all

// 1. Calculate Global Top
$topProject = null;
$maxVotes = -1;
foreach ($allProjects as $p) {
    if ($p['status'] === ProjectManager::STATUS_APPROVED && $p['voteCount'] > $maxVotes) {
        $maxVotes = $p['voteCount'];
        $topProject = $p;
    }
}

// 2. Group for Lists (Approved only)
$approvedProjects = array_filter($allProjects, fn($p) => $p['status'] === ProjectManager::STATUS_APPROVED);
$categoriesData = [];
foreach (ProjectManager::CATEGORIES as $key => $label) {
    $categoriesData[$key] = [
        'label' => $label,
        'projects' => []
    ];
}

foreach ($approvedProjects as $p) {
    if (isset($categoriesData[$p['category']])) {
        $categoriesData[$p['category']]['projects'][] = $p;
    }
}

// Sort each category
foreach ($categoriesData as &$data) {
    usort($data['projects'], fn($a, $b) => $b['voteCount'] <=> $a['voteCount']);
}
unset($data);

// 3. Matrix Data (Status vs Category)
// Rows: Categories. Cols: Statuses (Ints)
$statuses = [
    ProjectManager::STATUS_PENDING => 'pending',
    ProjectManager::STATUS_APPROVED => 'approved',
    ProjectManager::STATUS_REJECTED => 'rejected'
];

$matrix = [];
foreach (ProjectManager::CATEGORIES as $catKey => $catLabel) {
    $matrix[$catKey] = ['label' => $catLabel, 'counts' => array_map(fn() => 0, $statuses)];
}

foreach ($allProjects as $p) {
    $cat = $p['category'];
    $stat = $p['status'];
    if (isset($matrix[$cat]) && isset($matrix[$cat]['counts'][$stat])) {
        $matrix[$cat]['counts'][$stat]++;
    }
}

?>
<?php include 'includes/header.php'; ?>

<div class="container">
    <h2>Project Statistics</h2>

    <section class="stat-section">
        <h3>Most Popular Project</h3>
        <?php if ($topProject): ?>
            <div class="card" style="border-left: 5px solid gold; cursor:pointer;"
                onclick="alert('TODO: Open details for <?= $topProject['id'] ?>')">
                <h4><?= htmlspecialchars($topProject['title']) ?></h4>
                <p><strong><?= $topProject['voteCount'] ?> Votes</strong></p>
                <span
                    class="badge"><?= htmlspecialchars(ProjectManager::CATEGORIES[$topProject['category']] ?? $topProject['category']) ?></span>
            </div>
        <?php else: ?>
            <p>No votes yet.</p>
        <?php endif; ?>
    </section>

    <section class="stat-section">
        <h3>Top 3 per Category</h3>
        <div class="grid">
            <?php foreach ($categoriesData as $catKey => $data): ?>
                <?php if (!empty($data['projects'])): ?>
                    <div class="card">
                        <h4><?= $data['label'] ?></h4>
                        <ol>
                            <?php
                            $count = 0;
                            foreach ($data['projects'] as $p):
                                if ($count++ >= 3)
                                    break;
                                ?>
                                <li>
                                    <strong><?= $p['voteCount'] ?></strong> - <?= htmlspecialchars($p['title']) ?>
                                </li>
                            <?php endforeach; ?>
                        </ol>
                    </div>
                <?php endif; ?>
            <?php endforeach; ?>
        </div>
    </section>

    <section class="stat-section">
        <h3>Analytics</h3>

        <div style="display: flex; gap: 40px; flex-wrap: wrap;">
            <!-- Chart 1: By Category -->
            <div
                style="flex: 1; min-width: 300px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
                <h4>Projects by Category</h4>
                <?php
                // Calculate counts
                $catCounts = [];
                $maxCat = 0;
                foreach ($allProjects as $p) {
                    $c = $p['category'];
                    $catCounts[$c] = ($catCounts[$c] ?? 0) + 1;
                    if ($catCounts[$c] > $maxCat)
                        $maxCat = $catCounts[$c];
                }
                foreach (ProjectManager::CATEGORIES as $k => $label):
                    $count = $catCounts[$k] ?? 0;
                    $width = $maxCat > 0 ? ($count / $maxCat) * 100 : 0;
                    ?>
                    <div style="margin-bottom: 10px;">
                        <div style="display:flex; justify-content:space-between; font-size:0.9rem;">
                            <span><?= htmlspecialchars($label) ?></span>
                            <span><?= $count ?></span>
                        </div>
                        <div style="background:#eee; height:10px; border-radius:5px; overflow:hidden;">
                            <div style="background:var(--primary); width:<?= $width ?>%; height:100%;"></div>
                        </div>
                    </div>
                <?php endforeach; ?>
            </div>

            <!-- Chart 2: By Status -->
            <div
                style="flex: 1; min-width: 300px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
                <h4>Projects by Status</h4>
                <?php
                // Calculate counts
                $statCounts = [
                    ProjectManager::STATUS_PENDING => 0,
                    ProjectManager::STATUS_APPROVED => 0,
                    ProjectManager::STATUS_REJECTED => 0
                ];
                $maxStat = 0;
                foreach ($allProjects as $p) {
                    $s = (int) $p['status'];
                    if (isset($statCounts[$s])) {
                        $statCounts[$s]++;
                        if ($statCounts[$s] > $maxStat)
                            $maxStat = $statCounts[$s];
                    }
                }

                $statColors = [
                    ProjectManager::STATUS_PENDING => '#f39c12',
                    ProjectManager::STATUS_APPROVED => '#2ecc71',
                    ProjectManager::STATUS_REJECTED => '#e74c3c'
                ];
                $statLabels = [
                    ProjectManager::STATUS_PENDING => 'Pending',
                    ProjectManager::STATUS_APPROVED => 'Approved',
                    ProjectManager::STATUS_REJECTED => 'Rejected'
                ];

                foreach ($statCounts as $state => $count):
                    $width = $maxStat > 0 ? ($count / $maxStat) * 100 : 0;
                    ?>
                    <div style="margin-bottom: 10px;">
                        <div style="display:flex; justify-content:space-between; font-size:0.9rem;">
                            <span><?= $statLabels[$state] ?></span>
                            <span><?= $count ?></span>
                        </div>
                        <div style="background:#eee; height:10px; border-radius:5px; overflow:hidden;">
                            <div style="background:<?= $statColors[$state] ?>; width:<?= $width ?>%; height:100%;"></div>
                        </div>
                    </div>
                <?php endforeach; ?>
            </div>
        </div>

        <!-- Detailed Matrix -->
        <h4 style="margin-top: 30px;">Detailed Breakdown (Category vs Status)</h4>
        <table border="1" cellpadding="10" style="border-collapse: collapse; width: 100%; background:white;">
            <thead>
                <tr style="background:#f1f1f1;">
                    <th>Category</th>
                    <th>Pending</th>
                    <th>Approved</th>
                    <th>Rejected</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($matrix as $catKey => $row): ?>
                    <tr>
                        <td><strong><?= htmlspecialchars($row['label']) ?></strong></td>
                        <td style="color:#f39c12"><?= $row['counts'][ProjectManager::STATUS_PENDING] ?></td>
                        <td style="color:#2ecc71"><?= $row['counts'][ProjectManager::STATUS_APPROVED] ?></td>
                        <td style="color:#e74c3c"><?= $row['counts'][ProjectManager::STATUS_REJECTED] ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </section>
</div>

<?php include 'includes/footer.php'; ?>