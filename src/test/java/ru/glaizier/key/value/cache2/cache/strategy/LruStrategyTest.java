package ru.glaizier.key.value.cache2.cache.strategy;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author GlaIZier
 */
public class LruStrategyTest {

    private final Strategy<Integer> strategy = new LruStrategy<>();

    @Test
    public void getEmptyOnEmptyQueue() {
        assertThat(strategy.evict(), is(Optional.empty()));
    }

    @Test
    public void getOneAfterOneInsert() {
        assertFalse(strategy.updateStatistics(1));
        assertThat(strategy.evict(), is(Optional.of(1)));
    }

    @Test
    public void getOneAfterOneTwoInserts() {
        assertFalse(strategy.updateStatistics(1));
        assertFalse(strategy.updateStatistics(2));
        assertThat(strategy.evict(), is(Optional.of(1)));
    }

    @Test
    public void getTwoAfterOneTwoInsertsAndOneUpdate() {
        assertFalse(strategy.updateStatistics(1));
        assertFalse(strategy.updateStatistics(2));
        assertTrue(strategy.updateStatistics(1));
        assertThat(strategy.evict(), is(Optional.of(2)));
    }

}
