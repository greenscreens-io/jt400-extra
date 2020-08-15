/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Argument;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Response builder populate JT400 ProgramParameter of type output
 * with received values.
 */
enum JT400ExtResponseBuilder {
;
	enum TYPE {UNKNOWN, DEFAULT, BUFFER, BYTE_ARRAY}

	/**
	 * Fill output program parameters with received values
	 * @param programCall
	 * @param params
	 * @param format
	 * @param args
	 * @return
	 * @throws Exception
	 */
	final static public <K extends IJT400Params> void build(final ProgramCall programCall, final K params, final Class<K> args) throws Exception {

		final ProgramParameter[] argList = programCall.getParameterList();
		final Field[] fields = args.getDeclaredFields();

		if (argList == null) return;

		Output output = null;
		Id arg = null;

		for (Field field : fields) {
			arg = field.getAnnotation(Id.class);
			output = field.getAnnotation(Output.class);
			if (arg == null) continue;
			if (output == null) continue;
			build(programCall, params, args, field);

		}

	}

	/**
	 * Fill output service program parameters with received values
	 *
	 * @param programCall
	 * @param params
	 * @param args
	 * @throws Exception
	 */
	final static public <K extends IJT400Params> void build(final ServiceProgramCall programCall, final K params, final Class<K> args) throws Exception {

		if (programCall.getReturnValueFormat() == ServiceProgramCall.RETURN_INTEGER) {
			final Field retVal = args.getField("returnValue");
			if (retVal != null) {
				if (retVal.getType() == int.class) {
					final int val = programCall.getIntegerReturnValue();
					JT400ExtUtil.setField(retVal, params, val);
				}
			}
		}

		build((ProgramCall) programCall, params, args);
	}

	/**
	 *
	 * @param programCall
	 * @param params
	 * @param args
	 * @param field
	 * @throws Exception
	 */
	final static public <K extends IJT400Params> void build(final ProgramCall programCall, final K params, final Class<K> args, final Field field) throws Exception {

		final JT400Argument jt400Arg = field.getAnnotation(JT400Argument.class);
		final Id id = field.getAnnotation(Id.class);
		final ProgramParameter[] argList = programCall.getParameterList();

		if (argList.length < id.value()) {
			throw new RuntimeException("Argument length invalid");
		}

		final byte[] response = argList[id.value()].getOutputData();

		if (response == null) return;

		field.setAccessible(true);

		final TYPE type = getFieldType(field);
		Object value = null;

		switch (type) {
			case BUFFER:
				final ByteBuffer data = (ByteBuffer) field.get(params);
				if (data == null) {
					value = ByteBuffer.wrap(response);
				} else {
					data.rewind();
					data.put(response);
				}
				break;
			case BYTE_ARRAY:
				value = response;
				break;
			case DEFAULT:
				value = getValue(programCall.getSystem(), field, jt400Arg, response);
				break;
			default:

		}

		JT400ExtUtil.setField(field, params, value);
	}

	/**
	 * Field output format
	 * @param field
	 * @return
	 */
	final static private TYPE getFieldType(final Field field) {

		if (field.getType() == ByteBuffer.class) {
			return TYPE.BUFFER;
		}

		if (field.getType() == byte[].class) {
			return TYPE.BYTE_ARRAY;
		}

		if (!field.getType().isArray()) {
			return TYPE.DEFAULT;
		}

		return TYPE.UNKNOWN;

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
	final static private Object getValue(final AS400 as400, final Field field, final JT400Argument arg, final byte[] tmp)  throws Exception  {

		Object value = null;

		final int type = arg.type();

		switch (type) {
		case AS400DataType.TYPE_BIN1:
			value = JT400ExtBinaryConverter.asAS400Bin1(as400, field, tmp);
			break;
		case AS400DataType.TYPE_BIN2:
			value = JT400ExtBinaryConverter.asAS400Bin2(as400, field, tmp);
			break;
		case AS400DataType.TYPE_BIN4:
			value = JT400ExtBinaryConverter.asAS400Bin4(as400, field, tmp);
			break;
		case AS400DataType.TYPE_BIN8:
			value = JT400ExtBinaryConverter.asAS400Bin8(as400, field, tmp);
			break;

		case AS400DataType.TYPE_UBIN1:
			value = JT400ExtBinaryConverter.asAS400UBin1(as400, field, tmp);
			break;
		case AS400DataType.TYPE_UBIN2:
			value = JT400ExtBinaryConverter.asAS400UBin2(as400, field, tmp);
			break;
		case AS400DataType.TYPE_UBIN4:
			value = JT400ExtBinaryConverter.asAS400UBin4(as400, field, tmp);
			break;
		case AS400DataType.TYPE_UBIN8:
			value = JT400ExtBinaryConverter.asAS400UBin8(as400, field, tmp);
			break;

		case AS400DataType.TYPE_FLOAT4:
			value = JT400ExtBinaryConverter.asAS400Float4(as400, field, tmp);
			break;
		case AS400DataType.TYPE_FLOAT8:
			value = JT400ExtBinaryConverter.asAS400Float8(as400, field, tmp);
			break;

		case AS400DataType.TYPE_DECFLOAT:
			value = JT400ExtBinaryConverter.asAS400DecFloat(as400, field, arg.decimals(), tmp);
			break;

		case AS400DataType.TYPE_ZONED:
			value = JT400ExtBinaryConverter.asAS400ZonedDecimal(as400, field, arg.length(), arg.decimals(), tmp);
			break;
		case AS400DataType.TYPE_PACKED:
			value = JT400ExtBinaryConverter.asAS400PackedDecimal(as400, field, arg.length(), arg.decimals(), tmp);
			break;

		case AS400DataType.TYPE_DATE:
			value = JT400ExtBinaryConverter.asAS400Date(as400, field, arg.format(), tmp);
			break;
		case AS400DataType.TYPE_TIME:
			value = JT400ExtBinaryConverter.asAS400Time(as400, field, arg.format(), tmp);
			break;
		case AS400DataType.TYPE_TIMESTAMP:
			value = JT400ExtBinaryConverter.asAS400Timestamp(as400, field, tmp);
			break;

		case AS400DataType.TYPE_BYTE_ARRAY:

			if (field.getType() == byte.class) {
				value = tmp;
			} else if (field.getType() == ByteBuffer.class) {
				value = ByteBuffer.wrap(tmp);
			}
			break;

		case AS400DataType.TYPE_TEXT:
			value = JT400ExtBinaryConverter.asAS400Text(as400, field, arg.length(), tmp);
			break;

		default:
			if (field.getType() == String.class) {
				value = JT400ExtBinaryConverter.asAS400Text(as400, field, arg.length(), tmp);
			}
			break;
		}

		return value;
	}

}
