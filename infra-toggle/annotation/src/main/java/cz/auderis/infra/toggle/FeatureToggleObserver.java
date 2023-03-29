package cz.auderis.infra.toggle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a method that receives a notification when a feature toggle is
 * changed at runtime. The method must be declared in a class where the
 * feature toggles referenced in {@code value} are defined. The method must
 * have signature {@code static void methodName(String featureName, boolean newValue)}.
 * <p>
 * <b>Warning:</b> It is not an error to annotate non-static methods with this
 * annotation, however they will be ignored.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@FeatureToggleInstrumentation
public @interface FeatureToggleObserver {

    /**
     * One or more feature toggle names that are observed by the annotated method.
     *
     * @return feature toggle names
     */
    String[] value();

}
