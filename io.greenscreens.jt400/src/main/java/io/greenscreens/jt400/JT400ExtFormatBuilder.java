/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin1;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Date;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Time;
import com.ibm.as400.access.AS400Timestamp;
import com.ibm.as400.access.AS400ZonedDecimal;

import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Builder converting received data byte array into 
 * defined Java class structure instance
 */
enum JT400ExtFormatBuilder {
;
	
	/**
	 * Convert received byte array into JT400Format annotated class response definition 
	 * 
	 * @param format
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	final static public <T extends IJT400Format> T build(final AS400 as400, final Class<T> format, final ByteBuffer data) throws Exception {
		
		final T obj = format.getDeclaredConstructor().newInstance();
		
		final Field[] fields = format.getDeclaredFields();
		
		JT400Format jtformat = null;
		
		for (Field field : fields) {
			
			jtformat = field.getAnnotation(JT400Format.class);
			if (jtformat == null) continue;
		
			if (field.getType() == String.class) {
				setString(as400, obj, field, data);
			} else {
				set(as400, obj, field, data);
			}
		}
		
		return obj;
	}

	/**
	 * Set string value from received data format bytes
	 * 
	 * @param as400
	 * @param obj
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	final static private <T extends IJT400Format> void setString(final AS400 as400, final T obj, final Field field, final ByteBuffer buffer) throws Exception {
		
		final JT400Format format = field.getAnnotation(JT400Format.class);
		
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, format.offset(), format.length());
		
		final AS400Text text = new AS400Text(format.length(), as400); 
		
		final String value = ((String)(text.toObject(tmp))).trim();
		
		field.setAccessible(true);
		field.set(obj, value);
	}
	
	/**
	 * Set value from received data format bytes
	 * 
	 * @param as400
	 * @param obj
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	final static private <T extends IJT400Format> void set(final AS400 as400, final T obj, final Field field, final ByteBuffer buffer) throws Exception {
		
		final JT400Format format = field.getAnnotation(JT400Format.class);
		if (format == null) return;
		
		int len = format.length();
		if (len == 0) {
			len = getDataLength(format.type());
		}
		
		if (len == 0) return;
		
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, format.offset(), len);
		
		field.setAccessible(true);
		Object value = null;
		
		switch (format.type()) {
		case AS400DataType.TYPE_BIN1:
			final AS400Bin1 bin1 = new AS400Bin1();
			if (field.getType() == boolean.class) {
				value = (byte) bin1.toByte(tmp) == 1;
			} else if (field.getType() == Boolean.class) {				
				value = Boolean.valueOf(bin1.toByte(tmp) == 1);
			} else if (field.getType() == byte.class) {
				value = bin1.toByte(tmp);
			} else if (field.getType() == Byte.class) {
				value = bin1.toObject(tmp);				
			}			
			break;
		case AS400DataType.TYPE_BIN2:
			final AS400Bin2 bin2 = new AS400Bin2();
			if (field.getType() == short.class) {
				value = bin2.toShort(tmp);			
			} else if (field.getType() == Short.class) {
				value = bin2.toObject(tmp);
			}
			break;
		case AS400DataType.TYPE_BIN4:
			final AS400Bin4 bin4 = new AS400Bin4();
			if (field.getType() == int.class) {
				value = bin4.toInt(tmp);			
			} else if (field.getType() == Integer.class) {
				value = bin4.toObject(tmp);
			}			
			break;
		case AS400DataType.TYPE_BIN8:
			final AS400Bin8 bin8 = new AS400Bin8();
			if (field.getType() == long.class) {
				value = bin8.toLong(tmp);			
			} else if (field.getType() == Long.class) {
				value = bin8.toObject(tmp);
			}			
			break;			

		case AS400DataType.TYPE_UBIN1:
			final AS400Bin1 ubin1 = new AS400Bin1();
			if (field.getType() == boolean.class) {
				value = (byte) ubin1.toByte(tmp) == 1;
			} else if (field.getType() == Boolean.class) {
				value = Boolean.valueOf(ubin1.toByte(tmp) == 1);
			} else if (field.getType() == byte.class) {
				value = ubin1.toByte(tmp);
			} else if (field.getType() == Byte.class) {
				value = ubin1.toObject(tmp);				
			}
			break;
		case AS400DataType.TYPE_UBIN2:
			final AS400Bin2 ubin2 = new AS400Bin2();
			if (field.getType() == short.class) {
				value = ubin2.toShort(tmp);			
			} else if (field.getType() == Short.class) {
				value = ubin2.toObject(tmp);
			}			
			break;
		case AS400DataType.TYPE_UBIN4:
			final AS400Bin4 ubin4 = new AS400Bin4();
			if (field.getType() == int.class) {
				value = ubin4.toInt(tmp);			
			} else if (field.getType() == Integer.class) {
				value = ubin4.toObject(tmp);
			}
			break;
		case AS400DataType.TYPE_UBIN8:
			final AS400Bin8 ubin8 = new AS400Bin8();
			if (field.getType() == long.class) {
				value = ubin8.toLong(tmp);			
			} else if (field.getType() == Long.class) {
				value = ubin8.toObject(tmp);
			}			
			break;			
			
		case AS400DataType.TYPE_FLOAT4:
			final AS400Float4 float4 = new AS400Float4();
			if (field.getType() == float.class) {
				value = float4.toFloat(tmp);			
			} else if (field.getType() == Float.class) {
				value = float4.toObject(tmp);
			}			
			break;			
		case AS400DataType.TYPE_FLOAT8:
			final AS400Float8 float8 = new AS400Float8();
			if (field.getType() == double.class) {
				value = float8.toDouble(tmp);			
			} else if (field.getType() == Double.class) {
				value = float8.toObject(tmp);
			}			
			break;			

		case AS400DataType.TYPE_ZONED:
			final AS400ZonedDecimal zoned = new AS400ZonedDecimal(format.length(), format.decimals());			
			if (field.getType() == double.class) {
				value = zoned.toDouble(tmp);			
			} else if (field.getType() == BigDecimal.class) {
				value = zoned.toObject(tmp);
			}
			break;
		case AS400DataType.TYPE_PACKED:
			final AS400PackedDecimal packed = new AS400PackedDecimal(format.length(), format.decimals());			
			if (field.getType() == double.class) {
				value = packed.toDouble(tmp);			
			} else if (field.getType() == BigDecimal.class) {
				value = packed.toObject(tmp);
			}
			break;
			
		case AS400DataType.TYPE_DATE:
			if (field.getType() == Date.class) {
				final AS400Date date = new AS400Date();
				value = date.toObject(tmp);		
			}
			break;		
		case AS400DataType.TYPE_TIME:
			if (field.getType() == Time.class) {
				final AS400Time time = new AS400Time();
				value = time.toObject(tmp);			
			}
			break;
		case AS400DataType.TYPE_TIMESTAMP:
			if (field.getType() == Timestamp.class) {
				final AS400Timestamp timestamp = new AS400Timestamp();
				value = timestamp.toObject(tmp);
			}
			break;		
			
		case AS400DataType.TYPE_BYTE_ARRAY:
			if (field.getType() == byte.class) {
				value = tmp;			
			} else if (field.getType() == ByteBuffer.class) {
				value = ByteBuffer.wrap(tmp);
			}			
			break;
			
		case AS400DataType.TYPE_ARRAY:
			// TODO array of elements 
			break;
		
		case AS400DataType.TYPE_STRUCTURE:
			// TODO another format to read
			break;
		default:
			break;
		}
		
		if (value != null) {
			field.set(obj, value);
		}
	}

	/**
	 * Get byte length for standard data types
	 * 
	 * @param type
	 * @return
	 */
	private static int getDataLength(final int type) {

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
		default:
			return 0;
		}
		
	}

}
