package ru.glaizier.key.value.cache2.storage;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Interface for key-value storage
 * @author GlaIZier
 */
public interface Storage<K, V> extends RestrictedMap<K, V> {

    /**
     * @return previous value or empty if there was no such key before
     */
    Optional<V> put(@Nonnull K key, @Nonnull V value);

}
