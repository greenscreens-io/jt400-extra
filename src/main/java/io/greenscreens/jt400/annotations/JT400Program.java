/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Annotation to define program name and library location
 */
@Retention(RUNTIME)
@Target({ TYPE_PARAMETER, TYPE })
public @interface JT400Program {

	// program name
	String program();

	// program library
	String library();

	// number of arguments
	int arguments();

	// call timeout
	int timeout() default -1;

	// thread safe execution
	boolean threadSafe() default false;

	// declared list of return formats
	Class<? extends IJT400Format> [] formats() default {};

	// set to true when program is service
	boolean service() default false;

	// return format if program is service
	int returnFormat() default -1;

	// procedure name when it is service program
	String procedure() default "";
}
