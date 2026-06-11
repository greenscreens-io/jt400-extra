/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.JT400ExtFormatBuilder.TYPE;
import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

enum ByteBufferBuilderDynamic {
	;

	public static <T extends IJT400Format> ByteBuffer build(final AS400 as400, final T obj) throws Exception {

		final Map<Integer, ByteBuffer> bufferList = new HashMap<>();
		final Class<T> format = JT400ExtUtil.toClass(obj);

		final List<Class<T>> classes = JT400ExtUtil.getAllClass(new LinkedList<Class<T>>(), format);

		for (final Class<T> clazz : classes) {
			final Map<Integer, Field> fields = JT400ExtFormatBuilder.toFieldMap(clazz, TYPE.ALL);
			build(as400, obj, clazz, fields, bufferList);
		}

		return toBuffer(bufferList);
	}

	static private int bufferTotalSize(final Collection<ByteBuffer>  bufferList) {
		return bufferList.stream().map(ByteBuffer::capacity).reduce(0, (a, b) -> a + b);
	}

	/**
	 * merge list of buffers into a single buffer
	 * @param bufferList
	 * @return
	 */
	static ByteBuffer toBuffer(final Set<ByteBuffer>  bufferList) {
		final int size = bufferTotalSize(bufferList);
		final ByteBuffer buffer = ByteBuffer.allocate(size);
		bufferList.forEach(buffer::put);
		buffer.rewind();
		return buffer;
	}

	static ByteBuffer toBuffer(final Map<Integer, ByteBuffer> bufferList) {
		final int size = bufferTotalSize(bufferList.values());
		final ByteBuffer buffer = ByteBuffer.allocate(size);

		bufferList.entrySet().stream()
		.sorted(Map.Entry.comparingByKey())
		.map(Map.Entry::getValue)
		.forEach(buffer::put);

		buffer.rewind();
		return buffer;
	}

	static <T extends IJT400Format> void build(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final Map<Integer, ByteBuffer> bufferList) throws Exception {
		for (final Field field : fields.values()) {
			if (field.getType().isArray()) {
				setArray(as400, obj, clazz, fields, field, bufferList);
			} else {
				set(as400, obj, fields, field, bufferList);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected static <T extends IJT400Format> void setArray(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final Field field, final Map<Integer, ByteBuffer> bufferList)  throws Exception {


		if (!field.isAnnotationPresent(Id.class)) return;
		if (!field.isAnnotationPresent(JT400Format.class)) return;

		final Object value = JT400ExtUtil.getFieldValue(field, obj);

		if (Objects.isNull(value)) {
			return;
		}

		final Id id = field.getAnnotation(Id.class);
		final JT400Format fieldFormat = field.getAnnotation(JT400Format.class);
		final boolean isSubFormat = fieldFormat.type() == AS400DataType.TYPE_STRUCTURE;

		final Set<ByteBuffer> buffers = new HashSet<>();

		final int length = Array.getLength(value);
		for (int i = 0; i < length; i ++) {
			final Object arrayElement = Array.get(value, i);
			ByteBuffer buffer = null;
			if (isSubFormat) {
				if (arrayElement instanceof IJT400Format) {
					buffer = ByteBufferBuilder.build(as400, (T) arrayElement);
				}
			} else {
				buffer = set(as400, obj, fields, field);
			}
			if (Objects.nonNull(buffer)) {
				buffers.add(buffer);
			}
		}

		bufferList.put(id.value(), toBuffer(buffers));

	}

	private static <T extends IJT400Format> void set(final AS400 as400, final T obj, final Map<Integer, Field> fields, final Field field, final Map<Integer, ByteBuffer>  bufferList) throws Exception {
		final ByteBuffer buffer = set(as400, obj, fields, field);
		if (Objects.nonNull(buffer)) {
			final Id id = field.getAnnotation(Id.class);
			bufferList.put(id.value(), buffer);
		}
	}

	private static <T extends IJT400Format> ByteBuffer set(final AS400 as400, final T obj, final Map<Integer, Field> fields, final Field field) throws Exception {

		if (JT400ExtUtil.getFieldType(field) == ByteBuffer.class) {
			return JT400ExtUtil.getFieldValue(field, obj);
		}

		final JT400Format fieldformat = field.getAnnotation(JT400Format.class);

		if (fieldformat.type() == AS400DataType.TYPE_STRUCTURE) {
			return ByteBufferBuilder.build(as400, obj);
		}

		final int len = JT400ExtFormatBuilder.getDataLength(fieldformat);

		if (len == 0) {
			return null;
		}

		final AS400DataType inst = JT400ExtBinaryConverter.getDataInstance(as400, fieldformat, field);

		// format not supported
		if (inst == null) {
			return null;
		}

		final ByteBuffer buffer = ByteBuffer.allocate(len);
		final Object javaValue = JT400ExtUtil.getField(field, obj);
		final byte [] raw = inst.toBytes(javaValue);
		buffer.put(raw, 0, len);
		buffer.rewind();
		return buffer;

	}

}
