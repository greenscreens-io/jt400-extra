/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qzcaclt;

import java.nio.ByteBuffer;

import io.greenscreens.jt400.ERRC0100;
import io.greenscreens.jt400.interfaces.IJT400Format;

public class Builder {

	protected String clientHandle;
	protected String clientID;
	protected ByteBuffer errorCode;

	Builder() {}

	public Builder withClientHandle(String clientHandle) {
		this.clientHandle = clientHandle;
		return this;
	}

	public Builder withClientID(String clientID) {
		this.clientID = clientID;
		return this;
	}

	public Builder withErrorCode(ByteBuffer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public QZCACLT build() {
		return new QZCACLT(this);
	}
	
	public static <T extends IJT400Format> QZCACLT build(final String clientID, final int errorSize) {		

		final ByteBuffer err = ERRC0100.toBuffer(errorSize);
		return new Builder().withErrorCode(err).withClientID(clientID).build();

	}

}
