/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.JT400ExtFormatBuilder.TYPE;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Builder converting IJT400ExtFormat Java class instance to ByteBuffer for IBM i
 * It is an opposite of JT400ExtFormatBuilder
 */
enum ByteBufferBuilder {
;
	
	/**
	 * Main builder exposed method
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static  <T extends IJT400Format> ByteBuffer build(final AS400 as400, final T obj) throws Exception {
		
		final Class<T> format = JT400ExtUtil.toClass(obj);
		if (!format.isAnnotationPresent(JT400Format.class)) {
			throw new IOException("Format size not defined!");
		}
		
		final JT400Format ann = format.getDeclaredAnnotation(JT400Format.class);	
		return build(as400, obj, ByteBuffer.allocate(ann.length()));
	}
	
	/**
	 * Sequentially process all inherited JT400Format class
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param data
	 * @return
	 * @throws Exception
	 */
	static <T extends IJT400Format> ByteBuffer build(final AS400 as400, final T obj, final ByteBuffer buffer) throws Exception {

		final Class<T> format = JT400ExtUtil.toClass(obj);

		// Get all classes that makes format definition (super classes first)
		final List<Class<T>> classes = JT400ExtUtil.getAllClass(new LinkedList<Class<T>>(), format);
	
		Stream<Class<T>> stream = null;
		
		// filter only classes without offset position
		// process fields with fixed offsets first		
		stream = classes.stream().filter(entry -> !entry.isAnnotationPresent(JT400Ref.class));
		build(as400, obj, buffer, stream, TYPE.NORMAL);
		
		// filter only classes without offset position
		// process fields with relative offsets
		stream = classes.stream().filter(entry -> !entry.isAnnotationPresent(JT400Ref.class));		
		build(as400, obj, buffer, stream, TYPE.RELATIVE);

		// filter classes with relative offsets 
		// process fields taking byte offset position as relative
		stream = classes.stream().filter(entry -> entry.isAnnotationPresent(JT400Ref.class));		
		build(as400, obj, buffer, stream, TYPE.ALL);	
		
		return buffer;
	}

	/**
	 * Process all detected subclasses 
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param data
	 * @param stream
	 * @param all
	 */
	static <T extends IJT400Format> void build(final AS400 as400, final T obj, final ByteBuffer buffer, final Stream<Class<T>> stream, final TYPE type) {

		stream.forEach(clazz -> {
	    	final Map<Integer, Field> fields = JT400ExtFormatBuilder.toFieldMap(clazz, type);

	    	/** TODO add reference support
			if (clazz.isAnnotationPresent(JT400Ref.class)) {
				getSlice(obj, fields, buffer);
			}
			*/
			
			build(as400, obj, clazz, fields, buffer);
		});
		
	}

	/**
	 * Process each annotated field
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param clazz
	 * @param fields
	 * @param buffer
	 */
	static <T extends IJT400Format> void build(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final ByteBuffer buffer) {
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
	}

	/**
	 * Convert arrays of java values to arrays of byte array for IBM i
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param clazz
	 * @param fields
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	private static <T extends IJT400Format> void setArray(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final Field field, final ByteBuffer buffer)  throws Exception {
		
		final JT400Format fieldFormat = field.getAnnotation(JT400Format.class);
		
		final boolean isSubFormat = fieldFormat.type() == AS400DataType.TYPE_STRUCTURE;

		final int dataLen = JT400ExtFormatBuilder.getArrayDataLength(field);
		final int arraylen = JT400ExtFormatBuilder.getArrayLength(as400, fields, field, buffer);
		final int offset = JT400ExtFormatBuilder.getArrayOffset(as400, fields, field, buffer);

		final Object list = Array.newInstance(field.getType().getComponentType(), arraylen);

		int i = 0;

		while (i < arraylen) {

			// TODO
			
			if (isSubFormat) {
				
			} else {
				
			}

			i++;
		}
		
	}
	
	/**
	 * Convert Java value to byte array for IBM i 
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param fields
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

		int len = JT400ExtFormatBuilder.getDataLength(fieldformat);

		if (len == 0) return;

		if (buffer.limit() < fieldformat.offset() + len) {
			return;
		}
		
		// TODO 

	}

	/**
	 * Read JT400Format from field into new ByteBuffer and add buffer to master buffer
	 * @param <T>
	 * @param as400
	 * @param obj
	 * @param field
	 * @param buffer
	 * @throws Exception
	 */
	private static <T extends IJT400Format> void setStructure(final AS400 as400, final T obj, final Field field, final ByteBuffer buffer) throws Exception {

		if (!IJT400Format.class.isAssignableFrom(field.getType())) {
			return;
		}
		
		final JT400Format format = field.getAnnotation(JT400Format.class);
		final int len = JT400ExtFormatBuilder.getStructureLength(field);
		if (buffer.limit() < format.offset() + len) {
			return;
		}
		
		final ByteBuffer fieldBuffer = ByteBufferBuilder.build(as400, JT400ExtUtil.getField(field, obj));
		buffer.put(fieldBuffer.array(), format.offset(), len);
	}

}
