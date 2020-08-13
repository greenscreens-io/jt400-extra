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
 * 
 * Examples defining date
 * 
 *	@JT400Format(offset = 101, type = AS400DataType.TYPE_DATE, format = AS400Date.FORMAT_CYMD)
 *	protected double val;
 * 
 * Examples defining time
 * 
 *	@JT400Format(offset = 101, type = AS400DataType.TYPE_TIME, format = AS400Time.FORMAT_EUR)
 *	protected double val;
 * 
 * Examples defining dec-float
 * 
 *	@JT400Format(offset = 101, type = AS400DataType.TYPE_DECFLOAT, decimals = 5)
 *	protected double val;
 * 
 *	@JT400Format(offset = 101, type = AS400DataType.TYPE_DECFLOAT, decimals = 5)
 *	protected BigDecimal val;
 *
 * Examples defining arrays
 * 
 *	Array of string word 3 chars long, array size at field marked with offset of = 51
 *	@JT400Format(offset = 101, length = 3, of = 51)
 *	protected String[] val;
 * 
 *	Array of 10 integers
 *	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4, length = 10)
 *	protected int[] val;
 *  
 *	Array of integers, array size at field marked with offset 51
 *	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4, of = 51)
 *	protected int[] val;
 *
 *	Array of integers,
 *     array size at field marked with offset 40
 *     array starts at offset in field at 52
 *  @JT400Ref(offset = 52, length = 40)
 *	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4)
 *	protected int[] val;
 *  
 */
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, TYPE_PARAMETER, TYPE })
public @interface JT400Format {

	/**
	 * One of AS400DataType,
	 * If it is TYPE_ARRAY, set "of" to AS400DataType   
	 */
	int type() default -1;
	
	/**
	 * One of AS400DataType when type is TYPE_ARRAY 
	 * @return
	 */
	int of() default -1;
	
	/**
	 * Length for String or array
	 */
	int length() default 0;
	
	/**
	 * Decimal part for packed or zoned decimal
	 */
	int decimals() default 0;

	/**
	 * format for date type
	 */
	int format() default -1;
	
	/**
	 * Byte array offset where data starts
	 */
	int offset() default -1;
}
