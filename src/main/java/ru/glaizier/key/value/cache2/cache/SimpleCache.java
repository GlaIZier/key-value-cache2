package ru.glaizier.key.value.cache2.cache;

import static java.util.Optional.ofNullable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ru.glaizier.key.value.cache2.cache.strategy.Strategy;
import ru.glaizier.key.value.cache2.storage.Storage;

/**
 * Simple cache that updates strategy's statistics on get and put
 *
 * @author GlaIZier
 */
public class SimpleCache<K, V> implements Cache<K, V> {

    private final Storage<K, V> storage;

    private final Strategy<K> strategy;

    private final int capacity;

    public SimpleCache(Storage<K, V> storage, Strategy<K> strategy, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Wrong capacity!");
        }
        this.storage = storage;
        this.strategy = strategy;
        this.capacity = capacity;
    }

    @Override
    public Optional<V> get(K key) {
        // update statistics only if this key is present in the storage
        return storage.get(key)
            .map(v -> {
                strategy.updateStatistics(key);
                return v;
            });
    }

    @Override
    public Optional<Map.Entry<K, V>> put(K key, V value) {
        Objects.requireNonNull(key);

        Optional<Map.Entry<K, V>> evicted = Optional.empty();
        if (isFull()) {
            evicted = evict();
        }

        strategy.updateStatistics(key);
        storage.put(key, value);
        return evicted;
    }

    @Override
    public Optional<Map.Entry<K, V>> evict() {
        return strategy.evict()
            .map(evictedKey -> {
                V evictedValue = storage.remove(evictedKey).orElseThrow(IllegalStateException::new);
                return new AbstractMap.SimpleImmutableEntry<>(evictedKey, evictedValue);
            });
    }

    @Override
    public int getSize() {
        return storage.getSize();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}