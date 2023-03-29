package cz.auderis.infra.toggle.instrument.proc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class FeatureMethodTest extends CommonProcessorTest {

    @Test
    void shouldProcessFeatureMethodAnnotation() {
        // Given
        addSource("TestClass", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureMethod;
                public class TestClass {
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


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldRejectSingleToggleMethodAnnotation(boolean flagValue) {
        // Given
        addSource("TestClass", /* language=Java */ """
                package cz.auderis.test;
                import cz.auderis.infra.toggle.FeatureMethod;
                public class TestClass {
                    @FeatureMethod(name="feature2", flagValue=%s)
                    private boolean isFeature1Enabled() {
                        return false;
                    }
                }
                """.formatted(flagValue));
        // When
        final var compilation = compileSources();
        // Then
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("Incompatible feature-relate annotations");
    }


}
