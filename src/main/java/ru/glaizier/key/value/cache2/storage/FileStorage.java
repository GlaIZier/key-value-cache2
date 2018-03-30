package ru.glaizier.key.value.cache2.storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.glaizier.key.value.cache2.util.function.Functions.wrap;


public class FileStorage<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    final static String FILENAME_FORMAT = "%s-%s.ser";

    private final static Path TEMP_FOLDER = Paths.get(System.getProperty("java.io.tmpdir")).resolve("key-value-cache2");

    private final static Pattern FILENAME_PATTERN = Pattern.compile("^(\\d+)-(\\S+)\\.(ser)$");

    // Hashcode of key to List<Path> on the disk because there can be collisions
    private final Map<Integer, List<Path>> contents;

    private final Path folder;

    /**
     * Fully identified element of FileStorage
     */
    private static final class Element<K extends Serializable, V extends Serializable> {
        private final K key;
        private final V value;
        private final Path path;
        private final int contentsListIndex;

        Element(K key, V value, Path path, int contentsListIndex) {
            this.key = key;
            this.value = value;
            this.path = path;
            this.contentsListIndex = contentsListIndex;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Path getPath() {
            return path;
        }

        public int getContentsListIndex() {
            return contentsListIndex;
        }
    }

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
            contents = createContents(folder);
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
                Matcher matcher = FILENAME_PATTERN.matcher(fileName);
                if (matcher.find())
                    return Integer.parseInt(matcher.group(1));
                else
                    throw new IllegalStateException("Didn't find group in regexp!");
            }));
    }

    @Override
    public Optional<V> get(K key) {
        Objects.requireNonNull(key, "key");
        return findElement(key).map(Element::getValue);
    }

    @Override
    public Optional<V> put(K key, V value) {
        Objects.requireNonNull(key);
        return Optional.empty();
    }

    @Override
    public Optional<V> remove(K key) {
        Objects.requireNonNull(key, "key");
        return findElement(key)
            .flatMap(this::remove)
            .map(Element::getValue);
    }

    @Override
    public boolean contains(K key) {
        return contents.containsKey(key.hashCode());
    }

    @Override
    public int getSize() {
        return contents.values().stream()
            .mapToInt(List::size)
            .reduce(Integer::sum)
            .orElse(0);
    }

    /**
     * Searches at first for such key the list of paths and then
     * in a list of paths - specific entry using deserialization and keys' equality
     */
    private Optional<? extends Element<K, V>> findElement(K key) {
        Optional<List<Path>> keyPathsOpt = Optional.ofNullable(contents.get(key.hashCode()));
        // Use iteration through indexes as we use ArrayList for contents => list.get(index) will work fast
        return keyPathsOpt.flatMap(keyPaths ->
            IntStream.range(0, keyPaths.size())
                .mapToObj(i -> {
                    Path path = keyPaths.get(i);
                    Map.Entry<K, V> deserialized = deserialize(path);
                    return new Element<>(deserialized.getKey(), deserialized.getValue(), path, i);
                })
                .filter(element -> key.equals(element.key))
                .findFirst()
        );

    }

    @SuppressWarnings("unchecked")
    private Map.Entry<K, V> deserialize(Path path) {
        try (FileInputStream objFileInputStream = new FileInputStream(path.toFile());
             ObjectInputStream objObjectInputStream = new ObjectInputStream(objFileInputStream)) {
            Map.Entry deserialized = (Map.Entry) objObjectInputStream.readObject();
            K key = (K) deserialized.getKey();
            V value = (V) deserialized.getValue();
            return new AbstractMap.SimpleImmutableEntry<>(key, value);
        } catch (Exception e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    /**
     * Removes element from disk and contents and return removed element if exists
     */
    private Optional<? extends Element<K, V>> remove(Element<K, V> element) {
        wrap(Files::deleteIfExists, StorageException.class).apply(element.path);
        return Optional.ofNullable(contents.get(element.key.hashCode()))
            .map(keyPaths -> {
                keyPaths.remove(element.contentsListIndex);
                return element;
            });
    }

}
