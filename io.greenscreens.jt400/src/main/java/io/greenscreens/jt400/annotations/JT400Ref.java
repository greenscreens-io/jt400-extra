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
 * NOTE: for base numeric types, length is not required
 * Only for strings or arrays
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, TYPE_PARAMETER, TYPE })
public @interface JT400Ref {

	/**
	 * Reference to data length field
	 */
	int length() default -1;

	/**
	 * Reference to data offset field
	 */
	int offset() default -1;
}
