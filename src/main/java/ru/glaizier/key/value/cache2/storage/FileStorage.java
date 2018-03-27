package ru.glaizier.key.value.cache2.storage;

import static java.util.Optional.ofNullable;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileStorage<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    private final static Path TEMP_FOLDER = Paths.get(System.getProperty("java.io.tmpdir")).resolve("key-value-cache2");

    private final static String FILENAME_FORMAT = "%s-%s.ser";

    private final static Pattern FILENAME_PATTERN = Pattern.compile("(\\d+)-(\\S+)\\.(ser)");

    // Hashcode of key as String representation to List<Path> on the disk because there could be collisions
    private final Map<String, List<Path>> contents = new HashMap<>();

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
            createContents(folder);
        } catch (Exception e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    public static Map<Integer, List<Path>> createContents(Path folder) throws IOException {
        return Files.walk(folder)
            .filter(Files::isRegularFile)
            .filter(path -> Objects.nonNull(path.getFileName()))
            .filter(path -> {
                String fileName = path.getFileName().toString();
                return FILENAME_PATTERN.matcher(fileName).find();
            })
            .collect(Collectors.groupingBy(path -> {
                String fileName = path.getFileName().toString();
                return Integer.parseInt(FILENAME_PATTERN.matcher(fileName).group(1));
            }));
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.empty();
    }

    @Override
    public Optional<V> put(K key, V value) {
        Objects.requireNonNull(key);
        return Optional.empty();
    }

    @Override
    public Optional<V> remove(K key) {
        return Optional.empty();
    }

    @Override
    public boolean contains(K key) {
        return contents.containsKey(key);
    }

    @Override
    public int getSize() {
        return contents.size();
    }

}
