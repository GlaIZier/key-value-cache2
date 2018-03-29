package ru.glaizier.key.value.cache2.storage;

import static java.lang.String.format;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author mkhokhlushin
 */
public class FileStorageTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final Storage<Integer, String> storage = new FileStorage<>();

    @Test
    public void createContents() throws IOException {
        IntStream.rangeClosed(1, 2).forEach(i -> {
            try {
                temporaryFolder.newFile(format(FileStorage.FILENAME_FORMAT, String.valueOf(i), String.valueOf(i)));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
        temporaryFolder.newFile("somefile.ser");
        Map<Integer, List<Path>> contents = FileStorage.createContents(temporaryFolder.getRoot().toPath());

        assertThat(contents.size(), is(2));
        assertThat(contents.get(1).size(), is(1));
        assertThat(contents.get(1).get(0).toString(), not(isEmptyOrNullString()));
        assertThat(contents.get(2).size(), is(1));
        assertThat(contents.get(2).get(0).toString(), not(isEmptyOrNullString()));
    }

    @Test
    public void createContentsWhenFolderEmpty() throws IOException {
        Map<Integer, List<Path>> contents = FileStorage.createContents(temporaryFolder.getRoot().toPath());
        assertTrue(contents.isEmpty());
    }

}
