<?php
class JsonStorage {
    private $filepath;

    public function __construct($filename) {
        $this->filepath = $filename;
        // Create file if not exists
        if (!file_exists($this->filepath)) {
            file_put_contents($this->filepath, json_encode([]));
        }
    }

    // GET ALL DATA
    public function all() {
        return json_decode(file_get_contents($this->filepath), true) ?? [];
    }

    // INSERT NEW ITEM
    public function add($record) {
        $data = $this->all();
        // Auto-increment ID logic (optional but recommended)
        $id = count($data) > 0 ? max(array_column($data, 'id')) + 1 : 1;
        $record['id'] = $id;
        
        $data[] = $record;
        $this->save($data);
        return $id;
    }

    // FIND BY ID
    public function findById($id) {
        $data = $this->all();
        foreach ($data as $item) {
            if ($item['id'] == $id) return $item;
        }
        return null;
    }
    
    // FILTER (e.g., find all users over 18)
    public function filter($callback) {
        return array_filter($this->all(), $callback);
    }

    private function save($data) {
        file_put_contents($this->filepath, json_encode($data, JSON_PRETTY_PRINT));
    }
}
?>
