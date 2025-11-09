/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ibm.as400.access.ProgramParameter;

/**
 * Annotation to define program parameter
 */
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, TYPE_PARAMETER })
public @interface JT400Argument {

	/**
	 * parameter type
	 * @return
	 */
	int type() default -1;

	/**
	 * Data type of array
	 * @return
	 */
	int of() default -1;
	
	/**
	 * length for string, array, decimals
	 * @return
	 */
	int length() default 0;

	/**
	 * decimal part for packed and zoned decimal
	 * @return
	 */
	int decimals() default 0;

	/**
	 * time / date formats
	 * @return
	 */
	int format() default -1;

	/**
	 * type of argument pass rule, by value or reference
	 * default is by value
	 * @return
	 */
	int pass() default ProgramParameter.PASS_BY_VALUE;
}
