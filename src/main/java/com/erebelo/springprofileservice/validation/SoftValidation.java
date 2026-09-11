package com.erebelo.springprofileservice.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property whose Jakarta validation violations should be persisted as
 * soft validation failures instead of preventing persistence.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SoftValidation {
}
