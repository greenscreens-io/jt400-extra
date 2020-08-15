/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400.programs.qsys.qzcaclt;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Input;
import io.greenscreens.jt400.annotations.JT400Argument;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Params;

@JT400Program(
		library = "QSYS",
		program = "QZCACLT",
		arguments = 3,
		service = true,
		procedure = "QzcaGetClientHandle",
		returnFormat = ServiceProgramCall.NO_RETURN_VALUE
		)
public class QZCACLT implements IJT400Params {

	// for service programs to receive return value
	// int retVal = 0;

	/**
	 * A unique key to identify this managed node.
	 * This parameter was returned in the client handle assigned parameter on the Add Client API.
	 */
	@Id(0) @Output
	@JT400Argument(type = AS400DataType.TYPE_TEXT, length = 12, pass = ProgramParameter.PASS_BY_REFERENCE)
	//ByteBuffer receiver;
	String clientHandle;

	/**
	 * An administratively assigned system name for this managed client.
	 * By convention, this is the client's fully qualified domain name.
	 * A NULL-terminated string is required for this string.
	 * The length of the client ID is 1 through 255.
	 */
	@Id(1) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_REFERENCE)
	String clientID;

	@Id(2) @Input @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer errorCode;

	public String getClientHandle() {
		return clientHandle;
	}

	public void setClientHandle(String clientHandle) {
		this.clientHandle = clientHandle;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public ByteBuffer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ByteBuffer errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "QZCACLT [clientHandle=" + clientHandle + ", clientID=" + clientID
				+ ", errorCode=" + errorCode + "]";
	}

	QZCACLT(Builder builder) {
		this.clientHandle = builder.clientHandle;
		this.clientID = builder.clientID;
		this.errorCode = builder.errorCode;
	}

	/**
	 * Helper builder
	 * @param clazz
	 * @param displayName
	 * @return
	 */
	public static QZCACLT build(final String clientID, final int errorSize) {
		return Builder.build(clientID, errorSize);
	}

	/**
	 * Creates builder to build {@link QZCACLT}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

}
