package ru.glaizier.key.value.cache2.cache.strategy;

import java.util.Optional;

/**
 * Interface for cache strategy
 * @author GlaIZier
 */
// Todo think about the possibility to make it FunctionalInterface
public interface Strategy<K> {

    /**
     * Removes statistics for such key and evicts the first suited candidate
     * @return evicted key
     */
    Optional<K> evict();

    /**
     * Tells the strategy that current key element was recently used.
     * @param key
     * @return true if statistics has already existed for this element (the element is not new)
     */
    boolean updateStatistics(K key);

}
