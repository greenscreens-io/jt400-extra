/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
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

	static final AS400Bin1 BIN1_CONVERTER = new AS400Bin1();
	static final AS400Bin2 BIN2_CONVERTER = new AS400Bin2();
	static final AS400Bin4 BIN4_CONVERTER = new AS400Bin4();
	static final AS400Bin8 BIN8_CONVERTER = new AS400Bin8();

	static final AS400UnsignedBin1 UBIN1_CONVERTER = new AS400UnsignedBin1();
	static final AS400UnsignedBin2 UBIN2_CONVERTER = new AS400UnsignedBin2();
	static final AS400UnsignedBin4 UBIN4_CONVERTER = new AS400UnsignedBin4();
	static final AS400UnsignedBin8 UBIN8_CONVERTER = new AS400UnsignedBin8();

	static final AS400Float4 FLOAT4_CONVERTER = new AS400Float4();
	static final AS400Float8 FLOAT8_CONVERTER = new AS400Float8();

	static final ThreadLocal<Map<Integer, AS400DecFloat>> THREAD_DEC_POOL = ThreadLocal.withInitial(HashMap::new);
	static final ThreadLocal<Map<Long, AS400ZonedDecimal>> THREAD_ZONED_POOL = ThreadLocal.withInitial(HashMap::new);
	static final ThreadLocal<Map<Long, AS400PackedDecimal>> THREAD_PACKED_POOL = ThreadLocal.withInitial(HashMap::new);

	static final ThreadLocal<Map<Long, AS400Date>> THREAD_DATE_POOL = ThreadLocal.withInitial(HashMap::new);
	static final ThreadLocal<Map<Long, AS400Time>> THREAD_TIME_POOL = ThreadLocal.withInitial(HashMap::new);
	static final ThreadLocal<Map<Integer, AS400Timestamp>> THREAD_TIMESTAMP_POOL = ThreadLocal.withInitial(HashMap::new);

	static final ThreadLocal<Map<Long, AS400Text>> THREAD_TEXT_POOL = ThreadLocal.withInitial(HashMap::new);

	/**
	 * Helper method for getting reference length or offset
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public static  int getIntValue(final ByteBuffer buffer, final int offset) {
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, offset, 4);
		return BIN4_CONVERTER.toInt(tmp);
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

		final Class<?> clazz = field.getType();

		if (clazz == boolean.class) {
			return BIN1_CONVERTER.toByte(tmp) == 1;
		} else if (clazz == Boolean.class) {
			return Boolean.valueOf(BIN1_CONVERTER.toByte(tmp) == 1);
		} else if (clazz == byte.class) {
			return  BIN1_CONVERTER.toByte(tmp);
		} else if (clazz == Byte.class) {
			return  BIN1_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Boolean, boolean, Byte or byte type supported for AS400Bin1");
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

		final Class<?> clazz = field.getType();

		if (clazz == short.class) {
			return BIN2_CONVERTER.toShort(tmp);
		} else if (clazz == Short.class) {
			return BIN2_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Short or short type supported for AS400Bin2");
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

		final Class<?> clazz = field.getType();

		if (clazz == int.class) {
			return BIN4_CONVERTER.toInt(tmp);
		} else if (clazz == Integer.class) {
			return BIN4_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Integer or int type supported for AS400Bin4");
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

		final Class<?> clazz = field.getType();

		if (clazz == long.class) {
			return BIN8_CONVERTER.toLong(tmp);
		} else if (clazz == Long.class) {
			return BIN8_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Long or long type supported for AS400UnsignedBin1");
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

		final Class<?> clazz = field.getType();

		if (clazz == short.class) {
			return UBIN1_CONVERTER.toShort(tmp);
		} else if (clazz == Short.class) {
			return  UBIN1_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Short or short type supported for AS400UnsignedBin1");
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

		final Class<?> clazz = field.getType();

		if (clazz == int.class) {
			return UBIN2_CONVERTER.toInt(tmp);
		} else if (clazz == Integer.class) {
			return UBIN2_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Integer or int type supported for AS400UnsignedBin2");
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

		final Class<?> clazz = field.getType();

		if (clazz == long.class) {
			return UBIN4_CONVERTER.toLong(tmp);
		} else if (clazz == Long.class) {
			return UBIN4_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Long or long type supported for AS400UnsignedBin4");
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

		if (field.getType() == BigInteger.class) {
			return UBIN8_CONVERTER.toBigInteger(tmp);
		}

		throw new RuntimeException("BigInteger type supported for AS400UnsignedBin8");
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

		final Class<?> clazz = field.getType();

		if (clazz == float.class) {
			return FLOAT4_CONVERTER.toFloat(tmp);
		} else if (clazz == Float.class) {
			return FLOAT4_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Float or float type supported for AS400Float4");
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

		final Class<?> clazz = field.getType();

		if (clazz == double.class) {
			return  FLOAT8_CONVERTER.toDouble(tmp);
		} else if (clazz == Double.class) {
			return FLOAT8_CONVERTER.toObject(tmp);
		}

		throw new RuntimeException("Double or double type supported for AS400Float8");
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
	static Object asAS400DecFloat(final Field field, final int decimal, final byte[] tmp) {

		final Class<?> clazz = field.getType();

		if (clazz != double.class && clazz != Double.class && clazz != BigDecimal.class) {
			throw new IllegalArgumentException("Only BigDecimal, Double, or double types are supported for AS400ZonedDecimal");
		}

		final AS400DecFloat decFloat = THREAD_DEC_POOL.get().computeIfAbsent(decimal, k -> new AS400DecFloat(decimal));

		if (clazz == double.class || clazz == Double.class) {
			return decFloat.toDouble(tmp);
		}

		return decFloat.toObject(tmp);
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
	static Object asAS400ZonedDecimal(final Field field, final int length, final int decimals, final byte[] tmp) {

		final Class<?> clazz = field.getType();

		if (clazz != double.class && clazz != Double.class && clazz != BigDecimal.class) {
			throw new IllegalArgumentException("Only BigDecimal, Double, or double types are supported for AS400ZonedDecimal");
		}

		final long combinedKey = ((long) length << 32) | (decimals & 0xFFFFFFFFL);
		final AS400ZonedDecimal zoned = THREAD_ZONED_POOL.get().computeIfAbsent(combinedKey, k -> new AS400ZonedDecimal(length, decimals));

		// Set the internal flag based on the class type
		zoned.setUseDouble(clazz == Double.class);

		if (clazz == double.class) {
			return zoned.toDouble(tmp);
		}

		return zoned.toObject(tmp);

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

		final Class<?> clazz = field.getType();

		if (clazz != double.class && clazz != Double.class && clazz != BigDecimal.class) {
			throw new IllegalArgumentException("Only BigDecimal, Double, or double types are supported for AS400ZonedDecimal");
		}

		final long combinedKey = ((long) length << 32) | (decimals & 0xFFFFFFFFL);
		final AS400PackedDecimal packed = THREAD_PACKED_POOL.get().computeIfAbsent(combinedKey, k -> new AS400PackedDecimal(length, decimals));

		// Set the internal flag based on the class type
		packed .setUseDouble(clazz == Double.class);

		if (clazz == double.class) {
			return packed.toDouble(tmp);
		}

		return packed .toObject(tmp);

	}

	static private long toKey(final TimeZone timeZone, final int format) {
		final int tzHash = (timeZone == null) ? 0 : timeZone.hashCode();
		return ((long) tzHash << 32) | (format & 0xFFFFFFFFL);
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

		if (field.getType() != Date.class) {
			throw new IllegalArgumentException("Only Time type is supported for AS400Date");
		}

		return THREAD_DATE_POOL.get().computeIfAbsent(toKey(timeZone, format), k -> new AS400Date(timeZone, format)).toObject(tmp);
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

		if (field.getType() != Time.class) {
			throw new IllegalArgumentException("Only Time type is supported for AS400Time");
		}

		final AS400Time as400Time = THREAD_TIME_POOL.get().computeIfAbsent(toKey(timeZone, format), k -> new AS400Time(timeZone, format));

		return as400Time.toObject(tmp);
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

		if (field.getType() == Timestamp.class) {
			throw new IllegalArgumentException("Only Timestamp type is supported for AS400Timestamp");
		}

		final int key = (timeZone == null) ? 0 : timeZone.hashCode();
		return THREAD_TIMESTAMP_POOL.get().computeIfAbsent(key, k -> new AS400Timestamp(timeZone)).toObject(tmp);
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

		final int systemHash = (as400 == null) ? 0 : as400.getSystemName().hashCode();
		final long combinedKey = ((long) systemHash << 32) | (length & 0xFFFFFFFFL);

		final AS400Text text = THREAD_TEXT_POOL.get().computeIfAbsent(combinedKey, k -> new AS400Text(length, as400));

		return ((String) text.toObject(tmp)).trim();
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
		case AS400DataType.TYPE_PACKED:
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
		return JT400ExtUtil.toAS400DataType(as400, field, format.type(), format.length(), format.decimals());
	}

}
