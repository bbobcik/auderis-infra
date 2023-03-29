package cz.auderis.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element interacts with the infrastructure that
 * surrounds the application, such as a load balancer, a firewall, a
 * proxy server etc.
 *
 * @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(type = ExternalInterface.Type.INFRASTRUCTURE)
@Documented
public @interface InfrastructureInterface {

    /**
     * Describes the specifics of the interaction with the infrastructure.
     *
     * @return optional description of the infrastructure interaction
     */
    String description() default "";

    /**
     * Indicates whether the annotated element interacts exclusively with
     * the local machine or with the infrastructure as a whole by means of
     * network communication.
     * <p>
     * Default is {@code false}
     *
     * @return {@code true} if the annotated element interacts only with
     *         the local machine, {@code false} otherwise
     */
    boolean localOnly() default false;

}
