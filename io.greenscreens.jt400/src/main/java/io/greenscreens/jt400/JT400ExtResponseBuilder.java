/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Response builder populate JT400 ProgramParameter of type output
 * with received values.
 */
enum JT400ExtResponseBuilder {
;

	/**
	 *
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
			break;
		}

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

		final Id id = field.getAnnotation(Id.class);
		// final JT400Argument ann = field.getAnnotation(JT400Argument.class);
		final ProgramParameter[] argList = programCall.getParameterList();

		if (argList.length < id.value()) {
			throw new RuntimeException("Argument length invalid");
		}

		final byte[] response = argList[id.value()].getOutputData();

		if (response == null) return;

		field.setAccessible(true);

		if (field.getType() == ByteBuffer.class) {
			final ByteBuffer data = (ByteBuffer) field.get(params);
			if (data == null) {
				field.set(params, ByteBuffer.wrap(response));
			} else {
				data.rewind();
				data.put(response);
			}
		}

		if (field.getType() == byte[].class) {
			field.set(params, response);
		}

	}

}
