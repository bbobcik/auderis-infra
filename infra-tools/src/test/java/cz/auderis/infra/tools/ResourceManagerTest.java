package cz.auderis.infra.tools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ResourceManagerTest {

    @Test
    void shouldCloseResourcesInCorrectOrder() {
        // Given
        final var manager = new ResourceManager();
        final var counter = new AtomicInteger();
        // When
        try (final var rm = manager) {
            rm.getInstance(() -> new CountingResource(counter), CountingResource::close);
            rm.getInstance(() -> new CountingResource(counter), CountingResource::close);
            rm.getInstance(() -> new CountingResource(counter), CountingResource::close);
        }
        // Then
        // no exception means disposal order is correct
        assertThat("Resource manager not empty", counter.get(), is(0));
    }

    @Test
    void shouldCloseResourcesDespiteExceptions() {
        // Given
        final var manager = new ResourceManager();
        final var counter = new AtomicInteger();
        Exception savedException = null;
        // When
        try (final var rm = manager) {
            rm.getInstance(() -> new CountingResource(counter), CountingResource::close);
            rm.getInstance(() -> new CountingResource(counter, true), CountingResource::close);
            rm.getInstance(() -> new CountingResource(counter), CountingResource::close);
        } catch (Exception e) {
            savedException = e;
        }
        // no exception means disposal order is correct
        assertThat("Resource manager not empty", counter.get(), is(0));
        assertThat("Exception not propagated", savedException, is(notNullValue()));
        final var suppressedExceptions = savedException.getSuppressed();
        assertThat("Wrong number of suppressed exceptions", suppressedExceptions.length, is(1));
        assertThat("Wrong suppressed exception", suppressedExceptions[0].getMessage(), is("Resource 2 closed with exception"));
    }



    static class CountingResource implements AutoCloseable {
        final int referenceValue;
        final AtomicInteger counter;
        final boolean throwOnClose;

        public CountingResource(AtomicInteger counter) {
            this(counter, false);
        }

        public CountingResource(AtomicInteger counter, boolean throwOnClose) {
            this.counter = counter;
            this.referenceValue = counter.incrementAndGet();
            this.throwOnClose = throwOnClose;
        }

        @Override
        public void close() throws Exception {
            final var currentValue = counter.getAndDecrement();
            assertThat("Resource closed in wrong order", currentValue, is(referenceValue));
            if (throwOnClose) {
                throw new Exception("Resource " + referenceValue + " closed with exception");
            }
        }
    }

}
