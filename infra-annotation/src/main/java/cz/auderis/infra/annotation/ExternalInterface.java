package cz.auderis.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalInterface {

    ExternalInterface.Type value();
    String comment() default "";


    enum Type {
        USER,
        API,
        HEALTH_CHECK,
        INFRASTRUCTURE,
        OS,
        DATABASE,
        OTHER
    }

}
