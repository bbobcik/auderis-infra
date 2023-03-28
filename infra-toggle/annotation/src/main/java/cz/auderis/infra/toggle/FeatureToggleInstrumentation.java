package cz.auderis.infra.toggle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for feature toggle annotations. Notice that the annotation
 * has reduced retention and is not available at runtime, as it is intended
 * to be used only by annotation processors and similar tools, typically
 * at build time.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface FeatureToggleInstrumentation {
}
