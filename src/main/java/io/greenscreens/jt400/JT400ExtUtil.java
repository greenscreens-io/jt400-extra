/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.ServiceProgramCall;

import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.interfaces.IJT400Format;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Helper JT400 utility methods
 */
public enum JT400ExtUtil {
;

	/**
	 * Create JT400 program call path
	 *
	 * @param program
	 * @param library
	 * @return
	 */
	static public QSYSObjectPathName toQSYSPath(final String program, final String library, final boolean service) {
		final String pgm = Optional.of(program).get().toUpperCase().trim();
		final String lib = Optional.ofNullable(library).orElse("QSYS").toUpperCase().trim();
		return new QSYSObjectPathName(lib, pgm, service ? "SRVPGM" : "PGM");
	}

	/**
	 * Use reflection to set value to object field
	 * @param field
	 * @param obj
	 * @param value
	 * @throws Exception
	 */
	static public void setField(final Field field, final Object obj, final Object value) throws Exception {
		if (value != null) {
			field.setAccessible(true);
			field.set(obj, value);
		}
	}


	/**
	 * List all classes constructing format
	 * @param map
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static public <T extends IJT400Format> List<Class<T>> getAllClass(final List<Class<T>> list, final Class<T> clazz) {

		if (clazz.getSuperclass() != null) {
			getAllClass(list, (Class<T>) clazz.getSuperclass());
		}

		list.add(clazz);

		return list;
	}
	/**
	 * Generic JT400 program call
	 * @param as400
	 * @param program
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	static public ProgramCall call(final AS400 as400, final String program, final ProgramParameter[] parameters, final JT400Program ann) throws Exception {
		
		if (ann.service()) {
			return callService(as400, program, parameters, ann);			
		} else {
			return callProgram(as400, program, parameters, ann);			
		}
	}
	
	static public ProgramCall callProgram(final AS400 as400, final String program, final ProgramParameter[] parameters, final JT400Program ann) throws Exception {

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
	static public ServiceProgramCall callService(final AS400 as400, final String program, final ProgramParameter[] parameters, final JT400Program ann) throws Exception {

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
	static public <T extends IJT400Format> int getFormatLength(final Class<T> format) {
		final JT400Format fmt = format.getAnnotation(JT400Format.class);
		if (fmt == null) return 0;
		return fmt.length();
	}

	/**
	 * Generic byte array extraction from {ByteBuffer} instance
	 * @param buffer
	 * @param position
	 * @param len
	 * @return
	 */
	static public byte[] getBytesFrom(final ByteBuffer buffer, final int position, final int len) {
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
	static public <T extends IJT400Format, K extends IJT400Params> boolean contains(Class<T> format, final Class<K> params) {

		final Class<? extends IJT400Format>[] formats = params.getAnnotation(JT400Program.class).formats();

		for (Class<? extends IJT400Format> item : formats) {
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
	static public void printErrors(final AS400Message [] messages, final Logger logger) {
		for (AS400Message message : messages) {
			logger.warning(String.format(" %s : %s", message.getID(), message.getText()));
		}
	}

	/**
	 * Print JT400 errors to defined output stream
	 * @param messages
	 */
	static public void printErrors(final AS400Message [] messages, final OutputStream stream) {

		try {
			for (AS400Message message : messages) {
				byte [] bytes = String.format(" %s : %s\n", message.getID(), message.getText()).getBytes();
				stream.write(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print JT400 errors to defined writer
	 * @param messages
	 */
	static public void printErrors(final AS400Message [] messages, final Writer writer) {

		try {
			for (AS400Message message : messages) {
				String line = String.format(" %s : %s\n", message.getID(), message.getText());
				writer.write(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print JT400 errors to default console output
	 * @param messages
	 */
	static public void printErrors(final AS400Message [] messages) {
		printErrors(messages, System.out);
	}

	/**
	 * Convert JT400 errors into string
	 * @param messages
	 * @return
	 */
	static public String getErrors(final AS400Message [] messages) {
		final StringWriter writer = new StringWriter();
		printErrors(messages, writer);
		writer.flush();
		return writer.toString();
	}

	static private final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes();

	/**
	 * Convert ByteBuffer to hex string
	 * @param buffer
	 * @return
	 */
	static public String bytesToHex(final ByteBuffer buffer) {
		return bytesToHex(buffer.array());
	}
	
	/**
	 * Convert bytes to hex string
	 * @param bytes
	 * @return
	 */
	static public String bytesToHex(byte[] bytes) {

		final byte[] hexChars = new byte[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
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
	static public ByteBuffer hexToBuffer(final String s) {
		return ByteBuffer.wrap(hexToBytes(s));
	}
	
	/**
	 * Convert hex string to byte array
	 * @param s
	 * @return
	 */
	static public byte[] hexToBytes(final String s) {

		final int len = s.length();

		final byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
				+ Character.digit(s.charAt(i+1), 16));
		}

		return data;
	}
	
}
