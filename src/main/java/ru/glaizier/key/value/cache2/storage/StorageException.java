package ru.glaizier.key.value.cache2.storage;

/**
 * @author GlaIZier
 */
class StorageException extends RuntimeException {
    StorageException(String message) {
        super(message);
    }

    StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
