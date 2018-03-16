package ru.glaizier.key.value.cache2.cache.strategy;

import static java.util.Optional.ofNullable;
import java.util.Objects;
import java.util.Optional;

import ru.glaizier.key.value.cache2.util.LinkedHashSet;

/**
 * Most recently used. First eviction candidate is a candidate who has been used recently
 *
 * @author GlaIZier
 */
public class MruStrategy<K> implements Strategy<K> {

    /**
     * We need to be able to get by key, replace elements and get first in queue in O(1).
     * This can't be achieved by Java SE. So, I use my own implementation
     */
    private final LinkedHashSet<K> queue = new LinkedHashSet<>();

    @Override
    public Optional<K> evict() {
        // remove from queue if found key
        return Optional.ofNullable(queue.getHead())
            .map(evicted -> {
                queue.remove(evicted);
                return evicted;
            });
    }

    @Override
    public boolean updateStatistics(K key) {
        Objects.requireNonNull(key, "key");
        boolean contained = queue.remove(key);
        queue.addToHead(key);
        return contained;
    }
}