/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Builder converting received data byte array into
 * defined Java class structure instance
 */
enum JT400ExtFormatBuilder {
;

	/**
	 * NORMAL - all fields with static offsets
	 * RELATIVE - only fields that contains offset refs
	 * ALL - all annotated fields
	 */
	enum TYPE {NORMAL, RELATIVE, ALL}
	
	/**
	 * Convert received byte array into JT400Format type class response definition
	 *
	 * @param format
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static <T extends IJT400Format> T build(final AS400 as400, final Class<T> format, final ByteBuffer data) throws Exception {
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
	public static <T extends IJT400Format> T build(final AS400 as400, final T obj, final ByteBuffer data) {

		final Class<T> format = (Class<T>) obj.getClass();

		// Get all classes that makes format definition (super classes first)
		final List<Class<T>> classes = JT400ExtUtil.getAllClass(new LinkedList<Class<T>>(), format);
	
		Stream<Class<T>> stream = null;
		
		// filter only classes without offset position
		// process fields with fixed offsets first		
		stream = classes.stream().filter(entry -> !entry.isAnnotationPresent(JT400Ref.class));
		build(as400, obj, data, stream, TYPE.NORMAL);
		
		// filter only classes without offset position
		// process fields with relative offsets
		stream = classes.stream().filter(entry -> !entry.isAnnotationPresent(JT400Ref.class));		
		build(as400, obj, data, stream, TYPE.RELATIVE);

		// filter classes with relative offsets 
		// process fields taking byte offset position as relative
		stream = classes.stream().filter(entry -> entry.isAnnotationPresent(JT400Ref.class));		
		build(as400, obj, data, stream, TYPE.ALL);		

		return obj;
	}

	/**
	 * Convert received byte array into JT400Format type class response definition
	 * 
	 * @param as400
	 * @param obj
	 * @param data
	 * @param stream
	 * @param type
	 * @throws Exception
	 */
	private static <T extends IJT400Format> void build(final AS400 as400, final T obj, final ByteBuffer buffer, final Stream<Class<T>> stream, final TYPE type) {
		
		stream.forEach(clazz -> {
	    	final Map<Integer, Field> fields = toFieldMap(clazz, type);

	    	ByteBuffer data = buffer;
			if (clazz.isAnnotationPresent(JT400Ref.class)) {
				data = getSlice(obj, fields, buffer);
				if (data == null) return;
			}
			
			build(as400, obj, clazz, fields, data);
		});

	}

