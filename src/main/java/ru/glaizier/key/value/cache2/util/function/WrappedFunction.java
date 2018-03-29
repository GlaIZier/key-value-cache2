package ru.glaizier.key.value.cache2.util.function;

/**
 * @author mkhokhlushin
 */
@FunctionalInterface
public interface WrappedFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}
