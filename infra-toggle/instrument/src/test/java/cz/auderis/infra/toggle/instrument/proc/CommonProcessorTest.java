package cz.auderis.infra.toggle.instrument.proc;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import cz.auderis.infra.toggle.instrument.proc.FeatureToggleProcessor;
import org.junit.jupiter.api.BeforeEach;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

abstract class CommonProcessorTest {

    FeatureToggleProcessor annotationProcessor;
    Compiler compiler;
    List<JavaFileObject> sourceList = new ArrayList<>(4);
    protected String commonPrefix = "cz.auderis.test.";

    @BeforeEach
    void prepareCompiler() {
        annotationProcessor = new FeatureToggleProcessor();
        compiler = Compiler.javac().withProcessors(annotationProcessor);
    }

    protected void addSource(String name, String source) {
        if (null != commonPrefix) {
            name = commonPrefix + name;
        }
        sourceList.add(JavaFileObjects.forSourceString(name, source));
    }

    protected Compilation compileSources() {
        return compiler.compile(sourceList);
    }

}
