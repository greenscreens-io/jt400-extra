/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.ProgramParameter;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Input;
import io.greenscreens.jt400.annotations.JT400Argument;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Builder to generate ProgramCall parameters from
 * defined annotated Java class structure
 */
enum JT400ExtParameterBuilder {
	;

	/**
	 *
	 * @param as400
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <K extends IJT400Params> ProgramParameter[] build(final AS400 as400, final K obj) throws Exception {
		return build(as400, obj, (Class<K>) obj.getClass());
	}

	/**
	 * Convert JT400Params annotated class definition into JT400 ProrgamParameter list
	 *
	 * @param as400
	 * @param obj
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static <K extends IJT400Params> ProgramParameter[] build(final AS400 as400, final K obj, final Class<K> type) throws Exception {

		final int size = JT400ExtParameterBuilder.count(type);

		final List<Field> fields = JT400ExtUtil.getFields(type);

		final ProgramParameter [] list = new ProgramParameter[size == 0 ? fields.size() : size];

		for (final Field field : fields) {

			final ProgramParameter parm = build(as400, obj, field);
			if (parm != null) {
				list[field.getAnnotation(Id.class).value()] = parm;
			}

		}
		return list;
	}

	private static final byte BYTE_ZERO = 0;
	private static final short SHORT_ZERO = 0;

	/**
	 * Build a single parameter definition based on provided param descriptor class
	 *
	 * @param as400
	 * @param obj
	 * @param field
	 * @return
	 */
	private static <K extends IJT400Params> ProgramParameter build(final AS400 as400, final K obj, final Field field) throws Exception {

		final JT400Argument ann = field.getAnnotation(JT400Argument.class);
		if (ann == null) {
			return null;
		}

		final Input input = field.getAnnotation(Input.class);
		final Output output = field.getAnnotation(Output.class);
		final Class<?> clazz = field.getType();

		Object val = null;
		AS400DataType dataType = null;

		int length = ann.length();

		switch (ann.type()) {
		case AS400DataType.TYPE_BIN1:
			val = asByte(field, obj, BYTE_ZERO);
			break;
		case AS400DataType.TYPE_BIN2:
			val = asShort(field, obj, SHORT_ZERO);
			break;
		case AS400DataType.TYPE_BIN4:
			val = asInt(field, obj, 0);
			break;
		case AS400DataType.TYPE_BIN8:
			val = asLong(field, obj, 0l);
			break;
		case AS400DataType.TYPE_UBIN1:
			val = asByte(field, obj, BYTE_ZERO);
			break;
		case AS400DataType.TYPE_UBIN2:
			val = asShort(field, obj, SHORT_ZERO);
			break;
		case AS400DataType.TYPE_UBIN4:
			val = asInt(field, obj, 0);
			break;
		case AS400DataType.TYPE_UBIN8:
			val = asLong(field, obj, 0l);
			break;
		case AS400DataType.TYPE_FLOAT4:
			val = asFloat(field, obj, 0.0f);
			break;
		case AS400DataType.TYPE_FLOAT8:
			val = asDouble(field, obj, 0.0d);
			break;
		case AS400DataType.TYPE_ZONED:
			val = asBigDecimal(field, obj, new BigDecimal(0));
			break;
		case AS400DataType.TYPE_PACKED:
			val = asBigDecimal(field, obj, new BigDecimal(0));
			break;
		case AS400DataType.TYPE_DATE:
			val = asDate(field, obj, null);
			break;
		case AS400DataType.TYPE_TIME:
			val = asDate(field, obj, null);
			break;
		case AS400DataType.TYPE_TIMESTAMP:
			val = asDate(field, obj, null);
			break;
		case AS400DataType.TYPE_BYTE_ARRAY:

			byte [] tmp = null;

			if (clazz == ByteBuffer.class) {
				final ByteBuffer data = JT400ExtUtil.getFieldValue(field, obj);
				if (data != null) {
					tmp = data.array();
				}
			} else if (clazz == byte[].class) {
				tmp = JT400ExtUtil.getFieldValue(field, obj);
			} else {
				tmp = new byte[length];
			}

			val = tmp;
			if (tmp != null) {
				length = tmp.length;
			}
			if (val == null) {
				val = new byte[length];
			}

			break;
		case AS400DataType.TYPE_TEXT:
			final String txt = JT400ExtUtil.getFieldValue(field, obj, "");
			if (txt != null && length == 0) {
				length = txt.length();
			}
			val = txt;
			break;

		default:
			break;
		}

		dataType = JT400ExtUtil.toAS400DataType(as400, field, ann.type(), length, ann.decimals());

		if (dataType == null) {
			return null;
		}

		final boolean isInputData = input != null && val != null;
		final boolean isInputOnly = input != null && output == null;
		final boolean isOutputOnly = output != null && input == null;
		final boolean isInputOutput = output != null && input != null;

		final ProgramParameter parameter = new ProgramParameter();
		parameter.setParameterType(ann.pass());

		if (isOutputOnly) {
			parameter.setOutputDataLength(dataType.getByteLength());
		}

		if (isInputOnly && isInputData) {
			parameter.setInputData(dataType.toBytes(val));
		}

		if (isInputOutput) {
			parameter.setInputData(dataType.toBytes(val));
			parameter.setOutputDataLength(dataType.getByteLength());
		}

		return parameter;
	}

	static byte asByte(final Field field, final Object obj, final byte def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static short asShort(final Field field, final Object obj, final short def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static int asInt(final Field field, final Object obj, final int def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static long asLong(final Field field, final Object obj, final long def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static float asFloat(final Field field, final Object obj, final float def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static double asDouble(final Field field, final Object obj, final double def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static Date asDate(final Field field, final Object obj, final Date def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	static BigDecimal asBigDecimal(final Field field, final Object obj, final BigDecimal def) throws Exception {
		return JT400ExtUtil.getFieldValue(field, obj, def);
	}

	/**
	 * Detect number of arguments. First try to take from annotation, if not found,
	 * try to detect size by last order value on field annotation
	 *
	 * @param type
	 * @return
	 */
	private static <K extends IJT400Params> int count(final Class<K> type) {

		if (type.isAnnotationPresent(JT400Program.class)) {
			return type.getAnnotation(JT400Program.class).arguments();
		}

		return JT400ExtUtil.getFields(type)
				.stream()
				.map(field -> field.getAnnotation(Id.class))
				.mapToInt(Id::value)
				.max()
				.orElse(0);

	}

}
