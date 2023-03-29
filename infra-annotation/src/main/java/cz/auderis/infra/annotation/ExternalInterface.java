package cz.auderis.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Low-level annotation that marks that the annotated element interacts with
 * external environment. The annotation is used to mark API endpoints,
 * database-related code, methods facilitating health checks, etc.
 * <p>
 * The annotation is in addition used to meta-annotate other annotations, which
 * represent the type of environment interaction in a human-readable form, e.g.
 * {@link ApiEndpoint} or {@link OperatingSystemInterface}.
 *
 * @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalInterface {

    /**
     * Describes the character of the interaction with external environment
     * that can be expected from the annotated element. This should be a single
     * item from the {@link Type} enumeration, but it is possible to specify
     * multiple types for more complex scenarios. (However it is not recommended
     * as it may indicate a flaw in the architecture.)
     *
     * @return one or more types of external environment that the annotated
     * element interacts with
     */
    ExternalInterface.Type[] type();

    /**
     * Optional human-readable description of the character of the interaction
     * with the external environment.
     *
     * @return optional description of the external environment interaction
     */
    String description() default "";

    /**
     * Represents generic types of external environment interaction.
     */
    enum Type {

        /**
         * The annotated element facilitates some kind of user-oriented
         * processing, such as producing a web page.
         */
        USER,

        /**
         * The annotated element is a part of the application's API oriented
         * towards machine-machine communication, where the primary goal is
         * an exchange of data. It may be e.g. a REST API endpoint,
         * an inbound or outbound messaging connector etc.
         */
        API,

        /**
         * The annotated element is responsible for specialized machine-oriented
         * data exchange that doesn't involve main application logic and data,
         * but rather controls and/or queries application's internal state.
         */
        MONITORING,

        /**
         * The annotated element interacts with a structured or unstructured
         * data store.
         */
        DATABASE,

        /**
         * The annotated element interacts with the infrastructure that
         * surrounds the application, such as a load balancer, a firewall, a
         * proxy server etc.
         */
        INFRASTRUCTURE,

        /**
         * The annotated element interacts with the operating system of the local
         * machine, which may e.g. include control of OS services, management
         * of security attributes etc.
         * <p>
         * When the interaction is with a remote machine, prefer {@link #INFRASTRUCTURE}
         * type instead.
         */
        OS,

        /**
         * Denotes external interactions not described by any of the other
         * types.
         */
        OTHER
    }

}
