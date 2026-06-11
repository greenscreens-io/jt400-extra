/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ibm.as400.access.ProgramParameter;

/**
 * Annotation to define program parameter
 */
@Retention(RUNTIME)
@Target({ FIELD, RECORD_COMPONENT })
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
	 * default is by value (ProgramCall ignore this, only for ServiceProgramCall)
	 * @return
	 */
	int pass() default ProgramParameter.PASS_BY_VALUE;
}
