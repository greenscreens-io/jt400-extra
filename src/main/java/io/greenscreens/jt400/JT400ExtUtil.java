/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin1;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Date;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Time;
import com.ibm.as400.access.AS400Timestamp;
import com.ibm.as400.access.AS400UnsignedBin1;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.AS400UnsignedBin8;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.ServiceProgramCall;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.JT400Ref;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Format;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Helper JT400 utility methods
 */
public enum JT400ExtUtil {
	;

	private static final Logger LOG = LoggerFactory.getLogger(JT400ExtUtil.class);

	private static final ConcurrentHashMap<Class<?>, List<Field>> ALL_FIELDS = new ConcurrentHashMap<Class<?>, List<Field>>();
	private static final ConcurrentHashMap<Class<?>, List<Field>> OUT_FIELDS = new ConcurrentHashMap<Class<?>, List<Field>>();
	private static final ConcurrentHashMap<Class<?>, List<Field>> FORMAT_FIELDS = new ConcurrentHashMap<Class<?>, List<Field>>();

	private static final ConcurrentHashMap<Field, VarHandle> HANDLE_CACHE = new ConcurrentHashMap<>();

	/**
	 * Convert Field to calling VarHandle, optimization vs reflection
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static VarHandle fieldToHandle(final Field field) throws Exception {
		return HANDLE_CACHE.computeIfAbsent(field, f -> {
			try {
				enableField(f);

				final MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(
						f.getDeclaringClass(),
						MethodHandles.lookup()
						);

				return privateLookup.unreflectVarHandle(f);
			} catch (final Exception e) {
				throw new RuntimeException("Failed to create VarHandle for field: " + f.getName(), e);
			}
		});

	}

	/**
	 * Get class fields annotated with @Id
	 * @param type
	 * @return
	 */
	public static List<Field> getFields(final Class<?> type) {
		return ALL_FIELDS.computeIfAbsent(type, k -> JT400ExtUtil.getAllFields(type).stream()
				.filter(f -> f.isAnnotationPresent(Id.class))
				.map(JT400ExtUtil::enableField)
				.collect(Collectors.toList()));

	}

	/**
	 * Get all class fields annotated with @Output
	 * @param type
	 * @return
	 */
	public static List<Field> getOutputFields(final Class<?> type) {
		return OUT_FIELDS.computeIfAbsent(type, k -> getFields(type).stream()
				.filter(f -> f.isAnnotationPresent(Output.class))
				.collect(Collectors.toList()));
	}

	/**
	 * Get all class fields annotated with @JT400Format
	 * @param type
	 * @return
	 */
	public static List<Field> getFormatFields(final Class<?> type) {
		return FORMAT_FIELDS.computeIfAbsent(type, k -> JT400ExtUtil.getAllFields(type).stream()
				.filter(f -> f.isAnnotationPresent(JT400Format.class))
				.collect(Collectors.toList()));
	}


	/**
	 * Create JT400 program call path
	 *
	 * @param program
	 * @param library
	 * @return
	 */
	public static  QSYSObjectPathName toQSYSPath(final String program, final String library, final boolean service) {
		final String pgm = Optional.of(program).get().toUpperCase().trim();
		final String lib = Optional.ofNullable(library).orElse("QSYS").toUpperCase().trim();
		return new QSYSObjectPathName(lib, pgm, service ? "SRVPGM" : "PGM");
	}

	/**
	 * Get class of type IJT400Format from field
	 * @param <T>
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T extends IJT400Format> Class<T> toClass(final T obj) {
		return (Class<T>) obj.getClass();
	}

	/**
	 * Get value from field
	 * @param <T>
	 * @param field
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	static <T extends IJT400Format> Object getField(final Field field, final T obj) throws Exception {

		final JT400Ref fieldRef = field.getAnnotation(JT400Ref.class);
		final boolean isArray = field.getType().isArray();

		if (!isArray && Objects.nonNull(fieldRef)) {
			final Field arrField = getRefField(field, obj);
			if (Objects.nonNull(arrField)) {
				final Object o = JT400ExtUtil.getFieldValue(arrField, obj);
				if (Objects.nonNull(o)) {
					final int size = getArrayFieldLength(arrField, obj);
					JT400ExtUtil.setField(field, obj, size);
					return size;
				}
			}
		}

		Object val = JT400ExtUtil.getFieldValue(field, obj);
		if (Objects.isNull(val)) {
			val = field.getType().getDeclaredConstructor().newInstance();
		}
		return val;
	}

	static <T extends IJT400Format> int getArrayFieldLength(final Field field, final T obj) throws Exception {
		int size = 0;
		if (Objects.nonNull(field)) {
			final Object o = JT400ExtUtil.getFieldValue(field, obj);
			if (Objects.nonNull(o)) {
				if (field.getType().isArray()) {
					size = Array.getLength(o);
				} else if (o instanceof Collection<?>) {
					size = ((Collection<?>) o).size();
				}
			}
		}
		return size;
	}

	/**
	 * Convert list of structures into a bytearray
	 * @param system
	 * @param formats
	 * @return
	 * @throws Exception
	 */
	public static <T extends IJT400Format> ByteBuffer toBytes(final AS400 system,  final Collection<T> formats) throws Exception {
		return toBytes(system, formats, 0);
	}

