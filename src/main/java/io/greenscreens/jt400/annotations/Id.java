/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to define field index
 */
@Retention(RUNTIME)
@Target({ FIELD, RECORD_COMPONENT })
public @interface Id {

	int value();
}
