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
import java.util.Date;

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
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Time;
import com.ibm.as400.access.AS400Timestamp;
import com.ibm.as400.access.AS400UnsignedBin1;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.AS400UnsignedBin8;
import com.ibm.as400.access.AS400ZonedDecimal;
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

	private static final Logger LOG = LoggerFactory.getLogger(JT400ExtParameterBuilder.class);

	/**
	 *
	 * @param as400
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <K extends IJT400Params> ProgramParameter[] build(final AS400 as400, final K obj) {
		final Class<K> type = (Class<K>) obj.getClass();
		return build(as400, obj, type);
	}

	/**
	 * Convert JT400Params annotated class definition into JT400 ProrgamParameter list
	 *
	 * @param as400
	 * @param obj
	 * @param type
	 * @return
	 */
	public static  <K extends IJT400Params> ProgramParameter[] build(final AS400 as400, final K obj, final Class<K> type) {

		final int size = JT400ExtParameterBuilder.count(type);
		final Field[] fields = type.getDeclaredFields();

		final ProgramParameter [] list = new ProgramParameter[size];

		Id id = null;

		for (Field field : fields) {

			id = field.getAnnotation(Id.class);

			if (id == null) continue;

			final ProgramParameter parm = build(as400, obj, field);
			if (parm != null) {
				list[id.value()] = parm;
			}
		}

		return list;
	}

	/**
	 * Build a single parameter definition based on provided param descriptor class
	 *
	 * @param as400
	 * @param obj
	 * @param field
	 * @param ann
	 * @return
	 */
	private static  <K extends IJT400Params> ProgramParameter build(final AS400 as400, final K obj, final Field field) {

		ProgramParameter parameter = null;

		try {
			parameter = buildAnnotated(as400, obj, field);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(), e);
		}

		return parameter;
	}

	/**
	 *
	 * @param as400
	 * @param obj
	 * @param field
	 * @return
	 * @throws Exception
	 */
	private static  <K extends IJT400Params> ProgramParameter buildAnnotated(final AS400 as400, final K obj, final Field field) throws Exception {

		final JT400Argument ann = field.getAnnotation(JT400Argument.class);
		if (ann == null) return null;

		final Input input = field.getAnnotation(Input.class);
		final Output output = field.getAnnotation(Output.class);
		final Class<?> clazz = field.getType();
		
		Object val = null;
		AS400DataType dataType = null;

		switch (ann.type()) {
		case AS400DataType.TYPE_BIN1:
			final byte val1 = getFieldValue(obj, field, (byte) 0);
			val = val1;
			dataType = new AS400Bin1();
			break;
		case AS400DataType.TYPE_BIN2:
			final short val2 = getFieldValue(obj, field, (short) 0);
			val = val2;
			dataType = new AS400Bin2();
			break;
		case AS400DataType.TYPE_BIN4:
			final int val4 = getFieldValue(obj, field, 0);
			val = val4;
			dataType = new AS400Bin4();
			break;
		case AS400DataType.TYPE_BIN8:
			final long val8 = getFieldValue(obj, field, (long) 0);
			val = val8;
			dataType = new AS400Bin8();
			break;
		case AS400DataType.TYPE_UBIN1:
			final byte uval1 = getFieldValue(obj, field, (byte) 0);
			dataType = new AS400UnsignedBin1();
			val = uval1;
			break;
		case AS400DataType.TYPE_UBIN2:
			final short uval2 = getFieldValue(obj, field, (short) 0);
			val = uval2;
			dataType = new AS400UnsignedBin2();
			break;
		case AS400DataType.TYPE_UBIN4:
			final int uval4 = getFieldValue(obj, field, 0);
			val = uval4;
			dataType = new AS400UnsignedBin4();
			break;
		case AS400DataType.TYPE_UBIN8:
			final long uval8 = getFieldValue(obj, field, (long) 0);
			val = uval8;
			dataType = new AS400UnsignedBin8();
			break;
		case AS400DataType.TYPE_FLOAT4:
			final float float4 = getFieldValue(obj, field, (float) 0);
			val = float4;
			dataType = new AS400Float4();
			break;
		case AS400DataType.TYPE_FLOAT8:
			final double double8 = getFieldValue(obj, field, (float) 0);
			val = double8;
			dataType = new AS400Float8();
			break;
		case AS400DataType.TYPE_ZONED:
			final BigDecimal decZ = getFieldValue(obj, field, new BigDecimal(0));
			val = decZ;
			dataType = new AS400ZonedDecimal(ann.length(), ann.decimals());
			break;
		case AS400DataType.TYPE_PACKED:
			final BigDecimal decP = getFieldValue(obj, field, new BigDecimal(0));
			val = decP;
			dataType = new AS400PackedDecimal(ann.length(), ann.decimals());
			break;
		case AS400DataType.TYPE_DATE:
			final Date date = getFieldValue(obj, field, null);
			val = date;
			dataType = new AS400Date();
			break;
		case AS400DataType.TYPE_TIME:
			final Date time = getFieldValue(obj, field, null);
			val = time;
			dataType = new AS400Time();
			break;
		case AS400DataType.TYPE_TIMESTAMP:
			final Date timestamp = getFieldValue(obj, field, null);
			val = timestamp;
			dataType = new AS400Timestamp();
			break;
		case AS400DataType.TYPE_BYTE_ARRAY:

			int lena = ann.length();
			byte [] tmp = null;
			
			if (clazz == ByteBuffer.class) {
				final ByteBuffer data = getFieldValue(obj, field);
				if (data != null) tmp = data.array();
			} else if (clazz == byte[].class) {
				tmp = getFieldValue(obj, field);
			} else {
				tmp = new byte[lena];		
			}
			
			val = tmp;
			if (tmp != null) lena = tmp.length;
			if (val == null) val = new byte[lena];

			dataType = new AS400ByteArray(lena);
			break;
		case AS400DataType.TYPE_TEXT:
			int len = ann.length();
			final String txt = getFieldValue(obj, field, "");
			if (txt != null && len == 0) len = txt.length();
			val = txt;
			dataType = new AS400Text(len, as400);
			break;

		default:
			break;
		}

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

	/**
	 * Detect number of arguments. First try to take from annotation, if not found,
	 * try to detect size by last order value on field annotation
	 *
	 * @param type
	 * @return
	 */
	private static  <K extends IJT400Params> int count(final Class<K> type) {

		final JT400Program pgm = type.getAnnotation(JT400Program.class);

		if (pgm != null) return pgm.arguments();

		int order = 0;
		Id id = null;

		final Field[] fields = type.getFields();

		for (Field field : fields) {

			id = field.getAnnotation(Id.class);

			if (id != null) {
				order = Math.max(order, id.value());
			}

		}

		return order;
	}

	/**
	 * Generic field get value
	 *
	 * @param obj
	 * @param field
	 * @param def
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static  <T> T getFieldValue(final Object obj, final Field field, final T def) {

		if (obj == null) return def;

		try {
			JT400ExtUtil.enableField(field);
			T val = (T) field.get(obj);
			if (val != null) return val;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(), e);
		}

		return def;
	}

	/**
	 *
	 *
	 * @param obj
	 * @param field
	 * @return
	 */
	private static  <T> T getFieldValue(final Object obj, final Field field) {
		return getFieldValue(obj, field, null);
	}

}
