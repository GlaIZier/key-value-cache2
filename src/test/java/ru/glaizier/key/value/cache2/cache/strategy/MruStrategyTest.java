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
public class MruStrategyTest extends StrategyTest {

    private final Strategy<Integer> strategy = new MruStrategy<>();

    @Override
    protected Strategy<Integer> getStrategy() {
        return strategy;
    }

    @Test
    public void getEmptyOnEmptyQueue() {
        assertThat(strategy.evict(), is(Optional.empty()));
    }

    @Test
    public void getOneAfterOneInsert() {
        assertFalse(strategy.use(1));
        assertThat(strategy.evict(), is(Optional.of(1)));
    }

    @Test
    public void getTwoAfterOneTwoInserts() {
        assertFalse(strategy.use(1));
        assertFalse(strategy.use(2));
        assertThat(strategy.evict(), is(Optional.of(2)));
    }

    @Test
    public void getOneAfterOneTwoInsertsAndOneUpdate() {
        assertFalse(strategy.use(1));
        assertFalse(strategy.use(2));
        assertTrue(strategy.use(1));
        assertThat(strategy.evict(), is(Optional.of(1)));
    }

}
