/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to define struct format or format field
 */
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, TYPE_PARAMETER, TYPE })
public @interface JT400Format {
    
	int type() default -1;
	int length() default 0;
	int decimals() default 0;
	int offset() default -1;
}
