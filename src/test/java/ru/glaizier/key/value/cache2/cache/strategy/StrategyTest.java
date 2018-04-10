package ru.glaizier.key.value.cache2.cache.strategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author mkhokhlushin
 */
public abstract class StrategyTest {

    protected abstract Strategy<Integer> getStrategy();

    @Test
    public void remove(){
        getStrategy().use(1);
        getStrategy().use(2);
        assertTrue(getStrategy().remove(1));
        assertFalse(getStrategy().remove(1));
        assertTrue(getStrategy().remove(2));
        assertFalse(getStrategy().remove(2));
    }
}
