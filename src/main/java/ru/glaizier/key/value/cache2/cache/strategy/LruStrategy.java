package ru.glaizier.key.value.cache2.cache.strategy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


/**
 * Least recently used. First eviction candidate is a candidate who hasn't been used for the most long time
 * @author GlaIZier
 */
public class LruStrategy<K> implements Strategy<K> {

    /**
     * We need to be able to get by key, replace elements and get first in queue in O(1).
     * This can be achieved by Java SE means. This set starts iteration from the last added element
     */
    private final Set<K> queue = new LinkedHashSet<>();

    @Override
    public Optional<K> evict() {
        // Find first element for eviction and remove it if it was found
        return queue.stream()
            .findFirst()
            .map(evictedKey -> {
                queue.remove(evictedKey);
                return evictedKey;
            });
    }

    @Override
    public boolean use(K key) {
        Objects.requireNonNull(key, "key");
        boolean contained = queue.remove(key);
        queue.add(key);
        return contained;
    }

    @Override
    public boolean remove(K key) {
        Objects.requireNonNull(key, "key");
        return queue.remove(key);
    }
}
