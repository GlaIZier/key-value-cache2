package ru.glaizier.key.value.cache2.cache;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Multi level cache implementation which evicts elements from first (top) levels to below ones
 * @author mkhokhlushin
 */
public class MultiLevelCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

    private final List<Cache<K, V>> levels;

    public MultiLevelCache(Cache<K, V>... levels) {
        this(Arrays.asList(levels));
    }

    public MultiLevelCache(List<Cache<K, V>> levels) {
        Objects.requireNonNull(levels, "levels");
        if (levels.isEmpty()) {
            throw new IllegalArgumentException("Levels must not be empty!");
        }
        this.levels = Collections.unmodifiableList(levels);
    }

    /**
     * Searches key in all levels
     */
    // Todo move found to the first level
    @Override
    public Optional<V> get(K key) {
        return levels.stream()
            .map(cache -> cache.get(key))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }

    @Override
    public Optional<Map.Entry<K, V>> put(K key, V value) {
        // Todo remove value first
//        levels.stream()
//            .;
        return levels.get(0).put(key, value).flatMap(firstEvicted -> put(firstEvicted.getKey(), firstEvicted.getValue(), 1));
    }

    /**
     * Evicts consequently from levels by putting evicted elements to other levels
     * l0 -> ev0 + l1 -> ev1 + l2 -> el2 ...
     */
    @Override
    public Optional<Map.Entry<K, V>> evict() {
        return levels.get(0).evict().flatMap(firstEvicted -> put(firstEvicted.getKey(), firstEvicted.getValue(), 1));
    }

    /**
     * Puts the element to the start level and gets the evicted from the last level
     */
    private Optional<Map.Entry<K, V>> put(K key, V value, int startLevelIndex) {
        Optional<Map.Entry<K, V>> curEvicted = Optional.of(new AbstractMap.SimpleImmutableEntry<>(key, value));
        for (int levelIndex = startLevelIndex; levelIndex < levels.size() && curEvicted.isPresent(); levelIndex++) {
            curEvicted = levels.get(levelIndex).put(curEvicted.get().getKey(), curEvicted.get().getValue());
        }
        return curEvicted;
    }

    @Override
    // Todo implement this
    public Optional<V> remove(K key) {
        return null;
    }

    @Override
    public boolean contains(K key) {
        return false;
    }

    @Override
    public int getSize() {
        return levels.stream()
            .mapToInt(Cache::getSize)
            .reduce(0, Integer::sum);
    }

    @Override
    public int getCapacity() {
        return levels.stream()
            .mapToInt(Cache::getCapacity)
            .reduce(0, Integer::sum);
    }
}
