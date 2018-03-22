package ru.glaizier.key.value.cache2.storage;

import static java.util.Optional.ofNullable;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class FileStorage<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    private final static Path TEMP_FOLDER = Paths.get(System.getProperty("java.io.tmpdir")).resolve("key-value-cache2");

    private final Map<K, V> content = new HashMap<>();

    private final Path folder;

    public FileStorage() {
        this(TEMP_FOLDER);
    }

    public FileStorage(Path folder) {
        Objects.requireNonNull(folder, "folder");
        this.folder = folder;
        try {
            if (Files.notExists(folder)) {
                Files.createDirectories(folder);
            }
            //
        } catch (Exception e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<V> get(K key) {
        return ofNullable(content.get(key));
    }

    @Override
    public Optional<V> put(K key, V value) {
        Objects.requireNonNull(key);
        return ofNullable(content.put(key, value));
    }

    @Override
    public Optional<V> remove(K key) {
        return ofNullable(content.remove(key));
    }

    @Override
    public boolean contains(K key) {
        return content.containsKey(key);
    }

    @Override
    public int getSize() {
        return content.size();
    }

}
