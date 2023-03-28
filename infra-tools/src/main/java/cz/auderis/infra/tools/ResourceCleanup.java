package cz.auderis.infra.tools;

@FunctionalInterface
public interface ResourceCleanup<T> {

    void close(T resource) throws Exception;

}
