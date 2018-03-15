package ru.glaizier.key.value.cache2.cache;

import java.util.Map;
import java.util.Optional;

/**
 * @author GlaIZier
 */
public interface Cache<K, V>  {

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

    int getSize();

    int getCapacity();

    default boolean isFull() {
        return getSize() == getCapacity();
    }

}
