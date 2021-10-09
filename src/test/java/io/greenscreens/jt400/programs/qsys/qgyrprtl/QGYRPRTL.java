/*
 * Copyright (C) 2015, 2021 Green Screens Ltd.
 */
package io.greenscreens.jt400.programs.qsys.qgyrprtl;

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
 * Open List of Printers (QGYRPRTL) API
 * https://www.ibm.com/docs/en/i/7.4?topic=ssw_ibm_i_74/apis/QGYRPRTL.htm
 *
 */
@JT400Program(
		library = "QSYS",
		program = "QGYRPRTL",
		arguments = 7,
		formats = {PRTL0100.class, PRTL0200.class}
		)
public class QGYRPRTL implements IJT400Params {

	/**
	 * 
	 */
	@Id(0) @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, length = 142, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer receiver;
	
	/**
	 * 
	 */
	@Id(1) @Input
	@JT400Argument(type = AS400DataType.TYPE_BIN4, pass = ProgramParameter.PASS_BY_VALUE)
	int receiverLength;

	/**
	 * https://www.ibm.com/docs/en/ssw_ibm_i_74/apiref/oli.htm
	 * https://www.ibm.com/docs/en/i/7.4?topic=concepts-open-list-information-format
	 */
	@Id(2) @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE, length = 80)
	ByteBuffer listInformation;

	/**
	 * 
	 */
	@Id(3) @Input
	@JT400Argument(type = AS400DataType.TYPE_BIN4, pass = ProgramParameter.PASS_BY_VALUE)
	int receiveRecords;

	/**
	 * 
	 */	
	@Id(4) @Input
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_VALUE)
	ByteBuffer filterInfo;
	
	/**
	 * 
	 */
	@Id(5) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 8)
	String formatName = "PRTL0100";

	/**
	 * 
	 */	
	@Id(6) @Input @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer errorCode;
	
	public ByteBuffer getReceiver() {
		return receiver;
	}

	public void setReceiver(ByteBuffer receiver) {
		this.receiver = receiver;
	}

	public int getReceiverLength() {
		return receiverLength;
	}

	public void setReceiverLength(int receiverLength) {
		this.receiverLength = receiverLength;
	}

	public ByteBuffer getListInformation() {
		return listInformation;
	}

	public void setListInformation(ByteBuffer listInformation) {
		this.listInformation = listInformation;
	}

	public int getReceiveRecords() {
		return receiveRecords;
	}

	public void setReceiveRecords(int receiveRecords) {
		this.receiveRecords = receiveRecords;
	}

	public ByteBuffer getFilterInfo() {
		return filterInfo;
	}

	public void setFilterInfo(ByteBuffer filterInfo) {
		this.filterInfo = filterInfo;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public ByteBuffer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ByteBuffer errorCode) {
		this.errorCode = errorCode;
	}


	@Override
	public String toString() {
		return "QGYRPRTL [receiverLength=" + receiverLength + ", receiveRecords=" + receiveRecords + ", formatName="+ formatName + "]";
	}

	QGYRPRTL(final Builder builder) {
		this.receiver = builder.receiver;
		this.receiverLength = builder.receiverLength;
		this.errorCode = builder.errorCode;
		
		this.formatName = builder.formatName;
		this.filterInfo = builder.filterInfo;
		this.receiveRecords = builder.receiveRecords;
		this.listInformation = builder.listInformation;
	}

	/**
	 * Creates builder to build {@link QGYRPRTL}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

}
