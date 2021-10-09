package io.greenscreens.jt400;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
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
		
		classes.forEach(clazz -> {

	    	final Map<Integer, Field> fields = JT400ExtFormatBuilder.toFieldMap(clazz, TYPE.ALL);
			
			build(as400, obj, clazz, fields, bufferList);
		});

		return toBuffer(bufferList);
	}
	
	/**
	 * merge list of buffers into a single buffer
	 * @param bufferList
	 * @return
	 */
	static ByteBuffer toBuffer(final Set<ByteBuffer>  bufferList) {
		final int size = bufferList.stream().map((b) -> b.capacity()).reduce(0, (a, b) -> a + b).intValue();
		final ByteBuffer buffer = ByteBuffer.allocate(size);		
		bufferList.forEach(b -> buffer.put(b));
		buffer.rewind();		
		return buffer;
	}
	
	static ByteBuffer toBuffer(final Map<Integer, ByteBuffer> bufferList) {
		final int size = bufferList.values().stream().map((b) -> b.capacity()).reduce(0, (a, b) -> a + b).intValue();
		final ByteBuffer buffer = ByteBuffer.allocate(size);
		
		bufferList.keySet().stream().sorted().forEach(b -> buffer.put(bufferList.get(b)));
		buffer.rewind();		
		return buffer;
	}
	
	static <T extends IJT400Format> void build(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final Map<Integer, ByteBuffer> bufferList) {
		fields.forEach((k, field) -> {
			try {
				if (field.getType().isArray()) {
					setArray(as400, obj, clazz, fields, field, bufferList);
				} else {
					set(as400, obj, fields, field, bufferList);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected static <T extends IJT400Format> void setArray(final AS400 as400, final T obj, final Class<T> clazz, final Map<Integer, Field> fields, final Field field, final Map<Integer, ByteBuffer> bufferList)  throws Exception {

		final Id id = field.getAnnotation(Id.class);
		final JT400Format fieldFormat = field.getAnnotation(JT400Format.class);	
		final boolean isSubFormat = fieldFormat.type() == AS400DataType.TYPE_STRUCTURE;

		final Object value = field.get(obj);
		
		if (Objects.isNull(value)) return;
		
		final Set<ByteBuffer> buffers = new HashSet<>();
		
	    int length = Array.getLength(value);
	    for (int i = 0; i < length; i ++) {
	        Object arrayElement = Array.get(value, i);
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
		final Id id = field.getAnnotation(Id.class);
		final ByteBuffer buffer = set(as400, obj, fields, field);
		if (Objects.nonNull(buffer)) {
			bufferList.put(id.value(), buffer);			
		}		
	}
	
	private static <T extends IJT400Format> ByteBuffer set(final AS400 as400, final T obj, final Map<Integer, Field> fields, final Field field) throws Exception {

		final JT400Format fieldformat = field.getAnnotation(JT400Format.class);

		if (fieldformat.type() == AS400DataType.TYPE_STRUCTURE) {
			return ByteBufferBuilder.build(as400, obj);
		}

		final int len = JT400ExtFormatBuilder.getDataLength(fieldformat);

		if (len == 0) return null; 
		
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
