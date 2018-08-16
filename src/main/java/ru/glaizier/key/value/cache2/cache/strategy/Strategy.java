package ru.glaizier.key.value.cache2.cache.strategy;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Interface for cache strategy
 * @author GlaIZier
 */
public interface Strategy<K> {

    /**
     * Removes statistics for such key and evicts the first suited candidate
     * @return evicted key
     */
    Optional<K> evict();

    /**
     * Tells the strategy that current key element was recently used. Also, can be used to add new key to statistics
     * @return true if statistics has already existed for this element (the element is not new)
     */
    boolean use(@Nonnull K key);

    /**
     * Removes key from statistics
     * @return true if such key was removed
     */
    boolean remove(@Nonnull K key);

}
