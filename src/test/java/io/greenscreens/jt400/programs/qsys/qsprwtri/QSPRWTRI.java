/*
 * Copyright (C) 2015, 2021 Green Screens Ltd.
 */
package io.greenscreens.jt400.programs.qsys.qsprwtri;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.ProgramParameter;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Input;
import io.greenscreens.jt400.annotations.JT400Argument;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * Retrieve Writer Information (QSPRWTRI) API
 * https://www.ibm.com/docs/en/i/7.2?topic=ssw_ibm_i_72/apis/QSPRWTRI.htm
 *
 */
@JT400Program(
		library = "QSYS",
		program = "QSPRWTRI",
		arguments = 5,
		formats = {WTRI0100.class}
		)
public class QSPRWTRI implements IJT400Params {

	/**
	 * A unique key to identify this managed node.
	 * This parameter was returned in the client handle assigned parameter on the Add Client API.
	 */
	@Id(0) @Output
	//@JT400Argument(type = AS400DataType.TYPE_TEXT, length = 320, pass = ProgramParameter.PASS_BY_REFERENCE)
	// String receiver;
	
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, length = 320, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer receiver;
	
	/**
	 * The content and format of the writer information to be returned.
	 */
	@Id(1) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 8)
	String formatName = "WTRI0100";

	/**
	 * The name of the printer for which writer information is to be returned. 
	 * The following special value is supported for this parameter:
	 */
	@Id(2) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String printerName;
	
	@Id(3) @Input @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer errorCode;

	/**
	 * The name of the writer for which writer information is to be returned. 
	 * The value of this parameter must be set to blanks if the printer name parameter is not set to *WRITER.
	 */
	@Id(4) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String writerName;

	public ByteBuffer getReceiver() {
		return receiver;
	}

	public void setReceiver(final ByteBuffer receiver) {
		this.receiver = receiver;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(final String formatName) {
		this.formatName = formatName;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(final String printerName) {
		this.printerName = printerName;
	}

	public ByteBuffer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final ByteBuffer errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public String toString() {
		return "QSPRWTRI [formatName=" + formatName + ", printerName=" + printerName + ", writerName=" + writerName	+ "]";
	}

	QSPRWTRI(final Builder builder) {
		this.formatName = builder.format;
		this.printerName = builder.printerName;
		this.writerName = builder.writerName;
		this.receiver = builder.receiver;
		this.errorCode = builder.errorCode;
	}

	/**
	 * Creates builder to build {@link QSPRWTRI}.
	 * @param printerName
	 * @param writerName
	 * @param errorSize
	 * @return
	 */
	public static QSPRWTRI build(final String printerName, final String writerName, final int errorSize) {
		return Builder.build(printerName, writerName, errorSize);
	}

	/**
	 * Creates builder to build {@link QSPRWTRI}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

}
