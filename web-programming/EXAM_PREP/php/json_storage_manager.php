<?php
// A robust helper to read/write JSON files safely
class JsonStorage {
    private $filename;

    public function __construct($filename) {
        $this->filename = $filename;
    }

    public function all() {
        if (!file_exists($this->filename)) {
            return [];
        }
        $json = file_get_contents($this->filename);
        return json_decode($json, true) ?? [];
    }

    public function add($item) {
        $data = $this->all();
        $data[] = $item; // or use a specific ID key
        $this->save($data);
    }
    
    // Filter helper (like JS .filter)
    public function filter($callback) {
        return array_filter($this->all(), $callback);
    }

    public function save($data) {
        file_put_contents($this->filename, json_encode($data, JSON_PRETTY_PRINT));
    }
}

// Usage:
// $store = new JsonStorage('data.json');
// $users = $store->all();
?>