	/**
	 * Convert received byte array into JT400Format type class response definition
	 * 
	 * @param as400
	 * @param obj
	 * @param clazz
	 * @param fields
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	private static <T extends IJT400Format> T build(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final ByteBuffer buffer) {

		// process each annotated field
		fields.forEach((k, field) -> {
			try {
				if (field.getType().isArray()) {
					setArray(as400, obj, clazz, fields, field, buffer);
				} else {
					set(as400, obj, fields, field, buffer);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		return obj;
	}

	/**
	 * Set field data of type array
	 * 
	 * @param as400
	 * @param obj
	 * @param clazz
	 * @param fields
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static <T extends IJT400Format> void setArray(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer)  throws Exception {

		final JT400Format fieldFormat = field.getAnnotation(JT400Format.class);
		
		final boolean isSubFormat = fieldFormat.type() == AS400DataType.TYPE_STRUCTURE;

		final int dataLen = getArrayDataLength(field);
		final int arraylen = getArrayLength(as400, fields, field, buffer);
		final int offset = getArrayOffset(as400, fields, field, buffer);

		final Object list = Array.newInstance(field.getType().getComponentType(), arraylen);

		int i = 0;

		while (i < arraylen) {
			byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, offset + (dataLen * i), dataLen);
			Object value = null;

			if (isSubFormat) {
				value = JT400ExtFormatBuilder.build(as400, (Class<T>) field.getType().getComponentType(), ByteBuffer.wrap(tmp));
			} else {
				value = getValue(as400, field, fieldFormat, tmp);
			}

			Array.set(list, i, value);
			i++;
		}

		JT400ExtUtil.setField(field, obj, list);

	}

	/**
	 * Set value from received data format bytes
	 *
	 * @param as400
	 * @param obj
	 * @param clazz
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	private static <T extends IJT400Format> void set(final AS400 as400, final T obj, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer) throws Exception {

		final JT400Format fieldformat = field.getAnnotation(JT400Format.class);

		if (fieldformat.type() == AS400DataType.TYPE_STRUCTURE) {
			setStructure(as400, obj, field, buffer);
			return;
		}

		final int len = getDataLength(fieldformat);

		if (len == 0) return;

		if (buffer.limit() < fieldformat.offset() + len) {
			return;
		}

		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, fieldformat.offset(), len);
		final Object value = getValue(as400, field, fieldformat, tmp);

		JT400ExtUtil.setField(field, obj, value);

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
	private static <T extends IJT400Format> void setStructure(final AS400 as400, final T obj, final Field field, final ByteBuffer buffer) throws Exception {

		if (!IJT400Format.class.isAssignableFrom(field.getType())) {
			return;
		}

		final JT400Format format = field.getAnnotation(JT400Format.class);
		final int len = getStructureLength(field);
		if (buffer.limit() < format.offset() + len) {
			return;
		}

		// JAVA 14+
		//final ByteBuffer data = buffer.slice(format.offset(), len);
		final ByteBuffer data = slice(buffer, format.offset(), len);
		final Class<? extends IJT400Format> clazz = (Class<? extends IJT400Format>) field.getType();
		final Object value  = JT400ExtFormatBuilder.build(as400, clazz, data);

		JT400ExtUtil.setField(field, obj, value);

	}

	/**
	 * Get buffer slice for format with dynamic offset. being annotated with @JT400Ref 
	 * 
	 * @param as400
	 * @param obj
	 * @param fields
	 * @param field
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static <T extends IJT400Format> ByteBuffer getSlice(final T obj, final Map<Integer, Field> fields, final ByteBuffer buffer) {		
	
		ByteBuffer data = null;

		int offset = 0;
		int length = 0;
		
		final Class<T> format = (Class<T>) obj.getClass();
		
		if (format.isAnnotationPresent(JT400Ref.class)) {

			final JT400Ref ref = format.getAnnotation(JT400Ref.class);
			offset = JT400ExtBinaryConverter.getIntValue(buffer, ref.offset());
			length = JT400ExtBinaryConverter.getIntValue(buffer, ref.length());

			if (offset > 0 && length > 0) {
				// JAVA 14+
				// data = buffer.slice(offset, length);
				data = slice(buffer, offset, length);
			}
		}
		
		return data;
	}
	
	/**
	 * ByteBuffer slice support for pre JAVA14
	 * @param buffer
	 * @param offset
	 * @param length
	 * @return
	 */
	private static ByteBuffer slice(final ByteBuffer buffer, final int offset, final int length) {
		if ( buffer.capacity() < offset + length ) {
			throw new RuntimeException("ByteBuffer too small, check JT400Format definition.");
		}
		buffer.position(offset);
		final ByteBuffer data = buffer.slice();
		data.limit(length);
		return data;
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
	private static  Object getValue(final AS400 as400, final Field field, final JT400Format format, final byte[] tmp)  throws Exception  {

		Object value = null;

		final int type = format.type();

		switch (type) {
		case AS400DataType.TYPE_BIN1:
			value = JT400ExtBinaryConverter.asAS400Bin1(field, tmp);
			break;
		case AS400DataType.TYPE_BIN2:
			value = JT400ExtBinaryConverter.asAS400Bin2(field, tmp);
			break;
		case AS400DataType.TYPE_BIN4:
			value = JT400ExtBinaryConverter.asAS400Bin4(field, tmp);
			break;
		case AS400DataType.TYPE_BIN8:
			value = JT400ExtBinaryConverter.asAS400Bin8(field, tmp);
			break;

		case AS400DataType.TYPE_UBIN1:
			value = JT400ExtBinaryConverter.asAS400UBin1(field, tmp);
			break;
		case AS400DataType.TYPE_UBIN2:
			value = JT400ExtBinaryConverter.asAS400UBin2(field, tmp);
			break;
		case AS400DataType.TYPE_UBIN4:
			value = JT400ExtBinaryConverter.asAS400UBin4(field, tmp);
			break;
		case AS400DataType.TYPE_UBIN8:
			value = JT400ExtBinaryConverter.asAS400UBin8(field, tmp);
			break;

		case AS400DataType.TYPE_FLOAT4:
			value = JT400ExtBinaryConverter.asAS400Float4(field, tmp);
			break;
		case AS400DataType.TYPE_FLOAT8:
			value = JT400ExtBinaryConverter.asAS400Float8(field, tmp);
			break;

		case AS400DataType.TYPE_DECFLOAT:
			value = JT400ExtBinaryConverter.asAS400DecFloat(field, format.decimals(), tmp);
			break;

		case AS400DataType.TYPE_ZONED:
			value = JT400ExtBinaryConverter.asAS400ZonedDecimal(field, format.length(), format.decimals(), tmp);
			break;
		case AS400DataType.TYPE_PACKED:
			value = JT400ExtBinaryConverter.asAS400PackedDecimal(field, format.length(), format.decimals(), tmp);
			break;

		case AS400DataType.TYPE_DATE:
			value = JT400ExtBinaryConverter.asAS400Date(as400.getTimeZone(), field, format.format(), tmp);
			break;
		case AS400DataType.TYPE_TIME:
			value = JT400ExtBinaryConverter.asAS400Time(as400.getTimeZone(), field, format.format(), tmp);
			break;
		case AS400DataType.TYPE_TIMESTAMP:
			value = JT400ExtBinaryConverter.asAS400Timestamp(as400.getTimeZone(), field, tmp);
			break;

		case AS400DataType.TYPE_BYTE_ARRAY:

			if (field.getType() == byte.class) {
				value = tmp;
			} else if (field.getType() == ByteBuffer.class) {
				value = ByteBuffer.wrap(tmp);
			}
			break;

		case AS400DataType.TYPE_TEXT:
			value = JT400ExtBinaryConverter.asAS400Text(as400, field, format.length(), tmp);
			break;

		default:
			if (field.getType() == String.class) {
				value = JT400ExtBinaryConverter.asAS400Text(as400, field, format.length(), tmp);
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
	static int getArrayDataLength(final Field field) {

		final JT400Format format = field.getAnnotation(JT400Format.class);

		if (field.getType() == String[].class) {
			return format.length();
		}

		if (format.type() == AS400DataType.TYPE_STRUCTURE) {
			return getStructureLength(field);
		}

		int len = JT400ExtBinaryConverter.getDataLength(format.type());
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
	static int getStructureLength(final Field field) {

		JT400Format format = null;

		if (field.getType().isArray()) {
			// For some reason, Eclipse report this as an error in Java < 14+
			// Needed to refactor in 2-line block.
			//format = field.getType().componentType().getAnnotation(JT400Format.class);
			final Class<?> clazz = field.getType().getComponentType();
			format = clazz.getAnnotation(JT400Format.class);
		} else {
			format = field.getType().getAnnotation(JT400Format.class);
		}

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
	static int getDataLength(final JT400Format format) {

		if (format.length() > 0) {
			return format.length();
		}

		return JT400ExtBinaryConverter.getDataLength(format.type());
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
	static int getArrayLength(final AS400 as400, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer) throws Exception {

		final JT400Ref ref = field.getAnnotation(JT400Ref.class);
		if (ref != null && ref.length() > -1) {
			return getFieldOrRefLength(as400, fields, buffer, ref.length());			
		}

		final JT400Format jtformat = field.getAnnotation(JT400Format.class);
		if (jtformat.of() > -1) {
			return getFieldOrRefLength(as400, fields, buffer, jtformat.of());
		}
		
		return jtformat.length();

	}

	/**
	 * Get data offset position within byte array
	 * 
	 * @param as400
	 * @param format
	 * @param fields
	 * @param field
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	static int getArrayOffset(final AS400 as400, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer) throws Exception {

		final JT400Ref ref = field.getAnnotation(JT400Ref.class);
		if (ref != null) {
			return getFieldOrRefLength(as400, fields, buffer, ref.offset());
		}

		final JT400Format jt400Format = field.getAnnotation(JT400Format.class);
		return jt400Format.offset();

	}

	/**
	 * Get data value by offset position 
	 * @param as400
	 * @param fields
	 * @param buffer
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	private static  int getFieldOrRefLength(final AS400 as400, final Map<Integer, Field> fields, final ByteBuffer buffer, final int offset) throws Exception {

		if (offset < 0) return 0;
		
		if (fields.containsKey(offset)) {
			final Field refField = fields.get(offset);
			return getFieldValue(as400, refField, buffer);
		} else {
			return JT400ExtBinaryConverter.getIntValue(buffer, offset);
		}
		
	}
	
	/**
	 * Return value for service program call
	 * 
	 * @param as400
	 * @param fields
	 * @param field
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	private static  int getFieldValue(final AS400 as400, final Field field, final ByteBuffer buffer) throws Exception {

		final JT400Format fmt = field.getAnnotation(JT400Format.class);
		final byte [] tmp = JT400ExtUtil.getBytesFrom(buffer, fmt.offset(), getDataLength(fmt));
		final Object value = getValue(as400, field, fmt, tmp);

		if (value != null) {
			return (int) value;
		}

		return 0;
	}

	/**
	 * Get all class fields annotated with JT400Format and store into map by offset position as key
	 *
	 * @param format
	 * @return
	 * @throws Exception
	 */
	static <T extends IJT400Format> Map<Integer, Field> toFieldMap(final Class<T> format, final TYPE type) {

		final List<Field> list = Arrays.asList(format.getDeclaredFields());
		
		Stream<Field> stream = list.stream();
		stream = filter(stream, type);
		
		return collect(stream);

	}
	
	/**
	 * Filter fields by offset type
	 * @param stream
	 * @param type
	 * @return
	 */
	private static  Stream<Field> filter(final Stream<Field> stream, final TYPE type) {
		
		return stream.filter( f -> {
			
			if (!f.isAnnotationPresent(JT400Format.class)) {
				return false;
			}
			
			if (type == TYPE.ALL) return true;

			final boolean isRelative = f.isAnnotationPresent(JT400Ref.class);
			
			return type == TYPE.NORMAL && isRelative ||	type == TYPE.RELATIVE && !isRelative;
			
		});		
	}

	/**
	 * Convert Field list into map, keys are field data offset, or negative counters
	 * @param stream
	 * @param type
	 * @return
	 */
	private static  Map<Integer, Field> collect(final Stream<Field> stream) {
		
		final AtomicInteger cnt = new AtomicInteger(0);
		
		return stream.collect(Collectors.toMap(f -> {

			final JT400Format fmt = f.getAnnotation(JT400Format.class);
			
			if (fmt.offset() < 0) {
				return  cnt.decrementAndGet();
			}

			return f.getAnnotation(JT400Format.class).offset();

		}, field -> field));
	}

}
