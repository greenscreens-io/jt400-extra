/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qtocnetsts;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Input;
import io.greenscreens.jt400.annotations.JT400Argument;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Format;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * The Retrieve TCP/IP Attributes (QtocRtvTCPA) API retrieves TCP/IPv4 and TCP/IPv6 stack attributes.
 * 
 * https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_73/apis/qtocrtvtcpa.htm
 *
 */
@JT400Program(
		library = "QSYS",
		program = "QTOCNETSTS",
		arguments = 4,
		service = true,
		procedure = "QtocRtvTCPA",
		returnFormat = ServiceProgramCall.NO_RETURN_VALUE,
		formats = {TCPA0100.class}
		)
public class QTOCNETSTS implements IJT400Params {

	/**
	 * Receiver variable
	 */
	@Id(0) @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer receiver;

	/**
	 * Length of receiver variable
	 */
	@Id(1) @Input
	@JT400Argument(type = AS400DataType.TYPE_BIN4, pass = ProgramParameter.PASS_BY_REFERENCE)
	int length;
	
	/**
	 * Format name
	 */
	@Id(2) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, length = 8, pass = ProgramParameter.PASS_BY_REFERENCE)
	String format;

	/**
	 * Error code
	 */
	@Id(3) @Input @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer errorCode;
	
	public ByteBuffer getReceiver() {
		return receiver;
	}

	public int getLength() {
		return length;
	}

	public String getFormat() {
		return format;
	}

	public ByteBuffer getErrorCode() {
		return errorCode;
	}

	QTOCNETSTS(final Builder builder) {
		this.receiver = builder.receiver;
		this.length = builder.length;
		this.format = builder.format;
		this.errorCode = builder.errorCode;
	}

	@Override
	public String toString() {
		return "QTOCNETSTS [receiver=" + receiver + ", length=" + length + ", format=" + format + ", errorCode="
				+ errorCode + "]";
	}

	/**
	 * Creates builder to build {@link QTOCNETSTS}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static <T extends IJT400Format> QTOCNETSTS build(Class<T> clazz) {
		return Builder.build(clazz);
	}
	
}
