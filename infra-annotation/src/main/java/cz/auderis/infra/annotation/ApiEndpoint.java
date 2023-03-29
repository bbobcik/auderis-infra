package cz.auderis.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element is a part of the application's API oriented
 * towards machine-machine communication, where the primary goal is
 * an exchange of data. It may be e.g. a REST API endpoint,
 * an inbound or outbound messaging connector etc.
 *
 *  @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(type = ExternalInterface.Type.API)
@Documented
public @interface ApiEndpoint {

    /**
     * Optional human-readable description of the character of the interaction
     *
     * @return intent of the API
     */
    String description() default "";

    /**
     * Describes which protocol facilitates the application interface or which
     * application is the primary side of the communication. This
     * should be a short text, such as "REST", "JMS", "Kafka" etc.
     *
     * @return protocol description
     */
    String protocol() default "";

}
