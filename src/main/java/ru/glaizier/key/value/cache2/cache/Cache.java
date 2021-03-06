package ru.glaizier.key.value.cache2.cache;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import ru.glaizier.key.value.cache2.storage.RestrictedMap;

/**
 * @author GlaIZier
 */
public interface Cache<K, V> extends RestrictedMap<K, V> {

    /**
     * Put the element to the cache and get evicted element if exists
     */
    Optional<Map.Entry<K, V>> put(@Nonnull  K key, @Nonnull V value);

    /**
     * Removes first candidate to remove from cache
     *
     * @return key-value of removed candidate
     */
    Optional<Map.Entry<K, V>> evict();

    int getCapacity();

    default boolean isFull() {
        return getSize() == getCapacity();
    }

}
