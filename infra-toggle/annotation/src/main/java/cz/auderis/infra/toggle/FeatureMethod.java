package cz.auderis.infra.toggle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates methods that, based on {@code flagValue}, implement either
 * legacy function or a new feature. For each feature name, there must be
 * exactly two methods with identical signature, the one with legacy code
 * annotated with {@code flagValue=false} and its counterpart that implements
 * the new version annotated with {@code flagValue=true}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@FeatureToggleInstrumentation
public @interface FeatureMethod {

    /**
     * Name that identifies the feature toggle. A name must appear exactly
     * twice in the codebase, once with {@code flagValue=false} and once
     * with {@code flagValue=true}.
     *
     * @return feature identifier
     */
    String name();

    /**
     * Determines whether the annotated method implements the legacy code
     * ({@code false}) or the new feature ({@code true}).
     *
     * @return {@code true} for new feature, {@code false} for legacy code
     */
    boolean flagValue();

}
