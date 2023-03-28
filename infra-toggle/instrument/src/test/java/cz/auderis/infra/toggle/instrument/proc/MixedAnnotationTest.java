package cz.auderis.infra.toggle.instrument.proc;

import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class MixedAnnotationTest extends CommonProcessorTest {

    @Test
    void shouldProcessMixedFeatureAnnotations() {
        // Given
        addSource("TestClass", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureToggle;
                import cz.auderis.infra.toggle.FeatureMethod;
                public class TestClass {
                    @FeatureToggle(name="feature1")
                    private boolean isFeature1Enabled() {
                        return false;
                    }
                    @FeatureMethod(name="feature2", flagValue=false)
                    private String getOldValue(int x) {
                        return "legacy";
                    }
                    @FeatureMethod(name="feature2", flagValue=true)
                    private String getValue(int y) {
                        return "new";
                    }
                }
                """);
        // When
        final var compilation = compileSources();
        // Then
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void shouldRejectMultipleToggleAnnotation() {
        // Given
        addSource("TestClass", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureToggle;
                import cz.auderis.infra.toggle.FeatureMethod;
                public class TestClass {
                    @FeatureToggle(name="feature1")
                    @FeatureMethod(name="feature2", flagValue=false)
                    private boolean isFeature1Enabled() {
                        return false;
                    }
                }
                """);
        // When
        final var compilation = compileSources();
        // Then
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("Incompatible feature-related annotations");
    }

}
