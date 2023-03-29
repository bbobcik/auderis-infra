package cz.auderis.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element facilitates some kind of user-oriented
 * processing, such as producing a web page or consuming user input.
 *
 * @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(type = ExternalInterface.Type.USER)
@Documented
public @interface UserEndpoint {

    /**
     * Describes the character and the purpose of the interaction with a user.
     *
     * @return optional description of the user interaction
     */
    String description() default "";

}
