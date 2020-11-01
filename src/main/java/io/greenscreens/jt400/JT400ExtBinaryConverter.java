/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.TimeZone;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin1;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Date;
import com.ibm.as400.access.AS400DecFloat;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Time;
import com.ibm.as400.access.AS400Timestamp;
import com.ibm.as400.access.AS400UnsignedBin1;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.AS400UnsignedBin8;
import com.ibm.as400.access.AS400ZonedDecimal;

import io.greenscreens.jt400.annotations.JT400Format;

/**
 * Convert from byte array to specified field type / jt400 type
 */
enum JT400ExtBinaryConverter {
;

	/**
	 * Helper method for getting reference length or offset
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public static  int getIntValue(final ByteBuffer buffer, final int offset) {
		final AS400Bin4 bin4 = new AS400Bin4();
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, offset, 4);
		return bin4.toInt(tmp);
	}
	
	/**
	 * Convert byte data from AS400Bin1
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Bin1(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400Bin1 bin1 = new AS400Bin1();

		final Class<?> clazz = field.getType();
		
		if (clazz == boolean.class) {
			value = bin1.toByte(tmp) == 1;
		} else if (clazz == Boolean.class) {
			value = Boolean.valueOf(bin1.toByte(tmp) == 1);
		} else if (clazz == byte.class) {
			value = bin1.toByte(tmp);
		} else if (clazz == Byte.class) {
			value = bin1.toObject(tmp);
		} else throw new RuntimeException("Boolean, boolean, Byte or byte type supported for AS400Bin1");

		return value;
	}

	/**
	 * Convert byte data from AS400Bin2
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Bin2(final Field field, final byte[] tmp) {

		Object value = null;
		final AS400Bin2 bin2 = new AS400Bin2();
		final Class<?> clazz = field.getType();
		
		if (clazz == short.class) {
			value = bin2.toShort(tmp);
		} else if (clazz == Short.class) {
			value = bin2.toObject(tmp);
		} else throw new RuntimeException("Short or short type supported for AS400Bin2");

		return value;
	}

	/**
	 * Convert byte data from AS400Bin4
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Bin4(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400Bin4 bin4 = new AS400Bin4();
		final Class<?> clazz = field.getType();
		
		if (clazz == int.class) {
			value = bin4.toInt(tmp);
		} else if (clazz == Integer.class) {
			value = bin4.toObject(tmp);
		} else throw new RuntimeException("Integer or int type supported for AS400Bin4");

		return value;
	}

	/**
	 * Convert byte data from AS400Bin8
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Bin8(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400Bin8 bin8 = new AS400Bin8();
		final Class<?> clazz = field.getType();
		
		if (clazz == long.class) {
			value = bin8.toLong(tmp);
		} else if (clazz == Long.class) {
			value = bin8.toObject(tmp);
		} else throw new RuntimeException("Long or long type supported for AS400UnsignedBin1");

		return value;
	}

	/**
	 * Convert byte data from AS400UBin1
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400UBin1(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400UnsignedBin1 ubin1 = new AS400UnsignedBin1();
		final Class<?> clazz = field.getType();
		
		if (clazz == short.class) {
			value = ubin1.toShort(tmp);
		} else if (clazz == Short.class) {
			value = ubin1.toObject(tmp);
		} else throw new RuntimeException("Short or short type supported for AS400UnsignedBin1");

		return value;
	}

	/**
	 * Convert byte data from AS400UBin2
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400UBin2(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400UnsignedBin2 ubin2 = new AS400UnsignedBin2();
		final Class<?> clazz = field.getType();
		
		if (clazz == int.class) {
			value = ubin2.toInt(tmp);
		} else if (clazz == Integer.class) {
			value = ubin2.toObject(tmp);
		} else throw new RuntimeException("Integer or int type supported for AS400UnsignedBin2");

		return value;
	}

	/**
	 * Convert byte data from AS400UBin4
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400UBin4(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400UnsignedBin4 ubin4 = new AS400UnsignedBin4();
		final Class<?> clazz = field.getType();
		
		if (clazz == long.class) {
			value = ubin4.toLong(tmp);
		} else if (clazz == Long.class) {
			value = ubin4.toObject(tmp);
		} else throw new RuntimeException("Long or long type supported for AS400UnsignedBin4");

		return value;
	}

	/**
	 * Convert byte data from AS400UBin8
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400UBin8(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400UnsignedBin8 ubin8 = new AS400UnsignedBin8();
		if (field.getType() == BigInteger.class) {
			value = ubin8.toBigInteger(tmp);
		} else throw new RuntimeException("BigInteger type supported for AS400UnsignedBin8");

		return value;
	}

	/**
	 * Convert byte data from AS400Float4
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Float4(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400Float4 float4 = new AS400Float4();
		final Class<?> clazz = field.getType();
		
		if (clazz == float.class) {
			value = float4.toFloat(tmp);
		} else if (clazz == Float.class) {
			value = float4.toObject(tmp);
		} else throw new RuntimeException("Float or float type supported for AS400Float4");

		return value;
	}

	/**
	 * Convert byte data from AS400Float8
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Float8(final Field field, final byte[] tmp) {

		Object value = null;

		final AS400Float8 float8 = new AS400Float8();
		final Class<?> clazz = field.getType();
		
		if (clazz == double.class) {
			value = float8.toDouble(tmp);
		} else if (clazz == Double.class) {
			value = float8.toObject(tmp);
		} else throw new RuntimeException("Double or double type supported for AS400Float8");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400DecFloat(final Field field, final int decimal, final byte[] tmp) {

		Object value = null;

		final AS400DecFloat decFloat = new AS400DecFloat(decimal);
		final Class<?> clazz = field.getType();
		
		if (clazz == double.class) {
			value = decFloat.toDouble(tmp);
		} else if (clazz == Double.class) {
			value = Double.valueOf(decFloat.toDouble(tmp));
		} else if (clazz == BigDecimal.class) {
			value = decFloat.toObject(tmp);
		} else throw new RuntimeException("BigDecimal, Double or double type supported for AS400DecFloat");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400ZonedDecimal(final Field field, final int length, final int decimals, final byte[] tmp) {

		Object value = null;

		final AS400ZonedDecimal zoned = new AS400ZonedDecimal(length, decimals);
		final Class<?> clazz = field.getType();
		
		if (clazz == double.class) {
			value = zoned.toDouble(tmp);
		} else if (clazz == Double.class) {
			zoned.setUseDouble(true);
			value = zoned.toObject(tmp);
		} else if (clazz == BigDecimal.class) {
			zoned.setUseDouble(false);
			value = zoned.toObject(tmp);
		} else throw new RuntimeException("BigDecimal, Double or double type supported for AS400ZonedDecimal");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400PackedDecimal(final Field field, final int length, final int decimals, final byte[] tmp) {

		Object value = null;

		final AS400PackedDecimal packed = new AS400PackedDecimal(length, decimals);
		final Class<?> clazz = field.getType();
		
		if (clazz == double.class) {
			value = packed.toDouble(tmp);
		} else if (clazz == Double.class) {
			packed.setUseDouble(true);
			value = packed.toObject(tmp);
		} else if (clazz == BigDecimal.class) {
			packed.setUseDouble(false);
			value = packed.toObject(tmp);
		} else throw new RuntimeException("BigDecimal, Double or double type supported for AS400PackedDecimal");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Date(final TimeZone timeZone, final Field field, final int format, final byte[] tmp)  throws Exception {

		Object value = null;

		if (field.getType() == Date.class) {
			//char sep = getDateSeparator(format.format());
			final AS400Date date = new AS400Date(timeZone, format);
			value = date.toObject(tmp);
		} else throw new RuntimeException("Date type supported for AS400Date");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Time(final TimeZone timeZone, final Field field, final int format, final byte[] tmp)  throws Exception {

		Object value = null;

		if (field.getType() == Time.class) {
			final AS400Time time = new AS400Time(timeZone, format);
			value = time.toObject(tmp);
		} else throw new RuntimeException("Time type supported for AS400Time");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Timestamp(final TimeZone timeZone, final Field field, final byte[] tmp) throws Exception {

		Object value = null;

		if (field.getType() == Timestamp.class) {
			final AS400Timestamp timestamp = new AS400Timestamp(timeZone);
			value = timestamp.toObject(tmp);
		} else throw new RuntimeException("Timestamp type supported for AS400Timestamp");

		return value;
	}

	/**
	 *
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	static  Object asAS400Text(final AS400 as400, final Field field, final int length, final byte[] tmp) {
		final AS400Text text = new AS400Text(length, as400);
		return ((String)(text.toObject(tmp))).trim();
	}

	static char getDateSeparator(final int type) {

		switch (type) {
		case AS400Date.FORMAT_CDMY:
		case AS400Date.FORMAT_CMDY:
		case AS400Date.FORMAT_CYMD:
		case AS400Date.FORMAT_DMY:
		case AS400Date.FORMAT_LONGJUL:
		case AS400Date.FORMAT_MDY:
		case AS400Date.FORMAT_MY:
		case AS400Date.FORMAT_MYY:
		case AS400Date.FORMAT_USA:
		case AS400Date.FORMAT_YM:
		case AS400Date.FORMAT_YMD:
		case AS400Date.FORMAT_YYM:
		case AS400Date.FORMAT_JUL:
			return '/';
		case AS400Date.FORMAT_EUR:
			return '.';
		case AS400Date.FORMAT_ISO:
		case AS400Date.FORMAT_JIS:
			return '-';
		default:
			return ' ';
		}

	}

	/**
	 * Get byte length for standard data types
	 *
	 * @param type
	 * @return
	 */
	public static int getDataLength(final int type) {

		switch (type) {
		case AS400DataType.TYPE_BIN1:
		case AS400DataType.TYPE_UBIN1:
			return 1;
		case AS400DataType.TYPE_BIN2:
		case AS400DataType.TYPE_UBIN2:
			return 2;
		case AS400DataType.TYPE_BIN4:
		case AS400DataType.TYPE_UBIN4:
			return 4;
		case AS400DataType.TYPE_BIN8:
		case AS400DataType.TYPE_UBIN8:
			return 8;
		case AS400DataType.TYPE_FLOAT4:
			return 4;
		case AS400DataType.TYPE_FLOAT8:
			return 8;

		case AS400DataType.TYPE_DATE:
			return 0;
		case AS400DataType.TYPE_TIME:
			return 0;
		case AS400DataType.TYPE_TIMESTAMP:
			return 0;

		default:
			return 0;
		}

	}

	
	public static AS400DataType getDataInstance(final AS400 as400, final JT400Format format, final Field field) {

		switch (format.type()) {
		case AS400DataType.TYPE_TEXT:
			return new AS400Text(format.length(), as400);
			
		case AS400DataType.TYPE_BIN1:
			return new AS400Bin1();
		case AS400DataType.TYPE_BIN2:
			return new AS400Bin2();
		case AS400DataType.TYPE_BIN4:
			return new AS400Bin4();
		case AS400DataType.TYPE_BIN8:
			return new AS400Bin8();
			
		case AS400DataType.TYPE_UBIN1:
			return new AS400UnsignedBin1();
		case AS400DataType.TYPE_UBIN2:
			return new AS400UnsignedBin2();
		case AS400DataType.TYPE_UBIN4:
			return new AS400UnsignedBin4();
		case AS400DataType.TYPE_UBIN8:
			return new AS400UnsignedBin8();
			
		case AS400DataType.TYPE_FLOAT4:
			return new AS400Float4();
		case AS400DataType.TYPE_FLOAT8:
			return new AS400Float8();

		case AS400DataType.TYPE_DATE:
			return new AS400Date();			
		case AS400DataType.TYPE_TIME:
			return new AS400Time();
		case AS400DataType.TYPE_TIMESTAMP:
			return new AS400Timestamp();

		default:
			if (String.class.isAssignableFrom(field.getType())) {
				return new AS400Text(format.length(), as400);	
			}
			return null;
		}

	}

}
