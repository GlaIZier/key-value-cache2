package ru.glaizier.key.value.cache2.cache;

import java.io.Serializable;
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
        return null;
    }

    @Override
    public Optional<Map.Entry<K, V>> evict() {
        Optional<Map.Entry<K, V>> evictedOpt = levels.get(0).evict();
//        evictedOpt.map(evicted -> {
//            levels.stream()
//                .skip(1)
//                .forEach(level -> {
//                    level.put(evicted.getKey(), evicted.getValue());
//                });
//        })
//
//
//        for (Cache cache: levels) {
//
//        }
//        levels.forEach(level -> {
//            Optional<Map.Entry<K, V>> evicted = level.evict();
//
//        })
        return null;
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
