package ru.glaizier.key.value.cache2.storage;

import java.util.Optional;

/**
 * Interface for key-value storage
 * @author GlaIZier
 */
public interface Storage<K, V> extends ImmutableMap<K, V> {

    /**
     * @return previous value or empty if there was no such value
     */
    Optional<V> put(K key, V value);

}
