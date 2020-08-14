/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
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

	String program();
	String library();
	int arguments();
	Class<? extends IJT400Format> [] formats() default {};

}
