package ru.glaizier.key.value.cache2.cache.strategy;

import java.util.Optional;

import ru.glaizier.key.value.cache2.util.LinkedHashSet;

/**
 * Least recently used. First eviction candidate is a candidate who hasn't been used for the most long time
 * @author GlaIZier
 */
public class LruStrategy<K> implements Strategy<K> {

    /**
     * We need to be able to get by key, replace elements and get last in queue in O(1).
     * This can't be achieved by Java SE. So, I use my own implementation
     */
    LinkedHashSet<K> queue = new LinkedHashSet<>();

    @Override
    public Optional<K> evict() {
        K evictedKey = queue.getTail();
        queue.remove(evictedKey);
        return Optional.ofNullable(evictedKey);
    }

    @Override
    public boolean updateStatistics(K key) {
        boolean contained = queue.remove(key);
        queue.addToHead(key);
        return contained;
    }
}
