package cz.auderis.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element interacts with a structured or unstructured
 * data store.
 *
 * @author Boleslav Bobcik
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(type = ExternalInterface.Type.DATABASE)
@Documented
public @interface DatabaseInterface {

    /**
     * Optional description of the interaction if it exceeds the scope of
     * typical database operations. For normal data reads/writes the description
     * is often unnecessary, but operations such as database schema migration
     * or maintenance may require a more detailed description.
     *
     * @return optional description of the database interaction
     */
    String description() default "";

}
