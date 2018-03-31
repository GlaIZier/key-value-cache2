package ru.glaizier.key.value.cache2.util.function;

/**
 * @author GlaIZier
 */
@FunctionalInterface
public interface WrappedFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}
