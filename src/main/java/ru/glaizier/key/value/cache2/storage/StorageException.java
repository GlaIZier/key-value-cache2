package ru.glaizier.key.value.cache2.storage;

/**
 * @author GlaIZier
 */
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}