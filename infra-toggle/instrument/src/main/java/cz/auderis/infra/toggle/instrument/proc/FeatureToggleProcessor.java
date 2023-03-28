package cz.auderis.infra.toggle.instrument.proc;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SupportedAnnotationTypes({
        "cz.auderis.infra.toggle.FeatureToggle",
        "cz.auderis.infra.toggle.FeatureMethod",
        "cz.auderis.infra.toggle.FeatureToggleObserver",
        })
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class FeatureToggleProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Set<TypeElement> supportedAnnotationTypes;
    private RegisteredToggleManager toggleManager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        final var typeAnnotation = FeatureToggleProcessor.class.getAnnotation(SupportedAnnotationTypes.class);
        supportedAnnotationTypes = Stream.of(typeAnnotation.value())
                                         .map(elementUtils::getTypeElement)
                                         .collect(Collectors.toSet());
        toggleManager = new RegisteredToggleManager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (Collections.disjoint(annotations, supportedAnnotationTypes)) {
            return false;
        }
        for (final var annotation : supportedAnnotationTypes) {
            final Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (final var elem : annotatedElements) {
                processAnnotatedElement(roundEnv, annotation, elem);
            }

        }
        return true;
    }

    private void processAnnotatedElement(RoundEnvironment roundEnv, TypeElement annotation, Element elem) {
        final List<? extends AnnotationMirror> mirrors = elem.getAnnotationMirrors().stream().filter(this::isAnnotationMirrorSupported).toList();
        if (mirrors.size() != 1) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Incompatible feature-related annotations", elem);
            return;
        }


        final var instrumentationType = InstrumentationType.forType(annotation);








        System.out.println("Processing element " + elem + " annotated with " + annotation);


    }


    private boolean isAnnotationMirrorSupported(AnnotationMirror mirror) {
        final var element = mirror.getAnnotationType().asElement();
        assert element instanceof TypeElement;
        return supportedAnnotationTypes.contains(element);
    }

}
