/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Format;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Proxy class to generate JT400 call for given definitions
 */
final class JT400ExtInvocationHandler<P extends IJT400Params> implements InvocationHandler {

	private final AS400 as400;
	private final QSYSObjectPathName programName;
	private Class<P> input;

	/**
	 * Constructor for proxy handler
	 * @param as400
	 * @param input
	 * @param output
	 * @param library
	 * @param program
	 */
	public JT400ExtInvocationHandler(final AS400 as400, final Class<P> input, final QSYSObjectPathName qsysPath) {
		this.as400 = as400;
		this.input = input;
		this.programName = qsysPath;
	}

	/**
	 * Invocation handler caller
	 */
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

		if (!"call".equals(method.getName())) {
			throw new RuntimeException("Invalid method call");
		}

		final AS400 system = getSystem(args);
		final P values = getValues(args);
		final Class<P> params = getParams(values);

		call(system, values, params);

		final boolean useFormat = isMethodRetrunFormat(method);
		
		if (useFormat) {
			final int index = getFormatID(method);
			final ByteBuffer data = getOutput(values, params, index);
			final Class<? extends IJT400Format> clazz = getFormat(args);
			return JT400ExtFormatBuilder.build(as400, clazz, data);
		}

		return null;
	}

	/**
	 * Check if method call is void or JT400Format
	 *
	 * @param method
	 * @return
	 */
	private boolean isMethodRetrunFormat(final Method method) {
		return 	method.getReturnType() == IJT400Format.class;
	}

	/**
	 * Return response format parameter data 
	 * @param method
	 * @return
	 */
	private int getFormatID(final Method method) {
		final Id id = method.getReturnType().getAnnotation(Id.class);
		if (id != null) return id.value();
		return -1;
	}
	
	/**
	 * Detect which system to use
	 *
	 * @param args
	 * @return
	 */
	private AS400 getSystem(final Object[] args) {

		if (args.length == 0) return as400;

		if (isSystem(args[0])) {
			return (AS400) args[0];
		}

		return  as400;
	}

	/**
	 * Detect which argument is program params
	 *
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private P getValues(final Object[] args) {

		if (args.length == 0) return null;

		if (isSystem(args[0])) {
			return (P) args[1];
		}

		return (P) args[0];

	}

	/**
	 * Check if object of type AS400
	 *
	 * @param arg
	 * @return
	 */
	private boolean isSystem(final Object arg) {
		return arg.getClass() == AS400.class;
	}

	/**
	 * Return class type format
	 *
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IJT400Format> getFormat(final Object[] args) {
		return (Class<? extends IJT400Format>) args[args.length-1];
	}

	/**
	 * Get params class definition
	 *
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<P> getParams(final P values) {

		if (values != null) {
			return (Class<P>) values.getClass();
		}

		return input;
	}

	/**
	 * Find output ByteBuffer program receiver.
	 * Used only if caller returns automatically converted format
	 *
	 * @param params
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private ByteBuffer getOutput(final P params, final Class<P> args, final int index) throws Exception {

		// of not plain POJO, might require recursion for parent classes
		final Field[] fields = args.getDeclaredFields();

		for (Field field : fields) {
			
			if (!isOutputField(field)) continue; 
			
			final Id id = field.getAnnotation(Id.class);
			JT400ExtUtil.enableField(field);
			
			if (index < 0 || index == id.value()) {
				return (ByteBuffer) field.get(params);
			}
			
		}

		return null;
	}

	private boolean isOutputField(final Field field) {
		if (!field.isAnnotationPresent(Output.class)) return false; 
		if (!field.isAnnotationPresent(Id.class)) return false;
		if (field.getType() != ByteBuffer.class) return false;
		return true;
	}
	
	/**
	 * Make a call to remote program by converting Java class definition into JT400 call and
	 * converting byte response into Java class based on JT400 definition format
	 *
	 * @param as400
	 * @param params
	 * @param format
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private void call(final AS400 as400, final P params, final Class<P> args) throws Exception {

		if (as400 == null || args == null) {
			throw new RuntimeException("Not all definitions available!");
		}

		final JT400Program jt400PGM = args.getAnnotation(JT400Program.class);
		final ProgramParameter[] parameters = JT400ExtParameterBuilder.build(as400, params, args);
		final ProgramCall programCall = JT400ExtUtil.call(as400, programName.getPath(), parameters, jt400PGM);

		JT400ExtResponseBuilder.build(programCall, params, args);			

	}

}
