/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qsprwtri;

import java.nio.ByteBuffer;

import io.greenscreens.jt400.ERRC0100;
import io.greenscreens.jt400.JT400ExtUtil;
import io.greenscreens.jt400.interfaces.IJT400Format;

public class Builder {

	protected ByteBuffer receiver;
	protected int length;
	protected String printerName;
	protected String writerName;
	protected String format;
	protected ByteBuffer errorCode;

	Builder() {}

	public Builder withReceiver(ByteBuffer receiver) {
		this.receiver = receiver;
		return this;
	}

	public Builder withLength(int length) {
		this.length = length;
		return this;
	}
	
	public Builder withPrinterName(final String printerName) {
		this.printerName = printerName;
		return this;
	}
	
	public Builder withWriterName(final String writerName) {
		this.writerName = writerName;
		return this;
	}

	public Builder withFormat(String format) {
		this.format = format;
		return this;
	}
	
	public Builder withErrorCode(ByteBuffer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public QSPRWTRI build() {
		return new QSPRWTRI(this);
	}
	
	public static <T extends IJT400Format> QSPRWTRI build(final String printerName, final String writerName, final int errorSize) {		

		final int len = JT400ExtUtil.getFormatLength(WTRI0100.class);
		final ByteBuffer err = ERRC0100.toBuffer(errorSize);
		return new Builder().withErrorCode(err)
				.withPrinterName(printerName)
				.withWriterName(writerName)
				.withFormat("WTRI0100")
				.withReceiver(ByteBuffer.allocate(len))
				.build();

	}

}
