<?php
require_once __DIR__ . '/Storage.php';

class ProjectManager
{
    private $projectStorage;
    private $voteStorage;

    public function __construct()
    {
        $this->projectStorage = new Storage('projects.json');
        $this->voteStorage = new Storage('votes.json');
    }

    const CATEGORIES = [
        0 => 'Local small project',
        1 => 'Local large project',
        2 => 'Equal opportunity Budapest',
        3 => 'Green Budapest'
    ];

    // Status Constants
    const STATUS_PENDING = 0;
    const STATUS_APPROVED = 1;
    const STATUS_REJECTED = 2;
    const STATUS_NEEDS_REWORK = 3;

    public function createProject($title, $description, $categoryId, $postalCode, $imageUrl, $userId)
    {
        $errors = [];

        // Validation
        if (strlen($title) < 10)
            $errors['title'] = 'Title must be at least 10 characters';
        if (strlen($description) < 150)
            $errors['description'] = 'Description must be at least 150 characters';

        $categoryId = (int) $categoryId;
        if (!array_key_exists($categoryId, self::CATEGORIES)) {
            $errors['category'] = 'Invalid category';
        }

        // Postal Code: 1(01-23)(1-9) OR 1007
        if (!preg_match('/^(1(0[1-9]|1[0-9]|2[0-3])[1-9]|1007)$/', $postalCode)) {
            $errors['postal_code'] = 'Invalid Postal Code (Budapest format required: e.g. 1011, or 1007)';
        }

        if (!empty($imageUrl) && !filter_var($imageUrl, FILTER_VALIDATE_URL)) {
            $errors['image_url'] = 'Invalid Image URL';
        }

        if (!empty($errors)) {
            return ['errors' => $errors];
        }

        $newProject = [
            'id' => uniqid('proj_', true),
            'title' => $title,
            'description' => $description,
            'category' => $categoryId,         // Int
            'postal_code' => $postalCode,      // snake_case
            'image' => $imageUrl,              // snake_case
            'owner' => $userId,                // snake_case
            'status' => self::STATUS_PENDING,  // Int (0)
            'submitted' => date('Y-m-d H:i'),  // snake_case
        ];
        $this->projectStorage->add($newProject);
        return ['success' => true];
    }

    public function getAllProjects($status = null)
    {
        $projects = $this->projectStorage->filter(function ($p) use ($status) {
            if ($status === null)
                return true;
            return $p['status'] === $status;
        });

        // Compute vote counts
        $allVotes = $this->voteStorage->findAll();
        $voteCounts = [];
        foreach ($allVotes as $v) {
            if (!isset($v['project']))
                continue; // Skip malformed votes
            $pid = $v['project'];
            $voteCounts[$pid] = ($voteCounts[$pid] ?? 0) + 1;
        }

        foreach ($projects as &$p) {
            $p['voteCount'] = $voteCounts[$p['id']] ?? 0;
        }

        return $projects;
    }

    public function getProjectsByUser($userId)
    {
        $projects = $this->projectStorage->filter(function ($p) use ($userId) {
            return $p['owner'] === $userId;
        });

        // Compute votes
        $allVotes = $this->voteStorage->findAll();
        $voteCounts = [];
        foreach ($allVotes as $v) {
            if (!isset($v['project']))
                continue;
            $pid = $v['project'];
            $voteCounts[$pid] = ($voteCounts[$pid] ?? 0) + 1;
        }

        foreach ($projects as &$p) {
            $p['voteCount'] = $voteCounts[$p['id']] ?? 0;
        }
        return $projects;
    }

    public function getProject($id)
    {
        $project = $this->projectStorage->findById($id);
        if ($project) {
            // Count votes
            $votes = $this->voteStorage->filter(function ($v) use ($id) {
                return isset($v['project']) && $v['project'] === $id;
            });
            $project['voteCount'] = count($votes);
        }
        return $project;
    }

    public function updateProjectStatus($id, $statusId)
    {
        $this->projectStorage->update($id, function (&$p) use ($statusId) {
            $p['status'] = (int) $statusId;
            if ($statusId == self::STATUS_APPROVED && !isset($p['approved'])) {
                $p['approved'] = date('Y-m-d H:i');
            }
        });
    }

