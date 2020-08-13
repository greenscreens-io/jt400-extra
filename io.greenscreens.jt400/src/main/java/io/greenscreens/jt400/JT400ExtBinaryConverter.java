package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin1;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
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
	 * Convert byte data from AS400Bin1
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	final static Object asAS400Bin1(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;
		
		final AS400Bin1 bin1 = new AS400Bin1();
		
		if (field.getType() == boolean.class) {
			value = (byte) bin1.toByte(tmp) == 1;
		} else if (field.getType() == Boolean.class) {				
			value = Boolean.valueOf(bin1.toByte(tmp) == 1);
		} else if (field.getType() == byte.class) {
			value = bin1.toByte(tmp);
		} else if (field.getType() == Byte.class) {
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
	final static Object asAS400Bin2(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400Bin2 bin2 = new AS400Bin2();
		if (field.getType() == short.class) {
			value = bin2.toShort(tmp);			
		} else if (field.getType() == Short.class) {
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
	final static Object asAS400Bin4(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400Bin4 bin4 = new AS400Bin4();
		if (field.getType() == int.class) {
			value = bin4.toInt(tmp);			
		} else if (field.getType() == Integer.class) {
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
	final static Object asAS400Bin8(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400Bin8 bin8 = new AS400Bin8();
		if (field.getType() == long.class) {
			value = bin8.toLong(tmp);			
		} else if (field.getType() == Long.class) {
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
	final static Object asAS400UBin1(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400UnsignedBin1 ubin1 = new AS400UnsignedBin1();
		if (field.getType() == short.class) {
			value = ubin1.toShort(tmp);
		} else if (field.getType() == Short.class) {
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
	final static Object asAS400UBin2(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400UnsignedBin2 ubin2 = new AS400UnsignedBin2();
		if (field.getType() == int.class) {
			value = ubin2.toInt(tmp);			
		} else if (field.getType() == Integer.class) {
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
	final static Object asAS400UBin4(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400UnsignedBin4 ubin4 = new AS400UnsignedBin4();
		if (field.getType() == long.class) {
			value = ubin4.toLong(tmp);			
		} else if (field.getType() == Long.class) {
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
	final static Object asAS400UBin8(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

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
	final static Object asAS400Float4(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400Float4 float4 = new AS400Float4();
		if (field.getType() == float.class) {
			value = float4.toFloat(tmp);			
		} else if (field.getType() == Float.class) {
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
	final static Object asAS400Float8(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400Float8 float8 = new AS400Float8();
		if (field.getType() == double.class) {
			value = float8.toDouble(tmp);			
		} else if (field.getType() == Double.class) {
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
	final static Object asAS400DecFloat(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;

		final AS400DecFloat decFloat = new AS400DecFloat(format.decimals());
		if (field.getType() == double.class) {
			value = decFloat.toDouble(tmp);
		} else if (field.getType() == Double.class) {
			value = Double.valueOf(decFloat.toDouble(tmp));
		} else if (field.getType() == BigDecimal.class) {
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
	final static Object asAS400ZonedDecimal(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;
		
		final AS400ZonedDecimal zoned = new AS400ZonedDecimal(format.length(), format.decimals());			
		if (field.getType() == double.class) {
			value = zoned.toDouble(tmp);
		} else if (field.getType() == Double.class) {
			zoned.setUseDouble(true);
			value = zoned.toObject(tmp);
		} else if (field.getType() == BigDecimal.class) {
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
	final static Object asAS400PackedDecimal(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;
		
		final AS400PackedDecimal packed = new AS400PackedDecimal(format.length(), format.decimals());			
		if (field.getType() == double.class) {
			value = packed.toDouble(tmp);			
		} else if (field.getType() == Double.class) {
			packed.setUseDouble(true);
			value = packed.toObject(tmp);
		} else if (field.getType() == BigDecimal.class) {
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
	final static Object asAS400Date(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;
		
		if (field.getType() == Date.class) {
			//char sep = getDateSeparator(format.format());
			final AS400Date date = new AS400Date(as400.getTimeZone(), format.format());
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
	final static Object asAS400Time(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;
		
		if (field.getType() == Time.class) {
			final AS400Time time = new AS400Time(as400.getTimeZone(), format.format());
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
	final static Object asAS400Timestamp(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {

		Object value = null;
		
		if (field.getType() == Timestamp.class) {
			final AS400Timestamp timestamp = new AS400Timestamp(as400.getTimeZone());
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
	final static Object asAS400Text(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp) throws Exception {		
		final AS400Text text = new AS400Text(format.length(), as400); 		
		final String value = ((String)(text.toObject(tmp))).trim();
		return value;
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

}
