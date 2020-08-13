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

import io.greenscreens.jt400.JT400RefType;

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
	 * Reference field type for array data
	 */
	JT400RefType type();
		
	/**
	 * Byte array offset where reference data starts
	 */
	int offset() default -1;
}
