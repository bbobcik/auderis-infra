package cz.auderis.infra.toggle.processor;

import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class FeatureToggleTest extends CommonProcessorTest {

    @Test
    void shouldProcessFeatureToggleAnnotation() {
        // Given
        addSource("TestClass", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureToggle;
                public class TestClass {
                    @FeatureToggle(name="feature1")
                    private boolean isFeature1Enabled() {
                        return false;
                    }
                }
                """);
        // When
        final var compilation = compileSources();
        // Then
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void shouldRejectMultipleOccurrences() {
        // Given
        addSource("TestClass", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureToggle;
                public class TestClass {
                    @FeatureToggle(name="feature1")
                    private boolean isFeature1Enabled() {
                        return false;
                    }
                    @FeatureToggle(name="feature1")
                    boolean isFeature1EnabledAgain() {
                        return false;
                    }
                }
                """);
        // When
        final var compilation = compileSources();
        // Then
        assertThat(compilation).failed();
    }

    @Test
    void shouldProcessMultipleOccurrencesInDifferentClasses() {
        // Given
        addSource("TestClass1", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureToggle;
                public class TestClass1 {
                    @FeatureToggle(name="feature1")
                    private boolean isFeature1Enabled() {
                        return false;
                    }
                }
                """);
        addSource("TestClass2", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureToggle;
                public class TestClass2 {
                    @FeatureToggle(name="feature1")
                    boolean isFeature1EnabledAgain() {
                        return false;
                    }
                }
                """);
        // When
        final var compilation = compileSources();
        // Then
        assertThat(compilation).succeededWithoutWarnings();
    }

}
