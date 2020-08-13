/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Builder converting received data byte array into 
 * defined Java class structure instance
 */
enum JT400ExtFormatBuilder {
;
	
	/**
	 * Convert received byte array into JT400Format type class response definition 
	 * 
	 * @param format
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	final static public <T extends IJT400Format> T build(final AS400 as400, final Class<T> format, final ByteBuffer data) throws Exception {
		final T obj = format.getDeclaredConstructor().newInstance();
		return build(as400, obj, data);
	}

	/**
	 * Convert received byte array into JT400Format type class response definition
	 * 
	 * @param as400
	 * @param obj
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	final static public <T extends IJT400Format> T build(final AS400 as400, final T obj, final ByteBuffer data) throws Exception {
		
		final Class<T> format = (Class<T>) obj.getClass();
		
		final Map<Integer, Field> fields = toFieldMap(format);
		
		// process each annotated field
		fields.forEach((k, field) -> {
			try {
				if (field.getType().isArray()) {
					setArray(as400, obj, format, fields, field, data);
				} else {
					set(as400, obj, fields, field, data);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		});
		
		return obj;
	}
	
	/**
	 * Set field data of type array
	 * @param as400
	 * @param obj
	 * @param format
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	final static private <T extends IJT400Format> void setArray(final AS400 as400, final T obj, final Class<T> format, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer)  throws Exception {

		final JT400Format jt400Format = field.getAnnotation(JT400Format.class);

		final int dataLen = getArrayDataLength(fields, field);
		final int arraylen = getArrayLength(as400, format, fields, field, buffer);
		final int offset = getArrayOffset(as400, format, fields, field, buffer);
				
		final Object [] list = new Object[arraylen];
				
		int i = 0;
		
		while (i < arraylen) {
			byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, offset + (dataLen * i), dataLen);
			Object value = getValue(as400, fields, field, jt400Format, tmp);
			list[i] = value;
			i++;
		}
				
		if (list != null) {
			field.set(obj, list);
		}

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
	final static private <T extends IJT400Format> void set(final AS400 as400, final T obj, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer) throws Exception {
		
		final JT400Format format = field.getAnnotation(JT400Format.class);
				
		if (format.type() == AS400DataType.TYPE_STRUCTURE) {
			setStructure(as400, obj, field, buffer);
			return;
		}
			
		int len = getDataLength(format);
		
		if (len == 0) return;
		
		if (buffer.limit() < format.offset() + len) {
			return;
		}

		field.setAccessible(true);
		
		Object value = null;
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, format.offset(), len);
		
		value = getValue(as400, fields, field, format, tmp);
		
		if (value != null) {
			field.set(obj, value);
		}
	}

	/**
	 * Recursively call format builder for structured field
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	final static private <T extends IJT400Format> void setStructure(final AS400 as400, final T obj, final Field field, final ByteBuffer buffer) throws Exception {
				
		if (!IJT400Format.class.isAssignableFrom(field.getType())) {
			return;
		} 
		
		final JT400Format format = field.getAnnotation(JT400Format.class);
		final int len = getStructureLength(field);
		if (buffer.limit() < format.offset() + len) {
			return;
		}
		
		final ByteBuffer data = buffer.slice(format.offset(), len);
		final Class<? extends IJT400Format> clazz = (Class<? extends IJT400Format>) field.getType();
		final Object value  = JT400ExtFormatBuilder.build(as400, clazz, data);

		if (value != null) {
			field.set(obj, value);
		}
	}

	/**
	 * Get value from response buffer based on format structure
	 * 
	 * @param as400
	 * @param field
	 * @param format
	 * @param tmp
	 * @return
	 * @throws Exception
	 */
	final static private Object getValue(final AS400 as400, final Map<Integer, Field> fields, final Field field, final JT400Format format, final byte[] tmp)  throws Exception  {

		Object value = null;
		
		final int type = format.type();
		
		switch (type) {
		case AS400DataType.TYPE_BIN1:
			value = JT400ExtBinaryConverter.asAS400Bin1(as400, field, format, tmp);
			break;
		case AS400DataType.TYPE_BIN2:
			value = JT400ExtBinaryConverter.asAS400Bin2(as400, field, format, tmp);
			break;
		case AS400DataType.TYPE_BIN4:
			value = JT400ExtBinaryConverter.asAS400Bin4(as400, field, format, tmp);			
			break;
		case AS400DataType.TYPE_BIN8:
			value = JT400ExtBinaryConverter.asAS400Bin8(as400, field, format, tmp);			
			break;			

		case AS400DataType.TYPE_UBIN1:
			value = JT400ExtBinaryConverter.asAS400UBin1(as400, field, format, tmp);
			break;
		case AS400DataType.TYPE_UBIN2:
			value = JT400ExtBinaryConverter.asAS400UBin2(as400, field, format, tmp);
			break;
		case AS400DataType.TYPE_UBIN4:
			value = JT400ExtBinaryConverter.asAS400UBin4(as400, field, format, tmp);
			break;
		case AS400DataType.TYPE_UBIN8:
			value = JT400ExtBinaryConverter.asAS400UBin8(as400, field, format, tmp);
			break;			
			
		case AS400DataType.TYPE_FLOAT4:
			value = JT400ExtBinaryConverter.asAS400Float4(as400, field, format, tmp);			
			break;			
		case AS400DataType.TYPE_FLOAT8:
			value = JT400ExtBinaryConverter.asAS400Float8(as400, field, format, tmp);			
			break;			

		case AS400DataType.TYPE_DECFLOAT:
			value = JT400ExtBinaryConverter.asAS400DecFloat(as400, field, format, tmp);			
			break;			

		case AS400DataType.TYPE_ZONED:
			value = JT400ExtBinaryConverter.asAS400ZonedDecimal(as400, field, format, tmp);
			break;
		case AS400DataType.TYPE_PACKED:
			value = JT400ExtBinaryConverter.asAS400PackedDecimal(as400, field, format, tmp);
			break;
			
		case AS400DataType.TYPE_DATE:
			value = JT400ExtBinaryConverter.asAS400Date(as400, field, format, tmp);			
			break;		
		case AS400DataType.TYPE_TIME:
			value = JT400ExtBinaryConverter.asAS400Time(as400, field, format, tmp);			
			break;
		case AS400DataType.TYPE_TIMESTAMP:
			value = JT400ExtBinaryConverter.asAS400Timestamp(as400, field, format, tmp);			
			break;		
			
		case AS400DataType.TYPE_BYTE_ARRAY:
			
			if (field.getType() == byte.class) {
				value = tmp;			
			} else if (field.getType() == ByteBuffer.class) {
				value = ByteBuffer.wrap(tmp);
			}			
			break;

		case AS400DataType.TYPE_TEXT:
			value = JT400ExtBinaryConverter.asAS400Text(as400, field, format, tmp);
			break;

		default:
			if (field.getType() == String.class) {
				value = JT400ExtBinaryConverter.asAS400Text(as400, field, format, tmp);
			}
			break;
		}
		
		return value;
	}
	
