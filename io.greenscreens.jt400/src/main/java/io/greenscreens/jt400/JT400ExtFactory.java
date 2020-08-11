/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ProgramParameter;

import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.interfaces.IJT400Format;
import io.greenscreens.jt400.interfaces.IJT400Params;
import io.greenscreens.jt400.interfaces.IJT400Program;

/**
 * JT400Extra factory class to wrap JT400Program interface
 * or to build ProgramCall argument list from definition class 
 */
public enum JT400ExtFactory {
	;
	
	/**
	 * Build format from response bytes based on structure JT400Format structure
	 * 
	 * @param as400
	 * @param format
	 * @param data
	 * @return
	 * @throws Exception
	 */
	final static public <T extends IJT400Format> T build(final AS400 as400, final Class<T> format, final ByteBuffer data) throws Exception {
		return JT400ExtFormatBuilder.build(as400, format, data);
	}
	
	/**
	 * Build ProgramCall parameters from instance
	 * 
	 * @param as400
	 * @param obj
	 * @return
	 */
	final static public <K extends IJT400Params> ProgramParameter[] build(final AS400 as400, final K obj) {
		return JT400ExtParameterBuilder.build(as400, obj);
	}
	
	/**
	 * Create Proxy Instance from interface implementing IJT400Program
	 * 
	 * @param caller
	 * @param input
	 * @param library
	 * @param program
	 * @return
	 */
	final static public <I extends IJT400Params, T> IJT400Program<I> create(final Class<T> caller, final Class<I> input, final String library, final String program) {
		return create(null, caller, input, library, program);
	}

	/**
	 * Create Proxy Instance from interface implementing IJT400Program
	 * 
	 * @param as400
	 * @param caller
	 * @param input
	 * @return
	 */
	final static public <I extends IJT400Params, T> IJT400Program<I> create(final AS400 as400, final Class<T> caller, final Class<I> input) {
		
		final JT400Program pgm = input.getAnnotation(JT400Program.class);
		if (pgm == null) {
			throw new RuntimeException("Program not defined!");
		}
		
		return create(as400, caller, input, pgm.library(), pgm.program());		
	}
	
	/**
	 * Create Proxy Instance from interface implementing IJT400Program
	 * 
	 * @param as400
	 * @param caller
	 * @param input
	 * @param library
	 * @param program
	 * @return
	 */
	@SuppressWarnings("unchecked")
	final static public <I extends IJT400Params, P extends IJT400Program<I>, T> P create(final AS400 as400, final Class<T> caller, final Class<I> input, final String library, final String program) {

		final JT400ExtInvocationHandler<I> handler = new JT400ExtInvocationHandler<I>(as400, input, library, program);
		
		final Object instance = Proxy.newProxyInstance(
				  IJT400Program.class.getClassLoader(), 
				  new Class<?> [] {caller},
				  handler);
		
		return (P) instance;

	}

	/**
	 * Create Proxy Instance from interface implementing IJT400Program
	 * 
	 * @param program
	 * @return
	 */
	final static public <T extends IJT400Program<? extends IJT400Params>> T createProgram(final Class<T> program) {
		return createProgram(null, program);
	}

	/**
	 * Create Proxy Instance from interface implementing IJT400Program
	 * 
	 * @param AS400
	 * @param caller
	 * @return
	 */
	@SuppressWarnings("unchecked")	
	final static public <T extends IJT400Program<? extends IJT400Params>> T createProgram(final AS400 as400, final Class<T> caller) {
	
		Class<? extends IJT400Params> input = null;
		
		final AnnotatedType[] intfs = caller.getAnnotatedInterfaces();
		if (intfs != null) {
			for (AnnotatedType intf : intfs) {
				final Type type = intf.getType();
				if (type instanceof ParameterizedType) {
					final Type [] pTypes = ((ParameterizedType) type).getActualTypeArguments();
					for (Type pType : pTypes) {
						
						if (IJT400Params.class.isAssignableFrom((Class<?>) pType)) {
							input = (Class<? extends IJT400Params>) pType;
							break;
						}
						
					}
				} 
				
			}
		}
		
		return (T) create(as400, caller, input);
	}

}