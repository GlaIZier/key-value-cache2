package ru.glaizier.key.value.cache2.storage;

import static java.util.Optional.ofNullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class MemoryStorage<K, V> implements Storage<K, V> {

    private final Map<K, V> map;

    public MemoryStorage(Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> MemoryStorage<K, V> ofHashMap() {
        return new MemoryStorage<>(new HashMap<>());
    }

    @Override
    public Optional<V> get(K key) {
        return ofNullable(map.get(key));
    }

    @Override
    public Optional<V> put(K key, V value) {
        Objects.requireNonNull(key);
        return ofNullable(map.put(key, value));
    }

    @Override
    public Optional<V> remove(K key) {
        return ofNullable(map.remove(key));
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public int getSize() {
        return map.size();
    }

}