    public function requestRework($id, $comment)
    {
        $this->projectStorage->update($id, function (&$p) use ($comment) {
            $p['status'] = self::STATUS_NEEDS_REWORK;

            if (!isset($p['history']))
                $p['history'] = [];
            $p['history'][] = [
                'date' => date('Y-m-d H:i'),
                'action' => 'rework_requested',
                'comment' => $comment
            ];
        });
    }

    public function editProject($id, $title, $description, $category, $postalCode, $imageUrl)
    {
        $this->projectStorage->update($id, function (&$p) use ($title, $description, $category, $postalCode, $imageUrl) {
            $changes = [];

            // Compare and record changes
            if ($p['title'] !== $title)
                $changes['title'] = ['old' => $p['title'], 'new' => $title];
            if ($p['description'] !== $description)
                $changes['description'] = ['old' => $p['description'], 'new' => $description];
            if ($p['category'] != $category)
                $changes['category'] = ['old' => $p['category'], 'new' => (int) $category];
            if ($p['postal_code'] !== $postalCode)
                $changes['postal_code'] = ['old' => $p['postal_code'], 'new' => $postalCode];
            if ($p['image'] !== $imageUrl)
                $changes['image'] = ['old' => $p['image'], 'new' => $imageUrl];

            if (empty($changes))
                return; // No changes

            // Apply updates
            $p['title'] = $title;
            $p['description'] = $description;
            $p['category'] = (int) $category;
            $p['postal_code'] = $postalCode;
            $p['image'] = $imageUrl;

            // Reset to Pending
            $p['status'] = self::STATUS_PENDING;

            if (!isset($p['history']))
                $p['history'] = [];
            $p['history'][] = [
                'date' => date('Y-m-d H:i'),
                'action' => 'edited',
                'changes' => $changes
            ];
        });
    }

    // Voting Logic
    public function vote($userId, $projectId)
    {
        $project = $this->projectStorage->findById($projectId);
        if (!$project)
            return ['error' => 'Project not found'];

        // Check if voting is closed (2 weeks after approved)
        if (isset($project['approved'])) {
            $approvedTime = strtotime($project['approved']);
            $now = time();
            $diffDays = ($now - $approvedTime) / (60 * 60 * 24);
            if ($diffDays > 14) {
                return ['error' => 'Voting is closed for this project'];
            }
        }

        // Check if user already voted for THIS project
        $existingVote = $this->voteStorage->findOne(function ($v) use ($userId, $projectId) {
            return isset($v['user']) && isset($v['project']) && $v['user'] === $userId && $v['project'] === $projectId;
        });

        if ($existingVote) {
            // Withdraw vote
            $this->voteStorage->delete($existingVote['id']);
            return ['success' => true, 'action' => 'withdrawn'];
        }

        // Check 3 votes per category limit
        $catId = $project['category'];
        $userVotesInCat = $this->voteStorage->filter(function ($v) use ($userId, $catId) {
            // We need to fetch projects to know their category?
            // Vote storage only has user, project.
            // This logic needs to look up projects. Expensive-ish but JSON is small.
            return isset($v['user']) && $v['user'] === $userId;
        });

        // Filter those votes by category
        $countInCat = 0;
        foreach ($userVotesInCat as $uv) {
            if (!isset($uv['project']))
                continue;
            $p = $this->projectStorage->findById($uv['project']);
            if ($p && $p['category'] == $catId) {
                $countInCat++;
            }
        }

        if ($countInCat >= 3) {
            return ['error' => 'Max 3 votes per category'];
        }

        // Add Vote
        // Vote Schema: { "user": 456, "project": 123 }
        // I need an ID for the vote storage itself to support deletions easily (Storage class needs ID usually).
        // The user schema example for Votes is just { user, project }. 
        // My Storage class expects an 'id' for CRUD. I will add one.
        $this->voteStorage->add([
            'id' => uniqid('vote_', true),
            'user' => $userId,
            'project' => $projectId
        ]);

        return ['success' => true, 'action' => 'voted'];
    }

    public function getUserVotes($userId)
    {
        return $this->voteStorage->filter(function ($v) use ($userId) {
            return isset($v['user']) && $v['user'] === $userId;
        });
    }
}