	public static <T extends IJT400Format> ByteBuffer toBytes(final AS400 system,  final Collection<T> formats, final int offset) throws Exception {
		if (Objects.isNull(formats) || formats.size() == 0) {
			return ByteBuffer.allocate(offset);
		}

		final Set<ByteBuffer> rawFormtas = new HashSet<>();
		for (final IJT400Format format : formats) {
			rawFormtas.add(format.toBytes(system));
		}

		final int length = offset + rawFormtas.stream().mapToInt(ByteBuffer::capacity).sum();
		final ByteBuffer buffer = ByteBuffer.allocate(length).position(offset);

		for (final ByteBuffer format : rawFormtas) {
			buffer.put(format);
		}

		buffer.rewind();
		return buffer;

	}

	/**
	 * Return referenced array field
	 * @param <T>
	 * @param field
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	static <T extends IJT400Format> Field getRefField(final Field field, final T obj) throws Exception {

		final JT400Ref fieldRef = field.getAnnotation(JT400Ref.class);

		return Arrays.asList(obj.getClass().getDeclaredFields())
				.stream().filter(f -> f.isAnnotationPresent(Id.class))
				.filter(f -> f.getAnnotation(Id.class).value() == fieldRef.length())
				.findFirst().orElse(null);
	}

	/**
	 * Use reflection to set value to object field
	 * @param field
	 * @param obj
	 * @param value
	 * @throws Exception
	 */
	public static void setField(final Field field, final Object obj, final Object value) throws Exception {
		/*
        enableField(field);
		field.set(obj, value);
		 */
		if (obj == null) {
			return;
		}

		final VarHandle handle = fieldToHandle(field);
		if (handle != null) {
			handle.set(obj, value);
		}
	}

	/**
	 * Use reflection to get value to object field
	 * @param field
	 * @param obj
	 * @param value
	 * @throws Exception
	 */
	public static <T> T getFieldValue(final Field field, final Object obj, final T def) throws Exception {

		final VarHandle handle = fieldToHandle(field);

		/* Will always throw for exact call
        if (handle != null) {
            try {
                // 1. Use getExact to avoid Object[] array allocation on the heap.
                // 2. Cast the result directly to match the expected return type T.
                final T val = (T) handle.withInvokeExactBehavior().get(obj);

                if (val != null) {
                    return val;
                }
            } catch (WrongMethodTypeException e) {
                // This happens if the generic type T doesn't exactly match the field type at runtime
                LOG.error("Type mismatch using getExact: " + e.getMessage());
                LOG.debug(e.getMessage(), e);

                // Fallback gracefully to slower .get() if types weren't a perfect match
                try {
                    final T fallbackVal = (T) handle.get(obj);
                    if (fallbackVal != null) {
						return fallbackVal;
					}
                } catch (Throwable ex) {
                    LOG.error(ex.getMessage(), ex);
                }
            } catch (Throwable t) {
                // VarHandle signatures throw Throwable, catching specific runtime errors is cleaner
                LOG.error(t.getMessage());
                LOG.debug(t.getMessage(), t);
            }
        }
		 */

		if (handle != null) {
			try {
				final T val = (T) handle.get(obj);
				if (val != null) {
					return val;
				}
			} catch (final Throwable t) {
				LOG.error(t.getMessage());
				LOG.debug(t.getMessage(), t);
			}
		}

		return def;
	}

	/**
	 * etermine field class type supporting arrays
	 * @param field
	 * @return
	 */
	public static Class<?> getFieldType(final Field field) {
		return field.getType().isArray() ? field.getType().getComponentType() : field.getType();
	}

	public static <T> T getFieldValue(final Field field, final Object obj) throws Exception {
		return getFieldValue(field, obj, null);
	}

