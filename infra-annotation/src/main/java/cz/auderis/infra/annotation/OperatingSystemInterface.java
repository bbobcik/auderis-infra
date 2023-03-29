package cz.auderis.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element interacts with the operating system of the local
 * machine, which may e.g. include control of OS services, management
 * of security attributes etc.
 * <p>
 * When the interaction is with a remote machine, prefer {@link InfrastructureInterface}
 * type instead.
 *
 * @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(type = ExternalInterface.Type.OS)
public @interface OperatingSystemInterface {

    /**
     * Describes details of the interaction, such as what is the interfaced
     * aspect of the OS (e.g. process management, file system, network, etc.)
     * or why is this interaction needed.
     *
     * @return optional description of the OS interaction
     */
    String description() default "";

}