	/**
	 * Get data length for array format type
	 * @param field
	 * @param format
	 * @return
	 */
	private static int getArrayDataLength(final Map<Integer, Field> fields, final Field field) {
		
		final JT400Format format = field.getAnnotation(JT400Format.class);
		
		if (field.getType() == String[].class) {
			return format.length();
		}
		
		if (format.type() == AS400DataType.TYPE_STRUCTURE) {
			return getStructureLength(field);
		}
		
		int len = getDataLength(format.type());
		if (len == 0) {
			len = format.length();
		}
		
		return len;
	}
	
	/**
	 * Get data length for defined structure field
	 * @param field
	 * @return
	 */
	private static int getStructureLength(final Field field) {
		
		final JT400Format format = field.getType().getAnnotation(JT400Format.class);
		if (format != null) {
			return format.length();
		}
		
		return 0;
	}
	
	/**
	 * Get byte length for standard data types
	 * 
	 * @param type
	 * @return
	 */
	private static int getDataLength(final JT400Format format) {
		
		if (format.length() > 0) {
			return format.length();
		}
		
		return getDataLength(format.type());
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
	
	/**
	 * Get array data length from format or field reference by offset
	 * 
	 * @param as400
	 * @param format
	 * @param field
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	final static private <T extends IJT400Format> int getArrayLength(final AS400 as400, final Class<T> format, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer) throws Exception {

		final JT400Format jtformat = field.getAnnotation(JT400Format.class);
		if (jtformat.of() < 0) {
			return jtformat.length();
		}
		
		if (!fields.containsKey(jtformat.of())) return 0;
		
		final Field refField = fields.get(jtformat.of());
		final JT400Format fmt = refField.getAnnotation(JT400Format.class);
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, fmt.offset(), getDataLength(fmt));
		final Object value = getValue(as400, fields, refField, fmt, tmp);
		
		if (value != null) {
			return (int) value;
		}
		
		return 0;
	}

	/**
	 * Get data offset position within byte array
	 * @param as400
	 * @param format
	 * @param fields
	 * @param field
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	final static private <T extends IJT400Format> int getArrayOffset(final AS400 as400, final Class<T> format, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer) throws Exception {

		final JT400Format jt400Format = field.getAnnotation(JT400Format.class);
		
		return jt400Format.offset();
 
	}

	/**
	 * Get all class fields annotated with JT400Format and store into map by offset position as key
	 * 
	 * @param format
	 * @return
	 * @throws Exception
	 */
	final static private <T extends IJT400Format> Map<Integer, Field> toFieldMap(final Class<T> format) {

		return getAllFields(new ArrayList<Field>(), format).stream()
		    .filter( f -> f.isAnnotationPresent(JT400Format.class) )
			.collect(Collectors.toMap(f -> f.getAnnotation(JT400Format.class).offset(), field -> field));
		
	}

	/**
	 * Recursively get all fields including fields from super class 
	 * @param fields
	 * @param type
	 * @return
	 */
	public static List<Field> getAllFields(final List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
}
