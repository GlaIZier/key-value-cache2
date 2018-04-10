package ru.glaizier.key.value.cache2.cache;

import java.util.Map;
import java.util.Optional;

/**
 * @author GlaIZier
 */
// Todo add logger
// Todo add general interface for Storage and Cache
public interface Cache<K, V>  {

    /**
     * Can return empty element in case null was added as a value. To deal with such situations use contains method
     */
    Optional<V> get(K key);

    /**
     * Put the element to the cache and get evicted element if exists
     * @param key
     * @param value
     * @return
     */
    Optional<Map.Entry<K, V>> put(K key, V value);

    /**
     * Removes first candidate to remove from cache
     *
     * @return key-value of removed candidate
     */
    Optional<Map.Entry<K, V>> evict();

    /**
     * Removes key-value for such key
     * @return previous value or empty if there was no such value. Or return Optional.empty when no such key.
     * Use contains() to handle this situation
     */
    Optional<V> remove(K key);

    boolean contains(K key);

    int getSize();

    int getCapacity();

    default boolean isFull() {
        return getSize() == getCapacity();
    }

}
