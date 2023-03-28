package cz.auderis.infra.tools;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 *
 */
public class ResourceManager implements AutoCloseable {

    private final Deque<DisposerEntry> disposerStack;
    private int lastId;

    public ResourceManager() {
        this.disposerStack = new ArrayDeque<>(2);
    }

    public <T extends AutoCloseable> T getInstance(Callable<T> supplier) {
        return getInstance(supplier, AutoCloseable::close);
    }

    public <T> T getInstance(Callable<T> supplier, ResourceCleanup<? super T> cleanup) {
        if ((null == supplier) || (null == cleanup)) {
            throw new NullPointerException();
        }
        try {
            final T resource = supplier.call();
            if (null != resource) {
                final var disposer = new BasicDisposer<>(resource, cleanup);
                disposerStack.addLast(disposer);
            }
            return resource;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends AutoCloseable> Optional<T> getOptionalInstance(Callable<T> supplier) {
        return getOptionalInstance(supplier, AutoCloseable::close);
    }

    public <T> Optional<T> getOptionalInstance(Callable<T> supplier, ResourceCleanup<T> cleanup) {
        if ((null == supplier) || (null == cleanup)) {
            throw new NullPointerException();
        }
        try {
            final T resource = supplier.call();
            if (null == resource) {
                return Optional.empty();
            }
            final var disposer = new BasicDisposer<>(resource, cleanup);
            disposerStack.addLast(disposer);
            return Optional.of(resource);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public <T extends AutoCloseable> void register(T resource) {
        if (null != resource) {
            final var disposer = new BasicDisposer<>(resource, AutoCloseable::close);
            disposerStack.addLast(disposer);
        }
    }

    public <T> void addCleanup(T value, ResourceCleanup<? super T> cleanup) {
        if ((null == value) || (null == cleanup)) {
            throw new NullPointerException();
        }
        final var disposer = new BasicDisposer<>(value, cleanup);
        disposerStack.addLast(disposer);
    }

    public void addCleanup(Runnable cleanup) {
        if (null == cleanup) {
            throw new NullPointerException();
        }
        final var disposer = new BasicDisposer<>(null, (r) -> cleanup.run());
        disposerStack.addLast(disposer);
    }

    public Object createSavepoint() {
        if (!disposerStack.isEmpty()) {
            final var lastDisposer = disposerStack.getLast();
            if (lastDisposer instanceof Savepoint) {
                return lastDisposer;
            }
        }
        final var id = lastId++;
        final var savepoint = new Savepoint(id);
        disposerStack.addLast(savepoint);
        return savepoint;
    }

    public void rollbackToSavepoint(Object savepoint) {
        if (null == savepoint) {
            throw new NullPointerException();
        } else if (!(savepoint instanceof Savepoint)) {
            throw new IllegalArgumentException("Invalid savepoint");
        } else if (!disposerStack.contains(savepoint)) {
            throw new IllegalArgumentException("Savepoint not registered");
        }
        RuntimeException rollbackError = null;
        while (!disposerStack.isEmpty()) {
            final var lastDisposer = disposerStack.removeLast();
            if (lastDisposer == savepoint) {
                break;
            } else if (lastDisposer instanceof BasicDisposer<?> disposer) {
                try {
                    disposer.run();
                } catch (Exception e) {
                    if (null == rollbackError) {
                        rollbackError = new RuntimeException("Rollback failed");
                    }
                    rollbackError.addSuppressed(e);
                }
            }
        }
        if (null != rollbackError) {
            throw rollbackError;
        }
    }

    @Override
    public void close() {
        RuntimeException closeError = null;
        while (!disposerStack.isEmpty()) {
            final var lastDisposer = disposerStack.removeLast();
            if (lastDisposer instanceof BasicDisposer<?> disposer) {
                try {
                    disposer.run();
                } catch (Exception e) {
                    if (null == closeError) {
                        closeError = new RuntimeException("Failed to close resource manager");
                    }
                    closeError.addSuppressed(e);
                }
            }
        }
        if (null != closeError) {
            throw closeError;
        }
    }

    private sealed interface DisposerEntry permits BasicDisposer, Savepoint {
    }

    private final class BasicDisposer<T> implements DisposerEntry {
        private final T obj;
        private final ResourceCleanup<? super T> cleanup;

        BasicDisposer(T obj, ResourceCleanup<? super T> cleanup) {
            this.obj = obj;
            this.cleanup = cleanup;
        }

        public void run() throws Exception {
            cleanup.close(obj);
        }
    }

    private final class Savepoint implements DisposerEntry {
        private final int id;

        Savepoint(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof Savepoint other) {
                return this.id == other.id;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

}
