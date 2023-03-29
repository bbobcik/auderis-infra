package cz.auderis.infra.toggle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods that return the value of a feature toggle.
 * The annotated method must have no arguments and must return a
 * {@code boolean} value.
 * <p>
 * The return value of the annotated method should represent the desired
 * default state of the feature toggle.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@FeatureToggleInstrumentation
public @interface FeatureToggle {

    /**
     * Name that identifies the feature toggle. The name must be unique.
     *
     * @return unique identifier
     */
    String name();

    /**
     * Description of the feature toggle, that should explain its purpose.
     * This value may be used for automatic generation of feature documentation.
     *
     * @return optional description
     */
    String description() default "";

}
