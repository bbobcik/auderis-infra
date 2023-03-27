package cz.auderis.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExternalInterface(ExternalInterface.Type.HEALTH_CHECK)
public @interface HealthCheckEndpoint {
}