	/**
	 * Use reflection to set private field accessible
	 * @param field
	 */
	public static  Field enableField(final Field field) {
		final int modifiers = field.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			return field;
		}
		field.trySetAccessible();
		return field;
	}

	/**
	 * List all classes constructing format
	 * @param map
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T extends IJT400Format> List<Class<T>> getAllClass(final List<Class<T>> list, final Class<T> clazz) {

		if (clazz.getSuperclass() != null) {
			getAllClass(list, (Class<T>) clazz.getSuperclass());
		}

		list.add(clazz);

		return list;
	}

	public static  <T extends IJT400Format> List<Class<T>> getAllClass(final Class<T> clazz) {
		return getAllClass(new ArrayList<Class<T>>(), clazz);
	}

	public static List<Field> getAllFields(final Class<?> type) {
		return getAllFields(new ArrayList<Field>(), type);
	}

	public static List<Field> getAllFields(final List<Field> fields, final Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	/**
	 * Generic JT400 program call
	 * @param as400
	 * @param program
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static  ProgramCall call(final AS400 as400, final String program, final ProgramParameter[] parameters, final JT400Program ann) throws Exception {

		if (ann.service()) {
			return callService(as400, program, parameters, ann);
		} else {
			return callProgram(as400, program, parameters, ann);
		}
	}

	public static ProgramCall callProgram(final AS400 as400, final String program, final ProgramParameter[] parameters, final JT400Program ann) throws Exception {

		if (as400 == null) {
			throw new RuntimeException("Not all definitions available!");
		}

		final ProgramCall programCall = new ProgramCall(as400);
		programCall.setProgram(program, parameters);
		programCall.setThreadSafe(ann.threadSafe());

		if (ann.timeout() > -1) {
			programCall.setTimeOut(ann.timeout());
		}

		if (!programCall.run()) {
			throw new JT400Exception(programCall.getMessageList());
		}

		return programCall;
	}

	/**
	 * Generic JT400 ServiceProgramCall
	 * @param as400
	 * @param program
	 * @param parameters
	 * @param ann
	 * @return
	 * @throws Exception
	 */
	public static  ServiceProgramCall callService(final AS400 as400, final String program, final ProgramParameter[] parameters, final JT400Program ann) throws Exception {

		if (as400 == null) {
			throw new RuntimeException("Not all definitions available!");
		}

		final ServiceProgramCall programCall = new ServiceProgramCall(as400);
		programCall.setProgram(program, parameters);
		programCall.setThreadSafe(ann.threadSafe());
		programCall.setProcedureName(ann.procedure());
		programCall.setReturnValueFormat(ann.returnFormat());

		if (ann.timeout() > -1) {
			programCall.setTimeOut(ann.timeout());
		}

		if (!programCall.run()) {
			throw new JT400Exception(programCall.getMessageList());
		}

		return programCall;
	}

	/**
	 * Get length of provided data format
	 * @param <T>
	 * @param format
	 * @return
	 */
	public static  <T extends IJT400Format> int getFormatLength(final Class<T> format) {
		final JT400Format fmt = format.getAnnotation(JT400Format.class);
		if (fmt == null) {
			return 0;
		}
		return fmt.length();
	}

	public static  <T extends IJT400Format> String getFormatName(final Class<T> format) {
		final JT400Format fmt = format.getAnnotation(JT400Format.class);
		if (fmt == null) {
			return format.getSimpleName();
		}
		return fmt.name();
	}

	public static  <T extends IJT400Format> String getFormatName(final T format) {
		return getFormatName(format.getClass());
	}

	/**
	 * Generic byte array extraction from {ByteBuffer} instance
	 * @param buffer
	 * @param position
	 * @param len
	 * @return
	 */
	public static  byte[] getBytesFrom(final ByteBuffer buffer, final int position, final int len) {
		final byte [] data = new byte[len];
		buffer.rewind();
		buffer.position(position);
		buffer.get(data);
		return data;
	}

	/**
	 * Check if defined JT400Params contains requested supported dat output format
	 *
	 * @param format
	 * @param params
	 * @return
	 */
	public static  <T extends IJT400Format, K extends IJT400Params> boolean contains(final Class<T> format, final Class<K> params) {

		final Class<? extends IJT400Format>[] formats = params.getAnnotation(JT400Program.class).formats();

		for (final Class<? extends IJT400Format> item : formats) {
			if (item == format) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Print JT400 errors to defined logger
	 * @param messages
	 */
	public static  void printErrors(final AS400Message [] messages, final Logger logger) {
		for (final AS400Message message : messages) {
			logger.warn(" {} : {}", message.getID(), message.getText());
		}
	}

	/**
	 * Print JT400 errors to defined output stream
	 * @param messages
	 */
	public static  void printErrors(final AS400Message [] messages, final OutputStream stream) {

		try {
			for (final AS400Message message : messages) {
				final byte [] bytes = String.format(" %s : %s%n", message.getID(), message.getText()).getBytes();
				stream.write(bytes);
			}
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(), e);
		}
	}

	/**
	 * Print JT400 errors to defined writer
	 * @param messages
	 */
	public static  void printErrors(final AS400Message [] messages, final Writer writer) {

		try {
			for (final AS400Message message : messages) {
				final String line = String.format(" %s : %s%n", message.getID(), message.getText());
				writer.write(line);
			}
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(), e);
		}
	}

	/**
	 * Print JT400 errors to default console output
	 * @param messages
	 */
	public static void printErrors(final AS400Message [] messages) {
		printErrors(messages, System.err);
	}

	/**
	 * Convert JT400 errors into string
	 * @param messages
	 * @return
	 */
	public static  String getErrors(final AS400Message [] messages) {
		final StringWriter writer = new StringWriter();
		printErrors(messages, writer);
		writer.flush();
		return writer.toString();
	}

	private static  final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes();

	/**
	 * Convert ByteBuffer to hex string
	 * @param buffer
	 * @return
	 */
	public static String bytesToHex(final ByteBuffer buffer) {
		return buffer == null ? "" : bytesToHex(buffer.array());
	}

	/**
	 * Convert bytes to hex string
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(final byte[] bytes) {

		if (bytes == null) {
			return "";
		}

		final byte[] hexChars = new byte[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}

		return new String(hexChars, StandardCharsets.UTF_8);
	}

	/**
	 * Convert hex string to ByteBuffer
	 * @param s
	 * @return
	 */
	public static  ByteBuffer hexToBuffer(final String s) {
		return ByteBuffer.wrap(hexToBytes(s));
	}

	/**
	 * Convert hex string to byte array
	 * @param s
	 * @return
	 */
	public static  byte[] hexToBytes(final String s) {

		final int len = s.length();

		final byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}

		return data;
	}

	public static String pad(final String object, final String lib) {
		return pad(object) + pad(lib);
	}

	public static String pad(final String val) {
		return padRight(val, 10);
	}

	public static String padRight(final String s, final int n) {
		return String.format("%-" + n + "s", s);
	}

	public static String padLeft(final String s, final int n) {
		return String.format("%" + n + "s", s);
	}

	public static AS400DataType toAS400DataType(final AS400 as400, final Field field, final int type, final int length, final int decimal) {

		switch (type) {
		case AS400DataType.TYPE_TEXT:
			return new AS400Text(length, as400);

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

		case AS400DataType.TYPE_ZONED:
			return new AS400ZonedDecimal(length, decimal);
		case AS400DataType.TYPE_PACKED:
			return new AS400PackedDecimal(length, decimal);
		case AS400DataType.TYPE_BYTE_ARRAY:
			return new AS400ByteArray(length);
		default:
			if (String.class.isAssignableFrom(field.getType())) {
				return new AS400Text(length, as400);
			}
			return null;
		}

	}


	public static String toMessage(final Throwable e) {
		return toMessage(e, "");
	}

	public static String toMessage(final Throwable e, final String def) {

		/*
		if (Environment.isPrintStack()) {
			Optional.ofNullable(e).ifPresent(Throwable::printStackTrace);
		}
		 */

		final String rawMsg = Optional.ofNullable(e)
				.map(Throwable::getMessage)
				.or(() -> Optional.ofNullable(e)
						.map(Throwable::getCause)
						.map(Throwable::getMessage))
				.orElse(def);

		final String msg = rawMsg == null ? "" : rawMsg.trim();

		final String finalMsg = (e instanceof NoClassDefFoundError)
				? String.format("Class not found : %s", msg)
						: msg;

		return Optional.ofNullable(e)
				.map(Throwable::getClass)
				.map(Class::getCanonicalName)
				.map(className -> String.format("%s >>> %s", className, finalMsg))
				.orElse(finalMsg);
	}

	public static String _toMessage(final Throwable e, final String def) {

		String err = def == null ? "" : def.trim();

		if (Objects.nonNull(e)) {

			/*
			if (Environment.isPrintStack()) {
				e.printStackTrace();
			}
			 */

			err = e.getMessage();
			if (Objects.isNull(err) && Objects.nonNull(e.getCause())) {
				err = e.getCause().getMessage();
			}

			if (Objects.isNull(err)) {
				err = def;
			}

			if (e instanceof NoClassDefFoundError) {
				err = String.format("Class not found : %s", err);
			}

		}

		return String.format("%s >>> %s", e.getClass().getCanonicalName(), err);
	}


}
