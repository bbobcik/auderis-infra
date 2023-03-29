package cz.auderis.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element is responsible for specialized machine-oriented
 * data exchange that doesn't involve main application logic and data,
 * but rather queries and/or controls application's internal state.
 *
 * @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(type = ExternalInterface.Type.MONITORING)
public @interface MonitoringEndpoint {

    /**
     * Describes detail of monitoring aspects that are provided by the annotated
     * element.
     *
     * @return optional description
     */
    String description() default "";

    /**
     * Indicates whether the annotated element allows only introspection of
     * the application state (default) or whether it allows to control the
     * application operation as well.
     *
     * @return {@code true} if the annotated element allows only introspection,
     *         {@code false} if it allows to control the application operation
     */
    boolean readOnly() default true;

}
