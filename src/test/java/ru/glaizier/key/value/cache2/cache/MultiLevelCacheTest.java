package ru.glaizier.key.value.cache2.cache;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.glaizier.key.value.cache2.cache.strategy.LruStrategy;
import ru.glaizier.key.value.cache2.storage.FileStorage;
import ru.glaizier.key.value.cache2.storage.MemoryStorage;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MultiLevelCacheTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Cache<Integer, String> c;

    @Before
    public void init() {
        c = new MultiLevelCache<>(
                new SimpleCache<>(MemoryStorage.ofHashMap(), new LruStrategy<>(), 2),
                new SimpleCache<>(new FileStorage<>(temporaryFolder.getRoot().toPath()), new LruStrategy<>(), 2)
        );
    }

    @Test
    public void capacity() {
        assertThat(c.getCapacity(), is(4));
    }

}
