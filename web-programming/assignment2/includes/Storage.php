<?php

class Storage
{
    private $filePath;

    public function __construct(string $filename)
    {
        $this->filePath = __DIR__ . '/../data/' . $filename;
        $dir = dirname($this->filePath);
        if (!file_exists($dir)) {
            mkdir($dir, 0777, true);
        }
        if (!file_exists($this->filePath)) {
            file_put_contents($this->filePath, json_encode([]));
        }
    }

    public function findAll(): array
    {
        $content = file_get_contents($this->filePath);
        return json_decode($content, true) ?? [];
    }

    public function findById(string $id)
    {
        $items = $this->findAll();
        foreach ($items as $item) {
            if ($item['id'] === $id) {
                return $item;
            }
        }
        return null;
    }

    public function findOne(callable $callback)
    {
        $items = $this->findAll();
        foreach ($items as $item) {
            if ($callback($item)) {
                return $item;
            }
        }
        return null;
    }

    public function filter(callable $callback): array
    {
        $items = $this->findAll();
        return array_filter($items, $callback);
    }

    public function add(array $item): string
    {
        $items = $this->findAll();
        if (!isset($item['id'])) {
            $item['id'] = uniqid();
        }
        $items[] = $item;
        $this->save($items);
        return $item['id'];
    }

    public function update(string $id, callable $updater)
    {
        $items = $this->findAll();
        $updated = false;
        foreach ($items as &$item) {
            if ($item['id'] === $id) {
                $updater($item);
                $updated = true;
                break;
            }
        }
        if ($updated) {
            $this->save($items);
        }
    }

    public function delete(string $id)
    {
        $items = $this->findAll();
        $items = array_filter($items, function ($item) use ($id) {
            return $item['id'] !== $id;
        });
        $this->save(array_values($items));
    }

    private function save(array $items)
    {
        file_put_contents($this->filePath, json_encode($items, JSON_PRETTY_PRINT));
    }
}
