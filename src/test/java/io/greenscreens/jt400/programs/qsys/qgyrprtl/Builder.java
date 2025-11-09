/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qgyrprtl;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.ERRC0100;
import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.JT400ExtUtil;
import io.greenscreens.jt400.interfaces.IJT400Format;

public class Builder {

	protected AS400 as400;
	protected ByteBuffer receiver;
	protected int receiverLength;
	protected ByteBuffer listInformation;
	protected int receiveRecords;
	protected ByteBuffer filterInfo;
	protected String formatName;
	protected ByteBuffer errorCode;
	
	Builder() {}

	public Builder withReceiver(ByteBuffer receiver) {
		this.receiver = receiver;
		return this;
	}

	public Builder withLength(int length) {
		this.receiverLength = length;
		return this;
	}
	
	public Builder withListInformation(final ByteBuffer listInformation) {
		this.listInformation = listInformation;
		return this;
	}
	
	public Builder withReceiveRecords(final int receiveRecords) {
		this.receiveRecords = receiveRecords;
		return this;
	}

	public Builder withFilterInfo(final ByteBuffer filterInfo) {
		this.filterInfo = filterInfo;
		return this;
	} 

	public Builder withFilterInfo(final FILTERINFO info) throws Exception {
		this.filterInfo = JT400ExtFactory.build(as400, info);
		return this;
	}  
	
	public Builder withFormat(String formatName) {
		this.formatName = formatName;
		return this;
	}
	
	public Builder withErrorCode(ByteBuffer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public Builder withSystem(AS400 as400) {
		this.as400 = as400;
		return this;
	}
	
	public QGYRPRTL build() {
		return new QGYRPRTL(this);
	}
	
	public static <T extends IJT400Format> Builder create(final AS400 system, final Class<T> format, final int errorSize) {		

		final int len = JT400ExtUtil.getFormatLength(format);
		final int listLen = JT400ExtUtil.getFormatLength(OPENLIST.class);		
		final ByteBuffer err = ERRC0100.toBuffer(errorSize);
		return new Builder().withErrorCode(err)
				.withSystem(system)
				.withLength(len)
				.withFormat(format.getSimpleName())
				.withListInformation(ByteBuffer.allocate(listLen))
				.withReceiver(ByteBuffer.allocate(len));

	}

}
