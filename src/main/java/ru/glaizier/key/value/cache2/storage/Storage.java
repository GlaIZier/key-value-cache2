package ru.glaizier.key.value.cache2.storage;

import java.util.Optional;

/**
 * Interface for key-value storage
 * @author GlaIZier
 */
public interface Storage<K, V> {

    Optional<V> get(K key);

    /**
     * @return previous value or empty if there was no such value
     */
    Optional<V> put(K key, V value);

    /**
     * @return previous value or empty if there was no such value. Or return null when no such key.
     * Use contains() to handle this situation
     */
    Optional<V> remove(K key);

    boolean contains(K key);

    /**
     * @return current number of elements
     */
    int getSize();

}
